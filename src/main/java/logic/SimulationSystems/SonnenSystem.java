package logic.SimulationSystems;

import logic.PhysicThread;
import logic.objects.EquatorialCoordinateSystem;
import logic.objects.PhysikObjekt;
import logic.objects.vectors.RenderedVector;
import logic.objects.vectors.VectorHandler;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;

import static tools.FeedbackBuilder.*;

public class SonnenSystem implements SimulationSystem {

    public static final double schiefeDerEkliptik = (berechneWinkel(23, 26,17.3) / 360.0) * 2 * Math.PI;

    public PhysikObjekt sonne;
    public PhysikObjekt erde;
    private PhysikObjekt mars;
    private PhysikObjekt saturn;
    private PhysikObjekt jupiter;
    private PhysikObjekt venus;
    private PhysikObjekt merkur;
    private PhysikObjekt uranus;
    private PhysikObjekt neptun;

    private final float scaleFactor = 2f;
    private final double massSizeFactor = scaleFactor * Math.pow(10, -4);

    boolean standardBegin = true;

    private final String[] debuggedPlanets = new String[] {"sonne"};
    private final int[] debuggedValues = new int[] {SPEED, POSITION};

    // enthält jeweils den Planetennamen als Key und dann als Wert eine Array der Form [d1,d2,dt]
    private final HashMap<String, double[]> distanceMap = new HashMap();

    @Override
    public void initContent() {
        //Datum: 20.3.2000 7:34:16
        //JD: 2451623,77380ss

        //Sonne
        sonne = new PhysikObjekt(0, 0, 0);
        sonne.setSpeed(0,0,0);
        sonne.mass = 1.9891 * Math.pow(10, 30);
        sonne.fixedColor = new Vector3f(5250, 5250, 5250);
        sonne.name = "sonne";
        sonne.scale = 10 * scaleFactor;

        //Erde
        double d0 = aeToM(0.99595);
        double d1 = aeToM(0.99596);
        double dt = PhysicThread.secondsPerHour;
        double betragV = 29915;
        erde = new PhysikObjekt(d0, 0, 0);
        erde.mass = 5.9742 * Math.pow(10, 24);
        erde.fixedColor = new Vector3f(0, 191, 255);
        erde.scale = 2f * scaleFactor;
        erde.berechneGeschwindigkeitErde(d0, d1, dt, betragV, sonne);
        erde.name = "erde";

        addMars();
        addVenus();
        addSaturn();
        addJupiter();
        addMerkur();
        addUranus();
        addNeptun();

        //3d Koordinatensystem
        VectorHandler.addVector(new RenderedVector(new Vector3f(0,0,0), new Vector3f(1,0,0), 1000).withColor(1,0,0));
        VectorHandler.addVector(new RenderedVector(new Vector3f(0,0,0), new Vector3f(0,1,0), 1000).withColor(0,1,0));
        VectorHandler.addVector(new RenderedVector(new Vector3f(0,0,0), new Vector3f(0,0,1), 1000).withColor(0,0,1));
    }

