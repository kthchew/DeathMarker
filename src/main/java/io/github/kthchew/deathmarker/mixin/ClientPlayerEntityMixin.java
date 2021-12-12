package io.github.kthchew.deathmarker.mixin;

import com.mojang.authlib.GameProfile;
import io.github.kthchew.deathmarker.ClientPlayerEntityExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Mixin(ClientPlayerEntity.class)
@SuppressWarnings("unused")
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ClientPlayerEntityExtension {

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Shadow public abstract void sendMessage(Text message, boolean actionBar);

	@Shadow @Final protected MinecraftClient client;

	public void deathmarker_sendDeathLocationMessage() {
		// Only send the message when the death message appears and not when "Respawn" is clicked
		if (!(this.isDead())) {
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
			Identifier dimensionID = this.getEntityWorld().getRegistryKey().getValue();
			String dimension = dimensionID.toString();

			if (dimensionID.equals(DimensionType.OVERWORLD_ID)) {
				dimension = "Overworld";
			} else if (dimensionID.equals(DimensionType.THE_NETHER_ID)) {
				dimension = "The Nether";
			} else if (dimensionID.equals(DimensionType.THE_END_ID)) {
				dimension = "The End";
			}

			Text deathMessage;
			if (this.getY() >= this.getEntityWorld().getBottomY()) {
				deathMessage = new LiteralText(Formatting.RED + "[DEATH] " + Formatting.RESET + "Died at " + decimalFormat.format(this.getX()) + ", " + decimalFormat.format(this.getY()) + ", " + decimalFormat.format(this.getZ()) + " in " + dimension);
			} else {
				// Obfuscate coordinates if the player died in the void
				deathMessage = new LiteralText(Formatting.RED + "[DEATH] " + Formatting.RESET + "Died at " + Formatting.OBFUSCATED + decimalFormat.format(this.getX()) + ", " + decimalFormat.format(this.getY()) + ", " + decimalFormat.format(this.getZ()) + Formatting.RESET + " in " + dimension);
			}
			this.sendMessage(deathMessage, false);
		}
	}
}
