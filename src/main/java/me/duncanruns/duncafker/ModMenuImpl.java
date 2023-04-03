package me.duncanruns.duncafker;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.duncanruns.duncafker.gui.DuncAFKerConfigScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new DuncAFKerConfigScreen(Text.translatable("duncafker.configtitle"), parent);
    }
}
