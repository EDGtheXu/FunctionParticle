package thexu.functionparticle.partical.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;
import thexu.functionparticle.registry.ParticleRegistry;

public class BaseParticleOptions implements ParticleOptions {
    private final float scale;
    private final Vector3f color;
    private final String exp;

    public BaseParticleOptions(Vector3f color, float scale,String exp) {
        this.scale = scale;
        this.color = color;
        this.exp = exp;

    }

    public BaseParticleOptions(float r, float g, float b, float scale,String exp) {
        this(new Vector3f(r, g, b), scale,exp);
    }



    private BaseParticleOptions(Vector4f vector4f,String exp) {
        this(vector4f.x, vector4f.y, vector4f.z, vector4f.w,exp);
    }

    public float getScale() {
        return this.scale;
    }
    public String getExp() {
        return this.exp;
    }

    public Vector3f getColor() {
        return this.color;
    }

    //For networking. Encoder/Decoder functions very intuitive
    public static StreamCodec<? super ByteBuf, BaseParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, option) -> {
                buf.writeFloat(option.color.x);
                buf.writeFloat(option.color.y);
                buf.writeFloat(option.color.z);
                buf.writeFloat(option.scale);
                buf.writeInt(option.exp.length());
                buf.writeBytes(option.exp.getBytes());
            },
            (buf) -> {
                float a1 = buf.readFloat();
                float a2 = buf.readFloat();
                float a3 = buf.readFloat();
                float a4 = buf.readFloat();
                int a5 = buf.readInt();
                String a6 = String.valueOf(buf.readBytes(a5));
                return new BaseParticleOptions(a1, a2, a3, a4,a6);
            }
    );

    //For command only?
    public static MapCodec<BaseParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object ->
            object.group(
                    Codec.FLOAT.fieldOf("r").forGetter(p -> p.color.x),
                    Codec.FLOAT.fieldOf("g").forGetter(p -> p.color.y),
                    Codec.FLOAT.fieldOf("b").forGetter(p -> p.color.z),
                    Codec.FLOAT.fieldOf("scale").forGetter(p ->  p.scale),
                    Codec.STRING.fieldOf("scale").forGetter(p ->  p.exp)
            ).apply(object, BaseParticleOptions::new
            ));
    public @NotNull ParticleType<BaseParticleOptions> getType() {
        return ParticleRegistry.BASE_PARTICLE.get();
    }
}
