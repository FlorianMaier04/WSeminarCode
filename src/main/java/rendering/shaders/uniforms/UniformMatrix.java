package rendering.shaders.uniforms;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class UniformMatrix extends Uniform {

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public UniformMatrix(String name) {
		super(name);
	}

	public void loadMatrix(Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(super.getLocation(), false, matrixBuffer);
//		GL20.glUniformMatrix4(super.getLocation(), false, matrixBuffer);
	}

}
