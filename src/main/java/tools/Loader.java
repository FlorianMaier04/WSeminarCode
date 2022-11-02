package tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import tools.dataStructures.ModelTexture;
import tools.dataStructures.Vao;

public class Loader {


    public static Vao loadToVao(float[] positions, int[] indices) {
        Vao vao = Vao.create();
        vao.bind();
        vao.createIndexBuffer(indices);
        vao.createAttribute(0, positions, 3);
        vao.unbind();
        return vao;
    }

    public static Vao loadToVao(float[] positions, float[] texCords, int[] indices) {
        Vao vao = Vao.create();
        vao.bind();
        vao.createIndexBuffer(indices);
        vao.createAttribute(0, positions, 3);
        vao.createAttribute(1, texCords, 2);
        vao.unbind();
        return vao;
    }

}
