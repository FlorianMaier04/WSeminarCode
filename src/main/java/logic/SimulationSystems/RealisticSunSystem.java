package logic.SimulationSystems;

import logic.PhysicThread;
import logic.PhysicsEngine;
import logic.objects.EquatorialCoordinateSystem;
import logic.objects.PhysikObjekt;
import logic.objects.vectors.RenderedVector;
import logic.objects.vectors.VectorHandler;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import rendering.Scene;
import tools.Keyboard;

import static tools.FeedbackBuilder.*;

public class RealisticSunSystem implements SimulationSystem {

    int printCounter = 0;
    private final int updatesPerPrint = 800000;
    private int currentUpdatesPerPrint = updatesPerPrint;
    private final double secondsPerFrame = 8000 / 50;

    private PhysikObjekt sonne;
    private PhysikObjekt erde;
    private PhysikObjekt mars;
    private PhysikObjekt saturn;
    private PhysikObjekt jupiter;
    private PhysikObjekt venus;
    private PhysikObjekt merkur;
    private PhysikObjekt uranus;
    private PhysikObjekt neptun;

    public boolean displayVectors = false;

    private PhysicThread pt;

    private final float scaleFactor = 8f;

    private final String[] debuggedPlanets = new String[] {"sonne"};
    private final int[] debuggedValues = new int[] {ACCELLERATION,
            SPEED, POSITION};

    @Override
    public void initContent() {
        //Datum: 20.3.2000 7:34:16
        //JD: 2451623,77380
        //Erde
        double d0 = aeToM(0.99595);
        double d1 = aeToM(0.99596);
        double dt = PhysicsEngine.secondsPerHour;
        double betragV = 29915;
        erde = new PhysikObjekt(d0, 0, 0);
        erde.mass = 5.9742 * Math.pow(10, 24);
        erde.fixedColor = new Vector3f(0, 191, 255);
        erde.scale = 2f * scaleFactor;
        erde.berechneGeschwindigkeit(d0, d1, dt, betragV);
        erde.name = "erde";

        //Sonne
        sonne = new PhysikObjekt(0, 0, 0);
        sonne.initPhysics(0, 0, 0, 1.9891 * Math.pow(10, 30));
        sonne.fixedColor = new Vector3f(5250, 5250, 5250);
        sonne.name = "sonne";
        sonne.scale = 4 * scaleFactor;

        if(PhysicThread.windowStart || true) {
            addMars();
            addVenus();
            addSaturn();
            addJupiter();
            addMerkur();
            addUranus();
            addNeptun();
        }

        //3d Koordinatensystem
        VectorHandler.addVector(new RenderedVector(new Vector3f(0,0,0), new Vector3f(1,0,0), 1000).withColor(1,0,0));
        VectorHandler.addVector(new RenderedVector(new Vector3f(0,0,0), new Vector3f(0,1,0), 1000).withColor(0,1,0));
        VectorHandler.addVector(new RenderedVector(new Vector3f(0,0,0), new Vector3f(0,0,1), 1000).withColor(0,0,1));
    }

