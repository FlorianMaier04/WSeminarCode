package tools.objLoader;

import org.joml.Vector2f;
import org.joml.Vector3f;
import tools.dataStructures.MeshData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ObjFileLoader {

    public static MeshData loadOBJ(String fileName) {

        FileReader isr = null;
        File objFile = new File("assets/obj/"+fileName+".obj");

        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("File not found in res; don't use any extention: " + objFile.getAbsolutePath());
        }
        ObjReader reader = new ObjReader(isr);
        String line;
        List<Vertex> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        while (true) {
            line = reader.getNextLine();
            if (line.startsWith("v ")) {
                String[] currentLine = line.split(" ");
                Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
                        (float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3]));
                Vertex newVertex = new Vertex(vertices.size(), new Vector3f(vertex.x, vertex.y, vertex.z));
                vertices.add(newVertex);

            } else if (line.startsWith("vt ")) {
                String[] currentLine = line.split(" ");
                Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
                        (float) Float.valueOf(currentLine[2]));
                textures.add(texture);
            } else if (line.startsWith("f ")) {
                break;
            }
        }

        while (line != null && line.startsWith("f ")) {
            String[] currentLine = line.split(" ");
            String[] vertex1 = currentLine[1].split("/");
            String[] vertex2 = currentLine[2].split("/");
            String[] vertex3 = currentLine[3].split("/");

            processVertex(vertex1, vertices, indices);
            processVertex(vertex2, vertices, indices);
            processVertex(vertex3, vertices, indices);
            line = reader.getNextLine();
        }

        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        convertDataToArrays(vertices, textures, verticesArray, texturesArray);
        int[] indicesArray = convertIndicesListToArray(indices);

        MeshData data = new MeshData(verticesArray, texturesArray, indicesArray);
        return data;
    }

    private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        currentVertex.setTextureIndex(textureIndex);
        indices.add(index);
    }

    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private static void convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures, float[] verticesArray,
                                            float[] texturesArray) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getTextureIndex() == -1) {
                continue;
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
        }
    }

}
