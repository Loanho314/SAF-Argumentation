package com.mycompany.owl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVtoRDF {

	public static void main(String[] args) {
		String csvFile = "C:/Users/tho310/Data test/DBpedia/Query.csv"; // Path to your CSV file
		String outputFile = "C:/Users/tho310/Data test/DBpedia/output.dlgp"; // Output file where RDF triples will be
																				// written
		String line;
		String csvSplitBy = ",";

		// Create a set to store RDF triples and avoid duplicates
		Set<String> negTriples = new LinkedHashSet<>(); // Ordered set for triples 1 and 2
		Set<String> ruleTriples = new LinkedHashSet<>();
		Set<String> instanceTriples = new LinkedHashSet<>();// Ordered set for triples 3 and 4

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

			boolean firstLine = true;

			while ((line = br.readLine()) != null) {

				// Skip the header row
				if (firstLine) {
					firstLine = false;
					continue;
				}

				// Use comma as separator
				String[] row = line.split(csvSplitBy);

				// Assuming the CSV columns are in the order C0, C1, C2, a0
				
				


				String a0 = row[0].replace("\"", "");
				String C0 = row[1].replace("\"", "");				
				String C2 = row[2].replace("\"", "");
				String C1 = row[3].replace("\"", "");
				String C3 = row[4].replace("\"", "");
				String C4 = row[5].replace("\"", "");
				
						
				/*String C3 = row[4].replace("\"", "");
				String C6 = row[5].replace("\"", "");
				String C7 = row[6].replace("\"", "");
				String C5 = row[7].replace("\"", "");
				String C4 = row[8].replace("\"", "");*/
				
				
				

				// Format the RDF triples	
				

				
				

				String triple3 = "<" + C1 + ">(X0) :- <" + C0 + ">(X0).\n";
				String triple4 = "<" + C3 + ">(X0) :- <" + C1 + ">(X0).\n";
				String triple5 = "<" + C4 + ">(X0) :- <" + C3 + ">(X0).\n";
				
				/*String triple2 = "<" + C3 + ">(X0) :- <" + C2 + ">(X0).\n";
				String triple6 = "<" + C6 + ">(X0) :- <" + C5 + ">(X0).\n";				
				String triple7 = "<" + C4 + ">(X0) :- <" + C3 + ">(X0).\n";				
				String triple8 = "<" + C5 + ">(X0) :- <" + C2 + ">(X0).\n";*/

				String triple9 = "! :- " + "<" + C4 + ">(X0), <" + C2 + ">(X0).\n";
				
				String triple1 = "<" + C2 + ">(<" + a0 + ">).\n";
				String triple2 = "<" + C0 + ">(<" + a0 + ">).\n";

				

				// Add the first two types of triples to the first set
				ruleTriples.add(triple3);
				ruleTriples.add(triple4);
				ruleTriples.add(triple5);
				
				
				/*
				 *   ruleTriples.add(triple6);
				 * ruleTriples.add(triple7); ruleTriples.add(triple8);
				 */
				 
				 
				

				negTriples.add(triple9);

				// Add the other triples to the second set
				instanceTriples.add(triple1);
				instanceTriples.add(triple2);
			
			}

			// Write the ordered RDF triples to the output file
			for (String triple : negTriples) {
				bw.write(triple);
			}
			for (String triple : ruleTriples) {
				bw.write(triple);
			}
			for (String triple : instanceTriples) {
				bw.write(triple);
			}

			// Close the BufferedWriter
			bw.flush();

			System.out.println("RDF triples have been successfully written to " + outputFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
