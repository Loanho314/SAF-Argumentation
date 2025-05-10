package com.mycompany.owl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;

public class NQuadsToOWLConverter {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String inputFile = "C:/Users/tho310/Data test/LOV/data/lov.nq"; // Input N-Quads file path (e.g.,
																		// "C:/data/input.nq")
		String outputFile = "C:/Users/tho310/Data test/LOV/data/lov.owl"; // Output OWL file path (e.g.,
																			// "C:/output/output.owl")

		// Load N-Quads file
		Model model = ModelFactory.createDefaultModel();
		System.out.println("Loading: " + new File(inputFile).getAbsolutePath());
		RDFDataMgr.read(model, inputFile);

		// Save as OWL/RDFXML
		System.out.println("Writing: " + new File(outputFile).getAbsolutePath());

		try (OutputStream out = new FileOutputStream(outputFile)) {
			RDFDataMgr.write(out, model, Lang.RDFXML);
		}

		//RDFDataMgr.write(new File(outputFile).toPath(), model, RDFFormat.RDFXML);

		System.out.println("Conversion complete!");
	}

}
