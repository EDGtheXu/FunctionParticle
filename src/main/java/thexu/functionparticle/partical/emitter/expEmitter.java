package thexu.functionparticle.partical.emitter;

import com.mojang.math.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thexu.functionparticle.partical.emitShape;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.expParticle.BaseFunctionOption;
import thexu.functionparticle.partical.expPointKeys;
import thexu.functionparticle.partical.quickComputerKeys;
import thexu.functionparticle.registry.ModEntities;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.ArrayUtils.swap;

public class expEmitter extends Mob {
    private HashMap<String,String> map;
    private String exp;
    private List<Vector3f> pPoss;
    private List<Vector3f> path;
    private Vec2 rotInit;
    private Vec3 initPos;
    private int curCount = 0;
    private int curPath = 0;
    private int emitLife;
    private float rx=0;
    private float ry=0;
    private float rz=0;
    private float moveSpeed = 0;
    private Builder builder;

/*
    //字符串构造
    public expEncoder(Vec3 initPos, Vec2 rotDirection, ParticleGener.CoordinateSystem rotateCoordinate, String exp){

        this.initPos = initPos;
        this.exp = exp;
        //解码
        //expDecode();


//        if(rotateCoordinate== ParticleGener.CoordinateSystem.LOCAL){//旋转
//            for(var n : pPoss){
//                new Matrix4f()
//                        .rotate(Math.toRadians(-rotDirection.x)/2,new Vector3f(0,0,1))
//                        .transformPosition(n);
//                new Matrix4f()
//                        .rotate(Math.toRadians(-90-rotDirection.y),new Vector3f(0,1,0))
//                        .transformPosition(n);
//            }
//        }


    }
*/


    public expEmitter(EntityType<expEmitter> entityType, Level level){
        super(entityType,level);
        this.noPhysics = true;
    }

/** build构造 **/
    public expEmitter(Level level, Vec3 initPos, Vec2 rotDirection, Builder expMap){
        this(ModEntities.PARTICLE_EMITTER.get(), level);
        this.setPos(initPos);
        this.initPos = initPos;
        this.pPoss=expMap.getPointsClone();
        this.path=expMap.getPathClone();
        this.map= expMap.getResultClone();
        this.rx = expMap.rx;
        this.ry = expMap.ry;
        this.rz = expMap.rz;
        this.moveSpeed = expMap.emitMoveSpeed;
        this.builder = expMap;
        this.emitLife = builder.emitLife;;

        //若启用本地坐标轴，初始旋转方向
        rotInit = rotDirection;
        if(map.containsKey(expKeys.LOCAL.name()))
            map.put(expKeys.LOCAL.name(),rotDirection.x+":"+ rotDirection.y);

        //表达式点旋转
        if(rx!=0||ry!=0||rz!=0){
            for(var n : pPoss) new Matrix4f()
                    .rotate(Axis.XN.rotation(rx))
                    .rotate(Axis.YN.rotation(ry))
                    .rotate(Axis.ZN.rotation(rz))
                    .transformPosition(n);
        }

        this.exp = expEncode(map);

        //发射器位置初始化
        if(path!=null)
            for(var n :path){
                n.add(initPos.toVector3f());
            }

        //启用随机发射点
        if(builder.emitRandom){
            int n = pPoss.size();
            /******** 区别只有这两行 ********/
            for (int i = 0 ; i < n; i++) {
                // 从 i 到最后随机选一个元素
                int rand = (int) (Math.random()*n);
                /*************************/
                var t = pPoss.get(rand);
                pPoss.set(rand,pPoss.get(i));
                pPoss.set(i,t);

            }
        }

    }


/** 发射器运动轨迹 **/
    public void genPath(List<Vector3f> points){
        path = points;
    }

    public boolean moveNext(){
        return tickCount > 20;
    }



/** 构造类 **/
    public static class Builder{
        private final LinkedHashMap<String,String> result = new LinkedHashMap<>();
        private final List<Vector3f> Points = new ArrayList<>();
        private final List<Vector3f> path = new ArrayList<>();
        private emitShape shape = emitShape.NULL;

        private HashMap<String,String> getResultClone(){
            return new LinkedHashMap<>(result);
        }
        private List<Vector3f> getPointsClone(){
            List<Vector3f> r = new ArrayList<>();
            Points.forEach(a->r.add(new Vector3f(a)));
            return r;
        }
        private List<Vector3f> getPathClone(){
            List<Vector3f> r = new ArrayList<>();
            path.forEach(a->r.add(new Vector3f(a)));
            return r;
        }
        private float rx;
        private float ry;
        private float rz;
        private float power;
        private float emitMoveSpeed = 0;
        private boolean emitRandom = false;
        private int emitLife = 999999;//默认必须刷新完所有粒子
        private boolean circle = false;
        private boolean pathCircle = false;

        public Builder(){

        }



        //添加点的运动表达式
        public Builder add(expKeys key,String exp){
            result.put(key.name(),exp);
            return this;
        }
        public Builder add(quickComputerKeys key, String exp){
            result.put(key.name(),exp);
            return this;
        }

