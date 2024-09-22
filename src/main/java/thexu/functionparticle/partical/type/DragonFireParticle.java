package thexu.functionparticle.partical.type;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Math;
import org.joml.Vector3f;

import java.awt.*;

public class DragonFireParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final boolean mirrored;
    Vector3f speedInit;
    Vector3f posInit;
    float maxRange = 10f;
    float rotAngle = 30f;

    public DragonFireParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {

        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        speedInit = new Vector3f((float) xd, (float) yd, (float) zd).normalize();
        posInit = new Vector3f((float) xCoord, (float) yCoord, (float) zCoord);

/*
        //设置粒子初始旋转
        Vector3f curSpeed = new Vector3f((float) (xd), (float) (yd), (float) zd);
        curSpeed=curSpeed.add(curSpeed.cross((float) (yd * Math.random()), (float) ((float) zd * Math.random()), (float) ((float) xd * Math.random())).normalize().mul(0.2f));
        this.xd = curSpeed.x ;
        this.yd = curSpeed.y ;
        this.zd = curSpeed.z ;
*/



        this.scale(this.random.nextFloat() * 1.75f + 1f);
        this.lifetime = 10 + (int) (Math.random() * 10);
        sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        //this.gravity = -0.015F;
        this.mirrored = this.random.nextBoolean();

    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();

        /*
        this.xd += this.random.nextFloat() / 5.0F * (float) (this.random.nextBoolean() ? 1 : -1);
        this.yd += this.random.nextFloat() / 10.0F;
        this.zd += this.random.nextFloat() / 5.0F * (float) (this.random.nextBoolean() ? 1 : -1);
*/

/*
        Vector3f curPos = new Vector3f((float) xd, (float) yd, (float) zd);
        new Matrix4f()
                .rotate(Math.toRadians(rotAngle), speedInit)
                .transformPosition(curPos);

        this.xd = curPos.x ;
        this.yd = curPos.y ;
        this.zd = curPos.z ;
*/


        this.rotAngle += 10;
        this.setSpriteFromAge(this.sprites);

        Color from = Color.ORANGE;
        Color to = Color.CYAN;
        //this.setColor(0,0,from.getBlue()*1.0f/255);
        System.out.println((float)from.getRed()/255F+" " + (float)to.getRed()/255F+" " + (float) this.age /this.lifetime );

        this.setColor(
                Math.lerp((float)from.getRed()/255F,(float)to.getRed()/255F, (float) this.age /this.lifetime),
                Math.lerp((float)from.getGreen()/255F,(float)to.getGreen()/255F, (float) this.age /this.lifetime),
                Math.lerp((float)from.getBlue()/255F,(float)to.getBlue()/255F, (float) this.age /this.lifetime)
        );
        this.alpha = Math.lerp(1,0, (float) this.age /this.lifetime);
        this.setSpriteFromAge(this.sprites);
        if (age > lifetime * .5f && this.random.nextFloat() <= .35f) {
            this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        }

    }

    @Override
    protected float getU0() {
        return mirrored ? super.getU1() : super.getU0();
    }

    @Override
    protected float getU1() {
        return mirrored ? super.getU0() : super.getU1();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new DragonFireParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    public int getLightColor(float p_107564_) {
        return 240;
    }

    @Override
    public float getQuadSize(float p_107567_) {
        float f = ((float) this.age + p_107567_) / (float) this.lifetime;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        return this.quadSize * f;
    }
}
