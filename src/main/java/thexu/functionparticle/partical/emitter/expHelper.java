package thexu.functionparticle.partical.emitter;

import thexu.functionparticle.partical.emitShape;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.expPointKeys;
import thexu.functionparticle.partical.quickComputerKeys;

import java.awt.*;
import java.util.List;


public class expHelper {


    public List<expEmitter.Builder> exps = List.of(EXP_RANDOM,EXP_LOVE,EXP_SQUARE,EXP_MOVE_SPHERE);


    //public Function<LivingEntity, expEmitter.Builder> EXP_RANDOM_FUNCTION = (entity -> EXP_RANDOM.add(expKeys.INIT_SPEED,forward.apply(entity)));



    //example
    public static final expEmitter.Builder EXP_RANDOM = new expEmitter.Builder()
            .addExpPoints(new expEmitter.Builder.ExpPoints()
                    .addExp(expPointKeys.X,"x*sin(y)*cos(z)")
                    .addExp(expPointKeys.Y,"x*cos(y)-1")
                    .addExp(expPointKeys.Z,"x*sin(y)*sin(z)")
                    .addExp(expPointKeys.VARIABLE_X,"1:1:1")
                    .addExp(expPointKeys.VARIABLE_Y,"0.2:0.05:3")
                    .addExp(expPointKeys.VARIABLE_Z,"0:0.05:6.28")
//                    .addExp(expPointKeys.VARIABLE_Y,"1:1:1")
//                    .addExp(expPointKeys.VARIABLE_Z,"1:1:1")
                    .compute()
            )
            .genShapePath(new expEmitter.Builder.ShapePoints()
                    .genCircle(20,0.1f)
                    .rotate(0,0,0)
                    .addToList()
            )

//            .emitSpeed(1f)
            //.emitCircle(true)
            .emitLife(1000)
            .emitShape(thexu.functionparticle.partical.emitShape.CENTER,0.25F)
            .add(expKeys.EMIT_SPEED,"1")


            .add(expKeys.INIT_LIFETIME,"500")

            .add(expKeys.KEEP_SPEED,"")
            //.add(expKeys.INIT_SPEED,"")

            .add(quickComputerKeys.SPEED_X_RANDOM,"0.001:0.5")
            .add(quickComputerKeys.SPEED_Y_RANDOM,"0.001:0.5")
            .add(quickComputerKeys.SPEED_Z_RANDOM,"0.001:0.5")

            .add(quickComputerKeys.COLOR_R_LERP, Color.CYAN.getRed()/256.0+":"+Color.red.getRed()/256.0)
            .add(quickComputerKeys.COLOR_G_LERP,Color.CYAN.getGreen()/256.0+":"+Color.red.getGreen()/256.0)
            .add(quickComputerKeys.COLOR_B_LERP,Color.CYAN.getBlue()/256.0+":"+Color.red.getBlue()/256.0)
        //init
            .add(quickComputerKeys.GRAVITY,"1")
            .add(quickComputerKeys.SIZE_LERP,"0.03:0")
            .add(quickComputerKeys.INIT_SIZE_RANDOM,"0.03:0.05")
            .add(quickComputerKeys.INIT_SPEED_X_RANDOM ,"-0.1:0.1")
            .add(quickComputerKeys.INIT_SPEED_Y_RANDOM ,"-0.1:0.1")
            .add(quickComputerKeys.INIT_SPEED_Z_RANDOM ,"-0.1:0.1")

            ;



    public static final expEmitter.Builder EXP_SQUARE = new expEmitter.Builder()
            .addExpPoints(new expEmitter.Builder.ExpPoints()
                    .addExp(expPointKeys.X,"x*sin(y)*cos(z)")
                    .addExp(expPointKeys.Y,"x*cos(y)")
                    .addExp(expPointKeys.Z,"x*sin(y)*sin(z)")
                    .addExp(expPointKeys.VARIABLE_X,"1:1:1")
                    .addExp(expPointKeys.VARIABLE_Y,"0:0.1:3.14")
                    .addExp(expPointKeys.VARIABLE_Z,"0:0.1:6.28")
                    .compute()
            )

            .add(expKeys.T_LIFETIME,"100")
            .add(expKeys.EMIT_SPEED,"100")


