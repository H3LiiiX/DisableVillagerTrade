package me.dodo.disablevillagertrade.neoforge.events;

import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class WanderingTraderTickHandler {
    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof WanderingTrader trader) {
            if (trader.getUnhappyCounter() > 0) {
                trader.setUnhappyCounter(trader.getUnhappyCounter() - 1);
            }
        }
    }
}
