package tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import rendering.Camera;
import rendering.Scene;
import rendering.Window;

public class Maths {


    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(new Vector3f(translation.x, translation.y, translation.z), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    /**
     * creates a view matrix of the camera data this method takes hard-coded data
     * where the game doesn't have enough camera data
     *
     * @param camera
     * @return
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.rotation.x), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.rotation.y), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.rotation.z), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f negativeCameraPos = new Vector3f(-camera.position.x, -camera.position.y, -camera.position.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Window.getWindowHeight() / (float) (Window.getWindowWidth());
//        aspectRatio=2;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(Scene.FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = Scene.FAR_PLANE - Scene.NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((Scene.FAR_PLANE + Scene.NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * Scene.NEAR_PLANE * Scene.FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
        return projectionMatrix;
    }
}
