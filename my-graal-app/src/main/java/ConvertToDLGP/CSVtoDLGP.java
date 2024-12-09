package ConvertToDLGP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.jena.sparql.function.library.substring;

public class CSVtoDLGP {

	public static void main(String[] args) {

		String csvFile = "C:/Users/tho310/Data test/DBpedia/DisjointWith/2 - a1C0C1C2C3C4p0a0.csv"; // Path to your CSV file
		String outputFile = "C:/Users/tho310/Data test/DBpedia/DisjointWith/output.dlgp"; // Output file where RDF triples will be
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
				
			//2 - a1,C0,C1,C2,C3,C4,p0,a0

				String a1 = row[0].replace("\"", "");
				String C0 = row[1].replace("\"", "");				
				String C1 = row[2].replace("\"", "");
				String C2 = row[3].replace("\"", "");
				String C3 = row[4].replace("\"", "");
				String C4 = row[5].replace("\"", "");	
				String p0 = row[6].replace("\"", "");
				String a0 = row[7].replace("\"", "");
				//String C0 = row[8].replace("\"", "");
				//String C5 = row[9].replace("\"", "");
				//String a0 = row[10].replace("\"", "");
				//String a0 = row[11].replace("\"", "");
				

				// Format the RDF triples	
				/*
				 * ?a1 rdf:type ?C0. ?C1 rdfs:subClassOf ?C2. ?C2 rdfs:subClassOf ?C3. ?C3
				 * rdfs:subClassOf ?C4. ?C0 owl:disjointWith ?C4. ?p0 rdfs:range ?C1. ?a0 ?p0
				 * ?a1.
				 */
								 
				String triple1 = "<" + C2 + ">(X0) :- <" + C1 + ">(X0).\n";
				String triple2 = "<" + C3 + ">(X0) :- <" + C2 + ">(X0).\n";
				String triple3 = "<" + C4 + ">(X0) :- <" + C3 + ">(X0).\n";				
				//String triple4 = "<" + C4 + ">(X0) :- <" + C3 + ">(X0).\n";
				//String triple5 = "<" + C3 + ">(X0) :- <" + C1 + ">(X0).\n";
				//String triple6 = "<" + C1 + ">(X0) :- <" + C0 + ">(X0).\n";				
				//String triple7 = "<" + C8 + ">(X0) :- <" + C7 + ">(X0).\n";				
				//String triple8 = "<" + C5 + ">(X0) :- <" + C4 + ">(X0).\n";	
				//String triple12 = "<" + C2 + ">(X0) :- <" + C4 + ">(X0).\n";

				String triple9 = "! :- " + "<" + C0 + ">(X0), <" + C4 + ">(X0).\n";
				
				String triple10 = "<" + C0 + ">(<" + a1 + ">).\n";
				//String triple11 = "<" + C3 + ">(<" + a0 + ">).\n";
				
				String tripleRa = "<" + C1 + ">(X1) :- <" + p0 + ">(X0, X1).\n";
				String tripleRange;
				if (a1.substring(0, Math.min(a1.length(), 4)).equals("http")) {
					tripleRange = "<" + p0 + ">(<" + a0 + ">, <" + a1 + ">).\n";
				} else				
					tripleRange = "<" + p0 + ">(<" + a0 + ">, \"" + a1 + "\").\n";
				
				
				//String tripleDomain = "<" + C1 + ">(X0) :- <" + p0 + ">(X0, X1).\n";				
				/*String tripleDomain;
				if (a1.substring(0, Math.min(a1.length(), 4)).equals("http")) {
					 tripleDomain = "<" + p0 + ">(<" + a0 + ">, <" + a1 + ">).\n";
				} else
				
					 tripleDomain = "<" + p0 + ">(<" + a0 + ">, \"" + a1 + "\").\n";*/
				
				

				// Add the first two types of triples to the first set
				ruleTriples.add(triple1);
				ruleTriples.add(triple2);
				ruleTriples.add(triple3);
				//ruleTriples.add(tripleDomain);
				
				ruleTriples.add(triple4);
			//	ruleTriples.add(triple5);	
			//	ruleTriples.add(triple6);
			//	ruleTriples.add(triple7);
			//	ruleTriples.add(triple8);
				//ruleTriples.add(triple12);
				
				negTriples.add(triple9);

				// Add the other triples to the second set				
				  instanceTriples.add(triple10); 
				  instanceTriples.add(triple11);		
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
