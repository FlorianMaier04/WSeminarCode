package logic.SimulationSystems;

import logic.PhysicThread;
import logic.objects.PhysikObjekt;
import org.lwjgl.util.vector.Vector3f;


public interface SimulationSystem {

    /**
     * @return gibt ein Array an Planetennamen zurück. Diese Planeten wird der FeedbackBuilder verwenden
     */
    public String[] getDebuggedPlanets();

    /**
     * @return gibt ein Int array zurück, dass die ValueIds (inFeedbackBuilder definiert) der zu debuggenden Werte enthält.
     */
    public int[] getDebuggedValues();

    /**
     * eigentlich hat das Simulation System nur mit den Physikalischen Einstellungen zu tun,
     * der Einfachkeit halber kann aber hier eine target Position gesetzt werden, die die Kamera als
     * Startposition verwendet
     */
    public Vector3f getTargetCameraPosition();

    public void initContent();

    public void initPhysicThread(PhysicThread pt);

    public PhysikObjekt getEarth();

    Vector3f getTargetCameraRotation();
}
