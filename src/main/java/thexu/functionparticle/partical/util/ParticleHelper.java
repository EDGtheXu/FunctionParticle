package thexu.functionparticle.partical.util;



import net.minecraft.core.particles.ParticleOptions;
import org.joml.Vector3f;
import thexu.functionparticle.partical.expParticle.BaseFunctionOption;
import thexu.functionparticle.partical.expParticle.expEncoder;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.gener.ParticleGener;
import thexu.functionparticle.partical.type.BaseParticleOptions;
import thexu.functionparticle.partical.type.FogParticleOptions;
import thexu.functionparticle.registry.ParticleRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<expEncoder.Builder> exps = List.of(EXP_RANDOM);

    public static expEncoder.Builder EXP_RANDOM = new expEncoder.Builder()
            .add(expKeys.X,"x*sin(y)*cos(z)")
            .add(expKeys.Y,"x*cos(y)-1")
            .add(expKeys.Z,"x*sin(y)*sin(z)")
            .add(expKeys.VARIABLE_X,"1:1:1")
            .add(expKeys.VARIABLE_Y,"0:0.08:3.14")
            .add(expKeys.VARIABLE_Z,"0:0.08:6.28")

            .add(expKeys.EMIT_SPEED,"100")
            .add(expKeys.KEEP_SPEED,"")

            .add(expKeys.X_SPEED,"if(0.5,1,-1)*random(0,0.002)")
            .add(expKeys.Y_SPEED,"random(0,0.001)")
            .add(expKeys.Z_SPEED,"if(0.5,1,-1)*random(0,0.002)")

            .add(expKeys.T_LIFETIME,"100")
            .add(expKeys.T_COLOR, Color.yellow.getRGB()+"+3*x")
            .add(expKeys.T_SCALE,"0.03*(100-x)*0.01")
            .buildMap();

    public static expEncoder.Builder EXP_SQUARE = new expEncoder.Builder()
            .add(expKeys.X,"x*sin(y)*cos(z)")
            .add(expKeys.Y,"x*cos(y)")
            .add(expKeys.Z,"x*sin(y)*sin(z)")
            .add(expKeys.VARIABLE_X,"1:1:1")
            .add(expKeys.VARIABLE_Y,"0:0.1:3.14")
            .add(expKeys.VARIABLE_Z,"0:0.1:6.28")
            .add(expKeys.T_LIFETIME,"100")
            .add(expKeys.EMIT_SPEED,"10000")
            //.add(expKeys.T_SPEED_X,"0.2*x^0.2")
            //.add(expKeys.Y_SPEED,"random(0,1)-0.5")
            //.add(expKeys.Z_SPEED,"random(0,1)-0.5")
            .add(expKeys.T_COLOR, Color.yellow.getRGB()+"+x")
            .add(expKeys.T_SCALE,"0.1-0.001*x")
            .buildMap();


    public static expEncoder.Builder EXP_LOVE = new expEncoder.Builder()
            //.add(expKeys.X,"sin(x)")
            .add(expKeys.Y,"16*(sin(x))^3*0.1")
            .add(expKeys.Z,"(13*cos(x)-5cos(2*x)-2*cos(3*x)-cos(4*x))*0.1")
            .add(expKeys.VARIABLE_X,"0:0.02:6.28")

            .add(expKeys.EMIT_SPEED,"10000")
            //.add(expKeys.T_COLOR,"")
            .add(expKeys.T_SPEED_X,"0.2*x^0.2")
            //.add(expKeys.Y_SPEED,"random(0,1)-0.5")
            //.add(expKeys.Z_SPEED,"random(0,1)-0.5")

            .add(expKeys.T_COLOR, Color.red.getRGB() +"+x*0.00001*256*256*256")
            .add(expKeys.T_SCALE,"0.1-0.001*x")

            //.add(expKeys.EMIT_SPEED,"1000")
            .rotate(90,0,0)
            .buildMap();
}