        //所有点的初始化旋转
        public Builder rotate(int xRot, int yRot, int zRot){
            rx=(float) java.lang.Math.toRadians(xRot);
            ry=(float) java.lang.Math.toRadians(yRot);
            rz=(float) java.lang.Math.toRadians(zRot);
            return this;
        }

        //build结束标志
        public Builder buildMap(){
            return this;
        }

        public String buildString(){
            AtomicReference<String> str = new AtomicReference<>("");
            result.forEach((k,v)-> str.set(str + v + ";"));
            return str.get();
        }





        //添加点
        public Builder addExpPoints(ExpPoints points){
            this.Points.addAll( points.temp);
            return this;
        }
        public Builder addPShapePints(ShapePoints points){
            this.Points.addAll(points.buildPos) ;
            return this;
        }

        /** 发射器 **/
        //生成路径
        public Builder genExpPath(ExpPoints points){
            this.path.addAll(points.temp);
            return this;
        }
        public Builder genShapePath(ShapePoints points){
            this.path.addAll(points.buildPos);
            return this;
        }



        public Builder emitMoveSpeed(float v){
            this.emitMoveSpeed = v;
            return this;
        }

        public Builder emitLife(int tick){
            this.emitLife = tick;
            return this;
        }
        public Builder emitCircle(boolean b){
            this.circle = b;
            return this;
        }

        public Builder emitRandom(boolean b){
            this.emitRandom = b;
            return this;
        }

        public Builder emitShape(emitShape shape, float power){
            result.put(shape.name(),"");
            result.put(expKeys.INIT_SPEED.name(), "::");

            this.power = power;
            result.put("EMIT_POWER",power+"");
            return this;
        }
        public Builder emitPathCircle(boolean b){
            pathCircle = b;
            return this;

        }




        public static class ExpPoints {
            List<Vector3f> temp =new ArrayList<>();
            private final Map<String,String> result = new LinkedHashMap<>();

            public ExpPoints addExp(expPointKeys key, String exp){
                result.put(key.name(),exp);

                return this;
            }


            public ExpPoints scale(int x, int y, int z){
                for(var n : temp){ n.mul(x,y,z); }
                return this;
            }

            public ExpPoints rotate(int xRot, int yRot, int zRot){
                for(var n : temp){ new Matrix4f()
                        .rotate(Axis.XN.rotation(Math.toRadians(xRot)))
                        .rotate(Axis.YN.rotation(Math.toRadians(yRot)))
                        .rotate(Axis.ZN.rotation(Math.toRadians(zRot)))
                        .transformPosition(n);
                }
                return this;
            }
            public ExpPoints transLate(int x, int y, int z){
                for(var n : temp){ n.add(x,y,z); }
                return this;
            }
            public ExpPoints compute(){
                append(result,temp);
                return this;
            }
        }




        public static class ShapePoints {
            private List<Vector3f> buildPos = new ArrayList<>();
            private List<Vector3f> temp;
            //在x轴生成
            public ShapePoints genLine(float internal, float length){
                if(temp==null) temp = new ArrayList<>();
                for(float i=0;i<=length;i+=internal){
                    temp.add(new Vector3f(i,0,0));
                }
                return this;
            }

            public ShapePoints genPoint(Vector3f origen){
                if(temp==null) temp = new ArrayList<>();
                temp.add(origen);
                return this;
            }

            //yz平面
            public ShapePoints genCircle(float r, float internal){
                if(temp==null) temp = new ArrayList<>();
                for(float i=0;i<=360;i+=internal){
                    temp.add(new Vector3f(0f, r *Math.cos(i), r *Math.sin(i)));
                }
                return this;
            }

            public ShapePoints addToList(){
                if(temp==null) return this;
                buildPos.addAll(temp);
                temp = null;
                return this;
            }

            public ShapePoints transLate(int x, int y, int z){
                for(var n : temp){ n.add(x,y,z); }
                return this;
            }

            public ShapePoints scale(int x, int y, int z){
                for(var n : temp){ n.mul(x,y,z); }
                return this;
            }

            public ShapePoints rotate(int xRot, int yRot, int zRot){
                for(var n : temp){ new Matrix4f()
                        .rotate(Axis.XN.rotation((float) java.lang.Math.toRadians(xRot)))
                        .rotate(Axis.YN.rotation((float) java.lang.Math.toRadians(yRot)))
                        .rotate(Axis.ZN.rotation((float) java.lang.Math.toRadians(zRot)))
                        .transformPosition(n);
                }
                return this;
            }

            public ShapePoints confirm(){return this;}
        }




    }

