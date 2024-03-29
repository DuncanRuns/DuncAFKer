package me.duncanruns.duncafker.mixin;

import me.duncanruns.duncafker.DuncAFKer;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void preventControls(boolean slowDown, float f, CallbackInfo info) {
        if (DuncAFKer.shouldPreventMovement()) {
            this.pressingForward = false;
            this.pressingBack = false;
            this.pressingLeft = false;
            this.pressingRight = false;
            this.movementForward = 0.0f;
            this.movementSideways = 0.0f;
            info.cancel();
        }
    }
}
