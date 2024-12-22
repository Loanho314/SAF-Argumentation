package ConvertToDLGP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveReduplicates {

public static Set<String> removeReduplicates(Set<String> inputLines) {
    Set<String> uniqueLines = new HashSet<>();

    for (String line : inputLines) {
        String flippedLine = flipLine(line);
        if (!uniqueLines.contains(flippedLine)) {
            uniqueLines.add(line);
        }
    }
    return uniqueLines;
}

private static String flipLine(String line) {
    if (!line.startsWith("! :-")) return line; // Return unchanged if not formatted properly

    String[] parts = line.split(", ");
    if (parts.length != 2) return line; // Return unchanged if splitting fails

    // Swap and reconstruct the flipped line
    String header = parts[0].substring(0, 5); // Preserve "! :-"
    String flipped = header + parts[1].trim() + ", " + parts[0].trim().substring(5);
    return flipped;
}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputFile = "C:/Users/tho310/Data test/DBpedia/KB/symmetric_lines.txt";  // Input file name
        String outputFile = "C:/Users/tho310/Data test/DBpedia/KB/output.txt"; // Output file name

        try {
            // Read lines from the input file
            Set<String> lines = new HashSet<>(readFile(inputFile));

            // Remove reduplicates
            Set<String> result = removeReduplicates(lines);

            // Write the unique lines to the output file
            writeFile(outputFile, result);

            System.out.println("Processing complete. Unique lines written to " + outputFile);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static List<String> readFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    private static void writeFile(String fileName, Set<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}