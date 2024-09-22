package thexu.functionparticle.event;


import net.minecraft.client.particle.SnowflakeParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import thexu.functionparticle.partical.expParticle.BaseFunctionParticle;
import thexu.functionparticle.partical.type.*;
import thexu.functionparticle.registry.ParticleRegistry;

import static thexu.functionparticle.FunctionParticle.MODID;

@EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class registerParticalEvent {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
//        event.registerSpriteSet(ParticleRegistry.WISP_PARTICLE.get(), WispParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.BLOOD_GROUND_PARTICLE.get(), BloodGroundParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.ELECTRICITY_PARTICLE.get(), ElectricityParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.UNSTABLE_ENDER_PARTICLE.get(), UnstableEnderParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DRAGON_FIRE_PARTICLE.get(), DragonFireParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.FIRE_PARTICLE.get(), FireParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.EMBER_PARTICLE.get(), EmberParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.SIPHON_PARTICLE.get(), SiphonParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FOG_PARTICLE.get(), FogParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.BASE_PARTICLE.get(), BaseParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.BASE_FUNCTION_PARTICLE.get(), BaseFunctionParticle.Provider::new);

//        event.registerSpriteSet(ParticleRegistry.SHOCKWAVE_PARTICLE.get(), ShockwaveParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.ACID_PARTICLE.get(), AcidParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.ACID_BUBBLE_PARTICLE.get(), AcidBubbleParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.ZAP_PARTICLE.get(), ZapParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FIREFLY_PARTICLE.get(), FireflyParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.RING_SMOKE_PARTICLE.get(), RingSmokeParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.PORTAL_FRAME_PARTICLE.get(), PortalFrameParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.BLASTWAVE_PARTICLE.get(), BlastwaveParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.SPARK_PARTICLE.get(), SparkParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.SNOW_DUST.get(), SnowDustParticle.Provider::new);
//        event.registerSpriteSet(ParticleRegistry.CLEANSE_PARTICLE.get(), CleanseParticle.Provider::new);

    }
}
