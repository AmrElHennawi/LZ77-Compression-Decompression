import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class LZ77FileHandler {
    /**
     * This function prints a Tag object to both the console and an output stream.
     * It constructs a formatted string representation of the tag, replacing '\0'
     * with "NULL" in the output.
     * Its purpose is to provide a way to display and store Tag information.
     */
    public static void writeFile(List<Tag> tags, String outputFileName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        String output = null;
        for (Tag tag : tags) {
            if (tag.nextSymbol == '\0')
                output = "<" + tag.position + "," + tag.length + ",NULL>\n";
            else if (tag.nextSymbol == '\n')
                output = "<" + tag.position + "," + tag.length + ",endl>\n";
            else if (tag.nextSymbol == ',')
                output = "<" + tag.position + "," + tag.length + ",comma>\n";
            else
                output = "<" + tag.position + "," + tag.length + "," + tag.nextSymbol + ">\n";
            System.out.print(output);

            outputStream.write(output.getBytes());
        }
        outputStream.close();
    }

    /**
     * This function converts data from a file into a list of LZ77 tags. It reads
     * the file and parses each line, splitting it based on comma and angle
     * brackets. It extracts the position, length, and next symbol to create a Tag
     * object, which is added
     * to the list. Invalid lines are reported to the error output.
     */
    public static List<Tag> readFile(String iputFileName) {
        List<Tag> tags = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(iputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by comma and angle brackets
                String[] parts = line.split("[<,>]");
                if (parts.length == 4) {
                    int position = Integer.parseInt(parts[1]);
                    int length = Integer.parseInt(parts[2]);
                    char nextSymbol;
                    if (parts[3].equals("NULL")) {
                        nextSymbol = 0;
                    } else if (parts[3].equals("endl")) {
                        nextSymbol = 10;
                    } else if (parts[3].equals("comma")) {
                        nextSymbol = ',';
                    } else {
                        nextSymbol = parts[3].charAt(0);
                    }

                    Tag tag = new Tag(position, length, nextSymbol);
                    tags.add(tag);
                } else {
                    System.err.println("Invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tags;
    }
}
