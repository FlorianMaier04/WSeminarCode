package logic.objects;

import logic.PhysicThread;
import logic.PhysicsEngine;
import logic.objects.vectors.RenderedVector;
import logic.objects.vectors.VectorHandler;
import org.lwjgl.util.vector.Vector3f;
import rendering.Scene;
import tools.Maths;
import tools.dataStructures.Matrix3d;
import tools.dataStructures.Vector3d;

import java.util.List;

public class PhysikObjekt {

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


    public static PhysikObjekt initialisiereObjektMitAequatorkoordinate(EquatorialCoordinateSystem.EquatorialCoordinate equatorialCoordinate,
                                                                        PhysikObjekt earth, PhysikObjekt sun) {
        // einen normalisierten Vekotr errechnet, der von der Erde aus auf das zu erschaffende Objekt zeigt
        EquatorialCoordinateSystem system = new EquatorialCoordinateSystem(earth, sun);
        Vector3d coordinate = equatorialCoordinate.computeCoordinatePosition(system);

        System.out.println("coordinate: " + coordinate);
        PhysikObjekt object = new PhysikObjekt(coordinate.x , coordinate.y, coordinate.z);

        return object;
    }

    public PhysikObjekt(double x, double y, double z) {
        pos = new Vector3d(x, y, z);
        PhysicsEngine.addPhysicsObject(this);
        visualizationId = visualizationIdCounter;
        visualizationIdCounter++;
    }

    private int printCounter = 0;
    private Boolean stopSunMovement = true;
    private int sonnenVectorId = -1;
    public void update(List<PhysikObjekt> physikObjekts) {
        //results from all forces added
        Vector3d forceVector = new Vector3d(0, 0, 0);
        //computing force
        for (int i = 0; i < physikObjekts.size(); i++) {
            PhysikObjekt o2 = physikObjekts.get(i);
            if (o2 == this) continue;

            Vector3d diffVector = o2.pos.sub(pos);
            double r = diffVector.length();
            float force = (float) PhysicsEngine.getGForce(o2.mass, mass, r);
            forceVector = forceVector.add(diffVector.normalize().scale(force));
        }
        acceleration = forceVector.divide(mass);

//        if(name != null && name.equals("sonne") && !PhysicThread.windowStart) {
//            if(sonnenVectorId == -1) {
//                RenderedVector sonnenVector = new RenderedVector(pos,forceVector.normalize(), 10000)
//                        .withColor(0,1,1);
//                VectorHandler.addVector(sonnenVector);
//                sonnenVectorId = sonnenVector.handleId;
//                VectorHandler.addVector(
//                        new RenderedVector(pos,speed.normalize(), 10000)
//                                .withColor(0.5f,1, 0.5f)
//                );
//            }
//            VectorHandler.changeVector(sonnenVectorId,pos, forceVector.normalize(),
//                    (float)(forceVector.length() / Math.pow(10,20)));
//            VectorHandler.changeVector(sonnenVectorId + 1,pos, speed.normalize(),
//                    (float)(speed.length() * Math.pow(10,4)));
//            printCounter++;
//            if(printCounter > 3000) {
//                stopSunMovement = false;
////                System.out.println("force: "+forceVector+" speed: " + speed + " acceleration: " + acceleration);
//                printCounter = 0;
//            }
//        }

        Scene.handleVector(acceleration, speed, pos, displayVector, visualizationId);

        double timePassed = PhysicsEngine.secondsPerFrame;
        speed = speed.add(acceleration.scale(timePassed));
        pos = pos.add(speed.scale(timePassed));
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

    public void berechneGeschwindigkeit(double d0, double d1, double dt, double betragV) {
        double v3 = 0;
        double q = -0.5 * (Math.pow(d1, 2) - Math.pow(d0, 2) - Math.pow(betragV, 2));

        double cosAlpha = (Math.pow(d1, 2) - Math.pow(d0, 2) - Math.pow(betragV * dt, 2)) / (-2 * d0 * betragV * dt);

        double v2 = betragV - betragV * cosAlpha;
        double v1 = betragV * cosAlpha;

//        System.out.println("cosApha: " + cosAlpha);
//
//
//        double v1, v2;
//        double positionTimeFactor = 2 * pos.x * dt;
//        if(positionTimeFactor == 0) {
//            v1 = 0.0;
//        } else
//            v1 = q / (2 * pos.x * dt);
//
//        positionTimeFactor = 2 * pos.y * dt;
//        if(positionTimeFactor == 0) {
//            v2 = 0.0;
//        } else
//            v2 = q / positionTimeFactor;


        setSpeed(v1, v2, v3);
    }

    public void initPhysics(double speedAmount, double mass, @org.jetbrains.annotations.NotNull PhysikObjekt sun, double ekliptikWinkelGrad) {
        Vector3d sunVector = pos.sub(sun.pos);

        double ekliptikWinkel = (ekliptikWinkelGrad / 360) * 2 * Math.PI;
        Matrix3d rotationMatrixX = Maths.createRotationMatrixX(ekliptikWinkel);

        double x3 = 0;
        double x2 = speedAmount / Math.sqrt((Math.pow(sunVector.y,2)/Math.pow(sunVector.x,2)+1));
        double x1 = -(sunVector.y * x2) / sunVector.x;

        initPhysics(x1,x2,x3,mass);
    }

    public void setSpeed(double sx, double sy, double sz) {
        speed = new Vector3d(sx, sy, sz);
    }
}
