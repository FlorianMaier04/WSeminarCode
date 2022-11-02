package logic.objects;

import logic.PhysicThread;
import logic.PhysicsEngine;
import logic.objects.vectors.RenderedVector;
import logic.objects.vectors.VectorHandler;
import org.lwjgl.util.vector.Vector3f;
import rendering.Scene;
import tools.dataStructures.Vector3d;

import java.util.List;

public class PhysicObject {

    public Vector3d pos;
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public float scale;

    public Vector3d acceleration = new Vector3d(0, 0, 0);
    public Vector3d speed = new Vector3d(0, 0, 0);
    public double mass = 0;

    //used for showing debugging-info
    public static boolean displayVector = false;

    private static int visualizationIdCounter = 0;
    private int visualizationId;

    public Vector3f fixedColor = null;

    public String name = null;


    public static PhysicObject createObjectWithEquatorialCoordinates(EquatorialCoordinateSystem.EquatorialCoordinate equatorialCoordinate,
                                                                     PhysicObject earth, PhysicObject sun) {
        // einen normalisierten Vekotr errechnet, der von der Erde aus auf das zu erschaffende Objekt zeigt
        EquatorialCoordinateSystem system = new EquatorialCoordinateSystem(earth, sun);
        Vector3d coordinate = equatorialCoordinate.computeCoordinatePosition(system);

        PhysicObject object = new PhysicObject(coordinate.x, coordinate.y, coordinate.z);

        return object;
    }

    public PhysicObject(double x, double y, double z) {
        pos = new Vector3d(x, y, z);
        yearPoint = pos;
        PhysicsEngine.addPhysicsObject(this);
        visualizationId = visualizationIdCounter;
        visualizationIdCounter++;
    }

    private int printCounter = 0;
    private Boolean stopSunMovement = true;
    private int sonnenVectorId = -1;
    public void update(List<PhysicObject> physicObjects) {
        //results from all forces added
        Vector3d forceVector = new Vector3d(0, 0, 0);
        //computing force
        for (int i = 0; i < physicObjects.size(); i++) {
            PhysicObject o2 = physicObjects.get(i);
            if (o2 == this) continue;

            Vector3d diffVector = o2.pos.sub(pos);
            double r = diffVector.length();
            float force = (float) PhysicsEngine.getGForce(o2.mass, mass, r);
            forceVector = forceVector.add(diffVector.normalize().scale(force));
        }
        acceleration = forceVector.divide(mass);

        if(name != null && name.equals("sonne") && !PhysicThread.windowStart) {
            if(sonnenVectorId == -1) {
                RenderedVector sonnenVector = new RenderedVector(pos,forceVector.normalize(), 10000)
                        .withColor(0,1,1);
                VectorHandler.addVector(sonnenVector);
                sonnenVectorId = sonnenVector.handleId;
                VectorHandler.addVector(
                        new RenderedVector(pos,speed.normalize(), 10000)
                                .withColor(0.5f,1, 0.5f)
                );
            }
            VectorHandler.changeVector(sonnenVectorId,pos, forceVector.normalize(),
                    (float)(forceVector.length() / Math.pow(10,20)));
            VectorHandler.changeVector(sonnenVectorId + 1,pos, speed.normalize(),
                    (float)(speed.length() * Math.pow(10,4)));
            printCounter++;
            if(printCounter > 3000) {
                stopSunMovement = false;
//                System.out.println("force: "+forceVector+" speed: " + speed + " acceleration: " + acceleration);
                printCounter = 0;
            }
        }

        Scene.handleVector(acceleration, speed, pos, displayVector, visualizationId);

        double timePassed = PhysicsEngine.secondsPerFrame;
        speed = speed.add(acceleration.scale(timePassed));
        pos = pos.add(speed.scale(timePassed));
    }

    // Variablen für die Berechnung von Umlaufzeit

    private double[] yearPointDistanceHistory = new double[]{0.0,0.0};
    // der Ausgangspunkt des Planeten, wenn er ihn überfliegt beginnt ein neues Planetenjahr
    private final Vector3d yearPoint;
    private double recentOrbitalPeriod;
    private double averageOrbitalPeriod;
    private int averageCounter = 0;
    private double lastYearPointTime = -1;
    private double lastUpdateTime = -1;

