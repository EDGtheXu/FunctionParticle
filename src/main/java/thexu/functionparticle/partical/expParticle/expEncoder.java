package thexu.functionparticle.partical.expParticle;

import com.mojang.math.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thexu.functionparticle.partical.CoordinateSystem;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.registry.ModEntities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class expEncoder extends Entity {
    Map<String,String> map;
    private String exp;
    public List<Vector3f> pPoss= new ArrayList<>();

    private Vec2 rotInit;
    private CoordinateSystem coordinateSystem;
    public Vec3 initPos;
    private int emitCountTick;
    private float rx=0;
    private float ry=0;
    private float rz=0;
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

    public boolean isNoGravity(){return true;}
    public expEncoder(EntityType<? extends Entity> entityType, Level level){
        super(entityType,level);
        this.noPhysics = true;
    }

    //build构造
    public expEncoder(Level level, Vec3 initPos, Vec2 rotDirection, CoordinateSystem rotateCoordinate, Builder expMap){
        this(ModEntities.PARTICLE_EMITTER.get(), level);
        this.moveTo(initPos);

        this.initPos = initPos;
        this.map = expMap.result;
        this.rx = expMap.rx;
        this.ry = expMap.ry;
        this.rz = expMap.rz;

        //是否本地坐标轴
        map.put("COORDINATE",rotateCoordinate.name());
        //若启用本地坐标轴，初始旋转方向
        rotInit = rotDirection;
        coordinateSystem = rotateCoordinate;
        if(rotateCoordinate== CoordinateSystem.LOCAL)
            map.put("INIT_ROT",rotDirection.x+":"+ rotDirection.y);
        //计算map初始位置信息
        Expression x = new ExpressionBuilder(map.getOrDefault(expKeys.X.name(), "0")).variables("x","y","z").build();
        Expression y = new ExpressionBuilder(map.getOrDefault(expKeys.Y.name(), "0")).variables("x","y","z").build();
        Expression z = new ExpressionBuilder(map.getOrDefault(expKeys.Z.name(), "0")).variables("x","y","z").build();

        String variableX = map.getOrDefault(expKeys.VARIABLE_X.name(),"1:1:1");
        String variableY = map.getOrDefault(expKeys.VARIABLE_Y.name(),"1:1:1");
        String variableZ = map.getOrDefault(expKeys.VARIABLE_Z.name(),"1:1:1");

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
                    pPoss.add(new Vector3f((float) xc, (float) yc, (float) zc));
                }
            }
        }

        //表达式点旋转
        if(rx!=0||ry!=0||rz!=0){
            for(var n : pPoss) new Matrix4f()
                    .rotate(Axis.XN.rotation(rx))
                    .rotate(Axis.YN.rotation(ry))
                    .rotate(Axis.ZN.rotation(rz))
                    .transformPosition(n);
        }

        this.exp = expDecode(map);
        if(expMap.additionPoints!=null) this.pPoss.addAll(expMap.additionPoints);
    }




        //添加点
    public void append(Map<String,String> from){
        Expression x = new ExpressionBuilder(from.getOrDefault(expKeys.X.name(), "0")).variable("x").build();
        Expression y = new ExpressionBuilder(from.getOrDefault(expKeys.Y.name(), "0")).variable("x").build();
        Expression z = new ExpressionBuilder(from.getOrDefault(expKeys.Z.name(), "0")).variable("x").build();
        String variable = from.getOrDefault(expKeys.VARIABLE_X.name(),"0:0:0");
        String[] splits = variable.split(":");
        float min = Float.parseFloat(splits[0]);
        float step = Float.parseFloat(splits[1]);
        float max = Float.parseFloat(splits[2]);

        for(float i=min;i<=max;i+=step){
            double xc = x.setVariable("x",i).evaluate();
            double yc = y.setVariable("x",i).evaluate();
            double zc = z.setVariable("x",i).evaluate();
            pPoss.add(new Vector3f((float) xc, (float) yc, (float) zc));
        }

    }

    //添加几何构造点
    public void append(List<Vector3f> geometry){
        pPoss.addAll(geometry);
    }


    public String expDecode(Map<String,String> map){
        AtomicReference<String> str = new AtomicReference<>("");
        map.forEach((k,v)-> str.set(str + k+":"+ v + ";"));
        return str.get();
    }

    int curCount = 0;
    public int genParticle(int from,int count){
    for(int i=0;i<count;i++){
        if(from+i>=pPoss.size()) return i;
        var n = pPoss.get(from+i);

        //平移到initPos
        double x = n.x+initPos.x;
        double y = n.y+initPos.y;
        double z = n.z+initPos.z;
        //旋转到LOCAL
        if(coordinateSystem== CoordinateSystem.LOCAL){
            Vector3f newRot = new Vector3f((float) n.x, (float) n.y, (float) n.z);
            var ma1 = new Matrix4f().rotate((float) (-Math.toRadians(rotInit.x)/2),new Vector3f(0,0,1));
            ma1.transformPosition(newRot);
            var m2 = new Matrix4f().rotate((float) Math.toRadians(-90- rotInit.y),new Vector3f(0,1,0));
            m2.transformPosition(newRot);

            x = newRot.x+initPos.x;
            y = newRot.y+initPos.y;
            z = newRot.z+initPos.z;
        }

        String sendExp = exp+"INIT_POS"+":"+initPos.x+":"+initPos.y+":"+initPos.z+";";
        if(!level().isClientSide)
            ((ServerLevel)level()).sendParticles(new BaseFunctionOption(x,y,z,sendExp),x,y,z,1,0,0,0,0);
    }
    return count;
    }

    public void tick(){
        super.tick();
        if(level().isClientSide || map==null)return;
        //获取发射量
        Expression c = new ExpressionBuilder(map.getOrDefault(expKeys.EMIT_SPEED.name(), "0")).variable("x").build()
                .setVariable("x",tickCount);
        int count = (int) c.evaluate();
        if(genParticle(curCount,count)==0 && tickCount > 5)
            discard();
        curCount+=count;


    }



    public static class Builder{
        public Builder self;
        private final Map<String,String> result = new LinkedHashMap<>();
        public float rx;
        public float ry;
        public float rz;
        public List<Vector3f> additionPoints;
        public Builder(){

        }
        public Builder add(expKeys key,String exp){
            result.put(key.name(),exp);
            return this;
        }

        public Builder rotate(int xRot, int yRot, int zRot){
            rx=(float) java.lang.Math.toRadians(xRot);
            ry=(float) java.lang.Math.toRadians(yRot);
            rz=(float) java.lang.Math.toRadians(zRot);
            return this;
        }

        public Builder buildMap(){
            return this;
        }

        public String buildString(){
            AtomicReference<String> str = new AtomicReference<>("");
            result.forEach((k,v)-> str.set(str + v + ";"));
            return str.get();
        }

        public Builder addPoints(Points points){
            this.additionPoints = points.buildPos;
            return this;
        }


        public static class Points {
            private List<Vector3f> buildPos = new ArrayList<>();
            private List<Vector3f> temp;
            //在x轴生成
            public Points genLine(float internal, float length){
                if(temp==null) temp = new ArrayList<>();
                for(float i=0;i<=length;i+=internal){
                    temp.add(new Vector3f(i,0,0));
                }
                return this;
            }

            public Points genPoint(Vector3f origen){
                if(temp==null) temp = new ArrayList<>();
                temp.add(origen);
                return this;
            }

            //yz平面
            public Points genCircle(float r, float internal){
                if(temp==null) temp = new ArrayList<>();
                for(float i=0;i<=360;i+=internal){
                    temp.add(new Vector3f(0f, (float) Math.cos(i),(float) Math.sin(i)));
                }
                return this;
            }

            public Points addToList(){
                if(temp==null) return this;
                buildPos.addAll(temp);
                temp = null;
                return this;
            }

            public Points transLate(int x, int y, int z){
                for(var n : temp){ n.add(x,y,z); }
                return this;
            }

            public Points scale(int x, int y, int z){
                for(var n : temp){ n.mul(x,y,z); }
                return this;
            }

            public Points rotate(int xRot, int yRot, int zRot){
                for(var n : temp){ new Matrix4f()
                        .rotate(Axis.XN.rotation(xRot))
                        .rotate(Axis.YN.rotation(yRot))
                        .rotate(Axis.ZN.rotation(zRot))
                        .transformPosition(n);
                }
                return this;
            }

            public Points confirm(){return this;}
        }
    }





    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }


}
