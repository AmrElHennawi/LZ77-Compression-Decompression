import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
                System.out.println("Invalid option. Please choose 1, 2, or 3 .");
        }
    }

    public static void printTag(Tag tag, FileOutputStream outputStream) throws IOException {
        String output = "<" + tag.position + ", " + tag.length + ", " + tag.nextSymbol + ">\n";
        System.out.print(output);
        outputStream.write(output.getBytes());
    }

    public static void compressLZ77ToFile(String text, int windowSize, String outputFileName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        int windowStart = 0;

        for (int i = 0; i < text.length(); i++) {
            int bestMatchPosition = -1;
            int bestMatchLength = -1;
            int windowEnd = Math.min(i + windowSize, text.length());

            for (int j = i - 1; j >= windowStart && j >= 0; j--) {
                int k = 0;
                while (i + k < windowEnd && text.charAt(j + k) == text.charAt(i + k))
                    k++;
                if (k > bestMatchLength) {
                    bestMatchLength = k;
                    bestMatchPosition = i - j;
                }
            }
            if (bestMatchLength > 0) {
                char nextChar = (i + bestMatchLength < text.length()) ? text.charAt(i + bestMatchLength) : '0';
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

    public static void decompressLZ77ToFile(List<Tag> tags, String outputFileName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        String output = "";

        for (Tag tag : tags) {
            if (tag.position == 0 && tag.length == 0)
                output += tag.nextSymbol;
            else {
                int position = output.length() - tag.position;
                output += output.substring(position, position + tag.length)
                        + ((tag.nextSymbol != 0) ? tag.nextSymbol : "");
            }
        }

        System.out.println(output.toString());
        outputStream.write(output.getBytes());
    }

    public static void printMainMenu() {
        System.out.println("LZ77 Compression and Decompression Menu :)");
        System.out.println("1. Compression");
        System.out.println("2. Decompression");
        System.out.println("3. exit.");
        System.out.print("Choose an option (1, 2, or 3): ");
    }

    public static void compressionOption() {
        int windowSize;
        while (true) {
            try {
                System.out.print("Enter the window size: ");
                windowSize = Integer.parseInt(System.console().readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a number from 0 and above.");
            }
        }
        System.out.print("Enter the text to compress: ");
        String text = System.console().readLine();

        System.out.print("Enter the output file name: ");
        String outputFileName = System.console().readLine();
        try {
            compressLZ77ToFile(text, windowSize, outputFileName);
            System.out.println("Compression complete. Compressed data saved to " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
        }
    }

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
                    char nextSymbol = parts[3].trim().charAt(0);

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
