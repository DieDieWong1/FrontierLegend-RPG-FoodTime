package com.foodtimerhud.client.mixin;

import com.foodtimerhud.client.FoodTimerManager;
import com.foodtimerhud.client.FoodTooltipParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void onEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) {
            LivingEntity self = (LivingEntity) (Object) this;
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (self == player) {
                FoodTooltipParser.parseFood(stack, player).ifPresent(parsed -> {
                    if (parsed.durationSeconds > 0) {
                        FoodTimerManager.addTimer(parsed.name, parsed.durationSeconds);
                    }
                });
            }
        }
    }
}
