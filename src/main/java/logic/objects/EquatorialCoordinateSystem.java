package logic.objects;

import logic.SimulationSystems.SonnenSystem;
import tools.Maths;
import tools.dataStructures.Matrix3d;
import tools.dataStructures.Vector3d;

/**
 * wird initialisiert, um dann angewendet werden zu können
 */
public class EquatorialCoordinateSystem {



    public Vector3d earthSunVector;
    /**
     * @param earth die Erde mit den Koordinaten des Frühlingspunktes
     */
    public EquatorialCoordinateSystem(PhysikObjekt earth, PhysikObjekt sun) {
        earthSunVector = sun.pos.sub(earth.pos).normalize();


    }

    public static class EquatorialCoordinate {

        //Werte von 0 - 1 / 0.99999999 = 23:59:59 / 0 = 00:00:00
        private double computedRektaszension;
        //Wert von -0.5 bis 0.5 /90Grad = 0.5
        private double computedDeclination;
        private double distance = 1;

        /**
         * @param rektaszension [hours,minutes,seconds]
         * @param declination range 90 degrees to -90 degrees
         */
        public EquatorialCoordinate(double[] rektaszension, double declination) {
            // Umrechnung in Sekunden
            int secondsPerMinute = 60;
            int secondsPerHour = secondsPerMinute * 60;
            int rektaszensionVorzeichen = (int) (rektaszension[0] / Math.abs(rektaszension[0])) ;
            double sum = rektaszension[0] * secondsPerHour + rektaszension[1] * secondsPerMinute * rektaszensionVorzeichen + rektaszension[2] * rektaszensionVorzeichen;
            int total = 24 * secondsPerHour;
            computedRektaszension = sum / (double) total;
            computedDeclination = (declination) / 360;
        }

        public void withDistance(double distance) {
            this.distance = distance;
        }

        public Vector3d berechnePosition(EquatorialCoordinateSystem system) {
            double declinationAngle = computedDeclination * 2.0 * Math.PI;
            double rektaszensionAngle = computedRektaszension * 2.0 * Math.PI;

            Matrix3d rotationMatrixZ = Maths.createRotationMatrixZ(rektaszensionAngle);
            Matrix3d rotationMatrixY = Maths.createRotationMatrixY(declinationAngle);
            Matrix3d rotationMatrixX = Maths.createRotationMatrixX(SonnenSystem.schiefeDerEkliptik);

            Vector3d rotatedVector = system.earthSunVector;
            rotatedVector = rotationMatrixZ.multiply(rotatedVector);
            rotatedVector = rotationMatrixY.multiply(rotatedVector);
            rotatedVector = rotationMatrixX.multiply(rotatedVector);

            return rotatedVector.mulitply(distance);
        }
    }

}
