import java.io.*;
import java.util.ArrayList;
import java.util.List;

//The code defines a Tag class that stores information about matched substrings and literals in LZ77 compressed data. 
//The Tag class is a data structure to store and manipulate LZ77 compression information.
class Tag {
    public int position, length;
    public char nextSymbol;

    public Tag(int position, int length, char nextSymbol) {
        this.position = position;
        this.length = length;
        this.nextSymbol = nextSymbol;
    }
}

public class LZ77Compression {

    /**
     * in the main function for an LZ77 Compression and Decompression program.
     * It displays a menu to the user, providing options for compression,
     * decompression, or exiting the program. Depending on the user's choice, the
     * corresponding option is executed. The program runs in a loop until the user
     * selects the exit option.
     */
    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            String option = System.console().readLine();
            if (option.equalsIgnoreCase("1"))
                compressionOption();
            else if (option.equalsIgnoreCase("2"))
                decompressionOption();
            else if (option.equalsIgnoreCase("3")) {
                System.out.println("Thanks for using our LZ77 Algorithms!");
                System.exit(0);
            } else
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
        }
    }

    /**
     * This function compresses the content of an input file using the LZ77
     * compression algorithm and writes the compressed data to an output file. It
     * uses a sliding window of a specified size to find the best matches within
     * the window and encodes them as tags. The primary purpose is to perform LZ77
     * compression and save the compressed data in an output file, with error
     * handling for potential file I/O issues.
     */
    public static void compressLZ77ToFile(String inputFileName, int windowSize, String outputFileName)
            throws IOException {
        String text = readFromInputFile(inputFileName);
        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        int windowStart = 0;

        for (int i = 0; i < text.length(); i++) {
            int bestMatchPosition = -1;
            int bestMatchLength = -1;
            int windowEnd = Math.min(i + windowSize, text.length());

            for (int j = i - 1; j >= windowStart && j >= 0; j--) {
                int k = 0;
                while (i + k < windowEnd && j + k < i && text.charAt(j + k) == text.charAt(i + k))
                    k++;
                if (k > bestMatchLength) {
                    bestMatchLength = k;
                    bestMatchPosition = i - j;
                }
            }
            if (bestMatchLength > 0) {
                char nextChar = (i + bestMatchLength < text.length()) ? text.charAt(i + bestMatchLength) : '\0';
                Tag tag = new Tag(bestMatchPosition, bestMatchLength, nextChar);
                printTag(tag, outputStream);
                i += bestMatchLength;
                windowStart = i - windowSize + 1;
            } else {
                Tag tag = new Tag(0, 0, text.charAt(i));
                printTag(tag, outputStream);
            }
        }
        outputStream.close();
    }

    /**
     * This function decompresses a list of LZ77 tags and writes the decompressed
     * data to an output file. It reads the tags sequentially and reconstructs the
     * original data. The decompressed data is both printed to the
     * console and written to an output file. The function includes error handling
     * for possible file I/O issues.
     */
    public static void decompressLZ77ToFile(List<Tag> tags, String outputFileName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        String output = "";

        for (Tag tag : tags) {
            if (tag.position == 0 && tag.length == 0)
                output += tag.nextSymbol;
            else {
                int position = output.length() - tag.position;
                output += output.substring(position, Math.min(position + tag.length, output.length()))
                        + ((tag.nextSymbol != 0) ? tag.nextSymbol : "");
            }
        }

        System.out.println(output.toString());
        outputStream.write(output.getBytes());
        outputStream.close();
    }

    /**
     * This function prints a Tag object to both the console and an output stream.
     * It constructs a formatted string representation of the tag, replacing '\0'
     * with "NULL" in the output.
     * Its purpose is to provide a way to display and store Tag information.
     */
    public static void printTag(Tag tag, FileOutputStream outputStream) throws IOException {
        String output = null;
        if (tag.nextSymbol == '\0')
            output = "<" + tag.position + ", " + tag.length + ", NULL>\n";
        else
            output = "<" + tag.position + ", " + tag.length + ", " + tag.nextSymbol + ">\n";
        System.out.print(output);

        outputStream.write(output.getBytes());
    }

    /**
     * This function reads the contents of a specified file and returns them as a
     * single string. The function is designed
     * for reading file contents into a string for further processing.
     */
    private static String readFromInputFile(String inputFileName) throws IOException {
        String output = "";
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * This function converts data from a file into a list of LZ77 tags. It reads
     * the file and parses each line, splitting it based on comma and angle
     * brackets. It extracts the position, length, and next symbol to create a Tag
     * object, which is added
     * to the list. Invalid lines are reported to the error output.
     */
    public static List<Tag> convertFileToList(String iputFileName) {
        List<Tag> tags = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(iputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by comma and angle brackets
                String[] parts = line.split("[<,>]");
                if (parts.length == 4) {
                    int position = Integer.parseInt(parts[1].trim());
                    int length = Integer.parseInt(parts[2].trim());
                    char nextSymbol = (parts[3].trim().equals("NULL")) ? 0 : parts[3].trim().charAt(0);

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

    /**
     * This function prints the menu for the LZ77 Compression and Decompression
     * program.
     * It allows the user to choose between program options or to exit the program.
     */
    public static void printMainMenu() {
        System.out.println("LZ77 Compression and Decompression Menu :)");
        System.out.println("1. Compression");
        System.out.println("2. Decompression");
        System.out.println("3. exit.");
        System.out.print("Choose an option (1, 2, or 3): ");
    }

    /**
     * This function handles the compression option in an LZ77 Compression and
     * Decompression program.
     * It prompts the user to enter the input and output file names for performing
     * LZ77 compression on the input file,
     * handles and reports any IOException that may occur during the process.
     */
    public static void compressionOption() {
        System.out.print("Enter the input file name: ");
        String inputFileName = System.console().readLine();

        System.out.print("Enter the output file name: ");
        String outputFileName = System.console().readLine();
        try {
            compressLZ77ToFile(inputFileName, 12, outputFileName);
            System.out.println("Compression complete. Compressed data saved to " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
        }
    }

    /**
     * This function handles the decompression option in an LZ77 Compression and
     * Decompression program.
     * It prompts the user to enter the input and output file names for performing
     * LZ77 decompression on the input file,
     * handles and reports any IOException that may occur during the process.
     */
    public static void decompressionOption() {
        System.out.print("Enter the input file name: ");
        String inputFileName = System.console().readLine();

        List<Tag> tags = convertFileToList(inputFileName);

        System.out.print("Enter the output file name: ");
        String outputFileName = System.console().readLine();
        try {
            decompressLZ77ToFile(tags, outputFileName);
            System.out.println("Decompression complete. decompressed data saved to " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
        }
    }
}
