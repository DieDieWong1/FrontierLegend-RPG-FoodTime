package com.foodtimerhud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FoodTimerManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File SAVE_FILE = FabricLoader.getInstance().getConfigDir().resolve("food_timer_hud_data.json").toFile();

    public static class TimerEntry {
        public String foodName;
        public long startTime;
        public int totalSeconds;

        public TimerEntry(String foodName, long startTime, int totalSeconds) {
            this.foodName = foodName;
            this.startTime = startTime;
            this.totalSeconds = totalSeconds;
        }
    }

    private static List<TimerEntry> activeTimers = new ArrayList<>();

    public static void load() {
        if (SAVE_FILE.exists()) {
            try (FileReader reader = new FileReader(SAVE_FILE)) {
                Type type = new TypeToken<List<TimerEntry>>(){}.getType();
                List<TimerEntry> loaded = GSON.fromJson(reader, type);
                if (loaded != null) {
                    activeTimers = loaded;
                    cleanExpiredTimers();
                }
            } catch (Exception e) {
                FoodTimerHudClient.LOGGER.error("Failed to load food timers", e);
            }
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            GSON.toJson(activeTimers, writer);
        } catch (Exception e) {
            FoodTimerHudClient.LOGGER.error("Failed to save food timers", e);
        }
    }

    public static void addTimer(String name, int seconds) {
        // Remove existing timer with same name if any
        activeTimers.removeIf(t -> t.foodName.equals(name));
        activeTimers.add(new TimerEntry(name, System.currentTimeMillis(), seconds));
        save();
    }

    public static List<TimerEntry> getActiveTimers() {
        cleanExpiredTimers();
        return activeTimers;
    }

    private static void cleanExpiredTimers() {
        boolean removed = false;
        long currentTime = System.currentTimeMillis();
        Iterator<TimerEntry> it = activeTimers.iterator();
        while (it.hasNext()) {
            TimerEntry entry = it.next();
            long elapsed = (currentTime - entry.startTime) / 1000;
            if (elapsed >= entry.totalSeconds) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            save();
        }
    }
}
