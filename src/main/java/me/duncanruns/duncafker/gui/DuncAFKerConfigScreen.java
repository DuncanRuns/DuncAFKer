package me.duncanruns.duncafker.gui;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Language;
import org.apache.logging.log4j.Level;

public class DuncAFKerConfigScreen extends Screen {
    private final Screen parent;

    public DuncAFKerConfigScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    private Text getClickModeText() {
        return MutableText.of(new TranslatableTextContent("duncafker.clickmode", Language.getInstance().get(DuncAFKer.shouldUseInstead() ? "key.use" : "key.attack")));
    }

    @Override
    protected void init() {
        addDrawableChild(new ButtonWidget(width / 2 - 100, height / 2 - 22, 200, 20, getClickModeText(), button -> {
            DuncAFKer.setUseInstead(!DuncAFKer.shouldUseInstead());
            button.setMessage(getClickModeText());
        }));

        addDrawableChild(new ClickIntervalSliderWidget(width / 2 - 100, height / 2 + 2, 200, 20, MutableText.of(new TranslatableTextContent("duncafker.clickinterval", "" + DuncAFKer.getInterval()))));

        addDrawableChild(new ButtonWidget(width / 2 - 50, height / 2 + 40, 100, 20, ScreenTexts.DONE, button -> close()));
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(0);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, height / 2 - 50, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
