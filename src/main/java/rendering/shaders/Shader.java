package rendering.shaders;

import org.lwjgl.opengl.GL32;
import rendering.shaders.uniforms.Uniform;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public abstract class Shader {


    protected static final String shaderDirectory = "assets/shaders/";


    //setting up the default shader
    private final int vertexId, fragmentId, programId, geometryId;

    public Shader(String vertexFile, String fragmentFile) {
        //load and compile both shaders
        vertexId = loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentId = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            int len = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
            System.err.println(glGetProgramInfoLog(programId, 500));
            System.err.print(" || error attaching shaders !!!");
            System.exit(-1);
        }
        geometryId = -1;
    }


    public Shader(String vertexFile, String fragmentFile, String geometryFile) {
        //load and compile both shaders
        vertexId = loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentId = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
        geometryId = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);

        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        glAttachShader(programId, geometryId);
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            int len = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
            System.err.println(glGetProgramInfoLog(programId, 500));
            System.err.print(" || error attaching shaders !!!");
            System.exit(-1);
        }
    }


    protected void storeAllUniformLocations(Uniform... uniforms) {
        for (Uniform u : uniforms) {
            u.storeUniformLocation(programId);
        }
        glValidateProgram(programId);
    }

    public void start() {
        glUseProgram(programId);
    }

    public void stop() {
        glUseProgram(0);
    }

    protected int loadShader(String fileName, int type) {
        StringBuilder shaderSourceCode = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName + ".glsl"));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSourceCode.append(line).append("//\n");
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderId = glCreateShader(type);

        //Pass the source Code to the Shader Object
        glShaderSource(shaderId, shaderSourceCode);
        //finallyCompile
        glCompileShader(shaderId);
        //errors have to be checked manually
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderId, 500));
            System.err.println("Could not compile shader " + fileName);
            System.exit(-1);
        }

        return shaderId;
    }


}
