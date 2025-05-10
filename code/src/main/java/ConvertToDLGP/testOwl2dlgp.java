package ConvertToDLGP;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.util.FileManager;
import org.apache.jena.riot.Lang;
import java.io.*;
import java.util.zip.GZIPInputStream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.factory.TermFactory;
//import fr.lirmm.graphik.graal.apps.OWL2DLGP;
import fr.lirmm.graphik.graal.core.DefaultNegativeConstraint;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomFactory;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import fr.lirmm.graphik.graal.io.dlp.Directive;
import fr.lirmm.graphik.graal.io.dlp.Directive.Type;
import fr.lirmm.graphik.graal.io.dlp.DlgpWriter;
import fr.lirmm.graphik.graal.io.owl.OWL2Parser;
import fr.lirmm.graphik.graal.io.owl.OWL2ParserException;
import fr.lirmm.graphik.util.Apps;
import fr.lirmm.graphik.util.DefaultURI;
import fr.lirmm.graphik.util.Prefix;
import java.io.File;
import java.io.IOException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.LoggerFactory;

public class testOwl2dlgp {
	private static Predicate THING = new Predicate(new DefaultURI("http://www.w3.org/2002/07/owl#Thing"), 1);
	private static Atom NOTHING = DefaultAtomFactory.instance().create(
			new Predicate(new DefaultURI("http://www.w3.org/2002/07/owl#Nothing"), 1),
			new Term[] { DefaultTermFactory.instance().createVariable("X") });

//	@Parameter(names = { "-h", "--help" }, description = "Print this message", help = true)
//	private boolean help;
//
//	@Parameter(names = { "--version" }, description = "Print version information")
//	private boolean version;
//
//	@Parameter(names = { "-f", "--file" }, description = "OWL input file")
//	private String inputFile;
//
//	@Parameter(names = { "-o", "--output" }, description = "The output file")
//	private String outputFile;
//
//	@Parameter(names = { "-b", "--base" }, description = "specify dlgp base directive")
//	private String base;
//
//	@Parameter(names = { "-d", "--debug" }, description = "enable debug mode", hidden = true)
	private Boolean debugMode = Boolean.valueOf(false);

	public static void main(String[] args) throws OWL2ParserException, IOException {
//		String inputFile = "test.ttl";
//		String outputFile = "output_part00.dlgp";
//
////		OWL2DLGP options = new OWL2DLGP();
//		    JCommander commander = new JCommander(options, args);
//		    commander.setProgramName("java -jar owl2dlgp-*.jar");
//
//		    if (options.help) {
//		      commander.usage();
//		      System.exit(0);
//		    }
//
//		    if (options.version) {
//		      Apps.printVersion("owl2dlgp");
//		      System.exit(0);
//		    }

//		if (options.debugMode.booleanValue()) {
//			((Logger) LoggerFactory.getLogger(OWL2Parser.class)).setLevel(Level.DEBUG);
//		}
//		OWL2Parser parser;
//		
//		System.out.println("Converting...");
//
//		parser = new OWL2Parser(new File(inputFile));
//		DlgpWriter writer;
//
//		writer = new DlgpWriter(new File(outputFile));

//		if (!options.base.isEmpty()) {
//			writer.write(new Directive(Directive.Type.BASE, options.base));
//		}

//		while (parser.hasNext()) {
//			Object o = parser.next();
//			if (!(o instanceof Prefix)) {
//				writer.write(new Directive(Directive.Type.TOP, THING));
//				writer.write(new DefaultNegativeConstraint(new LinkedListAtomSet(new Atom[] { NOTHING })));
//				writer.write(o);
//				break;
//			}
//			writer.write(o);
//		}
//
//		while (parser.hasNext()) {
//			writer.write(parser.next());
//		}
//
//		writer.close();
//		
//		System.out.println("Done!");

		// Convert from nt to owl

//		String localNtPath = "C:/Users/tho310/Data test/DBpedia/ontology.nt";  // Windows
//		
//        // Output OWL file path
//        String outputOwlPath = "output.owl";
//    
//
//        // Create an empty RDF model
//        Model model = ModelFactory.createDefaultModel();
//
//        try {
//            System.out.println("Reading NT file from: " + localNtPath);
//            
//            // Correct way to read from FileInputStream
//            InputStream in = new FileInputStream(localNtPath);
//            RDFDataMgr.read(model, in, Lang.NTRIPLES);
//            in.close();
//            
//            System.out.println("Successfully loaded " + model.size() + " triples.");
//
//            // Write to OWL (RDF/XML format)
//            System.out.println("Converting to OWL and saving to: " + outputOwlPath);
//            
//            try (OutputStream out = new FileOutputStream(outputOwlPath)) {
//                RDFDataMgr.write(out, model, Lang.RDFXML);
//            }
//            
//            System.out.println("Conversion completed!");
//
//        } catch (FileNotFoundException e) {
//            System.err.println("File not found: " + localNtPath);
//        } catch (Exception e) {
//            System.err.println("Error during conversion: " + e.getMessage());
//            e.printStackTrace();
//        }

		// Convert from ttl to owl
//
//		String inputFile = "output_part01.ttl"; // Windows
//		// Output OWL file path
//		String outputFile = "output_part01.owl";
//
//		Model model = ModelFactory.createDefaultModel();
//		RDFParser.source(inputFile).lang(Lang.TTL).base("http://yago-knowledge.org/resource/").parse(model);
//
//		System.out.println("Converting...");
//
//		// Write in chunks
//		RDFDataMgr.write(new FileOutputStream(outputFile), model, RDFFormat.RDFXML);
//		model.close(); // Free memory immediately
//		System.out.println("Conversion completed!");

		String inputFile = "input.ttl"; // Change to your input file
		String outputFile = "output.rdf"; // Change to your output file

		// Create an empty model
		Model model = ModelFactory.createDefaultModel();

		// Read the Turtle file
		try {
			System.out.println("Reading Turtle file: " + inputFile);
			model.read(FileManager.get().open(inputFile), null, "TURTLE");

			// Write to RDF/XML format
			System.out.println("Writing RDF/XML file: " + outputFile);
			try (FileOutputStream out = new FileOutputStream(outputFile)) {
				model.write(out, "RDF/XML-ABBREV");
			}

			System.out.println("Conversion completed successfully!");
		} catch (IOException e) {
			System.err.println("Error during conversion: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
