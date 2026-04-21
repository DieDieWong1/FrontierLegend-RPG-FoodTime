package com.foodtimerhud.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HudEditorScreen extends Screen {
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private final Screen parent;
    private final List<FoodTimerManager.TimerEntry> dummyTimers;

    public HudEditorScreen(Screen parent) {
        super(Text.translatable("title.food_timer_hud.edit"));
        this.parent = parent;
        
        // Create dummy timers if the active list is empty to allow visual editing
            this.dummyTimers = new ArrayList<>();
            List<FoodTimerManager.TimerEntry> active = FoodTimerManager.getActiveTimers();
            if (active.isEmpty()) {
            FoodTimerManager.TimerEntry dummy1 = new FoodTimerManager.TimerEntry(
            Text.translatable("text.food_timer_hud.example_food").getString(),
            System.currentTimeMillis(),
            300
            );
            FoodTimerManager.TimerEntry dummy2 = new FoodTimerManager.TimerEntry(
            Text.translatable("text.food_timer_hud.another_food").getString(),
            System.currentTimeMillis(),
            120
            );
            
            dummyTimers.add(dummy1);
            dummyTimers.add(dummy2);
        } else {
            dummyTimers.addAll(active);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        FoodTimerHudRenderer.renderHud(context, this.textRenderer, this.dummyTimers);

        int[] bounds = FoodTimerHudRenderer.currentBounds;
        if (bounds != null && bounds[2] > 0 && bounds[3] > 0) {
            int x = bounds[0];
            int y = bounds[1];
            int w = bounds[2];
            int h = bounds[3];

            // Draw bounding box
            int color = isDragging ? 0xFF00FF00 : 0xFFFFFF00;
            context.drawBorder(x - 2, y - 2, w + 4, h + 4, color);
        }

        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("text.food_timer_hud.edit_instructions"), this.width / 2, 20, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int[] bounds = FoodTimerHudRenderer.currentBounds;
            if (bounds != null && bounds[2] > 0 && bounds[3] > 0) {
                int x = bounds[0] - 2;
                int y = bounds[1] - 2;
                int w = bounds[2] + 4;
                int h = bounds[3] + 4;

                if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
                    isDragging = true;
                    dragOffsetX = (int) mouseX - ConfigManager.config.hudX;
                    dragOffsetY = (int) mouseY - ConfigManager.config.hudY;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && button == 0) {
            ConfigManager.config.hudX = (int) mouseX - dragOffsetX;
            ConfigManager.config.hudY = (int) mouseY - dragOffsetY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && isDragging) {
            isDragging = false;
            ConfigManager.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        float scale = ConfigManager.config.hudScale;
        scale += (float) (amount * 0.05f);
        if (scale < 0.1f) scale = 0.1f;
        if (scale > 5.0f) scale = 5.0f;
        ConfigManager.config.hudScale = scale;
        ConfigManager.save();
        return true;
    }

    @Override
    public void close() {
        ConfigManager.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}