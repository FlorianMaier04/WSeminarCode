package tools.dataStructures;

public class Vector3l {

    public long x, y, z;

    public Vector3l(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3l(Vector3l vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public long getLength() {
        return (long) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3l normalise() {
        long length = getLength();
        return new Vector3l(x / length, y / length, z / length);
    }

    public Vector3l sub(Vector3l vec) {
        Vector3l result = new Vector3l(x - vec.x, y - vec.y, z - vec.z);
        return result;
    }


}
