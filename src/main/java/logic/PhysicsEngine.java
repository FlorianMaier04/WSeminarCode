package logic;

import logic.objects.PhysikObjekt;
import tools.dataStructures.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine extends Thread {

    //constant values
    public static final double G = 6.67384 * Math.pow(10, -11.0);
    public static final double meterPerAE = 1.496 * Math.pow(10, 11);
    public static final long secondsPerYear = 31536000;
    public static final long startTimeSeconds = 953534056 + secondsPerYear * 1970;
    public static final double startTimeDays = 895455583.3805;
    public static final double secondsPerDay =  86400;
    public static final double secondsPerHour = Math.pow(60.0, 2.0);

    /**
     * the amount of time that passes in 1 frame
     */
    public static double secondsPerFrame = -1;


    //in seconds
    public static double timePassed = 0;

    public static double timePassedDays() { return timePassed / (double) secondsPerDay; }
    public static double timePassedYears() { return timePassed / (double) secondsPerYear; }
    public static double timePassedRealYears() { return (timePassed + startTimeSeconds) / (double) secondsPerYear; }
    public static double convertSD(double time) { return time / (double) secondsPerDay; }
    public static double convertSY(double time) { return time / secondsPerYear; }

    //contains all objects to be computed by the physicsEngine
    public static List<PhysikObjekt> physikObjekts = new ArrayList<>();


    /**
     * @param m1 mass of object 1
     * @param m2 mass of object 2
     * @param r  radius between both objects in physics(-rendered) space
     * @return the Gravitation-Force between both objects in double
     */
    public static double getGForce(double m1, double m2, double r) {
        double force = G * m1 * m2 * (1 / Math.pow(r, 2));
        return force;
    }


    public static double getKineticEnergy(double m, Vector3d speed) {
        double energy = 0.5 * Math.pow(speed.length(), 2) * m;
        return energy;
    }

    public static double getPotentialEnergy(double m1, double m2, Vector3d pos1, Vector3d pos2) {
        double r = pos2.sub(pos1).length();
        System.out.println("r: " + r + " m1: " + m1 + " m2: " + m2 + " test: " + (m1 * m2));
        double energy = (G * m1 * m2) / r;
        return energy;
    }


    public static void update() {
        for (PhysikObjekt o : physikObjekts) {
            o.update(physikObjekts);
        }
        updateTime();
    }

    private static void updateTime() {
        timePassed += secondsPerFrame;
    }


    public static void addPhysicsObject(PhysikObjekt physikObjekt) {
        physikObjekts.add(physikObjekt);
    }

    public static void setSecondsPerFrame(double value) {
        secondsPerFrame = value;
    }
}
