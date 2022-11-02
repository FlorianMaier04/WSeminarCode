package tools.objLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjReader {

    private List<String> lines;
    private int currentIndex = -1;

    public ObjReader(FileReader isr) {
        BufferedReader reader = new BufferedReader(isr);
        String line;
        try {
            line = reader.readLine();

            lines = new ArrayList<String>();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (false) {
            List<String> vertex = new ArrayList<String>();
            List<String> texture = new ArrayList<String>();
            List<String> index = new ArrayList<String>();
            for (String s : lines) {
                if (s.startsWith("v"))
                    vertex.add(s);
                if (s.startsWith("t"))
                    texture.add(s);
                if (s.startsWith("f"))
                    index.add(s);
            }
            lines.clear();
            lines.addAll(vertex);
            lines.addAll(texture);
            lines.addAll(index);
        }
    }

    public String getNextLine() {
        currentIndex++;
        if (currentIndex >= lines.size())
            return null;
        return lines.get(currentIndex);
    }

}
