package me.duncanruns.duncafker.mixin;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    @Final
    public GameOptions options;

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void preventUse(CallbackInfo info) {
        if (DuncAFKer.shouldPreventClickActions()) {
            info.cancel();
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void preventAttack(CallbackInfoReturnable<Boolean> info) {
        if (DuncAFKer.shouldPreventClickActions()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
    private void preventPick(CallbackInfo info) {
        if (DuncAFKer.shouldPreventInteraction()) {
            info.cancel();
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void preventBreaking(boolean bl, CallbackInfo info) {
        if (DuncAFKer.shouldPreventInteraction()) {
            interactionManager.cancelBlockBreaking();
            info.cancel();
        }
    }

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void preventHotbar(CallbackInfo info) {
        if (DuncAFKer.shouldPreventInteraction()) {
            for (int i = 0; i < 9; i++) {
                while (options.hotbarKeys[i].wasPressed()) {
                }
            }
        }
    }
}
