package thexu.functionparticle.partical.type;

import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import thexu.functionparticle.partical.expParticle.BaseFunctionOption;

public class BaseParticle extends TextureSheetParticle {
    private static final Vector3f ROTATION_VECTOR = Util.make(new Vector3f(0.5F, 0.5F, 0.5F), Vector3f::normalize);
    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);
    private static final float DEGREES_90 = Mth.PI / 2f;

    BaseParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd, BaseParticleOptions options) {
        super(pLevel, pX, pY, pZ, xd, yd, zd);

//        this.xd = xd;
//        this.yd = yd;
//        this.zd = zd;

//        this.quadSize = 1.5f * options.getScale();
        this.lifetime = (int) (Math.random()*60+60);
//        this.gravity = .1f;
        float f = random.nextFloat() * 0.14F + 0.85F;
        this.rCol = options.getColor().x() * f;
        this.gCol = options.getColor().y() * f;
        this.bCol = options.getColor().z() * f;
        this.friction = 1;

        this.scale(options.getScale());
    }
    BaseParticle(ClientLevel pLevel, BaseFunctionOption options) {
        super(pLevel, 0, 0, 0, 0, 0, 0);
        //解析器


    }


    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.quadSize * (1 + Mth.clamp((this.age + pScaleFactor) / (float) this.lifetime * 0.75F, 0.0F, 1.0F)) * Mth.clamp(age / 5f, 0, 1);
    }


    private float noise(float offset) {
        float f = 10 * Mth.sin(offset * .01f);
        return f;
    }

/*
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialticks) {

        //Copied from Shriek Particle

        this.alpha = 1.0F - Mth.clamp(((float) this.age + partialticks - 20) / (float) this.lifetime, 0.2F, .7F);

//        this.renderBillboard(buffer, camera, partialticks);
        this.renderRotatedParticle(buffer, camera, partialticks, (p_234005_) -> {
            p_234005_.mul(Axis.YP.rotation(0));
            p_234005_.mul(Axis.XP.rotation(-DEGREES_90));
        });
        this.renderRotatedParticle(buffer, camera, partialticks, (p_234000_) -> {
            p_234000_.mul(Axis.YP.rotation(-(float) Math.PI));
            p_234000_.mul(Axis.XP.rotation(DEGREES_90));
        });
    }
*/
/*
    private void renderRotatedParticle(VertexConsumer pConsumer, Camera camera, float partialTick, Consumer<Quaternionf> pQuaternion) {
        Vec3 vec3 = camera.getPosition();
//        Vec3 zFightHack = camera.getPosition().subtract(this.x, this.y, this.z);
//        zFightHack = zFightHack.multiply(1f, 0.75f, 1f);
//        vec3 = zFightHack.add(this.x, this.y, this.z);
        float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
        Quaternionf quaternion = (new Quaternionf()).setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());

        pQuaternion.accept(quaternion);
        quaternion.transform(TRANSFORM_VECTOR);

        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f3 = this.getQuadSize(partialTick);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }

        int j = this.getLightColor(partialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.getU1(), this.getV1(), j);
        this.makeCornerVertex(pConsumer, avector3f[1], this.getU1(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[2], this.getU0(), this.getV0(), j);
        this.makeCornerVertex(pConsumer, avector3f[3], this.getU0(), this.getV1(), j);
    }


 */
    /*
    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float p_233996_, float p_233997_, int p_233998_) {
        Vec3 wiggle = new Vec3(noise((float) (age + this.x)), noise((float) (age - this.x)), noise((float) (age + this.z))).scale(.02f);
        pConsumer.addVertex(pVec3f.x() + (float) wiggle.x, pVec3f.y() + .08f + alpha * .125f, pVec3f.z() + (float) wiggle.z).setUv(p_233996_, p_233997_).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p_233998_);
    }*/

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<BaseParticleOptions> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull BaseParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BaseParticle shriekparticle = new BaseParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
            shriekparticle.pickSprite(this.sprite);
            shriekparticle.setAlpha(1.0F);
            return shriekparticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FunctionProvider implements ParticleProvider<BaseParticleOptions> {
        private final SpriteSet sprite;

        public FunctionProvider (SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull BaseParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BaseParticle shriekparticle = new BaseParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
            shriekparticle.pickSprite(this.sprite);
            shriekparticle.setAlpha(1.0F);
            return shriekparticle;
        }
    }

    @Override
    public int getLightColor(float p_107564_) {
        return 240;
    }


}
