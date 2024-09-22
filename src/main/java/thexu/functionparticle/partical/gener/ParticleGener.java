package thexu.functionparticle.partical.gener;

import com.mojang.math.Axis;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLPaths;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thexu.functionparticle.partical.CoordinateSystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static thexu.functionparticle.FunctionParticle.MODID;


public class ParticleGener {
    //public List<Particle> particles;
    public List<Vector3f> pPoss;
    public List<Vector3f> pPossInit;
    public Vec3 initPos;
    public ParticleGener(Vec3 initPos, Vec2 rotDirection, CoordinateSystem rotateCoordinate, List<Vector3f> allPosition){
        this.initPos = initPos;
        if(rotateCoordinate==CoordinateSystem.LOCAL){//旋转
            for(var n : allPosition){
                new Matrix4f()
                        .rotate(Math.toRadians(-rotDirection.x)/2,new Vector3f(0,0,1))
                        .transformPosition(n);
                new Matrix4f()
                        .rotate(Math.toRadians(-90-rotDirection.y),new Vector3f(0,1,0))
                        .transformPosition(n);
            }
        }

        pPoss = allPosition;

    }

    public ParticleGener(Vec3 initPos, Vec2 rotDirection, CoordinateSystem rotateCoordinate, String file){
        Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve(MODID);
        Path path = CONFIG_PATH.resolve(file);
        BufferedImage im;
        try {
            im = ImageIO.read(new FileInputStream(path.toString()));
            if(im!=null){
                System.out.println(im);
                im.getRGB(0,0);
            }else {
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Vector3f> allPosition = new ArrayList<>();


        this.initPos = initPos;
        if(rotateCoordinate==CoordinateSystem.LOCAL){//旋转
            for(var n : allPosition){
                new Matrix4f()
                        .rotate(Math.toRadians(-rotDirection.x)/2,new Vector3f(0,0,1))
                        .transformPosition(n);
                new Matrix4f()
                        .rotate(Math.toRadians(-90-rotDirection.y),new Vector3f(0,1,0))
                        .transformPosition(n);
            }
        }

        pPoss = allPosition;

    }



    public void genParticle(Level level, ParticleOptions particle, Function<Vector3f, Vector3f> speed){
        for(var n : pPoss){
            Vector3f sp = speed.apply(n);
            ((ServerLevel)level).sendParticles(particle,n.x+initPos.x,n.y+initPos.y,n.z+initPos.z,1,sp.x,sp.y,sp.z,0);
        }
    }



    public static class Build{
        private List<Vector3f> buildPos = new ArrayList<>();
        private List<Vector3f> temp;
        //在x轴生成
        public Build genLine(float internal, float length){
            if(temp==null) temp = new ArrayList<>();
            for(float i=0;i<=length;i+=internal){
                temp.add(new Vector3f(i,0,0));
            }
            return this;
        }

        public Build genPoint(Vector3f origen){
            if(temp==null) temp = new ArrayList<>();
            temp.add(origen);
            return this;
        }

        //yz平面
        public Build genCircle(float r,float internal){
            if(temp==null) temp = new ArrayList<>();
            for(float i=0;i<=360;i+=internal){
                temp.add(new Vector3f(0f, (float) Math.cos(i),(float) Math.sin(i)));
            }
            return this;
        }

        public Build addToList(){
            if(temp==null) return this;
            buildPos.addAll(temp);
            temp = null;
            return this;
        }

        public Build transLate(int x,int y,int z){
            for(var n : temp){ n.add(x,y,z); }
            return this;
        }

        public Build scale(int x,int y,int z){
            for(var n : temp){ n.mul(x,y,z); }
            return this;
        }

        public Build rotate(int xRot,int yRot,int zRot){
            for(var n : temp){ new Matrix4f()
                .rotate(Axis.XN.rotation(xRot))
                .rotate(Axis.YN.rotation(yRot))
                .rotate(Axis.ZN.rotation(zRot))
                .transformPosition(n);
            }
            return this;
        }

        public List<Vector3f> build(){return buildPos;}
    }


}
