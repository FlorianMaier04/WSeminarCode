package logic;

import logic.SimulationSystems.SonnenSystem;
import logic.objects.PhysikObjekt;
import rendering.Scene;
import rendering.input.InputFrame;
import tools.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class PhysicThread extends Thread {

    //Konstante Werte
    public static final double G = 6.67384 * Math.pow(10, -11.0);
    public static final double meterPerAE = 1.496 * Math.pow(10, 11);
    public static final long secondsPerYear = Math.round(60 * 60 * 24 * 365 + 5 * 60 * 60 + 48 * 60 + 46);

    public static final long startTimeSeconds = 953534056 + secondsPerYear * 1970; // Zeit die bis zu dem Frühlingspunkt 2000 vergangen ist (t0 meiner Simulation)
    public static final double secondsPerHour = Math.pow(60.0, 2.0);


    public static double timePassedYears() { return timePassed / (double) secondsPerYear; }
    public static double timePassedRealYears() { return (timePassed + startTimeSeconds) / (double) secondsPerYear; }



    public boolean runPhysicsSimulation = false;
    public static final boolean windowStart = true;

    public boolean running = true;

    public static final SonnenSystem activeSystem = new SonnenSystem();

    public static List<PhysikObjekt> physikObjekte = new ArrayList<>();

    public static double deltaT = 8000 / 250; // die Zeit, die in der Simulation ein Zeitschritt einnimmt in Sekunden
    public static double timePassed = 0; // Zeit die seit Simulationsstart vergangen ist (T)

    @Override
    public synchronized void start() {
        activeSystem.initPhysicThread(this);
        activeSystem.initContent();
        super.start();
    }

    @Override
    public void run() {
        if(windowStart)
            new InputFrame(this);
        while (running) {
            if (runPhysicsSimulation) {
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
        for(int i = 0;i < physikObjekte.size(); i++) {
            physikObjekte.get(i).update(physikObjekte);
        }
        for(int i = 0;i < physikObjekte.size(); i++) {
            physikObjekte.get(i).commitNewPos();
        }
        updateTime();
    }

//    private static double lastYearTime = 0;
//    private static int yearCounter = 0;
    private static void updateTime() {
        timePassed = timePassed + deltaT;
//        if(timePassed - lastYearTime > secondsPerYear) {
//            yearCounter++;
//            lastYearTime = timePassed;
//            System.out.println("year: "+ yearCounter);
//        }
    }
}
