package logic.objects;

import logic.PhysicsEngine;
import org.lwjgl.util.vector.Vector3f;
import tools.Maths;
import tools.dataStructures.Matrix3d;
import tools.dataStructures.Vector3d;

import java.util.List;

public class PhysikObjekt {

    public Vector3d pos;
    public float scale;

    public Vector3d speed = new Vector3d(0, 0, 0);
    public double mass = 0;

    public Vector3f fixedColor = null;

    public String name = null;


    public static PhysikObjekt initialisiereObjektMitAequatorkoordinate(EquatorialCoordinateSystem.EquatorialCoordinate equatorialCoordinate,
                                                                        PhysikObjekt earth, PhysikObjekt sun) {
        // einen normalisierten Vekotr errechnet, der von der Erde aus auf das zu erschaffende Objekt zeigt
        EquatorialCoordinateSystem system = new EquatorialCoordinateSystem(earth, sun);
        Vector3d coordinate = equatorialCoordinate.computeCoordinatePosition(system);

        PhysikObjekt object = new PhysikObjekt(coordinate.x , coordinate.y, coordinate.z);

        return object;
    }

    public PhysikObjekt(double x, double y, double z) {
        pos = new Vector3d(x, y, z);
        PhysicsEngine.addPhysicsObject(this);
    }

    public void update(List<PhysikObjekt> physikObjekte) {

        Vector3d f = new Vector3d(0, 0, 0);
        for (int j = 0; j < physikObjekte.size(); j++) {
            PhysikObjekt Oj = physikObjekte.get(j);
            if (Oj == this) continue; // i != i

            Vector3d diffVektor = Oj.pos.sub(pos);
            double r = diffVektor.length();
            double mi = mass, mj = Oj.mass;
            double force = (PhysicsEngine.G * mi * mj) / Math.pow(r, 2);

            f = f.add(diffVektor.normalize().scale(force));
        }
        Vector3d a = f.divide(mass);

        double timePassed = PhysicsEngine.secondsPerFrame;
        speed = speed.add(a.scale(timePassed));
        pos = pos.add(speed.scale(timePassed));
    }

    public void berechneGeschwindigkeit(double d0, double d1, double dt, double betragV) {
        double v3 = 0;

        double cosAlpha = (Math.pow(d1, 2) - Math.pow(d0, 2) - Math.pow(betragV * dt, 2)) / (-2 * d0 * betragV * dt);

        double v2 = betragV - betragV * cosAlpha;
        double v1 = -betragV * cosAlpha;

        setSpeed(v1, -v2, v3);
    }

    public void berechneGeschwindigkeit(double betragV, @org.jetbrains.annotations.NotNull PhysikObjekt sonne, double ekliptikWinkelGrad, double d0, double d1, double dt) {
        Vector3d sonnenVektor = pos.sub(sonne.pos);

        double ekliptikWinkel = (ekliptikWinkelGrad / 360) * 2 * Math.PI;
        Matrix3d rotationsMatrixX = Maths.createRotationMatrixX(ekliptikWinkel);

        double cosAlpha = (Math.pow(d1, 2) - Math.pow(d0, 2) - Math.pow(betragV * dt, 2)) / (-2 * d0 * betragV * dt);
        double alpha = Math.acos(cosAlpha);
        double alphaDegree = (alpha / (2 * Math.PI)) * 360;
        Matrix3d rotationsMatrixZ = Maths.createRotationMatrixZ(alpha);

        Vector3d rotatedSonnenVektor = rotationsMatrixZ.multiply(sonnenVektor);
        rotatedSonnenVektor = rotationsMatrixX.multiply(rotatedSonnenVektor);
//        double v1 = 0;
//        double v2 = betragV / Math.sqrt((Math.pow(sonnenVektor.y,2)/Math.pow(sonnenVektor.x,2)+1));
//        double v3 = -(sonnenVektor.y * v2) / sonnenVektor.x;

//        Vector3d v = new Vector3d(v1,v2,v3);
//        Vector3d gedrehterVector = rotationsMatrixX.multiply(v);

        setSpeed(rotatedSonnenVektor.normalize().scale(betragV));
    }

    public void setSpeed(double sx, double sy, double sz) {
        speed = new Vector3d(sx, sy, sz);
    }
    public void setSpeed(Vector3d v) {
        speed = new Vector3d(v.x,v.y,v.z);
    }
}
