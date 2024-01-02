import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileHandler {
    /**
     * This function reads the contents of a specified file and returns them as a
     * single string. The function is designed
     * for reading file contents into a string for further processing.
     */
    public static String readFile(String inputFileName) throws IOException {
        String output = "";
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                output = output + line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void writeFile(String path, String data) throws IOException {
        try (FileWriter destFile = new FileWriter(path)) {
            destFile.write(data);
        }

    }
}
