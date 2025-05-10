package fr.lirmm.graphik.graal.io.owl;

import java.io.*;
import java.util.*;

public class removeDuplicates {

	public static void main(String[] args) throws IOException {
		String file1Path = "C:\\Users\\tho310\\Data\\LOV\\dataset\\temp.txt";
		String mergedOutputPath = "C:\\Users\\tho310\\Data\\LOV\\dataset\\merged.txt";
		String matchingPredicatesPath = "C:\\Users\\tho310\\Data\\LOV\\dataset\\matching_predicates.txt";
		String cleanedOutputPath = "C:\\Users\\tho310\\Data\\LOV\\dataset\\cleaned_output.txt";

		// Step 1: Process files and build predicate maps
		Map<String, Set<String>> lineToPredicates = new HashMap<>();
		Map<Set<String>, List<String>> predicatesToLines = new HashMap<>();

		// Process file1
		processFile(file1Path, lineToPredicates, predicatesToLines);

		// Process file2 (if you have a second file)
		// processFile(file2Path, lineToPredicates, predicatesToLines);

		// Step 2: Identify duplicates (lines with same predicate sets)
		Set<String> linesToRemove = new HashSet<>();
		try (BufferedWriter matchingWriter = new BufferedWriter(new FileWriter(matchingPredicatesPath))) {
			for (Map.Entry<Set<String>, List<String>> entry : predicatesToLines.entrySet()) {
				List<String> lines = entry.getValue();
				if (lines.size() > 1) { // Only predicate sets that appear in multiple lines
					for (String line : lines) {
						matchingWriter.write(line);
						matchingWriter.newLine();
						linesToRemove.add(line);
					}
					matchingWriter.newLine(); // Separate groups with blank line
				}
			}
		}

		// Step 3: Write cleaned output (without duplicates)
		try (BufferedWriter cleanedWriter = new BufferedWriter(new FileWriter(cleanedOutputPath));
				BufferedWriter mergedWriter = new BufferedWriter(new FileWriter(mergedOutputPath))) {

			for (String line : lineToPredicates.keySet()) {
				mergedWriter.write(line);
				mergedWriter.newLine();

				if (!linesToRemove.contains(line)) {
					cleanedWriter.write(line);
					cleanedWriter.newLine();
				}
			}
		}

		System.out.println("Processing complete.");
		System.out.println("Merged file: " + mergedOutputPath);
		System.out.println("Lines with matching predicates: " + matchingPredicatesPath);
		System.out.println("Cleaned output: " + cleanedOutputPath);
	}

	private static void processFile(String filePath, Map<String, Set<String>> lineToPredicates,
			Map<Set<String>, List<String>> predicatesToLines) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					Set<String> predicates = extractPredicates(line);
					lineToPredicates.put(line, predicates);

// Group lines by their predicate sets
					predicatesToLines.computeIfAbsent(predicates, k -> new ArrayList<>()).add(line);
				}
			}
		}
	}

	 public static Set<String> extractPredicates(String line) {
	        Set<String> predicates = new HashSet<>();

	        // Split into head and body parts
	        String[] parts = line.split(":-");
	        if (parts.length < 1) return predicates;

	        // Extract head predicate
	        extractAndAddPredicate(parts[0].trim(), predicates);

	        // Extract body predicates if they exist
	        if (parts.length > 1) {
	            String[] bodyPredicates = parts[1].split(",");
	            for (String pred : bodyPredicates) {
	                extractAndAddPredicate(pred.trim(), predicates);
	            }
	        }

	        return predicates;
	    }

	    private static void extractAndAddPredicate(String term, Set<String> predicates) {
	        int start = term.indexOf('<');
	        int end = term.indexOf('>');
	        if (start != -1 && end != -1) {
	            String fullPredicate = term.substring(start + 1, end);
	            String simpleName = fullPredicate.substring(fullPredicate.lastIndexOf('/') + 1);
	            predicates.add(simpleName);
	        }
	    }
	}