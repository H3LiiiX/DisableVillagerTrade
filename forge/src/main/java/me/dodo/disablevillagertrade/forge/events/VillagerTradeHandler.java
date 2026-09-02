package me.dodo.disablevillagertrade.forge.events;

import me.dodo.disablevillagertrade.forge.DisableVillagerTradeForge;
import me.dodo.disablevillagertrade.forge.config.ForgeConfig;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Handles villager interaction events for Forge.
 * Uses Forge 60.x event handler pattern (no @SubscribeEvent annotation).
 */
public class VillagerTradeHandler {
    
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
        
        ForgeConfig config = DisableVillagerTradeForge.getConfig();
        
        String professionName = "NONE";
        if (abstractVillager instanceof Villager villager) {
            Holder<VillagerProfession> professionHolder = villager.getVillagerData().profession();
            professionName = professionHolder.unwrapKey()
                .map(key -> key.identifier().getPath().toUpperCase())
                .orElse("NONE");
        } else if (abstractVillager instanceof WanderingTrader) {
            if (config.isEnableWanderingTraderTrades()) {
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
        boolean shouldBlock = DisableVillagerTradeForge.getTradeBlocker().shouldBlockTrade(
            true,
            professionName,
            !abstractVillager.isNoAi(),      // hasAI is inverted
            !abstractVillager.isNoGravity(), // hasGravity is inverted
            dimensionName,
            config.getDisabledWorlds(),
            hasBypass,
            config.isEnableForOp(),
            isOp
        );
        
        if (shouldBlock) {
            // Cancel the interaction - use setCancellationResult for Forge 60.x
            event.setCancellationResult(InteractionResult.FAIL);
            
            if (config.isShakeHeadEnabled()) {
                abstractVillager.setUnhappyCounter(40);
            }
            
            if (abstractVillager instanceof WanderingTrader) {
                abstractVillager.playSound(net.minecraft.sounds.SoundEvents.WANDERING_TRADER_NO, 1.0F, abstractVillager.getVoicePitch());
            } else {
                abstractVillager.playSound(net.minecraft.sounds.SoundEvents.VILLAGER_NO, 1.0F, abstractVillager.getVoicePitch());
            }
            
            // Send message to player
            if (config.isMessageEnabled()) {
                player.sendSystemMessage(Component.literal(config.getMessage()));
            }
        }
    }
}
