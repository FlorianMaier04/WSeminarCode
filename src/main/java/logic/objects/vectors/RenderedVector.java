package logic.objects.vectors;

import logic.PhysicsEngine;
import org.lwjgl.util.vector.Vector3f;
import rendering.Scene;
import tools.dataStructures.Vector3d;

public class RenderedVector {

    public float scale;
    public double renderScale = 1;
    //must be normalized
    public Vector3f direction;
    public Vector3f pos;

    /**
     * used when dealt with in {@link VectorHandler} -1 means no id assigned
     */
    public int handleId = -1;

    public Vector3f color = new Vector3f(1, 0, 0);

    public RenderedVector(Vector3f pos, Vector3f direction, float scale) {
        this.scale = scale;
        this.direction = new Vector3f(direction);
        this.pos = new Vector3f(pos);
    }

    public RenderedVector(Vector3f pos, Vector3f scaledDirection) {
        this.scale = scaledDirection.length();
        if (scale == 0) scaledDirection = new Vector3f(1, 1, 1);
        this.direction = (Vector3f) new Vector3f(scaledDirection).normalise();
        this.pos = new Vector3f(pos);
    }

    public RenderedVector(Vector3d dPos, Vector3d dScaledDirection) {
        Vector3f pos = convertRealDistanceToRenderedDistance(dPos);
        Vector3f scaledDirection = dScaledDirection.convertToFloat();
        this.scale = scaledDirection.length();
        if (scale == 0) scaledDirection = new Vector3f(1, 1, 1);
        this.direction = (Vector3f) new Vector3f(scaledDirection).normalise();
        this.pos = new Vector3f(pos);
    }

    public RenderedVector(Vector3d dPos, Vector3d dDirection, float scale) {
        this.scale = scale;
        this.direction = dDirection.convertToFloat();
        this.pos = convertRealDistanceToRenderedDistance(dPos);
    }

    private Vector3f convertRealDistanceToRenderedDistance(Vector3d dVec) {
        Vector3f result = dVec.divide(Scene.metersPerRenderedDistance).convertToFloat();
        return result;
    }

    public void initialize(Vector3d dPos, Vector3d dDirection, float scale) {
        this.scale = scale;
        this.direction = dDirection.convertToFloat();
        this.pos = convertRealDistanceToRenderedDistance(dPos);
    }

    public void initialize(Vector3f pos, Vector3f direction, float scale) {
        this.scale = scale;
        this.pos = new Vector3f(pos);
        this.direction = new Vector3f(direction);
    }

    public void initialize(Vector3d dPos, Vector3d dScaledDirection) {
        Vector3f pos = convertRealDistanceToRenderedDistance(dPos);
        Vector3f scaledDirection = dScaledDirection.convertToFloat();
        this.scale = scaledDirection.length();
        if (scale == 0) scaledDirection = new Vector3f(1, 1, 1);
        this.direction = (Vector3f) new Vector3f(scaledDirection).normalise();
        this.pos = new Vector3f(pos);
    }

    public Vector3f computeTargetPosition() {
        Vector3f target = Vector3f.add(pos, (Vector3f) new Vector3f(direction).scale(scale * (float) renderScale), null);
        return target;
    }

    public RenderedVector withColor(float r, float g, float b) {
        //0 - 255 is the rgb value range
        color = new Vector3f(r * 255, g * 255, b * 255);
        return this;
    }
}
