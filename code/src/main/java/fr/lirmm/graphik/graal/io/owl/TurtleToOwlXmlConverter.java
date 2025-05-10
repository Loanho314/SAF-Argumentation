package fr.lirmm.graphik.graal.io.owl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import java.io.*;


public class TurtleToOwlXmlConverter {

	public static void main(String[] args)
			throws  IOException {
		// TODO Auto-generated method stub

		String inputPath = "C:\\Users\\tho310\\Data\\LOV\\dataset\\disjoint.txt";
		String outputPath = "C:\\Users\\tho310\\Data\\LOV\\dataset\\ouput-dis.txt";
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.equals(".")) {
                continue; // Skip empty lines or lines with just a dot
            }
            
            String[] parts = line.split("\\s+", 3); // Split into max 3 parts
            
            if (parts.length >= 3 && parts[1].equals("<http://www.w3.org/2002/07/owl#disjointWith>")) {
                String subject = parts[0];
                String object = parts[2].replaceAll("\\.$", ""); // Remove trailing dot
                
                // Format as ! :- <subject>(X0), <object>(X0).
                String formattedLine = String.format("! :- %s(X0), %s(X0).", subject, object);
                bw.write(formattedLine);
                bw.newLine();
            }
        }
            
//         // Split into subject, predicate, object (preserving < >)
//            String[] parts = line.split("\\s+", 3); // Split into max 3 parts
//            
//            if (parts.length >= 3) {
//                String subject = parts[0];
//                String predicate = parts[1];
//                String object = parts[2].replaceAll("\\.$", ""); // Remove trailing dot
//                
//                // Format as <object>(<subject>).
//                String formattedLine = String.format("%s(%s).", object, subject);
//                bw.write(formattedLine);
//                bw.newLine();
//            }

//            // Split the line into components (space-separated)
//            String[] parts = line.split("\\s+", 3); // Split into max 3 parts
//            
//            if (parts.length >= 3) {
//                String c1 = parts[0];
//                String c2 = parts[1];
//                String c3 = parts[2].replaceAll("\\.$", ""); // Remove trailing dot if present
//                
//                // Format as C2(C1,C3). (preserves brackets)
//                String formattedLine = String.format("%s(%s,%s).", c2, c1, c3);
//                bw.write(formattedLine);
//                bw.newLine();
//            }
        
        
        System.out.println("Conversion completed. Output written to: " + outputPath);
		
		
		/*
		 * Model model = ModelFactory.createDefaultModel();
		 * 
		 * // Read the TRIG file System.out.println("Reading TRIG file: " + inputPath);
		 * InputStream in = new FileInputStream(inputPath); RDFDataMgr.read(model, in,
		 * Lang.TRIG); in.close();
		 * 
		 * // Write to RDF/XML format System.out.println("Writing RDF/XML file: " +
		 * outputPath); OutputStream out = new FileOutputStream(outputPath);
		 * model.write(out, "RDF/XML"); out.close();
		 * 
		 * System.out.println("Conversion completed successfully!");
		 */

		/*
		 * // Initialize OWL Ontology Manager OWLOntologyManager manager =
		 * OWLManager.createOWLOntologyManager();
		 * 
		 * // Load TTL file (Turtle format) File inputFile = new File(inputPath);
		 * OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputFile);
		 * 
		 * // Save as OWL/XML File outputFile = new File(outputPath); //
		 * manager.saveOntology( // ontology, // new OWLXMLDocumentFormat(), // new
		 * FileOutputStream(outputFile) // );
		 * 
		 * manager.saveOntology(ontology, new RDFXMLDocumentFormat(), new
		 * FileOutputStream(outputFile));
		 * 
		 * System.out.println("Successfully converted TTL to OWL/XML!");
		 */

	}
}