package me.duncanruns.duncafker.mixin;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void changeLookDirectionStartMixin(double cursorDeltaX, double cursorDeltaY, CallbackInfo info) {
        if (DuncAFKer.shouldPreventMovement() && ((Object) this) instanceof ClientPlayerEntity) {
            info.cancel();
        }
    }
}