    private void addMerkur() {
        double[] rektaszension = new double[]{22, 24, 5.64}; // zum Datum
        double declination = computeDegree(-9,57,57.1); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(0.45153));
        merkur = createPlanet(coordinate, 3.285 * Math.pow(10, 23), -40466,
                new Vector3f(248, 248, 248),"merkur", 15329);
    }

    private void addVenus() {
        double[] rektaszension = new double[]{22, 41, 59.37}; // zum Datum
        double declination = computeDegree(-9,34,44.4); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(0.72825));
        venus = createPlanet(coordinate, 4.867 * Math.pow(10, 24), -34785,
                new Vector3f(248, 243, 43), "venus",38025);
    }

    private void addMars() {
        double[] rektaszension = new double[]{1, 43, 55.31}; // zum Datum
        double declination = computeDegree(10, 41, 6.9); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(1.46465d));
        mars = createPlanet(coordinate, 6.417 * Math.pow(10, 23), -25082,
                new Vector3f(204, 102, 0), "mars", 21344);
    }

    private void addSaturn() {
        double[] rektaszension = new double[]{2, 49, 35.59}; // zum Datum
        double declination = computeDegree(14,06,01.5);    // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(9.85299d));
        saturn = createPlanet(coordinate, 5.683 * Math.pow(10, 26), -10049,
                new Vector3f(204,153,51), "saturn", 378675);
    }

    private void addJupiter() {
        double[] rektaszension = new double[]{2, 18, 19.60}; // zum Datum
        double declination = computeDegree(12,49,7.8); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(4.97700));
        jupiter = createPlanet(coordinate, 1.898 * Math.pow(10, 27), -13645,
                new Vector3f(250,225,167), "jupiter", 439264);
    }

    private void addUranus() {
        double[] rektaszension = new double[]{21, 27, 6.95}; // zum Datum
        double declination = computeDegree(-15,42,54.4); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(19.93296));
        uranus = createPlanet(coordinate, 8.681 * Math.pow(10, 25), -6547,
                new Vector3f(85, 217, 238),"uranus", 160590);
    }

    private void addNeptun() {
        double[] rektaszension = new double[]{20, 32, 58.01}; // zum Datum
        double declination = computeDegree(-18,34,23.3); // zum Datum
        EquatorialCoordinateSystem.EquatorialCoordinate coordinate;
        coordinate = new EquatorialCoordinateSystem.EquatorialCoordinate(rektaszension, declination);
        coordinate.withDistance(aeToM(30.12019));
        neptun = createPlanet(coordinate, 1.024 * Math.pow(10, 26), -5425,
                new Vector3f(49, 65, 248),"neptun", 155600);
    }

    private final double massSizeFactor = 5 * Math.pow(10, -4);

    private PhysikObjekt createPlanet(EquatorialCoordinateSystem.EquatorialCoordinate coordinate, double mass, double speed, Vector3f color, String name, double radius) {
        PhysikObjekt result;
        result = PhysikObjekt.initialisiereObjektMitAequatorkoordinate(coordinate, erde, sonne);
        result.initPhysics(speed, mass, sonne);
        result.fixedColor = color;
        result.name = name;
        result.scale = (float)(radius * massSizeFactor);
        return result;
    }

    private double computeDegree(int degree, int minutes, double seconds) {
        int degreeVorzeichen = degree / Math.abs(degree);

        double sum = minutes * 60 * degreeVorzeichen + seconds * degreeVorzeichen;
        double totalSeconds = 60 * 60;
        return Math.abs(degree + sum / totalSeconds);
    }

    private final double meterPerAe = 1.495979 * Math.pow(10, 11);

    private double aeToM(double ae) {
        return ae * meterPerAe;
    }
    private double mToAE(double m) {
        return m / meterPerAe;
    }

    private int yearCounter = 0;

    private double lastYearTime = 0;

    private double lastEarthY = 0;

    private double lastDiff = 0;

    @Override
    public void update() {
//        if(Keyboard.isKeyPressed(GLFW.GLFW_KEY_H))
//            earth.speed = new Vector3d(earth.speed.x,earth.speed.y + 100,earth.speed.z);


        if (lastEarthY < 0 && erde.pos.y > 0) {
            double diff = ((1.48950 * Math.pow(10, 11)) - erde.pos.x);
            double timePassed = PhysicsEngine.timePassed;
            double yearTime = timePassed - lastYearTime;
            lastYearTime = PhysicsEngine.timePassed;
            System.out.println("year: " + PhysicsEngine.timePassedYears());
//            System.out.println("year: " + yearCounter + "yearTime: " + (yearTime / (60 * 60 * 24 * 365.25)));
            lastDiff = diff;
            yearCounter++;
        }

        lastEarthY = erde.pos.y;

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
    public PhysikObjekt getEarth() {
        return erde;
    }

    @Override
    public boolean displayVectors() {
        return displayVectors;
    }

    @Override
    public double getSecondsPerFrame() {
        return secondsPerFrame;
    }

    @Override
    public String[] getDebuggedPlanets() {
        return debuggedPlanets;
    }

    @Override
    public int[] getDebuggedValues() {
        return debuggedValues;
    }

    @Override
    public Vector3f getTargetCameraPosition() {
        return new Vector3f(400,0,0);
//        return new Vector3f(0, 0,1000);
    }

    @Override
    public Vector3f getTargetCameraRotation() {
        return new Vector3f(0,280,0);
//        return new Vector3f(0,0,0);
    }

}
