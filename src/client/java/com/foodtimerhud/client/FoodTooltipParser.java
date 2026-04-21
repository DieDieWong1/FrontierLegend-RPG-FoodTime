package com.foodtimerhud.client;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodTooltipParser {

    // Pattern to match time formats like "10m 30s", "1h 5m", "45s", "01:30", "1.5小时", "10分钟"
    private static final Pattern TIME_PATTERN = Pattern.compile("(?:([0-9.]+)\\s*(?:[hH]|小时|小時))?\\s*(?:([0-9.]+)\\s*(?:[mM]|分钟|分鐘|分))?\\s*(?:([0-9.]+)\\s*(?:[sS]|秒钟|秒鐘|秒))?");
    private static final Pattern DIGITAL_TIME_PATTERN = Pattern.compile("\\b(\\d+):(\\d+)(?::(\\d+))?\\b");

    public static class ParsedFoodData {
        public final String name;
        public final int durationSeconds;

        public ParsedFoodData(String name, int durationSeconds) {
            this.name = name;
            this.durationSeconds = durationSeconds;
        }
    }

    public static Optional<ParsedFoodData> parseFood(ItemStack stack, PlayerEntity player) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        String foodName = stack.getName().getString();
        int totalSeconds = 0;
        boolean foundTime = false;

        List<Text> tooltip = stack.getTooltip(player, TooltipContext.Default.BASIC);
        for (Text text : tooltip) {
            String line = text.getString();
            
            // Try digital format first e.g. 10:30
            Matcher digitalMatcher = DIGITAL_TIME_PATTERN.matcher(line);
            if (digitalMatcher.find()) {
                if (digitalMatcher.group(3) != null) {
                    // hh:mm:ss
                    int h = Integer.parseInt(digitalMatcher.group(1));
                    int m = Integer.parseInt(digitalMatcher.group(2));
                    int s = Integer.parseInt(digitalMatcher.group(3));
                    totalSeconds = h * 3600 + m * 60 + s;
                } else {
                    // mm:ss
                    int m = Integer.parseInt(digitalMatcher.group(1));
                    int s = Integer.parseInt(digitalMatcher.group(2));
                    totalSeconds = m * 60 + s;
                }
                foundTime = true;
                break;
            }

            // Try text format e.g. 1h 2m 3s, 1.5小时, 10分钟
            Matcher textMatcher = TIME_PATTERN.matcher(line);
            while (textMatcher.find()) {
                if (textMatcher.group(1) != null || textMatcher.group(2) != null || textMatcher.group(3) != null) {
                    double h = textMatcher.group(1) != null ? Double.parseDouble(textMatcher.group(1)) : 0;
                    double m = textMatcher.group(2) != null ? Double.parseDouble(textMatcher.group(2)) : 0;
                    double s = textMatcher.group(3) != null ? Double.parseDouble(textMatcher.group(3)) : 0;
                    totalSeconds = (int) Math.round(h * 3600 + m * 60 + s);
                    foundTime = true;
                    break;
                }
            }
            if (foundTime) break;
        }

        if (foundTime && totalSeconds > 0) {
            return Optional.of(new ParsedFoodData(foodName, totalSeconds));
        }

        return Optional.empty();
    }
}
