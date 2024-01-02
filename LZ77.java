import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LZ77 {
    /**
     * This function compresses the content of an input file using the LZ77
     * compression algorithm and writes the compressed data to an output file. It
     * uses a sliding window of a specified size to find the best matches within
     * the window and encodes them as tags. The primary purpose is to perform LZ77
     * compression and save the compressed data in an output file, with error
     * handling for potential file I/O issues.
     */
    public static List<Tag> encode(String text, int windowSize) throws IOException {
        List<Tag> tags = new ArrayList<>();
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
                tags.add(tag);
                i += bestMatchLength;
                windowStart = i - windowSize + 1;
            } else {
                Tag tag = new Tag(0, 0, text.charAt(i));
                tags.add(tag);
            }
        }
        return tags;
    }

    /**
     * This function decompresses a list of LZ77 tags and writes the decompressed
     * data to an output file. It reads the tags sequentially and reconstructs the
     * original data. The decompressed data is both printed to the
     * console and written to an output file. The function includes error handling
     * for possible file I/O issues.
     */
    public static String decode(List<Tag> tags) throws IOException {

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
        return output.toString();
    }

    // This function compresses the content of an input file using the LZ77
    public static void compressFile(String inputFileName, int windowSize, String outputFileName) throws IOException {
        String plainText = TextFileHandler.readFile(inputFileName);
        LZ77FileHandler.writeFile(encode(plainText, windowSize), outputFileName);
    }

    // This function decompresses a list of LZ77 tags and writes the decompressed
    public static void decompressFile(String inputFileName, String outputFileName) throws IOException {
        List<Tag> encodedData = LZ77FileHandler.readFile(inputFileName);
        TextFileHandler.writeFile(outputFileName, decode(encodedData));
    }
}
