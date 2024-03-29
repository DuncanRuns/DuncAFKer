package me.duncanruns.duncafker.gui;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.apache.logging.log4j.Level;

public class DuncAFKerConfigScreen extends Screen {
    private final Screen parent;

    public DuncAFKerConfigScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(matrices);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, height / 2 - 50, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        try {
            DuncAFKer.saveOptions();
        } catch (Exception e) {
            DuncAFKer.log(Level.ERROR, "Exception while saving options:");
            e.printStackTrace();
        }
        this.client.setScreen(parent);
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(getClickModeText(), button -> {
            DuncAFKer.setUseInstead(!DuncAFKer.shouldUseInstead());
            button.setMessage(getClickModeText());
        }).position(width / 2 - 100, height / 2 - 22).width(200).build());

        addDrawableChild(new ClickIntervalSliderWidget(width / 2 - 100, height / 2 + 2, 200, 20, Text.translatable("duncafker.clickinterval", "" + DuncAFKer.getInterval())));

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> close()).position(width / 2 - 50, height / 2 + 40).width(100).build());
    }

    private Text getClickModeText() {
        return Text.translatable("duncafker.clickmode", Language.getInstance().get(DuncAFKer.shouldUseInstead() ? "key.use" : "key.attack"));
    }
}
