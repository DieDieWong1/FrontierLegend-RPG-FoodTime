package com.foodtimerhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoodTimerHudClient implements ClientModInitializer {
	public static final String MOD_ID = "food_timer_hud";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static KeyBinding configKeyBinding;
	private static KeyBinding editHudKeyBinding;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing FoodTime (Client)");
		FoodTimerManager.load();
		FoodTimerHudRenderer.register();
		ConfigManager.load();

		configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.food_timer_hud.open_config",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_O,
				"category.food_timer_hud.keys"
		));

		editHudKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.food_timer_hud.edit_hud",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_P,
				"category.food_timer_hud.keys"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (configKeyBinding.wasPressed()) {
				if (client.player != null) {
					client.setScreen(ConfigManager.createConfigScreen(client.currentScreen));
				}
			}
			
			while (editHudKeyBinding.wasPressed()) {
				if (client.player != null) {
					client.setScreen(new HudEditorScreen(client.currentScreen));
				}
			}
		});
	}
}