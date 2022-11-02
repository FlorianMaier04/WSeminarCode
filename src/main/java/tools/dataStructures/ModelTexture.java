package tools.dataStructures;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.stream.DoubleStream;

import static org.lwjgl.opengl.GL11C.*;

public class ModelTexture {

    public final static String texturePath = "assets/res/";

    private int id;
    private int width, height;

    /**
     * @param fileName (only fileName)
     */
    public ModelTexture(String fileName) {
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(texturePath + fileName + ".png"));
            width = bi.getWidth();
            height = bi.getHeight();

            int[] pixels_raw = new int[width * height * 4];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            //new

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = pixels_raw[x * width + y];
//                    System.out.println("pixel: " + pixel + " byte: " + (byte) pixel);

                    pixels.put((byte) ((pixel >> 16) & 0xFF)); //Red
                    pixels.put((byte) ((pixel >> 8) & 0xFF)); //Green
                    pixels.put((byte) (pixel & 0xFF)); //Blue (shift by 0 so no shift)
                    pixels.put((byte) ((pixel >> 24) & 0xFF)); //Alpha
                }
            }

            pixels.flip();

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_BYTE, pixels);

        } catch (IOException e) {
            System.err.println("error loading texture: " + texturePath + fileName + ".png");
            e.printStackTrace();
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }


}
