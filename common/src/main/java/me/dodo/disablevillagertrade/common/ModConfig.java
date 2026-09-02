package me.dodo.disablevillagertrade.common;

import java.util.List;

/**
 * Platform-agnostic configuration interface.
 * Each platform (Bukkit, Fabric, Forge) implements this to provide config values.
 */
public interface ModConfig {
    
    /**
     * Checks if the blocked message should be shown to players.
     * @return true if message is enabled
     */
    boolean isMessageEnabled();
    
    /**
     * Gets the message to show when trading is blocked.
     * @return the blocked trade message (may contain formatting codes)
     */
    String getMessage();
    
    /**
     * Gets the list of worlds/dimensions where villager trading is allowed.
     * @return list of world names where trading is NOT blocked
     */
    List<String> getDisabledWorlds();
    
    /**
     * Checks if the update checker is enabled.
     * @return true if update checker is enabled
     */
    boolean isUpdateCheckerEnabled();
    
    /**
     * Gets the update check interval in hours.
     * @return the check interval in hours
     */
    int getUpdateCheckInterval();
    
    /**
     * Checks if players should be notified on join.
     * @return true if notify on join is enabled
     */
    boolean isNotifyOnJoin();
    
    /**
     * Gets the update notification message.
     * @return the update message (may contain placeholders)
     */
    String getUpdateMessage();
    
    /**
     * Checks if the mod is active for OP players.
     * If false, OP players bypass the block.
     * @return true if OP players are also blocked from trading.
     */
    boolean isEnableForOp();

    /**
     * Checks if villagers should shake their head when trade is blocked.
     * @return true if head shaking is enabled
     */
    boolean isShakeHeadEnabled();

    /**
     * Checks if trading with wandering traders is enabled.
     * If false, wandering traders are blocked just like normal villagers.
     * @return true if trading with wandering traders is allowed
     */
    boolean isEnableWanderingTraderTrades();

    /**
     * Reloads the configuration from disk.
     */
    void reload();
}

