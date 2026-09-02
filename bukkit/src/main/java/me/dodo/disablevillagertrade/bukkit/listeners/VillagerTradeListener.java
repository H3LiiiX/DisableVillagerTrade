package me.dodo.disablevillagertrade.bukkit.listeners;

import me.dodo.disablevillagertrade.bukkit.DisableVillagerTrade;
import me.dodo.disablevillagertrade.bukkit.config.BukkitConfig;
import me.dodo.disablevillagertrade.common.Constants;
import me.dodo.disablevillagertrade.common.TradeBlocker;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.Sound;

/**
 * Listens for player interactions with villagers and blocks trading when configured.
 */
public class VillagerTradeListener implements Listener {
    
    private final DisableVillagerTrade plugin;
    private final TradeBlocker tradeBlocker;

    public VillagerTradeListener(DisableVillagerTrade plugin) {
        this.plugin = plugin;
        this.tradeBlocker = new TradeBlocker();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractVillager)) {
            return;
        }
        
        AbstractVillager abstractVillager = (AbstractVillager) event.getRightClicked();
        Player player = event.getPlayer();
        BukkitConfig config = plugin.getPluginConfig();
        
        String professionName = "NONE";
        
        if (abstractVillager instanceof Villager) {
            professionName = ((Villager) abstractVillager).getProfession().name();
        } else if (abstractVillager instanceof WanderingTrader) {
            if (config.isEnableWanderingTraderTrades()) {
                return;
            }
            professionName = "WANDERING_TRADER";
        } else {
            return;
        }
        
        boolean shouldBlock = tradeBlocker.shouldBlockTrade(
            true,
            professionName,
            abstractVillager.hasAI(),
            abstractVillager.hasGravity(),
            player.getWorld().getName(),
            config.getDisabledWorlds(),
            player.hasPermission(Constants.PERMISSION_BYPASS),
            config.isEnableForOp(),
            player.isOp()
        );
        
        if (shouldBlock) {
            event.setCancelled(true);
            
            if (config.isShakeHeadEnabled()) {
                if (abstractVillager instanceof Villager) {
                    ((Villager) abstractVillager).shakeHead();
                }
            }
            
            // Play standard sound
            if (abstractVillager instanceof WanderingTrader) {
                abstractVillager.getWorld().playSound(abstractVillager.getLocation(), Sound.ENTITY_WANDERING_TRADER_NO, 1.0F, 1.0F);
            } else {
                abstractVillager.getWorld().playSound(abstractVillager.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            }
            
            if (config.isMessageEnabled()) {
                String message = ChatColor.translateAlternateColorCodes('&', config.getMessage());
                player.sendMessage(message);
            }
        }
    }
}
