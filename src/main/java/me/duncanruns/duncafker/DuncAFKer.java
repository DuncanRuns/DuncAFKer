package me.duncanruns.duncafker;

import com.google.gson.Gson;
import me.duncanruns.duncafker.mixin.MinecraftClientAccess;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class DuncAFKer implements ModInitializer {

    public static final String MOD_ID = "duncafker";
    public static final String MOD_NAME = "DuncAFKer";
    private static final Path CONFIG_PATH = Path.of("config", MOD_ID);
    private static final File CONFIG_FILE = new File(CONFIG_PATH.toFile(), "options.txt");
    public static Logger LOGGER = LogManager.getLogger();
    private static KeyBinding keyBinding;
    private static boolean afkLock = false;
    private static boolean wasPressed = false;

    private static boolean usingFreecam = false;
    private static Supplier<Boolean> checkFCMovementSupplier = null;

    private static int ticker = 0;
    private static int interval = 30;
    private static boolean doUse = false;

    private static boolean lockClicksToo = true;

    public static void saveOptions() throws Exception {
        if (!Files.isDirectory(CONFIG_PATH)) {
            Files.createDirectories(CONFIG_PATH);
        }
        DuncAFKerOptionsJson options = new DuncAFKerOptionsJson();
        options.doUse = doUse;
        options.interval = interval;
        Gson gson = new Gson();
        String out = gson.toJson(options);
        Files.writeString(CONFIG_FILE.toPath(), out);
    }

    public static void setUseInstead(boolean doUse) {
        DuncAFKer.doUse = doUse;
    }

    public static int getInterval() {
        return interval;
    }

    public static void setInterval(int interval) {
        DuncAFKer.interval = interval;
    }

    public static boolean shouldUseInstead() {
        return doUse;
    }

    public static boolean shouldPreventInteraction() {
        return afkLock;
    }

    public static boolean shouldPreventMovement() {
        if (usingFreecam && checkFCMovementSupplier.get()) {
            return false;
        }
        return afkLock;
    }

    public static boolean shouldPreventClickActions() {
        return afkLock && lockClicksToo;
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    private static void loadOptions() throws Exception {
        if (CONFIG_FILE.exists()) {
            String in = Files.readString(CONFIG_FILE.toPath());
            Gson gson = new Gson();
            DuncAFKerOptionsJson options = gson.fromJson(in, DuncAFKerOptionsJson.class);
            options.ensure();
            doUse = options.doUse;
            interval = options.interval;
        }
    }

    private static void tick(MinecraftClient client) {
        if (afkLock) {
            if (client.player == null) {
                afkLock = false;
                return;
            }
            ticker++;
            while (ticker >= interval) {
                ticker = 0;
                lockClicksToo = false;
                if (doUse) {
                    try {
                        ((MinecraftClientAccess) client).invokeDoItemUse();
                    } catch (Exception ignored) {
                    }
                } else {
                    try {
                        ((MinecraftClientAccess) client).setAttackCooldown(0);
                        ((MinecraftClientAccess) client).invokeDoAttack();
                    } catch (Exception ignored) {
                    }
                }
                lockClicksToo = true;
            }
        }

    }

    private static void checkForFreecamMod() {
        try {
            Class<?> freecamClazz = Class.forName("net.xolt.freecam.Freecam");
            Method m1 = freecamClazz.getMethod("isEnabled");
            Method m2 = freecamClazz.getMethod("isPlayerControlEnabled");
            usingFreecam = true;
            checkFCMovementSupplier = () -> {
                try {
                    return ((Boolean) m1.invoke(null)) && !((Boolean) m2.invoke(null));
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    return false;
                }
            };
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        try {
            loadOptions();
        } catch (Exception e) {
            log(Level.ERROR, "Exception while loading options:");
            e.printStackTrace();
        }

        checkForFreecamMod();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "duncafker.key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "duncafker.keybindcat"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.isPressed() && !wasPressed) {
                afkLock = !afkLock;
                ticker = 0;
                if (afkLock) {
                    client.player.sendMessage(Text.translatable("duncafker.enabled"), true);
                } else {
                    client.player.sendMessage(Text.translatable("duncafker.disabled"), true);
                }
            }
            wasPressed = keyBinding.isPressed();
        });

        // Tick Events
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tick(MinecraftClient.getInstance());
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isIntegratedServerRunning()) {
                tick(client);
            }
        });
    }
}