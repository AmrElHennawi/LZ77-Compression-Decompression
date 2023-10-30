import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class LZ77Compression {
    public static void printTag(int position, int length, char symbol, FileOutputStream outputStream) throws IOException {
        String tag = "<" + position + "," + length + "," + symbol + ">";
        System.out.println(tag);
        outputStream.write(tag.getBytes());
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
                while (i + k < windowEnd && text.charAt(j + k) == text.charAt(i + k)) {
                    k++;
                }
                if (k > bestMatchLength) {
                    bestMatchLength = k;
                    bestMatchPosition = i - j;
                }
            }

            if (bestMatchLength > 0) {
                printTag(bestMatchPosition, bestMatchLength, text.charAt(i + bestMatchLength), outputStream);
                i += bestMatchLength;
                windowStart = i - windowSize + 1;
            } else {
                printTag(0, 0, text.charAt(i), outputStream);
            }
        }

        outputStream.close();
    }

    public static void decompressLZ77ToFile() throws IOException {
        // Implement the LZ77 decompression logic here
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("LZ77 Compression and Decompression Menu :)");
        System.out.println("1. Compression");
        System.out.println("2. Decompression");
        System.out.print("Choose an option (1 or 2): ");

        int option = in.nextInt();
        in.nextLine(); // Consume the newline character

        if (option == 1) {
            System.out.print("Enter the window size: ");
            int windowSize = in.nextInt();
            in.nextLine(); // Consume the newline character

            System.out.print("Enter the text to compress: ");
            String text = in.nextLine();

            System.out.print("Enter the output file name: ");
            String outputFileName = in.nextLine();

            try {
                compressLZ77ToFile(text, windowSize, outputFileName);
                System.out.println("Compression complete. Compressed data saved to " + outputFileName);
            } catch (IOException error) {
                System.err.println("Error while writing to the file: " + error.getMessage());
            }
//        } else if (option == 2) {
//            // Implement the user input for the required decompression function parameters here.
//            try {
//                decompressLZ77ToFile();
//                System.out.println();
//            } catch (IOException error) {
//                System.err.println("Error while reading or writing to the file: " + error.getMessage());
//            }
        } else {
            System.out.println("Invalid option. Please choose 1 for compression or 2 for decompression.");
        }
    }
}
