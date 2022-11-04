package tools;

import logic.PhysicThread;
import logic.PhysicsEngine;
import logic.objects.PhysikObjekt;

import java.io.File;
import java.io.FileWriter;

/**
 * wird verwendet, Werte (z.B. Umlaufzeit) der Planeten zu erhalten und in eine lesbare Ausgabe zu konvertieren
 * Achtung!: sollte vom UI-Thread geupdatet werden
 */
public class FeedbackBuilder {

    public static final int POSITION = 2;
    public static final int SPEED = 3;
    public static final int DISTANCE_TO_EARTH = 5;
    public static final int X_COORDINATE = 6;
    public static final int Y_COORDINATE = 7;
    public static final int Z_COORDINATE = 8;

    private static final String[] valueDescriptions = new String[]{"rec. Umlauf", "av. Umlauf", "pos", "v", "a"};

    private final int ticksPerBuild = 1;
    private int tickCounter = ticksPerBuild;

    public void update() {
        if (tickCounter >= ticksPerBuild) {
            build();
            tickCounter = 0;
        } else
            tickCounter++;
    }

    private final long secondsPerUpdate = PhysicThread.secondsPerWrite;
    private long secondsToNextUpdate = 0;
    private double lastWrittenTime = 0;

    private void build() {
        String[] debuggedPlanets = PhysicThread.activeSystem.getDebuggedPlanets();
        boolean printOut = PhysicThread.writtenPlanet == null;
        printOut = false;
        String build = "";
        for (PhysikObjekt po : PhysicsEngine.physikObjekte) {
            if(po.name.equals(PhysicThread.writtenPlanet)) {
                if(lastWrittenTime == 0)
                    lastWrittenTime = PhysicsEngine.timePassed;
                double passedSeconds = PhysicsEngine.timePassed - lastWrittenTime;
                secondsToNextUpdate -= passedSeconds;
                if(secondsToNextUpdate <= 0) {
                    writeFileData(po);
                    secondsToNextUpdate = secondsPerUpdate;
                }
                lastWrittenTime = PhysicsEngine.timePassed;
            }
            if(printOut) {
                boolean debug = false;
                for (String n : debuggedPlanets) {
                    if (po.name.equals(n)) {
                        debug = true;
                        break;
                    }
                }
                if (debug)
                    build = addObject(po, build);
            }
        }
        if(printOut)
            System.out.println(build);

    }

    private String xLine = "", yLine = "";


    private final String newLine = System.lineSeparator();

    private void writeFileData(PhysikObjekt po) {
        switch(PhysicThread.writeMode) {
            case DISTANCE_TO_EARTH:
                double distance = po.pos.sub(PhysicThread.activeSystem.getEarth().pos).length() / PhysicsEngine.meterPerAE;
                xLine += PhysicsEngine.timePassedRealYears() + ",";
                yLine += distance + ",";
                break;
            case Y_COORDINATE:
                double y = po.pos.y / PhysicsEngine.meterPerAE;
                y = Math.abs(y);
                xLine += PhysicsEngine.timePassedRealYears() + ",";
                yLine += y + ",";
                break;
            case X_COORDINATE:
                double x = po.pos.x / PhysicsEngine.meterPerAE;
                x = Math.abs(x);
                xLine += PhysicsEngine.timePassedRealYears() + ",";
                yLine += x + ",";                break;
            case Z_COORDINATE:
                double z = po.pos.z / PhysicsEngine.meterPerAE;
                z = Math.abs(z);
                xLine += PhysicsEngine.timePassedRealYears() + ",";
                yLine += z + ",";
                break;

        }

    }

    public void finish() {
        System.out.println("finish");
        try {
            File file = new File(PhysicThread.writtenPlanetFilePath + PhysicThread.writtenPlanetFileName + ".txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("x:" + xLine + newLine);
            fileWriter.write("y:" + yLine + newLine);
            fileWriter.close();
            System.out.println("writing closed");
        } catch (Exception e) {
            System.out.println("error: " + e);
            e.printStackTrace();
        }
    }

    private final char valueSeparator = '/';
    private final char objectSeparator = '|';


    private String addObject(PhysikObjekt po, String build) {
        build += " " + objectSeparator + po.name + ": ";
        int[] debuggedValues = PhysicThread.activeSystem.getDebuggedValues();
        for (int valueId : debuggedValues) {
            switch (valueId) {
                case POSITION:
                    build = addValue(build, po.pos.toString(), 2);
                case SPEED:
                    build = addValue(build, po.speed.toString(), 3);
            }
        }
        return build;
    }

    private String addValue(String build, double value, int descriptionId) {
        build += valueDescriptions[descriptionId] + ": " + ds(value) + valueSeparator;
        return build;
    }

    private String addValue(String build, String value, int descriptionId) {
        build += valueDescriptions[descriptionId] + ": " + value + valueSeparator;
        return build;
    }

    private final int doubleDigits = 6;

    private String ds(double d) {
        String s = d + "";
        int eIndex = s.indexOf('E');
        String basePart = s.substring(0, Math.min(doubleDigits, s.length()));

        if (eIndex != -1) {
            if (eIndex < basePart.length())
                basePart = basePart.substring(0, eIndex);
            basePart += s.substring(eIndex);
        }
        return basePart;
    }


}
