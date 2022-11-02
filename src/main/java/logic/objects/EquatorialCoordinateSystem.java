package logic.objects;

import tools.dataStructures.Vector3d;

/**
 * wird initialisiert, um dann angewendet werden zu können
 */
public class EquatorialCoordinateSystem {



    public Vector3d earthSunVector;
    /**
     * @param earth die Erde mit den Koordinaten des Frühlingspunktes
     */
    public EquatorialCoordinateSystem(PhysicObject earth, PhysicObject sun) {
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
            double sum = rektaszension[0] * secondsPerHour + rektaszension[1] * secondsPerMinute + rektaszension[2];
            int total = 24 * secondsPerHour;
            computedRektaszension = sum / (double) total;
            computedDeclination = (declination) / 180;
        }

        public void withDistance(double distance) {
            this.distance = distance;
        }

        public Vector3d computeCoordinatePosition(EquatorialCoordinateSystem system) {
            Vector3d rotatedVector = new Vector3d(Math.sin(computedRektaszension * 2 * Math.PI),Math.sin(computedDeclination * 2 * Math.PI),0);
            Vector3d normalizedVector;
            if(rotatedVector.length() == 0)
                normalizedVector = rotatedVector;
            else

                normalizedVector = rotatedVector.normalize();
            Vector3d systemVector = system.earthSunVector.add(normalizedVector);
            System.out.println("normalized Vector: " + normalizedVector + " system: "+systemVector + " declination: "+computedDeclination);
            System.out.println("scale: "+distance);
            return systemVector.normalize().scale(distance);
        }
    }

}
