package logic.objects;

import org.lwjgl.util.vector.Vector3f;
import tools.Loader;
import tools.dataStructures.MeshData;
import tools.dataStructures.ModelTexture;
import tools.dataStructures.Vao;
import tools.objLoader.ObjFileLoader;

public class RenderObject {


    public Vector3f color = new Vector3f(1, 1, 1);
    private Vao model;


    public Vector3f pos;
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public float scale;

    public RenderObject(float x, float y, float z, float scale, String modelName) {
        pos = new Vector3f(x, y, z);
        this.scale = scale;
        MeshData md = ObjFileLoader.loadOBJ(modelName);
        model = Loader.loadToVao(md.getVertices(), md.getTextureCoords(), md.getIndices());
    }

    public Vao getModel() {
        return model;
    }
}