    /**
     * um herauszufinden, ob der Planet einen vollen Umlauf geschafft hatt,
     * wird die Distanz zu seiner initialposition berechnet, außerdem werden
     * die Distanzen der letzten beiden Berechnungsschritte gespeichert.
     * Durch die Subtraktion der beiden gespeicherten Werte wird herausgefunden,
     * ob die Distanz zwischen dem Planeten und dem [yearPoint] größer oder kleiner wurde.
     * Durch die Subtraktion der neu errechneten Distanz und der davorigen Distanz kann man
     * herausfinden, ob der Trend fortgesetz wurde, ob der Planet sich jetzt nicht mehr annähert sondern
     * wieder wegfliegt. Wenn dies der Fall ist klar, dass der Planet seinen [yearPoint] überflogen hat
     * und damit einen vollen Umlauf hinter sich gebracht hat. Die Möglichkeit, dass sich der Planet zuerst
     * vom [yearPoint] wegbewegt hat und sich dann wieder annähert gibt es ebenfalls, in diesem Szenario hat
     * der Planet die hälfte seines Umlaufes geschafft.
     */
    public void computePlanetYear() {
        if(lastYearPointTime == -1) {
            lastYearPointTime = PhysicsEngine.timePassed;
            lastUpdateTime = PhysicsEngine.timePassed;
        }

        double yearPointDistance = pos.sub(yearPoint).length();
        int recentTrend = normaliseNumber(yearPointDistanceHistory[1] - yearPointDistanceHistory[0]);
        int newestTrend = normaliseNumber(yearPointDistanceHistory[0] - yearPointDistance);

//        double recentDiff = (yearPointDistanceHistory[1] - yearPointDistanceHistory[0]);
//        double newestDiff = (yearPointDistanceHistory[0] - yearPointDistance);
//
//        if(recentDiff > newestDiff) {
//            if(!lastUpdatePositive) {
//                computeOrbitalPeriod();
//            }
//            lastUpdatePositive = true;
//        } else
//            lastUpdatePositive = false;
        if(recentTrend == 1 && newestTrend == -1) {
            double recentDiff = (yearPointDistanceHistory[1] - yearPointDistanceHistory[0]);
            double newestDiff = (yearPointDistanceHistory[0] - yearPointDistance);

            double recentRatio = recentDiff / (recentDiff + newestDiff);
            double newestRatio = newestDiff / (recentDiff + newestDiff);

            double middleTime = PhysicsEngine.timePassed * newestRatio + lastUpdateTime * recentRatio;
//            System.out.println("diff: "+ (PhysicsEngine.timePassed - middleTime) );
            computeOrbitalPeriod();
        }

        yearPointDistanceHistory[1] = yearPointDistanceHistory[0];
        yearPointDistanceHistory[0] = yearPointDistance;
        lastUpdateTime = PhysicsEngine.timePassed;
    }

    /**
     * wird aufgerufen, wenn ein neues Planetenjahr erreicht wurde
     */
    private void computeOrbitalPeriod() {
        double yearLength = PhysicsEngine.timePassed - lastYearPointTime;
        recentOrbitalPeriod = yearLength;
        averageOrbitalPeriod = (averageCounter * averageOrbitalPeriod + recentOrbitalPeriod)
                / (averageCounter + 1);

        lastYearPointTime = PhysicsEngine.timePassed;
        averageCounter++;
    }

    public double getRecentOrbitalPeriod() {
        return recentOrbitalPeriod;
    }

    public double getAverageOrbitalPeriod() {
        return averageOrbitalPeriod;
    }

    /**
     * normalises an one dimensional vector
     */
    private int normaliseNumber(double number) {
        return (int)(number / Math.abs(number));
    }

    public void initPhysics(double sx, double sy, double sz, double mass) {
        setSpeed(sx, sy, sz);
        this.mass = mass;
    }

    public void initPhysics(double speedAmount, double mass, @org.jetbrains.annotations.NotNull PhysicObject sun) {
        Vector3d sunVector = pos.sub(sun.pos);
        // alles spielt sich in der x1, x2 Ebene ab
        double x3 = 0;
        double x2 = speedAmount / Math.sqrt((Math.pow(sunVector.y,2)/Math.pow(sunVector.x,2)+1));
        double x1 = -(sunVector.y * x2) / sunVector.x;

        Vector3d speedVector = new Vector3d(x1,x2,x3);
        System.out.println("speedVector: "+ speedVector.length());

        initPhysics(x1,x2,x3,mass);
    }

    public void setSpeed(double sx, double sy, double sz) {
        speed = new Vector3d(sx, sy, sz);
    }
}
