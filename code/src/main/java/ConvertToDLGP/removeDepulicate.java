package ConvertToDLGP;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class removeDepulicate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String filePath = "C:/Users/tho310/Data test/input.txt"; // Replace with your file path
	    //  String retainedLinesFile = "C:/Users/tho310/Data test/DBpedia/retained_lines1.txt";
	    //    String removedLinesFile = "C:/Users/tho310/Data test/DBpedia/removed_lines1.txt";

	        try {
	            // Read all lines from the file
	            List<String> lines = Files.readAllLines(Paths.get(filePath));

	            // Use LinkedHashSet to remove duplicates and maintain order
	            Set<String> uniqueLines = new LinkedHashSet<>(lines);

	            // Write the unique lines back to the file
	            Files.write(Paths.get(filePath), uniqueLines);

	            System.out.println("Duplicate lines removed successfully!");
	        } catch (IOException e) {
	            System.err.println("An error occurred while processing the file: " + e.getMessage());
	        } 
		 
		/*try {
			// Read all lines from the file
	        List<String> lines = Files.readAllLines(Paths.get(filePath));

	        // A LinkedHashMap to store unique lines by their "resource/" content
	        Map<String, String> uniqueLinesMap = new LinkedHashMap<>();
	        List<String> removedLines = new ArrayList<>();

	        // Regular expression to extract content after "resource/"
	        Pattern pattern = Pattern.compile("resource/([^>]+)>");

	        for (String line : lines) {
	            Matcher matcher = pattern.matcher(line);
	            if (matcher.find()) {
	                String resourceContent = matcher.group(1);
	                // Check if the resourceContent is already present
	                if (uniqueLinesMap.containsKey(resourceContent)) {
	                    // Add the duplicate line to removedLines
	                    removedLines.add(line);
	                } else {
	                    // Add the unique line to the map
	                    uniqueLinesMap.put(resourceContent, line);
	                }
	            } else {
	                // If no "resource/" content is found, treat it as a unique line
	                uniqueLinesMap.put(UUID.randomUUID().toString(), line);
	            }
	        }

	        // Write the retained lines (unique lines) to the retained lines file
	        Files.write(Paths.get(retainedLinesFile), uniqueLinesMap.values());

	        // Write the removed lines (duplicates) to the removed lines file
	        Files.write(Paths.get(removedLinesFile), removedLines);

	        System.out.println("Processing completed. Retained and removed lines have been written to separate files.");

	        } catch (IOException e) {
	            System.err.println("An error occurred while processing the file: " + e.getMessage());
	        } */
		 
		 // check symmetric
		 
		 /*try {
	            // Read all lines from the file
	            List<String> lines = Files.readAllLines(Paths.get(filePath));

	            // A set to store normalized pairs and their symmetric counterparts
	            Set<String> pairs = new HashSet<>();
	            List<String> symmetricLines = new ArrayList<>();

	            // Regular expression to extract terms after "ontology/"
	            Pattern pattern = Pattern.compile("<http://dbpedia\\.org/ontology/([^>]+)>\\(.*\\) :- <http://dbpedia\\.org/ontology/([^>]+)>\\(.*\\)\\.");

	            for (String line : lines) {
	                Matcher matcher = pattern.matcher(line);
	                if (matcher.matches()) {
	                    String leftTerm = matcher.group(1);
	                    String rightTerm = matcher.group(2);

	                    // Create normalized pair and its reverse
	                    String pair = leftTerm + "->" + rightTerm;
	                    String reversePair = rightTerm + "->" + leftTerm;

	                    // Check if the reverse pair exists
	                    if (pairs.contains(reversePair)) {
	                        symmetricLines.add(line);
	                        // Add the symmetric counterpart (already processed)
	                        for (String existingLine : lines) {
	                            Matcher existingMatcher = pattern.matcher(existingLine);
	                            if (existingMatcher.matches()) {
	                                if (existingMatcher.group(1).equals(rightTerm) && existingMatcher.group(2).equals(leftTerm)) {
	                                    symmetricLines.add(existingLine);
	                                    break;
	                                }
	                            }
	                        }
	                    } else {
	                        pairs.add(pair);
	                    }
	                }
	            }

	            // Write symmetric lines to a new file or print them
	            String outputPath = "symmetric_lines.txt";
	            Files.write(Paths.get(outputPath), symmetricLines);

	            System.out.println("Symmetric lines written to: " + outputPath);
	        } catch (IOException e) {
	            System.err.println("An error occurred while processing the file: " + e.getMessage());
	        }*/
	}
}

