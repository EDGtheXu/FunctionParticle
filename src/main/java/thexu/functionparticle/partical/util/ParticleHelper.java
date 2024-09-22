package thexu.functionparticle.partical.util;



import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import thexu.functionparticle.partical.expParticle.BaseFunctionOption;
import thexu.functionparticle.partical.expParticle.expEncoder;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.expPointKeys;
import thexu.functionparticle.partical.gener.ParticleGener;
import thexu.functionparticle.partical.quickComputerKeys;
import thexu.functionparticle.partical.type.BaseParticleOptions;
import thexu.functionparticle.partical.type.FogParticleOptions;
import thexu.functionparticle.registry.ParticleRegistry;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ParticleHelper {
    //public static final ParticleOptions DRAGON_FIRE = ParticleRegistry.DRAGON_FIRE_PARTICLE.get();
    public static final ParticleOptions FIRE = ParticleRegistry.DRAGON_FIRE_PARTICLE.get();
    public static final ParticleOptions FIRE_EMITTER = ParticleRegistry.FIRE_PARTICLE.get();
    public static final ParticleOptions BLOOD = ParticleRegistry.BLOOD_PARTICLE.get();
    public static final ParticleOptions WISP = ParticleRegistry.WISP_PARTICLE.get();
    public static final ParticleOptions BLOOD_GROUND = ParticleRegistry.BLOOD_GROUND_PARTICLE.get();
    public static final ParticleOptions SNOWFLAKE = ParticleRegistry.SNOWFLAKE_PARTICLE.get();
    public static final ParticleOptions ELECTRICITY = ParticleRegistry.ELECTRICITY_PARTICLE.get();
    public static final ParticleOptions UNSTABLE_ENDER = ParticleRegistry.UNSTABLE_ENDER_PARTICLE.get();
    public static final ParticleOptions EMBERS = ParticleRegistry.EMBER_PARTICLE.get();
    public static final ParticleOptions SIPHON = ParticleRegistry.SIPHON_PARTICLE.get();
    public static final ParticleOptions ACID = ParticleRegistry.ACID_PARTICLE.get();
    public static final ParticleOptions ACID_BUBBLE = ParticleRegistry.ACID_BUBBLE_PARTICLE.get();
    public static final ParticleOptions FOG = new FogParticleOptions(new Vector3f(1, 1, 1), 1);
    public static final ParticleOptions VOID_TENTACLE_FOG = new FogParticleOptions(new Vector3f(0.0f, 0.075f, .13f), 2);
    public static final ParticleOptions ROOT_FOG = new FogParticleOptions(new Vector3f(61 / 255f, 40 / 255f, 18 / 255f), .4f);
    public static final ParticleOptions COMET_FOG = new FogParticleOptions(new Vector3f(.75f, .55f, 1f), 1.5f);
    public static final ParticleOptions FOG_THUNDER_LIGHT = new FogParticleOptions(new Vector3f(.5f, .5f, .5f), 1.5f);
    public static final ParticleOptions FOG_THUNDER_DARK = new FogParticleOptions(new Vector3f(.4f, .4f, .4f), 1.5f);
    public static final ParticleOptions POISON_CLOUD = new FogParticleOptions(new Vector3f(.08f, 0.64f, .16f), 1f);
    public static final ParticleOptions ICY_FOG = new FogParticleOptions(new Vector3f(208 / 255f, 249 / 255f, 255 / 255f), 0.6f);
    public static final ParticleOptions SUNBEAM = new FogParticleOptions(new Vector3f(0.95f, 0.97f, 0.36f), 1f);
    public static final ParticleOptions FIREFLY = ParticleRegistry.FIREFLY_PARTICLE.get();
    public static final ParticleOptions PORTAL_FRAME = ParticleRegistry.PORTAL_FRAME_PARTICLE.get();
//    public static final ParticleOptions FIERY_SPARKS = new SparkParticleOptions(new Vector3f(1, .6f, 0.3f));
//    public static final ParticleOptions ELECTRIC_SPARKS = new SparkParticleOptions(new Vector3f(0.333f, 1f, 1f));
    public static final ParticleOptions SNOW_DUST = ParticleRegistry.SNOW_DUST.get();
    public static final ParticleOptions CLEANSE_PARTICLE = ParticleRegistry.CLEANSE_PARTICLE.get();

    public static final ParticleOptions BASE_TEST = new BaseParticleOptions(new Vector3f(0.95f, 0.97f, 0.36f), 0.1f,"hello world!");
    public static final ParticleOptions BASE_FUNCTION_PARTICLE_OPTION = new BaseFunctionOption(0,0,0,"  ");

}
