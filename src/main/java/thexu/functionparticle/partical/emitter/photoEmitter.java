package thexu.functionparticle.partical.emitter;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLPaths;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thexu.functionparticle.FunctionParticle;
import thexu.functionparticle.partical.expKeys;
import thexu.functionparticle.partical.expParticle.BaseFunctionOption;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class photoEmitter extends expEmitter{
    int internalPix = 3;
    List<Tuple> ts = new ArrayList<Tuple>();
    record Tuple(int r,int g,int b,int a){};

    public photoEmitter(EntityType<photoEmitter> entityType, Level level){
        super( entityType,level);
        this.noPhysics = true;
    }


    public photoEmitter(Level level, Vec3 initPos, Vec2 rotDirection, String pathStr, float internal, Builder expMap) {
        super(level,initPos,rotDirection,expMap);
        Path CONFIG_PATH = FMLPaths.MODSDIR.get().resolve(FunctionParticle.MODID);
        Path path = CONFIG_PATH.resolve(pathStr);
        BufferedImage im;
        try {
            im = ImageIO.read(new FileInputStream(path.toString()));
            if(im!=null){
                for(int i=0;i<im.getWidth();i++){
                    for(int j=0;j<im.getHeight();j++){
                        if(i%internalPix==0 && j%internalPix==0){
                            Object data = im.getRaster().getDataElements(i, j, null);
                            int r = im.getColorModel().getRed(data);
                            int g = im.getColorModel().getGreen(data);
                            int b = im.getColorModel().getBlue(data);
                            int a = im.getColorModel().getAlpha(data);
//                            if(r>200&&g>200&&b>200){
//                                continue;
//                            }else{
                            ts.add(new Tuple(r, g, b, a));
                            pPoss.add(new Vector3f(i*internal, -(float) j*internal,0));
//                            }
                        }
                    }
                }
                System.out.println("图片初始化完成");
            }else {
                throw new RuntimeException("未创建mods文件夹");
            }
        } catch (IOException e) {
            throw new RuntimeException("未创建mods文件夹");
        }

        performChange();
    }

    public void randomPos(){
        int n = pPoss.size();
        /******** 区别只有这两行 ********/
        for (int i = 0 ; i < n; i++) {
            // 从 i 到最后随机选一个元素
            int rand = (int) (Math.random()*n);
            /*************************/
            var t = pPoss.get(rand);
            pPoss.set(rand,pPoss.get(i));
            pPoss.set(i,t);

            var tt = ts.get(rand);
            ts.set(rand,ts.get(i));
            ts.set(i,tt);
        }
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
                var ma1 = new Matrix4f().rotate((float) (-org.joml.Math.toRadians(rotInit.x)/2),new Vector3f(0,0,1));
                ma1.transformPosition(newRot);
                var m2 = new Matrix4f().rotate((float) Math.toRadians(-90- rotInit.y),new Vector3f(0,1,0));
                m2.transformPosition(newRot);

                x = newRot.x+initPos.x;
                y = newRot.y+initPos.y;
                z = newRot.z+initPos.z;
            }
            //编码
            String sendExp = exp+"INIT_POS"+":"+initPos.x+":"+initPos.y+":"+initPos.z+";";
            //更改颜色

            float a = (float) (ts.get(from+i).a/256.0);
            float b = (float) (ts.get(from+i).b/256.0);
            float g = (float) (ts.get(from+i).g/256.0);
            float r = (float) (ts.get(from+i).r/256.0);
            sendExp+="COLOR_R_LERP:"+r+":"+r+";";
            sendExp+="COLOR_G_LERP:"+g+":"+g+";";
            sendExp+="COLOR_B_LERP:"+b+":"+b+";";
            sendExp+="COLOR_A_LERP:"+a+":"+a+";";

            if(!level().isClientSide)
                ((ServerLevel)level()).sendParticles(new BaseFunctionOption(x,y,z,sendExp),x,y,z,1,0,0,0,0);
        }
        return count;
    }
}
