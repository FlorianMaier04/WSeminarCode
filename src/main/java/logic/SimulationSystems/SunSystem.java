package logic.SimulationSystems;

import logic.PhysicThread;
import logic.PhysicsEngine;
import logic.objects.PhysicObject;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import rendering.Scene;
import tools.Keyboard;
import tools.dataStructures.Vector3d;

import static logic.PhysicsEngine.G;
import static logic.PhysicsEngine.meterPerAE;

public class SunSystem implements SimulationSystem {

    int printCounter = 0;
    private final int updatesPerPrint = 200000;
    private int currentUpdatesPerPrint = updatesPerPrint;
    private final double secondsPerFrame = 15;

    private PhysicObject sun;
    private PhysicObject earth;
    private PhysicObject testEarth;
    private PhysicObject moon;
    private PhysicObject mars;
    private PhysicObject mercury;
    private PhysicObject venus;
    private PhysicObject jupiter;
    private PhysicObject saturn;


    public boolean displayVectors = false;

    private PhysicThread pt;

    @Override
    public void initContent() {
        //Erde
        earth = new PhysicObject(1.471 * Math.pow(10, 11), 0, 0, 2f);
        earth.initPhysics(0, -30290, 0, 5.9742 * Math.pow(10, 24));
        earth.fixedColor = new Vector3f(0, 191, 255);
        earth.scale = 2f;

        //Mond
//        moon = new PhysicObject(earth.pos.x, earth.pos.y + 363.3 * Math.pow(10, 6), earth.pos.z, 2f);
//        moon.initPhysics(earth.speed.x, earth.speed.y, earth.speed.z - 1023, 7.346 * Math.pow(10, 22));
//        moon.fixedColor = new Vector3f(255, 1, 1);
//        mon.name="moon";

        //Sonne
        sun = new PhysicObject(0, 0, 0, 15);
        sun.initPhysics(0, 0, 0, 1.9891 * Math.pow(10, 30));
        sun.fixedColor = new Vector3f(248, 243, 43);
        sun.name = "sun";
        sun.scale = 15;

        //Mars
        mars = new PhysicObject(1.381 * meterPerAE, 0, 0, 1.5f);
        mars.initPhysics(0, -26500, 0, 6.417 * Math.pow(10, 23));
        mars.fixedColor = new Vector3f(205, 102, 29);
//        mars.name = "mars";
        earth.scale = 1.5f;

        //Merkur
        mercury = new PhysicObject(0.3075 * meterPerAE, 0, 0, 1f);
        mercury.initPhysics(0, -47870, 0, 3.301 * Math.pow(10, 23));
        mercury.fixedColor = new Vector3f(136, 136, 136);
//        mercury.name = "mercury";

        //Venus
        venus = new PhysicObject(0.718 * meterPerAE, 0, 0, 1.5f);
        venus.initPhysics(0, -35020, 0, 4.875 * Math.pow(10, 24));
        venus.fixedColor = new Vector3f(136 * 1.5f, 136 * 1.5f, 136 * 1.5f);
//        venus.name = "venus";

        //Jupiter
        jupiter = new PhysicObject(4.950 * meterPerAE, 0, 0, 9);
        jupiter.initPhysics(0, -13060, 0, 1.899 * Math.pow(10, 27));
        jupiter.fixedColor = new Vector3f(139, 90, 43);
//        jupiter.name = "jupiter";


        //Saturn
        saturn = new PhysicObject(9.041 * meterPerAE, 0, 0, 7.5f);
        saturn.initPhysics(0, -9680, 0, 5.683 * Math.pow(10, 26));
        saturn.fixedColor = new Vector3f(255, 211, 155);
//        saturn.name = "saturn";

        //test
        double kineticEnergy = PhysicsEngine.getKineticEnergy(earth.mass, earth.speed);
        double potentialEnergy = PhysicsEngine.getPotentialEnergy(earth.mass, sun.mass, earth.pos, sun.pos);
        double energy = kineticEnergy + potentialEnergy;
        System.out.println("energy: " + energy + " potentialEnergy: " + potentialEnergy + " kineticEnergy: " + kineticEnergy);


        // testErde
        double alpha = 0.5;
        testEarth = new PhysicObject(alpha * 1.471 * Math.pow(10, 11), 0, 0, 2f);
        double v1 = -30290;
        double r1 = testEarth.pos.sub(sun.pos).scale(1 / alpha).length();
        double v2 = Math.sqrt((2 * G * sun.mass * (1 - alpha)) / (r1 * alpha) + Math.pow(v1, 2));
        testEarth.initPhysics(0, -v2, 0, 5.9742 * Math.pow(10, 24));
        testEarth.fixedColor = new Vector3f(255, 200, 255 / 4);

    }

    private int yearCounter = 0;

    private long lastYearTime = System.currentTimeMillis();

    @Override
    public void update() {
//        Vector3d earthMarsVector = mars.pos.sub(earth.pos);

        if (PhysicsEngine.timePassed / (60 * 60 * 24 * 365.25) > yearCounter) {
            yearCounter++;
            System.out.println("happy new year: " + yearCounter + " secondsForYear: " + (double) (System.currentTimeMillis() - lastYearTime) / 1000.0);
            lastYearTime = System.currentTimeMillis();
        }


        if (printCounter == currentUpdatesPerPrint) {
            currentUpdatesPerPrint = updatesPerPrint;
            double pre = PhysicsEngine.secondsPerFrame;
            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_H)) {
                PhysicsEngine.secondsPerFrame = PhysicsEngine.secondsPerFrame - Math.pow(10, 1);
            } else if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_J)) {
                PhysicsEngine.secondsPerFrame = PhysicsEngine.secondsPerFrame + Math.pow(10, 1);
            }
            if (PhysicsEngine.secondsPerFrame == 0 || PhysicsEngine.secondsPerFrame < 0) {
                PhysicsEngine.secondsPerFrame = 0.1;
            }
            if (PhysicsEngine.secondsPerFrame != pre) {
                System.err.println();
                System.err.println("secondsPerFrame: " + PhysicsEngine.secondsPerFrame);
                System.err.println();
            }
            if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_B)) {
                Scene.render = !Scene.render;
                currentUpdatesPerPrint = 200000 * 5;
            }
            printCounter = 0;
        }
        printCounter++;
    }

    @Override
    public void initPhysicThread(PhysicThread pt) {
        this.pt = pt;
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
        return displayVectors;
    }

    @Override
    public double getSecondsPerFrame() {
        return secondsPerFrame;
    }

}
