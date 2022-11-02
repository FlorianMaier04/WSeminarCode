package logic;

import logic.SimulationSystems.RealisticSunSystem;
import logic.SimulationSystems.SimulationSystem;
import logic.objects.PhysicObject;
import rendering.Scene;
import rendering.input.InputFrame;
import tools.FeedbackBuilder;
import tools.Keyboard;

import static org.lwjgl.glfw.GLFW.*;

public class PhysicThread extends Thread {

    public boolean running = true;

    //time messuring:

    private long lastTime = 0;
    private long timePerUpdate = 0;    //is set every second new in milliseconds

    public static final SimulationSystem activeSystem = new RealisticSunSystem();

    public static String writtenPlanet = null;
    public static String writtenPlanetFileName = "";
    public static final String writtenPlanetFilePath = "GraphShower/res/";
    public static int writeMode = FeedbackBuilder.DISTANCE_TO_EARTH;
    public static long secondsPerWrite = PhysicsEngine.secondsPerYear / (16 * 16);

    @Override
    public synchronized void start() {
        initContent();
        super.start();
    }

    private void initContent() {
        activeSystem.initPhysicThread(this);
        PhysicsEngine.setSecondsPerFrame(activeSystem.getSecondsPerFrame());
        PhysicObject.displayVector = activeSystem.displayVectors();
        activeSystem.initContent();
    }

    public boolean runPhysicsSimulation = false;

    public static final boolean windowStart = false;

    @Override
    public void run() {
        if(windowStart)
            new InputFrame(this);
        while (running) {
            if (runPhysicsSimulation) {
                activeSystem.update();
                updatePhysics();
            } else {
                if (Keyboard.isKeyPressed(GLFW_KEY_G)) {
                    runPhysicsSimulation = true;
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (Keyboard.isKeyPressed(GLFW_KEY_ESCAPE)) {
                running = false;
            }
        }
        Scene.fb.finish();
        System.exit(0);
    }

    private void updatePhysics() {
        PhysicsEngine.update();
    }
}
