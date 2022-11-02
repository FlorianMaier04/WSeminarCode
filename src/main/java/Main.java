import logic.PhysicThread;
import rendering.Window;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        PhysicThread pt = new PhysicThread();
        pt.start();
        while(!pt.runPhysicsSimulation && PhysicThread.windowStart) {
            Thread.sleep(100);
        }
        Window window = Window.get();
        window.run();
    }


}
