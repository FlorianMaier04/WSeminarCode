package tools.dataStructures;

public class MeshData {

    private static final int DIMENSIONS = 3;

    private float[] vertices;
    private float[] textureCoords;
    private int[] indices;


    public MeshData(float[] vertices, float[] textureCoords, int[] indices) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = indices;
//        for (float f : textureCoords) {
//            System.out.println("textureCoords: " + f);
//        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVertexCount() {
        return vertices.length / DIMENSIONS;
    }
}
