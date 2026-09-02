package me.dodo.disablevillagertrade.common;

import java.util.List;

/**
 * Core business logic for determining if a villager trade should be blocked.
 * This class is platform-agnostic and can be used by Bukkit, Fabric, Forge, etc.
 */
public class TradeBlocker {
    
    /**
     * Determines if a trade interaction should be blocked.
     *
     * @param isVillager         Whether the entity is a villager
     * @param profession         The villager's profession name (e.g., "NONE", "FARMER")
     * @param hasAI              Whether the villager has AI
     * @param hasGravity         Whether the villager has gravity (used to detect custom villagers)
     * @param worldName          The name of the world/dimension
     * @param disabledWorlds     List of worlds where trading is allowed (not blocked)
     * @param hasBypassPermission Whether the player has bypass permission
     * @return true if the trade should be blocked, false otherwise
     */
    public boolean shouldBlockTrade(
            boolean isVillager,
            String profession,
            boolean hasAI,
            boolean hasGravity,
            String worldName,
            List<String> disabledWorlds,
            boolean hasBypassPermission,
            boolean enableForOp,
            boolean isOp
    ) {
        // Not a villager - don't block
        if (!isVillager) {
            return false;
        }
        
        // NONE profession villagers can be interacted with (unemployed villagers)
        if ("NONE".equals(profession)) {
            return false;
        }
        
        // World is in disabled list - don't block (trading allowed in disabled worlds)
        if (disabledWorlds != null && disabledWorlds.contains(worldName)) {
            return false;
        }
        
        // Check bypass
        if (hasBypassPermission) {
            // If the player is OP and enableForOp is true, the mod is enabled for them,
            // so they don't bypass just because they are OP (unless they have explicit permission
            // from a permissions plugin, but since vanilla OP gives permission implicitly,
            // we override it if enableForOp is true and they are OP).
            if (isOp && enableForOp) {
                // Do not bypass
            } else {
                return false;
            }
        }
        
        // Only block if villager has AI and gravity (normal villager)
        // Custom villagers (shops, NPCs) often have AI/gravity disabled
        return hasAI && hasGravity;
    }
}

