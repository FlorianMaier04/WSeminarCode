package tools;

import logic.PhysicThread;
import logic.objects.PhysikObjekt;
import tools.dataStructures.Vector3d;

import java.io.File;
import java.io.FileWriter;

/**
 * wird verwendet, Werte (z.B. Umlaufzeit) der Planeten zu erhalten und in eine lesbare Ausgabe zu konvertieren
 * Achtung!: sollte vom UI-Thread geupdatet werden
 */
public class FeedbackBuilder {

    public static final int POSITION = 2;
    public static final int SPEED = 3;
    public static final int DISTANCE_TO_SUN = 1;
    public static final int X_COORDINATE = 6;
    public static final int Y_COORDINATE = 7;
    public static final int Z_COORDINATE = 8;

    public static final int X_COORDINATE_SUN = 9;
    public static final int Y_COORDINATE_SUN = 10;
    public static final int Z_COORDINATE_SUN = 11;

    public static final int OPPOSITIONS_VEKTOREN = 12;

    public static String writtenPlanet = null;
    public static String writtenPlanetFileName = "";
    public static final String writtenPlanetFilePath = "GraphShower/res/";
    public static int writeMode = DISTANCE_TO_SUN;
    public static long secondsPerWrite = PhysicThread.secondsPerYear / (16 * 16);

    public void update() {
        build();
    }

    private final long secondsPerUpdate = secondsPerWrite;
    private long secondsToNextUpdate = 0;
    private double lastWrittenTime = 0;

    private void build() {
        for (PhysikObjekt po : PhysicThread.physikObjekte) {
            if(po.name.equals(writtenPlanet)) {
                if(lastWrittenTime == 0)
                    lastWrittenTime = PhysicThread.timePassed;
                double passedSeconds = PhysicThread.timePassed - lastWrittenTime;
                secondsToNextUpdate -= passedSeconds;
                if(secondsToNextUpdate <= 0) {
                    writeFileData(po);
                    secondsToNextUpdate = secondsPerUpdate;
                }
                lastWrittenTime = PhysicThread.timePassed;
            }
        }
    }

    private String xLine = "", yLine = "";


    private final String newLine = System.lineSeparator();

    private final String trennzeichen = ",";

    private void writeFileData(PhysikObjekt po) {
        switch(writeMode) {
            case Y_COORDINATE:
                double y = po.pos.y / PhysicThread.meterPerAE;
                y = Math.abs(y);
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += y + trennzeichen;
                break;
            case X_COORDINATE:
                double x = po.pos.x / PhysicThread.meterPerAE;
                x = Math.abs(x);
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += x + trennzeichen;
                break;
            case Z_COORDINATE:
                double z = po.pos.z / PhysicThread.meterPerAE;
                z = Math.abs(z);
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += z + trennzeichen;
                break;
            case Y_COORDINATE_SUN:
                y = po.pos.y / PhysicThread.meterPerAE;
                y = Math.abs(y);
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += y + trennzeichen;
                break;
            case X_COORDINATE_SUN:
                x = (po.pos.sub(PhysicThread.activeSystem.erde.pos).x) / PhysicThread.meterPerAE;
                x = Math.abs(x);
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += x + trennzeichen;
                break;
            case Z_COORDINATE_SUN:
                z = po.pos.z / PhysicThread.meterPerAE;
                z = Math.abs(z);
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += z + trennzeichen;
                break;
            case DISTANCE_TO_SUN:
                Vector3d sunVector = po.pos.sub(PhysicThread.activeSystem.sonne.pos);
                double length = sunVector.length();
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += length + trennzeichen;
                break;
            case OPPOSITIONS_VEKTOREN:
                sunVector = po.pos.sub(PhysicThread.activeSystem.sonne.pos).normalize();
                Vector3d earthVector = po.pos.sub(PhysicThread.activeSystem.erde.pos).normalize();
                double scalar = earthVector.skalarProdukt(sunVector);
                double phi = Math.acos(scalar / (earthVector.length() * sunVector.length()));
                xLine += PhysicThread.timePassedRealYears() + trennzeichen;
                yLine += phi + trennzeichen;
        }

    }

    public void finish() {
        System.out.println("finish");
        try {
            File file = new File(writtenPlanetFilePath + writtenPlanetFileName + ".txt");
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
}
