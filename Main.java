import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMainMenu();

            try {
                String choice = scanner.nextLine().toLowerCase();

                switch (choice) {
                    case "1", "compress" -> handleCompression(scanner);
                    case "2", "decompress" -> handleDecompression(scanner);
                    case "3", "exit" -> {
                        System.out.println("Thank you for using LZ77 program\n");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid option! Please enter a valid choice.");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found - " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error: IOException - " + e.getMessage());
            }
        }
    }

    // Helper method to print the menu
    private static void printMainMenu() {
        System.out.println("Options:");
        System.out.println("1. Compress file");
        System.out.println("2. Decompress file");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    // Handle compression process
    private static void handleCompression(Scanner scanner) throws IOException {
        System.out.print("Please enter the input filename: ");
        String inputFilePath = scanner.nextLine();

        System.out.print("Please enter the destination filename: ");
        String destFilePath = scanner.nextLine();

        LZ77.compressFile(inputFilePath, 12, destFilePath);
        System.out.println("Compression successful: " + inputFilePath + " -> " + destFilePath + "\n");
    }

    // Handle decompression process
    private static void handleDecompression(Scanner scanner) throws IOException {
        System.out.print("Please enter the input filename: ");
        String inputFilePath = scanner.nextLine();

        System.out.print("Please enter the destination filename: ");
        String destFilePath = scanner.nextLine();

        LZ77.decompressFile(inputFilePath, destFilePath);
        System.out.println("Decompression successful: " + inputFilePath + " -> " + destFilePath + "\n");
    }
}
