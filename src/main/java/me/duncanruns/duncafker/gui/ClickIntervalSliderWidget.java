package me.duncanruns.duncafker.gui;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class ClickIntervalSliderWidget extends SliderWidget {
    public ClickIntervalSliderWidget(int x, int y, int width, int height, Text text) {
        super(x, y, width, height, text, (DuncAFKer.getInterval() - 1) / 199d);
    }

    @Override
    protected void updateMessage() {
        setMessage(MutableText.of(new TranslatableTextContent("duncafker.clickinterval", "" + DuncAFKer.getInterval())));
    }

    @Override
    protected void applyValue() {
        DuncAFKer.setInterval(1 + (int) (199 * value));
    }
}
