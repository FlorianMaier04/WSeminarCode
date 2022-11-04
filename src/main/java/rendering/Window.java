package rendering;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import tools.Keyboard;
import tools.Mouse;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public static final int width = 1920, height = 1080;
    String title;

    private static Window window = null;
    //adress for memory space
    private long windowAdress;


    private Scene scene;

    private Window() {
        this.title = "test";
    }

    public static Window get() {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public static int getWindowWidth() {
        return get().height;
    }

    public static int getWindowHeight() {
        return get().width;
    }

    public void run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        loop();
    }

    private void init() {
        //GLFWErrorCallback will print errors to System.err
        GLFWErrorCallback.createPrint(System.err).set();

        //init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        //create Window
        windowAdress = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (windowAdress == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(windowAdress, Keyboard::keyCallback);
        glfwSetCursorPosCallback(windowAdress, Mouse::mousePosCallback);
        glfwSetMouseButtonCallback(windowAdress, Mouse::mouseButtonCallback);
        glfwSetScrollCallback(windowAdress, Mouse::mouseScrollCallback);

        // Make the OpenGl context current
        glfwMakeContextCurrent(windowAdress);
        //enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(windowAdress);

        //important: creates Capabilities for current context
        GL.createCapabilities();


        scene = new Scene();

        lockMouse(true);
    }

    private void loop() {
        while (!glfwWindowShouldClose(windowAdress)) {
            //Poll events
            glfwPollEvents();

//            GL11.glClearColor(1, 1, 1, 1);
            GL11.glClearColor(0, 0, 0, 0);

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            scene.update();
            glfwSwapBuffers(windowAdress);
        }

    }


    private void render() {
        //enable depthtest
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }


    public void lockMouse(boolean lock) {
        glfwSetInputMode(windowAdress, GLFW_CURSOR, lock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

}
