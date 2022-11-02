package rendering.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import rendering.shaders.uniforms.Uniform;
import rendering.shaders.uniforms.UniformFloat;
import rendering.shaders.uniforms.UniformMatrix;
import rendering.shaders.uniforms.UniformVec3;

public class VectorShader extends Shader {


    private static final String vertexFile = shaderDirectory + "vectorVertex";
    private static final String fragmentFile = shaderDirectory + "vectorFragment";
    private static final String geometryFile = shaderDirectory + "vectorGeometry";

    //uniforms
    public UniformMatrix uViewMatrix = new UniformMatrix("viewMatrix");
    public UniformMatrix uProjectionMatrix = new UniformMatrix("projectionMatrix");
    public UniformMatrix uTransformationMatrix = new UniformMatrix("transformationMatrix");
    public UniformMatrix uTargetTransformationMatrix = new UniformMatrix("targetTransformationMatrix");
    public UniformVec3 uColor = new UniformVec3("uColor");


    public VectorShader() {
        super(vertexFile, fragmentFile, geometryFile);
        storeAllUniformLocations(uViewMatrix, uProjectionMatrix, uTransformationMatrix, uTargetTransformationMatrix, uColor);
    }


}
