package me.dodo.disablevillagertrade.fabric.mixin;

import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public class WanderingTraderTickMixin {
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onAiStep(CallbackInfo ci) {
        WanderingTrader trader = (WanderingTrader) (Object) this;
        if (trader.getUnhappyCounter() > 0) {
            trader.setUnhappyCounter(trader.getUnhappyCounter() - 1);
        }
    }
}
