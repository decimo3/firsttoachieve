package com.decimo3.firsttoachieve.mixin;

import com.decimo3.firsttoachieve.PlayerAdvancementState;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {

	@Shadow
	@Final
	private ServerPlayerEntity owner;

	@Inject(method = "grantCriterion", at = @At("HEAD"), cancellable = true)
	private void beforeGrant(
			Advancement advancement,
			String criterion,
			CallbackInfoReturnable<Boolean> cir) {

		if (advancement.getId().getPath().startsWith("recipes"))
		{
			return;
		}

		Identifier id = advancement.getId();

		ServerWorld world = owner.getServerWorld();

		PlayerAdvancementState state = PlayerAdvancementState.get(world);

		String advancementId = id.toString();

		if (state.isClaimed(advancementId)) {

			owner.sendMessage(
					Text.literal(
							"Outro jogador já realizou essa conquista!"),
					false);

			cir.setReturnValue(false);
		}
	}

	@Inject(method = "grantCriterion", at = @At("RETURN"))
	private void afterGrant(
			Advancement advancement,
			String criterion,
			CallbackInfoReturnable<Boolean> cir) {

		if (!cir.getReturnValue()) {
			return;
		}

		var progress = ((PlayerAdvancementTracker) (Object) this).getProgress(advancement);

		if (!progress.isDone()) {
			return;
		}

		ServerWorld world = owner.getServerWorld();

		PlayerAdvancementState state = PlayerAdvancementState.get(world);

		String id = advancement.getId().toString();

		if (!state.isClaimed(id)) {

			state.claim(id);

			owner.getServer().getPlayerManager()
					.broadcast(
							Text.literal(
									owner.getName().getString()
											+ " foi o primeiro a completar "
											+ id),
							false);
		}
	}
}
