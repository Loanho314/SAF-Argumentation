package ConvertToDLGP;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GroupByPrefix {

	public static void main(String[] args) throws IOException {

		String filePath = "C:/Users/tho310/Data test/DBpedia/input.txt";
        String outputFilePath = "C:/Users/tho310/Data test/DBpedia/output.txt";

        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // A TreeMap to group lines by the prefix before ": "
        Map<String, List<String>> groupedLines = new TreeMap<>();

        for (String line : lines) {
            // Split the line at ": "
            int delimiterIndex = line.indexOf(":- ");
            if (delimiterIndex != -1) {
                String key = line.substring(0, delimiterIndex); // Prefix before ": "
                groupedLines.putIfAbsent(key, new ArrayList<>());
                groupedLines.get(key).add(line);
            }
        }

        // Build the output
        List<String> outputLines = new ArrayList<>();
        for (List<String> group : groupedLines.values()) {
            outputLines.addAll(group);
        }

        // Write the grouped lines to the output file
        Files.write(Paths.get(outputFilePath), outputLines);

        System.out.println("Lines grouped and written to the output file successfully.");

	}

}