    //添加几何构造点
    public void append(List<Vector3f> geometry){
        pPoss.addAll(geometry);
    }


/** 生成粒子 **/
    public void tick(){

        super.tick();
        if(!level().isClientSide && tickCount>emitLife && tickCount > 20) discard();

        //轨迹运动
        if(path!=null){
            if(curPath < path.size()){
                Vec3 target = new Vec3(path.get(curPath));
                Vec3 dir = target.subtract(position());
                if(dir.length() < moveSpeed*1.1){
                    curPath++;
                }else{
                    var v = dir.normalize().scale(moveSpeed);
                    this.setDeltaMovement(v);

                }
            }else{
                curPath = 0;
                if(builder!=null && builder.circle) return;

            }
        }


        if(level().isClientSide)return;
        if(map==null){
            discard();
            return;
        }
        //获取发射量
        Expression c = new ExpressionBuilder(map.getOrDefault(expKeys.EMIT_SPEED.name(), "1")).variable("x").build()
                .setVariable("x",tickCount);
        int count = (int) c.evaluate();
        if(genParticle(curCount,count) == 0 && tickCount > 20) {
            if (builder != null && builder.circle) {
                curCount = count;
                genParticle(curCount,count);
            } else discard();
        }
        else curCount+=count;

        //可能每次刚好发射完所有的粒子
        if( builder != null && !builder.pathCircle && !builder.circle && curPath==0 && tickCount > 20)
            discard();



    }
    public int genParticle(int from,int count){
        for(int i=0;i<count;i++){
            if(from+i>=pPoss.size()) return i;
            var n = pPoss.get(from+i);



            //平移到发射器位置
            double x = n.x+this.getX();
            double y = n.y+this.getY();
            double z = n.z+this.getZ();
            //旋转到LOCAL
            if(map.containsKey(expKeys.LOCAL.name())){
                Vector3f newRot = new Vector3f((float) n.x, (float) n.y, (float) n.z);
                var ma1 = new Matrix4f().rotate((float) (-Math.toRadians(rotInit.x)/2),new Vector3f(0,0,1));
                ma1.transformPosition(newRot);
                var m2 = new Matrix4f().rotate((float) Math.toRadians(-90- rotInit.y),new Vector3f(0,1,0));
                m2.transformPosition(newRot);

                x = newRot.x+initPos.x;
                y = newRot.y+initPos.y;
                z = newRot.z+initPos.z;
            }
            //编码

            String sendExp = exp+"INIT_POS"+":"+initPos.x+":"+initPos.y+":"+initPos.z+";";
            if(!level().isClientSide)
                ((ServerLevel)level()).sendParticles(new BaseFunctionOption(x,y,z,sendExp),x,y,z,1,0,0,0,0);
        }
        return count;
    }

/** 编码器 **/
    public String expEncode(Map<String,String> map){
        AtomicReference<String> str = new AtomicReference<>("");
        map.forEach((k,v)-> str.set(str + k+":"+ v + ";"));
        return str.get();
    }
    //添加点
    public static void append(Map<String,String> from,List<Vector3f> to){
        Expression x = new ExpressionBuilder(from.getOrDefault(expPointKeys.X.name(), "0")).variables("x","y","z").build();
        Expression y = new ExpressionBuilder(from.getOrDefault(expPointKeys.Y.name(), "0")).variables("x","y","z").build();
        Expression z = new ExpressionBuilder(from.getOrDefault(expPointKeys.Z.name(), "0")).variables("x","y","z").build();

        String variableX = from.getOrDefault(expPointKeys.VARIABLE_X.name(),"1:1:1");
        String variableY = from.getOrDefault(expPointKeys.VARIABLE_Y.name(),"1:1:1");
        String variableZ = from.getOrDefault(expPointKeys.VARIABLE_Z.name(),"1:1:1");

        String[] spX = variableX.split(":");
        float[] vx = new float[]{Float.parseFloat(spX[0]),Float.parseFloat(spX[1]),Float.parseFloat(spX[2])};
        String[] spY = variableY.split(":");
        float[] vy = new float[]{Float.parseFloat(spY[0]),Float.parseFloat(spY[1]),Float.parseFloat(spY[2])};
        String[] spZ = variableZ.split(":");
        float[] vz = new float[]{Float.parseFloat(spZ[0]),Float.parseFloat(spZ[1]),Float.parseFloat(spZ[2])};


        for(float i=vx[0];i<=vx[2];i+=vx[1]){
            for(float j=vy[0];j<=vy[2];j+=vy[1]){
                for(float k=vz[0];k<=vz[2];k+=vz[1]){
                    double xc = x.setVariable("x",i).setVariable("y",j).setVariable("z",k).evaluate();
                    double yc = y.setVariable("x",i).setVariable("y",j).setVariable("z",k).evaluate();
                    double zc = z.setVariable("x",i).setVariable("y",j).setVariable("z",k).evaluate();
                    to.add(new Vector3f((float) xc, (float) yc, (float) zc));
                }
            }
        }

    }

   public boolean canBeSeenByAnyone(){
        return false;
    }
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return false;
    }


    public boolean isNoGravity(){return true;}
/*

    private static  EntityDataAccessor<Vector3f> DATA_V = SynchedEntityData.defineId(expEmitter.class, EntityDataSerializers.VECTOR3);
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_V,new Vector3f());
    }
*/

}
