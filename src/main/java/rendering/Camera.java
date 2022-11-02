package rendering;


import org.lwjgl.util.vector.Vector3f;
import tools.Keyboard;
import tools.Mouse;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    public Vector3f position = new Vector3f(0, 0, 100);

    public Vector3f rotation = new Vector3f(0, 0, 0);

    private static final float positionChange = 0.1f;
    private static final float mouseSensitivity = 0.05f;

    boolean init = true;

    public void init() {
        Mouse.endFrame();
    }

    private long lastNanoTime;

    public void update() {
        //Daten der Planeten kriegen (eso) Homanbahnen
        float dx = 0, dy = 0, dz = 0;
        long timeDiff = System.nanoTime() - lastNanoTime;
        float timeRelevantChange = positionChange * timeDiff / 500000;

        if (Keyboard.isKeyPressed(GLFW_KEY_UP) || Keyboard.isKeyPressed(GLFW_KEY_W))
            dz = -timeRelevantChange;
        if (Keyboard.isKeyPressed(GLFW_KEY_DOWN) || Keyboard.isKeyPressed(GLFW_KEY_S))
            dz = timeRelevantChange;
        if (Keyboard.isKeyPressed(GLFW_KEY_RIGHT) || Keyboard.isKeyPressed(GLFW_KEY_D))
            dx = timeRelevantChange;
        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT) || Keyboard.isKeyPressed(GLFW_KEY_A))
            dx = -timeRelevantChange;
        if (Keyboard.isKeyPressed(GLFW_KEY_SPACE))
            dy = timeRelevantChange;
        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
            dy = -timeRelevantChange;

        computeRotation();

        //apply difference to rotated coordinate axis

        position.z += dz * Math.cos(-rotation.y * Math.PI / 180);
        position.x += dz * Math.sin(-rotation.y * Math.PI / 180);

        position.x += dx * Math.cos(rotation.y * Math.PI / 180);
        position.z += dx * Math.sin(rotation.y * Math.PI / 180);

        position.y += dy;

        if (Keyboard.isKeyPressed(GLFW_KEY_X)) {
            rotation = new Vector3f(0, 0, 0);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_3)) {
            rotation = new Vector3f(0, 90, 0);
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_7)) {
            rotation = new Vector3f(90, 0, 0);
        }

        lastNanoTime = System.nanoTime();
    }


    private void computeRotation() {
        if (init) {
            Mouse.endFrame();
            init = false;
        }
        if (Keyboard.isKeyPressed(GLFW_KEY_U))
            rotation.y += positionChange;


        float dx = Mouse.getDx();
        float dy = Mouse.getDy();
        Mouse.endFrame();

        rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0), rotation);
        //keeping the rotation angle between 0 and 360
        if (rotation.y < 0) {
            rotation.y = 360 - rotation.y;
        }
    }

    public void setPosition(Vector3f pos) {
        this.position = pos;
    }

}
