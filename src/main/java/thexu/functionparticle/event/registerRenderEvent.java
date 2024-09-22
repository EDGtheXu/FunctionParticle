package thexu.functionparticle.event;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import thexu.functionparticle.registry.ModEntities;
import thexu.functionparticle.partical.gener.emitRenderer;


import static thexu.functionparticle.FunctionParticle.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class registerRenderEvent {

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //particle
        event.registerEntityRenderer(ModEntities.PARTICLE_EMITTER.get(), emitRenderer::new);


    }

}
