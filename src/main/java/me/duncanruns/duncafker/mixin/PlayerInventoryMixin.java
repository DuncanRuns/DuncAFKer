package me.duncanruns.duncafker.mixin;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    private void scrollMixin(double scrollAmount, CallbackInfo info) {
        if (DuncAFKer.shouldPreventInputs()) {
            info.cancel();
        }
    }
}
