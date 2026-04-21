package com.foodtimerhud.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class FoodTimerHudRenderer {

    // [x, y, width, height]
    public static int[] currentBounds = new int[]{0, 0, 0, 0};

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!ConfigManager.config.enableHud) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.options.hudHidden || client.player == null) {
                return;
            }

            if (client.currentScreen instanceof HudEditorScreen) {
                return;
            }

            List<FoodTimerManager.TimerEntry> timers = FoodTimerManager.getActiveTimers();
            if (timers.isEmpty()) {
                return;
            }

            renderHud(drawContext, client.textRenderer, timers);
        });
    }

    public static void renderHud(DrawContext drawContext, TextRenderer textRenderer, List<FoodTimerManager.TimerEntry> timers) {
        int startX = ConfigManager.config.hudX;
        int startY = ConfigManager.config.hudY;
        float scale = ConfigManager.config.hudScale;

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(startX, startY, 0);
        drawContext.getMatrices().scale(scale, scale, scale);

        long currentTime = System.currentTimeMillis();
        int yOffset = 0;
        int maxWidth = 0;

        for (FoodTimerManager.TimerEntry timer : timers) {
            long elapsed = (currentTime - timer.startTime) / 1000;
            long remaining = timer.totalSeconds - elapsed;

            if (remaining > 0 || timer.totalSeconds == -1) {
                long displayRemaining = Math.max(0, remaining);
                long h = displayRemaining / 3600;
                long m = (displayRemaining % 3600) / 60;
                long s = displayRemaining % 60;

                String timeStr;
                if (h > 0) {
                    timeStr = String.format("%02d:%02d:%02d", h, m, s);
                } else {
                    timeStr = String.format("%02d:%02d", m, s);
                }

                String displayStr = timer.foodName + ": " + timeStr;
                drawContext.drawTextWithShadow(textRenderer, displayStr, 0, yOffset, 0xFFFFFF);
                
                int width = textRenderer.getWidth(displayStr);
                if (width > maxWidth) maxWidth = width;

                yOffset += 12;
            }
        }

        drawContext.getMatrices().pop();

        int scaledWidth = (int) (maxWidth * scale);
        int scaledHeight = (int) (yOffset * scale);
        currentBounds[0] = startX;
        currentBounds[1] = startY;
        currentBounds[2] = scaledWidth;
        currentBounds[3] = scaledHeight;
    }
}