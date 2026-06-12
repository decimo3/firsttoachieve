package com.decimo3.firsttoachieve;

import java.time.Instant;

import net.fabricmc.api.ModInitializer;

import net.minecraft.advancement.Advancement;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.decimo3.firsttoachieve.PlayerAdvancementState.AdvancementRecord;

public class FirstToAchieve implements ModInitializer {
	public static final String MOD_ID = "firsttoachieve";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// This code runs as soon as Minecraft is in a mod-load-ready state.
	// However, some things (like resources) may still be uninitialized.
	// Proceed with mild caution.
	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world from FirstToAchieve Mod!");
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> {
					dispatcher.register(
							CommandManager.literal("firsttoachieve")
									.executes(context -> {
										ServerWorld world = context.getSource().getWorld();
										PlayerAdvancementState state = PlayerAdvancementState.get(world);
										if (state.getAdvancements().isEmpty()) {
											context.getSource().sendFeedback(() -> Text.literal("Nenhuma conquista registrada."), false);
											return 1;
										}
										context.getSource().sendFeedback(() -> Text.literal("Conquistas registradas:"), false);
										ServerAdvancementLoader loader = context.getSource().getServer().getAdvancementLoader();
										for (AdvancementRecord advancement : state.getAdvancements()) {
											Advancement advancementObj = loader.get(new Identifier(advancement.advancementId()));
											String advancementName = (advancementObj.getDisplay() != null)
													? advancementObj.getDisplay().getTitle().getString()
													: advancementObj.getId().toString();
											Instant datetime = Instant.ofEpochMilli(advancement.timestamp());
											String message = "- " + datetime + " | " + advancement.playerName() + " | " + advancementName;
											context.getSource().sendFeedback(() -> Text.literal(message), false);
										}
										return 1;
									}
								)
							);
				});
	}
}