    private void addMerkur() {
        double d0 = aeToM(0.45153d);
        double d1 = aeToM(0.45165d);
        double de = aeToM(0.77070);
        double dt = PhysicThread.secondsPerHour;
        distanceMap.put("merkur",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{22, 24, 5.64}; // zum Datum
        double declination = berechneWinkel(-9,57,57.1); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        merkur = createPlanet(coordinate, 3.285 * Math.pow(10, 23), -40466,
                new Vector3f(248, 248, 248),"merkur", 15329,7.0);
    }

    private void addVenus() {
        double d0 = aeToM(0.72825d);
        double d1 = aeToM(0.72826d);
        double de = aeToM(1.55518);
        double dt = PhysicThread.secondsPerHour * 8;
        distanceMap.put("venus",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{22, 41, 59.37}; // zum Datum
        double declination = berechneWinkel(-9,34,44.4); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        venus = createPlanet(coordinate, 4.867 * Math.pow(10, 24), -34785,
                new Vector3f(248, 243, 43), "venus",38025,3.4);
    }

    private void addMars() {
        double d0 = aeToM(1.46465d);
        double d1 = aeToM(1.46470d);
        double de = aeToM(2.26749);
        double dt = PhysicThread.secondsPerHour;
        distanceMap.put("mars",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{1, 43, 55.31}; // zum Datum
        double declination = berechneWinkel(10, 41, 6.9); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        mars = createPlanet(coordinate, 6.417 * Math.pow(10, 23), -25082,
                new Vector3f(204, 102, 0), "mars", 21344,1.9);
    }

    private void addSaturn() {
        double d0 = aeToM(9.16659d);
        double d1 = aeToM(9.16658d);
        double de = aeToM(9.85299);
        double dt = PhysicThread.secondsPerHour * 2;
        distanceMap.put("saturn",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{2, 49, 35.59}; // zum Datum
        double declination = berechneWinkel(14,06,01.5);    // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        saturn = createPlanet(coordinate, 5.683 * Math.pow(10, 26), -10049,
                new Vector3f(204,153,51), "saturn", 378675,2.5);
    }

    private void addJupiter() {
        double d0 = aeToM(4.97700);
        double d1 = aeToM(4.97701);
        double de = aeToM(5.74049);

        double dt = PhysicThread.secondsPerHour;
        distanceMap.put("jupiter",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{2, 18, 19.60}; // zum Datum
        double declination = berechneWinkel(12,49,7.8); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        jupiter = createPlanet(coordinate, 1.898 * Math.pow(10, 27), -13645,
                new Vector3f(250,225,167), "jupiter", 439264,1.3);
    }

    private void addUranus() {
        double d0 = aeToM(19.93296);
        double d1 = aeToM(19.93297);
        double de = aeToM(20.67600);

        double dt = PhysicThread.secondsPerHour * 2;
        distanceMap.put("uranus",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{21, 27, 6.95}; // zum Datum
        double declination = berechneWinkel(-15,42,54.4); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        uranus = createPlanet(coordinate, 8.681 * Math.pow(10, 25), -6547,
                new Vector3f(85, 217, 238),"uranus", 160590,0.8);
    }

    private void addNeptun() {
        double d0 = aeToM(30.12019);
        double d1 = aeToM(30.12018);
        double de = aeToM(30.69436);
        double dt = PhysicThread.secondsPerHour * 22;
        distanceMap.put("neptun",new double[]{d0,d1,dt});

        double[] rektaszension = new double[]{20, 32, 58.01}; // zum Datum
        double declination = berechneWinkel(-18,34,23.3); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(de);
        neptun = createPlanet(coordinate, 1.024 * Math.pow(10, 26), -5425,
                new Vector3f(49, 65, 248),"neptun", 155600,1.8);
    }

    private PhysikObjekt createPlanet(EquatorialCoordinateSystem.EquatorialCoordinate coordinate, double mass,
                                      double speed, Vector3f color, String name, double radius, double bahnebenenSchiefe) {
        PhysikObjekt result;
        result = PhysikObjekt.initialisiereObjektMitAequatorkoordinate(coordinate, erde, sonne);
        double[] dArray = distanceMap.get(name);
        result.name = name;
        result.berechneGeschwindigkeit(dArray[0], dArray[1], dArray[2],speed,(bahnebenenSchiefe / 360.0) * 2 * Math.PI,sonne);
        result.mass = mass;
        result.fixedColor = color;
        result.scale = (float)(radius * massSizeFactor);
        return result;
    }

    /**
     * rechnet den gegeben Winkel in eine Kommazahl um
     * Bsp. 23Grad 30 winkelminuten 0 winkelsekunden -> 23,5 Grad
     */
    private static double berechneWinkel(int winkel, int winkelMinuten, double winkelSekunden) {
        int degreeVorzeichen = 1;
        if(winkel != 0)
            degreeVorzeichen = winkel / Math.abs(winkel);

        double sum = winkelMinuten * 60 * degreeVorzeichen + winkelSekunden * degreeVorzeichen;
        double totalSeconds = 60 * 60;
        return winkel + sum / totalSeconds;
    }

    private final double meterPerAe = 1.495979 * Math.pow(10, 11);

    private double aeToM(double ae) {
        return ae * meterPerAe;
    }
    private double mToAE(double m) {
        return m / meterPerAe;
    }

    @Override
    public Vector3f getTargetCameraPosition() {
        if(standardBegin)
            return new Vector3f(0, 0,1000);
        return new Vector3f(400,0,0);
    }

    @Override
    public Vector3f getTargetCameraRotation() {
        if(standardBegin)
            return new Vector3f(0,0,0);
        return new Vector3f(0,280,0);
    }

}
