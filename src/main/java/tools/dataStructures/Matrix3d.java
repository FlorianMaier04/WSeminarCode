package tools.dataStructures;

import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;

public class Matrix3d {


    public double[][] values = new double[3][3];


    public Vector3d multiply(Vector3d v) {
        Vector3d s0 = getSpalte(0);
        Vector3d s1 = getSpalte(1);
        Vector3d s2 = getSpalte(2);
        s0 = s0.scale(v.x);
        s1 = s1.scale(v.y);
        s2 = s2.scale(v.z);
        Vector3d result = (s0.add(s1)).add(s2);
        return result;
    }

    private Vector3d getSpalte(int y) {
        Vector3d result = new Vector3d(values[0][y],values[1][y],values[2][y]);
        return result;
    }



    @Override
    public String toString() {
        String result = "";
        for(int x = 0; x < values.length; x++) {
//            result += " x" + x + ": ";
            for(int y = 0; y < values.length; y++) {
                result += doubleToString(values[x][y]) + "|";
            }
            result += System.lineSeparator();
        }
        return result;
    }

    private final int decimals = 3;

    private String doubleToString(double d) {
        String basePart = d + "";
        int index = basePart.indexOf("E");
        String ePart = "";
        if(index != -1) {
            ePart = basePart.substring(index);
        }
        basePart = basePart.substring(0,Math.min(decimals + 1,basePart.length()));
        return basePart + ePart;
    }
}
