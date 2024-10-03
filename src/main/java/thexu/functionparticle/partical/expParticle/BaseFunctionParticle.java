package thexu.functionparticle.partical.expParticle;

import com.mojang.math.MatrixUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import thexu.functionparticle.partical.CoordinateSystem;
import thexu.functionparticle.partical.emitShape;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.quickComputerKeys;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class BaseFunctionParticle extends TextureSheetParticle {
    private Map<String,String> map;

    private boolean local = false;
    private boolean init_speed = false;
    private Float SIZE_INIT;

    //中心坐标初始位置
    private double xInit;
    private double yInit;
    private double zInit;

    //用于重新计算位置平移旋转
    private double xdCache;
    private double ydCache;
    private double zdCache;

    //用于保存中心坐标位置
    private double xCenter;
    private double yCenter;
    private double zCenter;

    private boolean keepSpeed = false;

    private double directionXInit;
    private double directionYInit;

    private Expression xdExp;
    private Expression ydExp;
    private Expression zdExp;

    private Expression xdtExp;
    private Expression ydtExp;
    private Expression zdtExp;

    private Expression offsetXExp;
    private Expression offsetYdExp;
    private Expression offsetZdExp;

    private Expression xyzColorExp;
    private Expression tColorExp;


    private Expression xyzScaleExp;
    private Expression tScaleExp;

    private Expression xyzLifeTimeExp;
    private Expression tLifeTimeExp;


    List<Function> functions = new ArrayList<>();

    public final Function distance = new Function("distance",0) {
        @Override
        public double apply(double... args) {
            return Math.sqrt(xCenter*xCenter+yCenter*yCenter+zCenter*zCenter);
        }
    };

    public final Function randomExp = new Function("randomExp",2) {
        @Override
        public double apply(double... args) {
            return Math.random()*(args[1]-args[0])+args[0];
        }
    };

    public final Function expMax = new Function("if", 3) {
        @Override
        public double apply(double... args) {
            if(args[0]>Math.random())
                return args[1];
            return args[2];
        }
    };

    public final BiFunction<Float,Float,Float> randomFunc =
            (a,b)->random.nextFloat()* a* (random.nextFloat()<b?1:-1);
    public final java.util.function.Function<float[], Float> lifeLerpFunc =
            (a)-> (float) (a[0]+(a[1]-a[0])*(age*1.0 /lifetime));



    List<Consumer<BaseFunctionParticle>> activeExpressions = new ArrayList<>();


    BaseFunctionParticle(ClientLevel pLevel,double x,double y,double z ,BaseFunctionOption options) {
        super(pLevel, x, y, z, 0, 0, 0);

        functions.addAll(List.of(distance, randomExp,expMax));

        this.xd=0;this.yd=0;this.zd=0;

        this.lifetime = 100;
        this.quadSize = 0.1f;
        //解析器
        map = Decoder(options.getExp());
        this.gravity = 0;

        //初始化信息
        initAttributes(map);
        this.xCenter = x - xInit;

        this.yCenter = y - yInit;
        this.zCenter = z - zInit;

        if(init_speed && keepSpeed){
            if(map.containsKey(emitShape.CENTER.name())) {
                var dir = new Vec3(xCenter,yCenter,zCenter).normalize().scale(Double.parseDouble(map.get("EMIT_POWER")));
                this.xdCache += dir.x;
                this.ydCache += dir.y;
                this.zdCache += dir.z;

            }
            else{
                String[] sp = map.get(expKeys.INIT_SPEED.name()).split(":");
                this.xdCache += Double.parseDouble(sp[0]);
                this.ydCache += Double.parseDouble(sp[1]);
                this.zdCache += Double.parseDouble(sp[2]);
            }
        }

        //初始化颜色
        activeExpressions.forEach(a->a.accept(this));
    }

    @Override
    public void tick(){

        //重置速度
        if(!keepSpeed){
            this.xdCache = 0;
            this.ydCache = 0;
            this.zdCache = 0;
        }

        //应用函数效果
        activeExpressions.forEach(a->a.accept(this));

        //提前移除
        if(this.quadSize<=0) removed = true;

        this.xdCache*=friction;
        this.ydCache*=friction;
        this.zdCache*=friction;

        if(local){// 如果是本地坐标系，更新速度cache旋转后的速度
            Vector3f rotd = new Vector3f((float) this.xdCache, (float) this.ydCache, (float) this.zdCache);
            var ma1 = new Matrix4f().rotate((float) (-Math.toRadians(directionXInit)/2),new Vector3f(0,0,1));
            ma1.transformPosition(rotd);
            var m2 = new Matrix4f().rotate((float) Math.toRadians(-90-directionYInit),new Vector3f(0,1,0));
            m2.transformPosition(rotd);

            this.xd = rotd.x;
            this.yd = rotd.y;
            this.zd = rotd.z;
        }else{//否则直接更新
            this.xd = xdCache;
            this.yd = ydCache;
            this.zd = zdCache;
        }

        //更新位置缓存
        this.xCenter+=xdCache;
        this.yCenter+=ydCache;
        this.zCenter+=zdCache;

        super.tick();

        //距离过远直接舍弃
        if((x-xInit*x-xInit+y-yInit*y-yInit+z-zInit*z-zInit) > 200) remove();
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<BaseFunctionOption> {
        private final SpriteSet sprite;

        public Provider (SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull BaseFunctionOption options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BaseFunctionParticle shriekparticle = new BaseFunctionParticle(pLevel,pX,pY,pZ, options);
            shriekparticle.pickSprite(this.sprite);
            shriekparticle.setAlpha(1.0F);
            return shriekparticle;
        }
    }

    @Override
    public int getLightColor(float p_107564_) {
        return 240;
    }


    public Map<String,String> Decoder(String exp){
        Map<String,String> kv = new LinkedHashMap<>();
        var splits = exp.split(";");
        for(String s : splits){
            var sp = s.split(":",2);
            kv.put(sp[0],sp[1]);
        }
        return kv;
    }

    public void initAttributes(Map<String,String> map){
        Set<String> set=map.keySet();
        boolean size_lerp_init = false;

        for (var n : set) {
            String v = map.get(n);
            var sp = v.split(":");

                /* init */
            if(n.equals("INIT_POS")){
                this.xInit = Double.parseDouble(sp[0]);
                this.yInit = Double.parseDouble(sp[1]);
                this.zInit = Double.parseDouble(sp[2]);

            }else if(n.equals(expKeys.LOCAL.name())) {//转向到本地坐标
                this.directionXInit = Double.parseDouble(sp[0]);
                this.directionYInit = Double.parseDouble(sp[1]);
                local = true;
            }else if(n.equals(expKeys.INIT_SPEED.name())){
                init_speed = true;


            }else if(n.equals(expKeys.INIT_LIFETIME.name())){
                this.lifetime = (int) new ExpressionBuilder(v).variables("x","y","z").functions(functions).build()
                        .setVariable("x",x).setVariable("y",y).setVariable("z",z).evaluate();

                /* color */
            }else if(n.equals(expKeys.X_Y_ZCOLOR.name())){
                this.xyzColorExp = new ExpressionBuilder(v).variables("x","y","z").functions(functions).build();
                activeExpressions.add( p -> {
                    int color = (int) xyzColorExp.setVariable("x", p.xCenter).setVariable("y", p.yCenter).setVariable("z", p.zCenter).evaluate();
                    p.bCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.gCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.rCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.alpha = (float) ((color & 255) / 255.0);
                });
            }else if(n.equals(expKeys.T_COLOR.name())){
                this.tColorExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add( p -> {
                    int color = (int) tColorExp.setVariable("x", p.age).evaluate();
                    p.bCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.gCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.rCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.alpha = (float) ((color & 255) / 255.0);
                });

                /* force */
            }else if(n.equals(expKeys.OFFSET_X.name())){
                this.offsetXExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add( p -> p.xdCache += offsetXExp.setVariable("x", p.xCenter).evaluate());
            }else if(n.equals(expKeys.OFFSET_Y.name())){
                this.offsetYdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add( p -> p.ydCache += offsetYdExp.setVariable("x", p.yCenter).evaluate());
            }else if(n.equals(expKeys.OFFSET_Z.name())){
                this.offsetZdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.zdCache += offsetZdExp.setVariable("x", p.zCenter).evaluate());

                /* speed */
            }else if(n.equals(expKeys.X_SPEED.name())){
                this.xdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.xdCache += xdExp.setVariable("x", p.xCenter).evaluate());
            }else if(n.equals(expKeys.Y_SPEED.name())){
                this.ydExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.ydCache += ydExp.setVariable("x", p.yCenter).evaluate());
            }else if(n.equals(expKeys.Z_SPEED.name())){
                this.zdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add( p -> p.zdCache += zdExp.setVariable("x", p.zCenter).evaluate());

            }else if(n.equals(expKeys.T_SPEED_X.name())){
                this.xdtExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.xdCache += xdtExp.setVariable("x", age).evaluate());
            }else if(n.equals(expKeys.T_SPEED_Y.name())){
                this.ydtExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.ydCache += ydtExp.setVariable("x", age).evaluate());
            }else if(n.equals(expKeys.T_SPEED_Z.name())) {
                this.zdtExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.zdCache += zdtExp.setVariable("x", age).evaluate());
            }else if(n.equals(expKeys.KEEP_SPEED.name())){
                keepSpeed = true;

                /* lifetime */
            }else if(n.equals(expKeys.X_Y_Z_LIFETIME.name())){
                this.xyzLifeTimeExp = new ExpressionBuilder(v).variables("x","y","z").functions(functions).build();
                activeExpressions.add(p -> p.lifetime = (int) xyzLifeTimeExp.setVariable("x", p.xCenter).setVariable("y", p.yCenter).setVariable("z", p.zCenter).evaluate());
            }else if(n.equals(expKeys.T_LIFETIME.name())){
                this.tLifeTimeExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.lifetime = (int) tLifeTimeExp.setVariable("x", p.age).evaluate());

                /* scale */
            }else if(n.equals(expKeys.X_Y_Z_SCALE.name())){
                this.xyzScaleExp = new ExpressionBuilder(v).variables("x","y","z").functions(functions).build();
                activeExpressions.add(p -> p.quadSize = (float) xyzLifeTimeExp.setVariable("x", p.xCenter).setVariable("y", p.yCenter).setVariable("z", p.zCenter).evaluate());
            }else if(n.equals(expKeys.T_SCALE.name())){
                this.tScaleExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.add(p -> p.quadSize = (float) tScaleExp.setVariable("x", p.age).evaluate());
            }
            /** BUILTIN **/
            else if(n.equals(quickComputerKeys.SPEED_X_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                activeExpressions.add(p -> p.xdCache += randomFunc.apply(sp0,sp1));
            }
            else if(n.equals(quickComputerKeys.SPEED_Y_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                activeExpressions.add(p -> p.ydCache += randomFunc.apply(sp0,sp1));
            }
            else if(n.equals(quickComputerKeys.SPEED_Z_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                activeExpressions.add(p -> p.zdCache += randomFunc.apply(sp0,sp1));
            }
            /** BUILTIN COLOR**/
            else if(n.equals(quickComputerKeys.COLOR_R_LERP.name())){
                float[] sps = new float[]{Float.parseFloat(sp[0]),Float.parseFloat(sp[1])};
                activeExpressions.add(p -> p.rCol = lifeLerpFunc.apply(sps));
            }
            else if(n.equals(quickComputerKeys.COLOR_G_LERP.name())){
                float[] sps = new float[]{Float.parseFloat(sp[0]),Float.parseFloat(sp[1])};
                activeExpressions.add(p -> p.gCol = lifeLerpFunc.apply(sps));
            }
            else if(n.equals(quickComputerKeys.COLOR_B_LERP.name())){
                float[] sps = new float[]{Float.parseFloat(sp[0]),Float.parseFloat(sp[1])};
                activeExpressions.add(p -> p.bCol = lifeLerpFunc.apply(sps));
            }
            else if(n.equals(quickComputerKeys.COLOR_A_LERP.name())){
                float[] sps = new float[]{Float.parseFloat(sp[0]),Float.parseFloat(sp[1])};
                activeExpressions.add(p -> p.alpha = lifeLerpFunc.apply(sps));
            }

            /** BUILTIN SIZE **/
            else if(n.equals(quickComputerKeys.SIZE_LERP.name())){

                size_lerp_init = true;
            }
            /** BUILTIN INIT **/

            /** GRAVITY **/
            else if(n.equals(quickComputerKeys.GRAVITY.name())){
                this.gravity = Float.parseFloat(sp[0]);
            }

            /** FRICTION **/
            else if(n.equals(quickComputerKeys.FRICTION.name())){
                this.friction = Float.parseFloat(sp[0]);
            }

            /** SIZE **/
            else if(n.equals(quickComputerKeys.INIT_SIZE_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                this.quadSize = random.nextFloat()*sp1+sp0;
                SIZE_INIT = quadSize;
            }

            /** SPEED **/
            else if(n.equals(quickComputerKeys.INIT_SPEED_X_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                this.xdCache = random.nextFloat()*sp1+sp0;
            }
            else if(n.equals(quickComputerKeys.INIT_SPEED_Y_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                this.ydCache = random.nextFloat()*sp1+sp0;
            }
            else if(n.equals(quickComputerKeys.INIT_SPEED_Z_RANDOM.name())){
                float sp0 = Float.parseFloat(sp[0]);
                float sp1 = Float.parseFloat(sp[1]);
                this.zdCache = random.nextFloat()*sp1+sp0;
            }
        }

        if(size_lerp_init){
            String[]sp = map.get(quickComputerKeys.SIZE_LERP.name()).split(":");
            float[] sps ;
            if(SIZE_INIT==null) sps = new float[]{Float.parseFloat(sp[0]),Float.parseFloat(sp[1])};
            else sps = new float[]{Float.parseFloat(sp[0])+SIZE_INIT,Float.parseFloat(sp[1])};
            activeExpressions.add(p -> p.quadSize = lifeLerpFunc.apply(sps));

        }

    }
}
