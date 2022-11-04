package tools.dataStructures;

import org.lwjgl.util.vector.Vector3f;

public class Vector3d {

    public double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }


    public double length() {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));
    }

    public Vector3d normalize() {
        double length = length();
        return new Vector3d(x / length, y / length, z / length);
    }

    public Vector3d sub(Vector3d vec) {
        Vector3d result = new Vector3d(x - vec.x, y - vec.y, z - vec.z);
        return result;
    }

    public Vector3d add(Vector3d vec) {
        Vector3d result = new Vector3d(x + vec.x, y + vec.y, z + vec.z);
        return result;
    }

    public Vector3d mulitply(double scale) {
        return new Vector3d(x * scale, y * scale, z * scale);
    }

    public Vector3d divide(double dividend) {
        return new Vector3d(x / dividend, y / dividend, z / dividend);
    }

    public Vector3f convertToFloat() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    @Override
    public String toString() {
        return "{" +
                doubleToString(x) + ", "
                + doubleToString(y) + ", "
                + doubleToString(z) +
                '}';
    }

    private final int decimals = 5;

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

    public Vector3d rotateXY(double angle) {
        double newX = x * Math.cos(angle) - y * Math.sin(angle);
        double newY = x * Math.sin(angle) + y * Math.cos(angle);
        return new Vector3d(newX, newY, z);
    }

    public Vector3d rotateYZ(double angle) {
        double newY = y * Math.cos(angle) - z * Math.sin(angle);
        double newZ = y * Math.sin(angle) + z * Math.cos(angle);
        return new Vector3d(x, newY, newZ);
    }
}