            .add(quickComputerKeys.COLOR_R_LERP, Color.orange.getRed()/256.0+":"+Color.red.getRed()/256.0)
            .add(quickComputerKeys.COLOR_G_LERP,Color.orange.getGreen()/256.0+":"+Color.red.getGreen()/256.0)
            .add(quickComputerKeys.COLOR_B_LERP,Color.orange.getBlue()/256.0+":"+Color.red.getBlue()/256.0)
            .add(expKeys.T_SCALE,"0.1-0.001*x")
            .buildMap();


    public static final expEmitter.Builder EXP_LOVE = new expEmitter.Builder()
            .addExpPoints(new expEmitter.Builder.ExpPoints()
                    .addExp(expPointKeys.Y,"16*(sin(x))^3*0.1")
                    .addExp(expPointKeys.Z,"(13*cos(x)-5cos(2*x)-2*cos(3*x)-cos(4*x))*0.1")
                    .addExp(expPointKeys.VARIABLE_X,"0:0.02:6.28")
                    .compute()
            )
            .add(expKeys.EMIT_SPEED,"10000")
            .add(expKeys.T_SPEED_X,"0.2*x^0.2")
            .add(expKeys.T_COLOR, Color.red.getRGB() +"+x*0.00001*256*256*256")
            .add(expKeys.T_SCALE,"0.1-0.001*x")
            .rotate(90,0,0)
            .buildMap();



    public static final expEmitter.Builder EXP_MOVE_SPHERE = new expEmitter.Builder()
            .addExpPoints(new expEmitter.Builder.ExpPoints()
                            .addExp(expPointKeys.X,"x*sin(y)*cos(z)")
                            .addExp(expPointKeys.Y,"x*cos(y)")
                            .addExp(expPointKeys.Z,"x*sin(y)*sin(z)")
                            .addExp(expPointKeys.VARIABLE_X,"0:0.0001:0.01")
                            .addExp(expPointKeys.VARIABLE_Y,"0:0.1:3")
                            .addExp(expPointKeys.VARIABLE_Z,"0:0.1:6.28")
//                    .addExp(expPointKeys.VARIABLE_Y,"1:1:1")
//                    .addExp(expPointKeys.VARIABLE_Z,"1:1:1")
                            .compute()
            )
            /*
            .genShapePath(new expEmitter.Builder.ShapePoints().genCircle(5,1)
                    .rotate(0,0,0)
                    .addToList())
            */

            .genExpPath(new expEmitter.Builder.ExpPoints()
                    .addExp(expPointKeys.Y,"16*(sin(x))^3*0.1*3")
                    .addExp(expPointKeys.Z,"((13*cos(x)-5cos(2*x)-2*cos(3*x)-cos(4*x))*0.1)*3")
                    .addExp(expPointKeys.VARIABLE_X,"0:0.1:6.28")
                    .compute()
                    .rotate(90,0,0)
            )

            .emitShape(emitShape.CENTER,0.1F)
            //.add(expKeys.KEEP_SPEED,"")

            //.emitLife(1000)
            .emitMoveSpeed(0.1f)
            .emitRandom(true)
            //.emitCircle(true)


            .add(expKeys.INIT_LIFETIME,"500")
            .add(expKeys.EMIT_SPEED,"20")


            //.add(expKeys.INIT_SPEED,"")
/*
            .add(quickComputerKeys.SPEED_X_RANDOM,"0.01:0.5")
            .add(quickComputerKeys.SPEED_Y_RANDOM,"0.01:0.6")
            .add(quickComputerKeys.SPEED_Z_RANDOM,"0.01:0.5")
*/
            .add(quickComputerKeys.COLOR_R_LERP, Color.orange.getRed()/256.0+":"+Color.red.getRed()/256.0)
            .add(quickComputerKeys.COLOR_G_LERP,Color.orange.getGreen()/256.0+":"+Color.red.getGreen()/256.0)
            .add(quickComputerKeys.COLOR_B_LERP,Color.orange.getBlue()/256.0+":"+Color.red.getBlue()/256.0)
            //init
            .add(quickComputerKeys.SIZE_LERP,"0.2:0.2")
            .add(quickComputerKeys.FRICTION,"0.95")
/*
            .add(quickComputerKeys.INIT_SPEED_X_RANDOM ,"-0.1:0.1")
            .add(quickComputerKeys.INIT_SPEED_Y_RANDOM ,"-0.1:0.1")
            .add(quickComputerKeys.INIT_SPEED_Z_RANDOM ,"-0.1:0.1")
            */
            ;




}
