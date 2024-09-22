package thexu.functionparticle.partical.expParticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thexu.functionparticle.partical.CoordinateSystem;
import thexu.functionparticle.partical.expKeys;

import java.util.*;
import java.util.function.Consumer;

public class BaseFunctionParticle extends TextureSheetParticle {
    private Map<String,String> map;

    private CoordinateSystem coordinateSystem= CoordinateSystem.WORLD;;

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

    boolean keepSpeed = false;


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

    public final Function random = new Function("random",2) {
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



    Map<Expression, Consumer<BaseFunctionParticle>> activeExpressions = new LinkedHashMap<>();

    BaseFunctionParticle(ClientLevel pLevel,double x,double y,double z ,BaseFunctionOption options) {
        super(pLevel, x, y, z, 0, 0, 0);

        functions.addAll(List.of(distance,random,expMax));

        this.xd=0;this.yd=0;this.zd=0;
        this.friction = 0.5f;
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

        activeExpressions.values().forEach(a->a.accept(this));
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
        activeExpressions.values().forEach(a->a.accept(this));

        //提前移除
        if(this.quadSize<=0) removed = true;

        //速度应用旋转和平移
        if(coordinateSystem== CoordinateSystem.LOCAL){
            //Vector3f newRot = new Vector3f((float) this.xCenter, (float) this.yCenter, (float) this.zCenter);
            Vector3f rotd = new Vector3f((float) this.xdCache, (float) this.ydCache, (float) this.zdCache);
            var ma1 = new Matrix4f().rotate((float) (-Math.toRadians(directionXInit)/2),new Vector3f(0,0,1));
            //ma1.transformPosition(newRot);
            ma1.transformPosition(rotd);
            var m2 = new Matrix4f().rotate((float) Math.toRadians(-90-directionYInit),new Vector3f(0,1,0));
            //m2.transformPosition(newRot);
            m2.transformPosition(rotd);

            //更新速度
            this.xd = rotd.x;
            this.yd = rotd.y;
            this.zd = rotd.z;

        }

        //更新位置缓存
        this.xCenter+=xdCache;
        this.yCenter+=ydCache;
        this.zCenter+=zdCache;

        super.tick();

        if((x-xInit*x-xInit+y-yInit*y-yInit+z-zInit*z-zInit) > 200) removed = true;
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
        for (var n : set) {
            String v = map.get(n);
            var sp = v.split(":");

                /* init */
            if(n.equals("INIT_POS")){
                this.xInit = Double.parseDouble(sp[0]);
                this.yInit = Double.parseDouble(sp[1]);
                this.zInit = Double.parseDouble(sp[2]);

            }else if(n.equals("INIT_ROT")) {
                this.directionXInit = Double.parseDouble(sp[0]);
                this.directionYInit = Double.parseDouble(sp[1]);
                this.coordinateSystem = CoordinateSystem.LOCAL;
            }else if(n.equals(expKeys.INIT_SPEED.name())){
                this.xd = Double.parseDouble(sp[0]);
                this.yd = Double.parseDouble(sp[1]);
                this.zd = Double.parseDouble(sp[2]);

                /* color */
            }else if(n.equals(expKeys.X_Y_ZCOLOR.name())){
                this.xyzColorExp = new ExpressionBuilder(v).variables("x","y","z").functions(functions).build();
                activeExpressions.put(xyzColorExp, p -> {
                    int color = (int) xyzColorExp.setVariable("x", p.xCenter).setVariable("y", p.yCenter).setVariable("z", p.zCenter).evaluate();
                    p.bCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.gCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.rCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.alpha = (float) ((color & 255) / 255.0);
                });
            }else if(n.equals(expKeys.T_COLOR.name())){
                this.tColorExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(tColorExp, p -> {
                    int color = (int) tColorExp.setVariable("x", p.age).evaluate();
                    p.bCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.gCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.rCol = (float) ((color & 255) / 255.0);color >>= 8;
                    p.alpha = (float) ((color & 255) / 255.0);
                });

                /* force */
            }else if(n.equals(expKeys.OFFSET_X.name())){
                this.offsetXExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(offsetXExp, p -> p.xdCache += offsetXExp.setVariable("x", p.xCenter).evaluate());
            }else if(n.equals(expKeys.OFFSET_Y.name())){
                this.offsetYdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(offsetYdExp, p -> p.ydCache += offsetYdExp.setVariable("x", p.yCenter).evaluate());
            }else if(n.equals(expKeys.OFFSET_Z.name())){
                this.offsetZdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(offsetZdExp, p -> p.zdCache += offsetZdExp.setVariable("x", p.zCenter).evaluate());

                /* speed */
            }else if(n.equals(expKeys.X_SPEED.name())){
                this.xdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(xdExp, p -> p.xdCache += xdExp.setVariable("x", p.xCenter).evaluate());
            }else if(n.equals(expKeys.Y_SPEED.name())){
                this.ydExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(ydExp, p -> p.ydCache += ydExp.setVariable("x", p.yCenter).evaluate());
            }else if(n.equals(expKeys.Z_SPEED.name())){
                this.zdExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(zdExp, p -> p.zdCache += zdExp.setVariable("x", p.zCenter).evaluate());

            }else if(n.equals(expKeys.T_SPEED_X.name())){
                this.xdtExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(xdtExp, p -> p.xdCache += xdtExp.setVariable("x", age).evaluate());
            }else if(n.equals(expKeys.T_SPEED_Y.name())){
                this.ydtExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(ydtExp, p -> p.ydCache += ydtExp.setVariable("x", age).evaluate());
            }else if(n.equals(expKeys.T_SPEED_Z.name())) {
                this.zdtExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(zdtExp, p -> p.zdCache += zdtExp.setVariable("x", age).evaluate());
            }else if(n.equals(expKeys.KEEP_SPEED.name())){
                keepSpeed = true;

                /* lifetime */
            }else if(n.equals(expKeys.X_Y_Z_LIFETIME.name())){
                this.xyzLifeTimeExp = new ExpressionBuilder(v).variables("x","y","z").functions(functions).build();
                activeExpressions.put(xyzLifeTimeExp, p -> p.lifetime = (int) xyzLifeTimeExp.setVariable("x", p.xCenter).setVariable("y", p.yCenter).setVariable("z", p.zCenter).evaluate());
            }else if(n.equals(expKeys.T_LIFETIME.name())){
                this.tLifeTimeExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(tLifeTimeExp, p -> p.lifetime = (int) tLifeTimeExp.setVariable("x", p.age).evaluate());

                /* scale */
            }else if(n.equals(expKeys.X_Y_Z_SCALE.name())){
                this.xyzScaleExp = new ExpressionBuilder(v).variables("x","y","z").functions(functions).build();
                activeExpressions.put(xyzScaleExp, p -> p.quadSize = (float) xyzLifeTimeExp.setVariable("x", p.xCenter).setVariable("y", p.yCenter).setVariable("z", p.zCenter).evaluate());
            }else if(n.equals(expKeys.T_SCALE.name())){
                this.tScaleExp = new ExpressionBuilder(v).variable("x").functions(functions).build();
                activeExpressions.put(tScaleExp, p -> p.quadSize = (float) tScaleExp.setVariable("x", p.age).evaluate());
            }
        }
    }
}
