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

		String csvFile = "C:/Users/tho310/Data test/RDF/YAGO/16 - a0C5C6C1C2C0C3C4.csv"; // Path to your CSV
																								// file
		String outputFile = "C:/Users/tho310/Data test/RDF/YAGO/16 - a0C5C6C1C2C0C3C4.dlgp"; // Output file where RDF
																							// triples will be
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

				//a0C5C6C1C2C0C3C4

				String a0 = row[0].replace("\"", "");
				String C5 = row[1].replace("\"", "");
				String C6 = row[2].replace("\"", "");
				String C1 = row[3].replace("\"", "");
				String C2 = row[4].replace("\"", "");
				String C0 = row[5].replace("\"", "");
				String C3 = row[6].replace("\"", "");
				String C4 = row[7].replace("\"", "");
			//	String C4 = row[8].replace("\"", "");
				//String C5 = row[9].replace("\"", "");
				//String C8 = row[10].replace("\"", "");
				// String a0 = row[11].replace("\"", "");

				// Format the RDF triples


				String triple10 = C5 + "(" + a0 + ").\n";
				String triple11 = C6 + "(" + a0 + ").\n";
				String triple9 = "! :- " + C1 + "(X0), " + C2 + "(X0).\n";
				String triple1 = C1 + "(X0) :- " + C0 + "(X0).\n";
				String triple2 = C4 + "(X0) :- " + C3 + "(X0).\n";
				String triple3 = C0 + "(X0) :- " + C4 + "(X0).\n";
				String triple4 = C3 + "(X0) :- " + C5 + "(X0).\n";
				String triple5 = C2 + "(X0) :- " + C6 + "(X0).\n";
				
						//String triple1 = "<" + C2 + ">(X0) :- <" + C0 + ">(X0).\n";
				//String triple2 = "<" + C3 + ">(X0) :- <" + C2 + ">(X0).\n";
				//String triple3 = "<" + C5 + ">(X0) :- <" + C1 + ">(X0).\n";
				//String triple4 = "<" + C4 + ">(X0) :- <" + C3 + ">(X0).\n";
				//String triple5 = "<" + C3 + ">(X0) :- <" + C1 + ">(X0).\n";
				//String triple6 = "<" + C1 + ">(X0) :- <" + C0 + ">(X0).\n";
				//String triple7 = "<" + C2 + ">(X0) :- <" + C2 + ">(X0).\n";
				//String triple8 = "<" + C3 + ">(X0) :- <" + C2 + ">(X0).\n";
				//String triple12 = "<" + C1 + ">(X0) :- <" + C0 + ">(X0).\n";
				//String triple9 = "! :- " + "<" + C3 + ">(X0), <" + C5 + ">(X0).\n";
				// String triple10 = "<" + C0 + ">(<" + a0 + ">).\n";
				 //String triple11 = "<" + C1 + ">(<" + a0 + ">).\n";

			/*	String tripleRa = "<" + C1 + ">(X1) :- <" + p0 + ">(X0, X1).\n";
				String tripleRange;
				if (a1.substring(0, Math.min(a1.length(), 4)).equals("http")) {
					tripleRange = "<" + p0 + ">(<" + a0 + ">, <" + a1 + ">).\n";
				} else
					tripleRange = "<" + p0 + ">(<" + a0 + ">, \"" + a1 + "\").\n"; */

				/*String tripleDo = "<" + C1 + ">(X0) :- <" + p0 + ">(X0, X1).\n";			
				String tripleDomain; 
				if (a1.substring(0, Math.min(a1.length(), 4)).equals("http")) { 
					  tripleDomain = "<" + p0 + ">(<" + a0 + ">, <" + a1 + ">).\n"; 
				}  else	
					  tripleDomain = "<" + p0 + ">(<" + a0 + ">, \"" + a1 + "\").\n";*/
				 

				// Add the first two types of triples to the first set
				ruleTriples.add(triple1);
				ruleTriples.add(triple2);
				ruleTriples.add(triple3);
				ruleTriples.add(triple4);
				ruleTriples.add(triple5);
				//ruleTriples.add(triple6);
				//ruleTriples.add(triple7);
				//ruleTriples.add(triple8);
				//ruleTriples.add(tripleRa);
				//ruleTriples.add(tripleDo);

				//ruleTriples.add(triple4);
				//ruleTriples.add(triple5);
				//ruleTriples.add(triple6);
				//ruleTriples.add(triple7);
				//ruleTriples.add(triple8);
				//ruleTriples.add(triple12);

				negTriples.add(triple9);

				// Add the other triples to the second set
				instanceTriples.add(triple10);
				instanceTriples.add(triple11);
				//instanceTriples.add(tripleRange);
				//instanceTriples.add(tripleDomain);
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

		}catch(

	IOException e)
	{
		e.printStackTrace();
	}

}

}
