package me.dodo.disablevillagertrade.fabric.mixin;

import me.dodo.disablevillagertrade.common.Constants;
import me.dodo.disablevillagertrade.fabric.DisableVillagerTradeFabric;
import me.dodo.disablevillagertrade.fabric.config.FabricConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to intercept wandering trader interactions and block trading.
 */
@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {
    
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        // Only process on server side
        if (player.level().isClientSide()) {
            return;
        }
        
        WanderingTrader trader = (WanderingTrader) (Object) this;
        FabricConfig config = DisableVillagerTradeFabric.getConfig();
        
        // Check if wandering trader trades are enabled
        if (config.isEnableWanderingTraderTrades()) {
            return;
        }
        
        // Get dimension name
        String dimensionName = player.level().dimension().identifier().toString();
        
        // Check bypass permission (only for server players)
        boolean hasBypass = false;
        boolean isOp = false;
        if (player instanceof ServerPlayer serverPlayer) {
            hasBypass = DisableVillagerTradeFabric.hasPermission(serverPlayer, Constants.PERMISSION_BYPASS);
            isOp = serverPlayer.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_GAMEMASTER); // Level 2 is OP
        }
        
        // Check if trade should be blocked
        // We pass "WANDERING_TRADER" as profession to bypass NONE checks, but shouldBlockTrade is just logic.
        boolean shouldBlock = DisableVillagerTradeFabric.getTradeBlocker().shouldBlockTrade(
            true,
            "WANDERING_TRADER",
            !trader.isNoAi(),       // hasAI
            !trader.isNoGravity(),  // hasGravity
            dimensionName,
            config.getDisabledWorlds(),
            hasBypass,
            config.isEnableForOp(),
            isOp
        );
        
        if (shouldBlock) {
            // Cancel the interaction
            cir.setReturnValue(InteractionResult.FAIL);
            
            if (config.isShakeHeadEnabled()) {
                trader.setUnhappyCounter(40);
            }
            
            trader.playSound(net.minecraft.sounds.SoundEvents.WANDERING_TRADER_NO, 1.0F, trader.getVoicePitch());
            
            // Send message to player
            if (config.isMessageEnabled() && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.literal(config.getMessage()));
            }
        }
    }
}
