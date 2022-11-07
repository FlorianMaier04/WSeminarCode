package logic.SimulationSystems;

import logic.PhysicThread;
import logic.objects.PhysikObjekt;
import org.lwjgl.util.vector.Vector3f;


public interface SimulationSystem {

    /**
     * eigentlich hat das Simulation System nur mit den Physikalischen Einstellungen zu tun,
     * der Einfachkeit halber kann aber hier eine target Position gesetzt werden, die die Kamera als
     * Startposition verwendet
     */
    public Vector3f getTargetCameraPosition();

    public void initContent();

    Vector3f getTargetCameraRotation();
}
