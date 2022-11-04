package rendering;

import logic.PhysicThread;
import logic.PhysicsEngine;
import logic.objects.PhysikObjekt;
import logic.objects.RenderObject;
import logic.objects.vectors.RenderedVector;
import logic.objects.vectors.VectorHandler;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import rendering.shaders.StandardShader;
import tools.FeedbackBuilder;
import tools.Maths;
import tools.dataStructures.Vao;
import tools.dataStructures.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Scene {


    public static final float FOV = 60;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 50000;


    //Render Logic
    private final Matrix4f pm;
    private Camera camera;


    //RenderObjects
    private List<RenderObject> objects = new ArrayList<>();
    StandardShader sShader;

    public Scene() {
        pm = Maths.createProjectionMatrix();
        camera = new Camera();
        camera.setPosition(PhysicThread.activeSystem.getTargetCameraPosition());
        camera.setRotation(PhysicThread.activeSystem.getTargetCameraRotation());
        init();
        camera.init();
    }


    public void init() {
        sShader = new StandardShader();
        VectorHandler.init(pm);
    }

    public static FeedbackBuilder fb = new FeedbackBuilder();

    public void update() {
        fb.update();
        loadPhysicsData();
        if (!render) return;
        camera.update();

        glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        Matrix4f viewMatrix = renderObjects();
        glDisable(GL_DEPTH_TEST);

        VectorHandler.update(viewMatrix);
    }

    //parameters for the rendered view of the physics problem
    /**
     * the rendered distance 1 means 10^9 meters for the physics engine
     * must be always taken into account when computing physical values like force
     */
    public static final long metersPerRenderedDistance = (long) Math.pow(10, 9);
    public static boolean render = true;


    private void loadPhysicsData() {
        for (int i = 0; i < PhysicsEngine.physikObjekts.size(); i++) {
            RenderObject o = null;
            boolean objectPresent = i < objects.size();
            if (objectPresent) o = objects.get(i);
            RenderObject newObject = convertPhysicToRenderObject(PhysicsEngine.physikObjekts.get(i), o);
            if (objectPresent) objects.set(i, newObject);
            else objects.add(newObject);
        }
    }

    private static double maxMass = 0;


    private RenderObject convertPhysicToRenderObject(PhysikObjekt po, RenderObject ro) {
        if (maxMass < po.mass) maxMass = po.mass;
        RenderObject object = ro;
        if (object == null) {
            object = new RenderObject(0, 0, 0, 0, "sphere");
        }
        object.rotation = new Vector3f(po.rotation);
        object.scale = po.scale;
        object.pos = po.pos.divide(metersPerRenderedDistance).convertToFloat();
        if (po.fixedColor != null) {
            object.color = new Vector3f(po.fixedColor);
        } else {
            float colorValue = (float) ((po.mass / maxMass));
            object.color = new Vector3f(0.25f + 0.75f * colorValue, 0.25f + 0.5f * colorValue, 0.4f);
        }
        return object;
    }

    private Matrix4f renderObjects() {
        sShader.start();

        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        sShader.uViewMatrix.loadMatrix(viewMatrix);
        sShader.uProjectionMatrix.loadMatrix(pm);
        for (RenderObject o : objects) {
            renderObject(o);
        }

        sShader.stop();
        return viewMatrix;
    }

    private void renderObject(RenderObject o) {
        Vao model = o.getModel();
        sShader.uTransformationMatrix.loadMatrix(Maths.createTransformationMatrix(o.pos, o.rotation, o.scale));
        sShader.uColor.loadVec3(o.color);

        //bind vao to use
        model.bind(0);

        //actual draw
        glDrawElements(GL_TRIANGLES, model.getIndexCount(), GL_UNSIGNED_INT, 0);

    }
}