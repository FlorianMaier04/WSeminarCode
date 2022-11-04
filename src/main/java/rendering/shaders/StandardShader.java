package rendering.shaders;


import rendering.shaders.uniforms.*;

public class StandardShader extends Shader {

    private static final String vertexFile = shaderDirectory + "defaultVertex";
    private static final String fragmentFile = shaderDirectory + "defaultFragment";

    //uniforms
    public UniformMatrix uViewMatrix = new UniformMatrix("viewMatrix");
    public UniformMatrix uProjectionMatrix = new UniformMatrix("projectionMatrix");
    public UniformMatrix uTransformationMatrix = new UniformMatrix("transformationMatrix");
    public UniformVec3 uColor = new UniformVec3("starColor");

    public StandardShader() {
        super(vertexFile, fragmentFile);
        storeAllUniformLocations(uViewMatrix, uProjectionMatrix, uTransformationMatrix, uColor);
    }
}
