package tools;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import rendering.Camera;
import rendering.Scene;
import rendering.Window;
import tools.dataStructures.Matrix3d;

public class Maths {

    public static Matrix3d createRotationMatrixX(double r1) {
        Matrix3d matrix = new Matrix3d();
        matrix.values[0][0] = 1;
        matrix.values[0][1] = 0;
        matrix.values[0][2] = 0;

        matrix.values[1][0] = 0;
        matrix.values[1][1] = Math.cos(r1);
        matrix.values[1][2] = -Math.sin(r1);

        matrix.values[2][0] = 0;
        matrix.values[2][1] = Math.sin(r1);
        matrix.values[2][2] = Math.cos(r1);
        return matrix;
    }

    public static Matrix3d createRotationMatrixZ(double r3) {
        Matrix3d matrix = new Matrix3d();
        matrix.values[0][0] = Math.cos(r3);
        matrix.values[0][1] = -Math.sin(r3);
        matrix.values[0][2] = 0;

        matrix.values[1][0] = Math.sin(r3);
        matrix.values[1][1] = Math.cos(r3);
        matrix.values[1][2] = 0;

        matrix.values[2][0] = 0;
        matrix.values[2][1] = 0;
        matrix.values[2][2] = 1;
        return matrix;
    }

    public static Matrix3d createRotationMatrixY(double r2) {
        Matrix3d matrix = new Matrix3d();
        matrix.values[0][0] = Math.cos(r2);
        matrix.values[0][1] = 0;
        matrix.values[0][2] = Math.sin(r2);

        matrix.values[1][0] = 0;
        matrix.values[1][1] = 1;
        matrix.values[1][2] = 0;

        matrix.values[2][0] = -Math.sin(r2);
        matrix.values[2][1] = 0;
        matrix.values[2][2] = Math.cos(r2);
        return matrix;
    }

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
