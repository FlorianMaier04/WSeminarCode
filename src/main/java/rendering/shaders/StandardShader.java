package rendering.shaders;


import rendering.shaders.uniforms.*;

public class StandardShader extends Shader {

    private static final String vertexFile = shaderDirectory + "defaultVertex";
    private static final String fragmentFile = shaderDirectory + "defaultFragment";
    public static final int STANDARD_TEXTURE_UNIT = 0;

    //uniforms
    public UniformMatrix uViewMatrix = new UniformMatrix("viewMatrix");
    public UniformMatrix uProjectionMatrix = new UniformMatrix("projectionMatrix");
    public UniformMatrix uTransformationMatrix = new UniformMatrix("transformationMatrix");
    public UniformFloat uBrightness = new UniformFloat("brightness");
    public UniformVec3 uColor = new UniformVec3("starColor");
    public UniformBoolean uUseTexture = new UniformBoolean("useTexture");
    public UniformSampler texture = new UniformSampler("modelTexture");

    public StandardShader() {
        super(vertexFile, fragmentFile);
        storeAllUniformLocations(uViewMatrix, uProjectionMatrix, uTransformationMatrix, uBrightness, uColor, uUseTexture);
        connectTextureUnits();
    }

    private void connectTextureUnits() {
        super.start();
        texture.loadTexUnit(STANDARD_TEXTURE_UNIT);
        super.stop();
    }
}
