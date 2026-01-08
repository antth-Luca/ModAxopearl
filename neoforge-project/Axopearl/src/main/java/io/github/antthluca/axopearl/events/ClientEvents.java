package io.github.antthluca.axopearl.events;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.init.InitMenuTypes;
import io.github.antthluca.axopearl.screens.EletricAnvilScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Axopearl.MODID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(InitMenuTypes.ELETRIC_ANVIL_MENU.get(), EletricAnvilScreen::new);
    }
}
