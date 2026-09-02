package me.dodo.disablevillagertrade.forge.events;

import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraftforge.event.entity.living.LivingEvent;

public class WanderingTraderTickHandler {
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof WanderingTrader trader) {
            if (trader.getUnhappyCounter() > 0) {
                trader.setUnhappyCounter(trader.getUnhappyCounter() - 1);
            }
        }
    }
}
