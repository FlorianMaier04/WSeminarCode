package logic.objects.vectors;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import rendering.shaders.VectorShader;
import tools.Loader;
import tools.Maths;
import tools.dataStructures.Vao;
import tools.dataStructures.Vector3d;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class VectorHandler {

    private static List<RenderedVector> renderedVectors = new ArrayList<>();

    private static VectorShader shader;

    //used to render every Vector (direction,pos,scale are applied in shaders)
    private static final float[] vertexArray = new float[]{0f, 0f, 0f, 1f, 1f, 1f};
    private static final int[] indicesArray = new int[]{1, 2};

    private static Vao vecModel;

    private static int idCount = 0;

    private static Matrix4f projectionMatrix;

    public static void init(Matrix4f pm) {
        projectionMatrix = pm;
        shader = new VectorShader();
        //setting up the model for every rendered vector
        vecModel = Loader.loadToVao(vertexArray, indicesArray);
    }

    public static void update(Matrix4f viewMatrix) {
        shader.start();
        //bind vao to use
        vecModel.bind(0);
        shader.uProjectionMatrix.loadMatrix(projectionMatrix);
        shader.uViewMatrix.loadMatrix(viewMatrix);

        for (RenderedVector v : renderedVectors) {
            renderVector(v);
        }
        //unbind
        vecModel.unbind(0);

        shader.stop();
    }

    private static void renderVector(RenderedVector v) {
        shader.uTransformationMatrix.loadMatrix(Maths.createTransformationMatrix(v.pos, new Vector3f(0, 0, 0), 1f));
        shader.uTargetTransformationMatrix.loadMatrix(Maths.createTransformationMatrix(v.computeTargetPosition(), new Vector3f(0, 0, 0), 1f));
        shader.uColor.loadVec3(v.color);
        //actual draw
        glDrawElements(GL_LINES, vecModel.getIndexCount(), GL_UNSIGNED_INT, 0);
    }

    //functions for controlling the VectorHandler

    /**
     * adds {@param v} to the list of rendered vectors
     * returs the handleId for changing or deleting this vector
     */
    public static int addVector(RenderedVector v) {
        v.handleId = idCount;
        idCount++;
        renderedVectors.add(v);
        return v.handleId;
    }

    /**
     * @return 1 when sucessfull, 0 when the Vector with {@param handleId} couldn't be found
     */
    public static int changeVector(int handleId, Vector3f pos, Vector3f direction, float scale) {
        for (int i = 0; i < renderedVectors.size(); i++) {
            if (renderedVectors.get(i).handleId == handleId) {
                renderedVectors.get(i).initialize(pos, direction, scale);
                return 1;
            }
        }
        return 0;
    }

    /**
     * @return 1 when sucessfull, 0 when the Vector with {@param handleId} couldn't be found
     */
    public static int changeVector(int handleId, Vector3d pos, Vector3d scaledDirection) {
        for (int i = 0; i < renderedVectors.size(); i++) {
            if (renderedVectors.get(i).handleId == handleId) {
                renderedVectors.get(i).initialize(pos, scaledDirection);
                return 1;
            }
        }
        return 0;
    }

    /**
     * @return 1 when sucessfull, 0 when the Vector with {@param handleId} couldn't be found
     */
    public static int changeVector(int handleId, Vector3d pos, Vector3d direction, float scale) {
        for (int i = 0; i < renderedVectors.size(); i++) {
            if (renderedVectors.get(i).handleId == handleId) {
                renderedVectors.get(i).initialize(pos, direction, scale);
                return 1;
            }
        }
        return 0;
    }

    /**
     * @return 1 when sucessfull, 0 when the Vector with {@param handleId} couldn't be found
     */
    public static int removeVector(int handleId) {
        for (int i = 0; i < renderedVectors.size(); i++) {
            if (renderedVectors.get(i).handleId == handleId) {
                renderedVectors.remove(i);
                return 1;
            }
        }
        return 0;
    }


}
