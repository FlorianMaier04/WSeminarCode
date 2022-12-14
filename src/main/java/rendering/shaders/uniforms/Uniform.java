package rendering.shaders.uniforms;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public abstract class Uniform {

    private static final int NOT_FOUND = -1;

    private String name;
    private int location;

    protected Uniform(String name) {
        this.name = name;
    }

    public void storeUniformLocation(int programID) {
        location = glGetUniformLocation(programID, name);
        if (location == NOT_FOUND) {
            System.err.println("No uniform variable called " + name + " found!");
        }
    }

    public int getLocation() {
        return location;
    }

}
