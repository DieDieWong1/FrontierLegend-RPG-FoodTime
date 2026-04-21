package com.foodtimerhud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigManager {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("food_timer_hud.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Config config = new Config();

    public static class Config {
        public boolean enableHud = true;
        public int hudX = 10;
        public int hudY = 50;
        public float hudScale = 1.0f;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Config loaded = GSON.fromJson(reader, Config.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (Exception e) {
                FoodTimerHudClient.LOGGER.error("Failed to load config", e);
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            FoodTimerHudClient.LOGGER.error("Failed to save config", e);
        }
    }

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.food_timer_hud.config"));

        builder.setSavingRunnable(ConfigManager::save);

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.food_timer_hud.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.food_timer_hud.enable"), config.enableHud)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableHud = newValue)
                .build());

        general.addEntry(entryBuilder.startIntField(Text.translatable("option.food_timer_hud.x"), config.hudX)
                .setDefaultValue(10)
                .setSaveConsumer(newValue -> config.hudX = newValue)
                .build());

        general.addEntry(entryBuilder.startIntField(Text.translatable("option.food_timer_hud.y"), config.hudY)
                .setDefaultValue(50)
                .setSaveConsumer(newValue -> config.hudY = newValue)
                .build());

        general.addEntry(entryBuilder.startFloatField(Text.translatable("option.food_timer_hud.scale"), config.hudScale)
                .setDefaultValue(1.0f)
                .setSaveConsumer(newValue -> config.hudScale = newValue)
                .build());

        return builder.build();
    }
}
