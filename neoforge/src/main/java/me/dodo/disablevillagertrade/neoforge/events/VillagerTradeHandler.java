package me.dodo.disablevillagertrade.neoforge.events;

import me.dodo.disablevillagertrade.neoforge.DisableVillagerTradeNeoForge;
import me.dodo.disablevillagertrade.neoforge.config.NeoForgeConfig;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Handles villager interaction events for NeoForge.
 */
public class VillagerTradeHandler {
    
    @SubscribeEvent
    public void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        // Only process AbstractVillager interactions
        if (!(event.getTarget() instanceof AbstractVillager abstractVillager)) {
            return;
        }
        
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Only process for server players
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        
        String professionName = "NONE";
        if (abstractVillager instanceof Villager villager) {
            Holder<VillagerProfession> professionHolder = villager.getVillagerData().profession();
            professionName = professionHolder.unwrapKey()
                .map(key -> key.identifier().getPath().toUpperCase())
                .orElse("NONE");
        } else if (abstractVillager instanceof WanderingTrader) {
            if (NeoForgeConfig.ENABLE_WANDERING_TRADER_TRADES.get()) {
                return;
            }
            professionName = "WANDERING_TRADER";
        } else {
            return; // Custom villager type?
        }
        
        // Get dimension name
        String dimensionName = player.level().dimension().identifier().toString();
        
        // Check bypass permission (op level 2+)
        boolean hasBypass = player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
        boolean isOp = hasBypass; // In vanilla, this is the same
        
        // Check if trade should be blocked
        boolean shouldBlock = DisableVillagerTradeNeoForge.getTradeBlocker().shouldBlockTrade(
            true,
            professionName,
            !abstractVillager.isNoAi(),      // hasAI
            !abstractVillager.isNoGravity(), // hasGravity
            dimensionName,
            NeoForgeConfig.DISABLED_DIMENSIONS.get().stream()
                .map(Object::toString)
                .toList(),
            hasBypass,
            NeoForgeConfig.ENABLE_FOR_OP.get(),
            isOp
        );
        
        if (shouldBlock) {
            // Cancel the interaction
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            
            if (NeoForgeConfig.SHAKE_HEAD_ENABLED.get()) {
                abstractVillager.setUnhappyCounter(40);
            }
            
            if (abstractVillager instanceof WanderingTrader) {
                abstractVillager.playSound(net.minecraft.sounds.SoundEvents.WANDERING_TRADER_NO, 1.0F, abstractVillager.getVoicePitch());
            } else {
                abstractVillager.playSound(net.minecraft.sounds.SoundEvents.VILLAGER_NO, 1.0F, abstractVillager.getVoicePitch());
            }
            
            // Send message to player
            if (NeoForgeConfig.MESSAGE_ENABLED.get()) {
                player.sendSystemMessage(Component.literal(NeoForgeConfig.MESSAGE_TEXT.get()));
            }
        }
    }
}
