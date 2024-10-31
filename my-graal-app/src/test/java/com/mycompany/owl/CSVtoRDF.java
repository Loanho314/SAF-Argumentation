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
        String csvFile = "C:/Users/tho310/Data test/DBpedia/7Classes-1DisjoinWith.csv";  // Path to your CSV file
        String outputFile = "C:/Users/tho310/Data test/DBpedia/output.dlgp";  // Output file where RDF triples will be written
        String line;
        String csvSplitBy = ",";
        
        // Create a set to store RDF triples and avoid duplicates
        Set<String> negTriples = new LinkedHashSet<>(); // Ordered set for triples 1 and 2
        Set<String> ruleTriples = new LinkedHashSet<>();
        Set<String> otherTriples = new LinkedHashSet<>();// Ordered set for triples 3 and 4

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
                String C0 = row[0].replace("\"", "");
                String C1 = row[1].replace("\"", "");
                String C2 = row[2].replace("\"", "");
                String C3 = row[3].replace("\"", "");
                String C4 = row[4].replace("\"", "");
                String C5 = row[4].replace("\"", "");
                String C6 = row[4].replace("\"", "");
                String C7 = row[4].replace("\"", "");
                String a0 = row[5].replace("\"", "");

                // Format the RDF triples
                
				/*
				 * String triple1 = "! :- " + "<" + C0 + ">(X0), <" + C1 + ">(X0).\n"; String
				 * triple2 = "<" + C0 + ">(X0) :- <" + C2 + ">(X0).\n"; String triple3 = "<" +
				 * C1 + ">(<" + a0 + ">).\n"; String triple4 = "<" + C2 + ">(<" + a0 + ">).\n";
				 */
                
               // String rdfTriple1 = "<" + C0 + "> <http://www.w3.org/2002/07/owl#disjointWith> <" + C1 + ">.\n";
               // String rdfTriple2 = "<" + C2 + "> <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + C0 + ">.\n";
               // String rdfTriple3 = "<" + a0 + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <" + C1 + ">.\n";
               // String rdfTriple4 = "<" + a0 + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <" + C2 + ">.\n";

                /*SELECT * WHERE {?C0 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C1. 
                	?C2 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C3. 
                	?C4 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C0. 
                	?C1 <http://www.w3.org/2002/07/owl#disjointWith> ?C3. 
                	?a0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C2.
                	?a0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C4. }
                				 
				 String triple1 = "<" + C1 + ">(X0) :- <" + C0 + ">(X0).\n";
				 String triple2 = "<" + C3 + ">(X0) :- <" + C2 + ">(X0).\n";
				 String triple3 = "<" + C0 + ">(X0) :- <" + C4 + ">(X0).\n";
				 String triple4 = "! :- " + "<" + C1 + ">(X0), <" + C3 + ">(X0).\n";
				 String triple5 = "<" + C2 + ">(<" + a0 + ">).\n";
				 String triple6 = "<" + C4 + ">(<" + a0 + ">).\n"; */
                
               /* SELECT * WHERE {?a0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C0.
                	 ?a0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?C2.
                	  
                	?C1 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C3. 
                	?C6 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C7.
                	 ?C0 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C1. 
                	?C5 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C6. 
                	?C3 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C4. 
                	?C2 <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?C5.
                	
                	 ?C4 <http://www.w3.org/2002/07/owl#disjointWith> ?C7. } */
                
                 String triple1 = "<" + C3 + ">(X0) :- <" + C1 + ">(X0).\n";
				 String triple2 = "<" + C7 + ">(X0) :- <" + C6 + ">(X0).\n";
				 String triple3 = "<" + C1 + ">(X0) :- <" + C0 + ">(X0).\n";
				 String triple7 = "<" + C6 + ">(X0) :- <" + C5 + ">(X0).\n";
				 String triple8 = "<" + C4 + ">(X0) :- <" + C3 + ">(X0).\n";
				 String triple9 = "<" + C5 + ">(X0) :- <" + C2 + ">(X0).\n";
				 
				 String triple4 = "! :- " + "<" + C4 + ">(X0), <" + C7 + ">(X0).\n";
				 
				 String triple5 = "<" + C0 + ">(<" + a0 + ">).\n";
				 String triple6 = "<" + C2 + ">(<" + a0 + ">).\n";
				
                
                // Add the first two types of triples to the first set
				 ruleTriples.add(triple1);
				 ruleTriples.add(triple2);
				 ruleTriples.add(triple3);
				 
                negTriples.add(triple4);
                

                // Add the other triples to the second set
                otherTriples.add(triple5);
                otherTriples.add(triple6);
            }
            
            
         // Write the ordered RDF triples to the output file
            for (String triple : negTriples) {
                bw.write(triple);
            }
            for (String triple : ruleTriples) {
                bw.write(triple);
            }
            for (String triple : otherTriples) {
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
