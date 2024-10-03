package thexu.functionparticle.partical.util;

import net.minecraft.world.phys.Vec3;
import org.ejml.simple.SimpleMatrix;
import java.util.ArrayList;
import java.util.List;

public class BezierCurse {
    java.util.List<Vec3> points;
    SimpleMatrix T;
    SimpleMatrix M = new SimpleMatrix(new double[][]{
            {-1, 3,-3, 1},
            { 3,-6, 3, 0},
            {-3, 3, 0, 0},
            { 1, 0, 0, 0}
    });
    SimpleMatrix P;
    int size;
    public BezierCurse(Vec3...pos){
        points = new ArrayList<>();
        points.addAll(List.of(pos));
        size = points.size();
        P = new SimpleMatrix(size,3);
        T = new SimpleMatrix(1,size);
        for(int i=0;i<size;i++){
            P.setRow(i,0,pos[i].x,pos[i].y,pos[i].z);
            T.setColumn(i,0,size-i-1);
        }
        P = M.mult(P);
    }
    public BezierCurse(List<Vec3> ps){
        points=ps;
        size = points.size();
        P = new SimpleMatrix(size,3);
        T = new SimpleMatrix(1,size);
        for(int i=0;i<size;i++){
            P.setRow(i,0, points.get(i).x, points.get(i).y, points.get(i).z);
            T.setColumn(i,0,size-i-1);
        }
        P = M.mult(P);
    }

    public double[] cal(double t){
        SimpleMatrix matrix = T.copy();
        for(int i=0;i<size;i++){
            matrix.setColumn(i,0,Math.pow(t,T.get(0,i)));
        }
        matrix = matrix.mult(P);
        return new double[]{matrix.get(0),matrix.get(1),matrix.get(2)};
    }

}
