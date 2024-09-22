package thexu.functionparticle.partical.expParticle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import thexu.functionparticle.registry.ParticleRegistry;

import java.nio.charset.StandardCharsets;

public class BaseFunctionOption implements ParticleOptions {
    private final String exp;
    private final double x;
    private final double y;
    private final double z;
    public BaseFunctionOption(double x,double y,double z,String exp) {
        this.x=x;this.y=y;this.z=z;
        this.exp = exp;
    }
    public String getExp() {
        return this.exp;
    }
    //For networking. Encoder/Decoder functions very intuitive
    public static StreamCodec<? super ByteBuf, BaseFunctionOption> STREAM_CODEC = StreamCodec.of(
            (buf, option) -> {
                buf.writeDouble(option.x);
                buf.writeDouble(option.y);
                buf.writeDouble(option.z);
                var bytes = option.exp.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            },
            (buf) -> {
                double x = buf.readDouble();
                double y = buf.readDouble();
                double z = buf.readDouble();
                int a1 = buf.readInt();
                String a2 = buf.readBytes(a1).toString(StandardCharsets.UTF_8);
                return new BaseFunctionOption(x,y,z,a2);
            }
    );

    //For command only?
    public static MapCodec<BaseFunctionOption> MAP_CODEC = RecordCodecBuilder.mapCodec(object ->
            object.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(p->p.x),
                    Codec.DOUBLE.fieldOf("y").forGetter(p->p.y),
                    Codec.DOUBLE.fieldOf("z").forGetter(p->p.z),

                    Codec.STRING.fieldOf("exp").forGetter(p ->  p.exp)
            ).apply(object, BaseFunctionOption::new
            ));
    public @NotNull ParticleType<BaseFunctionOption> getType() {
        return ParticleRegistry.BASE_FUNCTION_PARTICLE.get();
    }
}
