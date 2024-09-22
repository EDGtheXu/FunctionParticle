package thexu.functionparticle.partical.util;

import net.minecraft.world.entity.LivingEntity;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.expParticle.expEncoder;
import thexu.functionparticle.partical.expPointKeys;
import thexu.functionparticle.partical.quickComputerKeys;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class expHelper {


    public List<expEncoder.Builder> exps = List.of(EXP_RANDOM);



    public static final Function<LivingEntity,String> forward = (entity -> {
        var v = entity.getForward().normalize();
        return v.x + ":" + v.y + ":" + v.z;
    });


    public Function<LivingEntity, expEncoder.Builder> EXP_RANDOM_FUNCTION = (entity -> EXP_RANDOM.add(expKeys.INIT_SPEED,forward.apply(entity)));

    public static expEncoder.Builder EXP_RANDOM = new expEncoder.Builder()
            .addExpPoints(new expEncoder.Builder.ExpPoints()
                    .addExp(expPointKeys.X,"x*sin(y)*cos(z)")
                    .addExp(expPointKeys.Y,"x*cos(y)-1")
                    .addExp(expPointKeys.Z,"x*sin(y)*sin(z)")
                    .addExp(expPointKeys.VARIABLE_X,"1:1:1")
                    .addExp(expPointKeys.VARIABLE_Y,"0:0.05:3.14")
                    .addExp(expPointKeys.VARIABLE_Z,"0:0.05:6.28")
                    .confirm()
            )

            .add(expKeys.INIT_LIFETIME,"100")
            .add(expKeys.EMIT_SPEED,"5000")
            .add(expKeys.KEEP_SPEED,"")
            //.add(expKeys.INIT_SPEED,"")

            .add(quickComputerKeys.SPEED_X_RANDOM,"0.02:0.5")
            .add(quickComputerKeys.SPEED_Y_RANDOM,"0.005:0.8")
            .add(quickComputerKeys.SPEED_Z_RANDOM,"0.02:0.5")

            .add(quickComputerKeys.COLOR_R_LERP, Color.CYAN.getRed()/256.0+":"+Color.red.getRed()/256.0)
            .add(quickComputerKeys.COLOR_G_LERP,Color.CYAN.getGreen()/256.0+":"+Color.red.getGreen()/256.0)
            .add(quickComputerKeys.COLOR_B_LERP,Color.CYAN.getBlue()/256.0+":"+Color.red.getBlue()/256.0)

            .add(quickComputerKeys.SIZE_LERP,"0.03:0");

    public static expEncoder.Builder EXP_SQUARE = new expEncoder.Builder()
            .addExpPoints(new expEncoder.Builder.ExpPoints()
                    .addExp(expPointKeys.X,"x*sin(y)*cos(z)")
                    .addExp(expPointKeys.Y,"x*cos(y)")
                    .addExp(expPointKeys.Z,"x*sin(y)*sin(z)")
                    .addExp(expPointKeys.VARIABLE_X,"1:1:1")
                    .addExp(expPointKeys.VARIABLE_Y,"0:0.1:3.14")
                    .addExp(expPointKeys.VARIABLE_Z,"0:0.1:6.28")
                    .confirm()
            )
            .add(expKeys.T_LIFETIME,"100")
            .add(expKeys.EMIT_SPEED,"10000")

            .add(expKeys.T_COLOR, Color.yellow.getRGB()+"+x")
            .add(expKeys.T_SCALE,"0.1-0.001*x")
            .buildMap();


    public static expEncoder.Builder EXP_LOVE = new expEncoder.Builder()
            .addExpPoints(new expEncoder.Builder.ExpPoints()
                    .addExp(expPointKeys.Y,"16*(sin(x))^3*0.1")
                    .addExp(expPointKeys.Z,"(13*cos(x)-5cos(2*x)-2*cos(3*x)-cos(4*x))*0.1")
                    .addExp(expPointKeys.VARIABLE_X,"0:0.02:6.28")
                    .confirm()
            )
            .add(expKeys.EMIT_SPEED,"10000")
            .add(expKeys.T_SPEED_X,"0.2*x^0.2")
            .add(expKeys.T_COLOR, Color.red.getRGB() +"+x*0.00001*256*256*256")
            .add(expKeys.T_SCALE,"0.1-0.001*x")
            .rotate(90,0,0)
            .buildMap();
}
