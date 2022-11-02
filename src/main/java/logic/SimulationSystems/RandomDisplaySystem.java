package logic.SimulationSystems;

import logic.PhysicThread;
import logic.objects.PhysicObject;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class RandomDisplaySystem implements SimulationSystem {

    private final double secondsPerFrame = 1;

    @Override
    public void initContent() {
        float spawnRange = 1000;
        int spawnCount = 0;
        Random r = new Random();
        for (int i = 0; i < spawnCount; i++) {
            float x = r.nextFloat() * spawnRange, y = r.nextFloat() * spawnRange / 3f, z = r.nextFloat() * spawnRange;
            float size = r.nextFloat() * 5;
            PhysicObject o = new PhysicObject(x, y, z);
            o.scale = size;
            o.initPhysics(0, 0, 0, Math.pow(10, 30));
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void initPhysicThread(PhysicThread pt) {

    }

    @Override
    public PhysicObject getEarth() {
        return null;
    }

    @Override
    public String[] getDebuggedPlanets() {
        return new String[0];
    }

    @Override
    public int[] getDebuggedValues() {
        return new int[0];
    }

    @Override
    public Vector3f getTargetCameraPosition() {
        return new Vector3f(0,0,100);
    }

    @Override
    public boolean displayVectors() {
        return false;
    }

    @Override
    public double getSecondsPerFrame() {
        return secondsPerFrame;
    }
}

