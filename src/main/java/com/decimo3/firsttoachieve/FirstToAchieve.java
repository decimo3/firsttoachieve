package com.decimo3.firsttoachieve;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	}
}
