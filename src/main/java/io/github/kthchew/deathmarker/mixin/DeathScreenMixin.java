package io.github.kthchew.deathmarker.mixin;

import io.github.kthchew.deathmarker.ClientPlayerEntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
@SuppressWarnings("unused")
public abstract class DeathScreenMixin extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void sendDeathMessage(CallbackInfo ci) {
        assert MinecraftClient.getInstance().player != null;
        ((ClientPlayerEntityExtension) MinecraftClient.getInstance().player).deathmarker_sendDeathLocationMessage();
    }
}
