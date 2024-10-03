package thexu.functionparticle.partical.emitter;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;
import java.util.function.Function;

public class emitShape {

    //向前发射
    public static final Function<LivingEntity, String> forward = (entity -> {
        var v = entity.getForward().normalize();
        return v.x + ":" + v.y + ":" + v.z;
    });

    //四周发射
/*
    public static final Function<Vec3, String> center= (v -> {
        var v = entity.getForward().normalize();
        return v.x + ":" + v.y + ":" + v.z;
    });
*/



}
