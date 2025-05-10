package fr.lirmm.graphik.graal.io.owl;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.jena.query.ARQ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RIOT;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import fr.lirmm.graphik.NAry.App1;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.kb.KnowledgeBaseException;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import fr.lirmm.graphik.graal.kb.KBBuilderException;

import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.io.ParseException;
import fr.lirmm.graphik.graal.io.dlp.DlgpWriter;
import fr.lirmm.graphik.graal.io.owl.OWL2Parser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.io.FileInputStream;

@SuppressWarnings("deprecation")
public class Rdf2Dlgp {

	// public static Term termSubject
	// public static Term termSubject;

//	public static DefaultAtom atom;
//	
//	private static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
//    private static final DefaultTermFactory TERM_FACTORY = DefaultTermFactory.instance();
//    
//    public static void convertRdfToDlgp(Model model, String outputPath) throws Exception {
//        Set<Atom> atomSet = new HashSet<>(); // Using Set to avoid duplicates automatically
//        
//        try (StmtIterator iter = model.listStatements()) {
//            while (iter.hasNext()) {
//                Statement stmt = iter.nextStatement();
//                Resource subject = stmt.getSubject();
//                
//                // Skip blank nodes
//                if (subject.isAnon()) {
//                    continue;
//                }
//                
//                Atom atom = createAtomFromStatement(stmt);
//                if (atom != null) {
//                    atomSet.add(atom);
//                }
//            }
//        }
//        
//        writeAtomsToFile(atomSet, outputPath);
//        System.out.println("Conversion completed successfully!");
//    }

//    private static Atom createAtomFromStatement(Statement stmt) {
//        Resource subject = stmt.getSubject();
//        Property predicate = stmt.getPredicate();
//        RDFNode object = stmt.getObject();
//        
//        Term termSubject = TERM_FACTORY.createConstant(uriToDlgpFormat(subject.getURI()));
//        
//        if (RDF_TYPE.equals(predicate.getURI())) {
//            // Handle rdf:type statements
//            Predicate typePredicate = new Predicate(uriToDlgpFormat(object.asResource().getURI()), 1);
//            return new DefaultAtom(typePredicate, termSubject);
//        } else {
//            // Handle regular property statements
//            Predicate propertyPredicate = new Predicate(uriToDlgpFormat(predicate.getURI()), 2);
//            Term termObject = createTermForObject(object);
//            
//            if (termObject != null) {
//                return new DefaultAtom(propertyPredicate, termSubject, termObject);
//            }
//        }
//        
//        return null;
//    }
//    
//    private static Term createTermForObject(RDFNode object) {
//        if (object.isLiteral()) {
//            return TERM_FACTORY.createLiteral(object.asLiteral().getLexicalForm());
//        } else if (object.isResource()) {
//            Resource resource = object.asResource();
//            return resource.isAnon() ? null : TERM_FACTORY.createConstant(uriToDlgpFormat(resource.getURI()));
//        }
//        return null;
//    }
//    
//    private static String uriToDlgpFormat(String uri) {
//        return "<" + uri + ">";
//    }
//    
//    private static void writeAtomsToFile(Set<Atom> atoms, String outputPath) throws Exception {
//        try (PrintWriter out = new PrintWriter(outputPath)) {
//            for (Atom atom : atoms) {
//                out.println(atom.toString() + ".");
//            }
//        }
//    }

	public static Model processRDFFile(String inputFileName) throws IOException {
		Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(inputFileName);

		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		String extension = getFileExtension(inputFileName).toLowerCase();

		// For NT or TTL files: convert to temporary RDF/XML file first
		if (extension.equals("nt") || extension.equals("ttl") || extension.equals("trig") || extension.equals("nq")) {
			// Create temporary file for conversion
			File tempFile = File.createTempFile("converted_", ".rdf");
			tempFile.deleteOnExit();

			// Read original format
			Model tempModel = ModelFactory.createDefaultModel();
			if (extension.equals("nt")) {
				tempModel.read(in, null, "N-TRIPLES");
			}
			if (extension.equals("ttl")) {
				RIOT.init();
				System.setProperty("riot.strict", "false");
				model.getReader().setProperty("error-mode", "lax");
				tempModel.read(in, null, "TURTLE");
			}
			if (extension.equals("nq")) {
				RDFParser.source(in).lang(Lang.NQ).parse(tempModel);
			}
			if (extension.equals("trig")) {
				RDFParser.source(in).lang(Lang.TRIG).parse(tempModel);
			}

			// Write to temporary file as RDF/XML
			try (OutputStream out = new FileOutputStream(tempFile)) {
				tempModel.write(out, "RDF/XML");

			}

			// Read the converted file
			try (InputStream convertedIn = new FileInputStream(tempFile)) {
				model.read(convertedIn, null);
			}
		}
		// For RDF/XML or OWL files: read directly
		else if (extension.equals("rdf") || extension.equals("owl")) {
			model.read(in, null);
		} else {
			throw new IllegalArgumentException("Unsupported file format: " + extension);
		}

		return model;
	}

	private static String getFileExtension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex == -1) {
			return "";
		}
		return filename.substring(dotIndex + 1);
	}

	public static void main(String[] args) throws OWLOntologyCreationException, IOException, AtomSetException,
			KBBuilderException, KnowledgeBaseException {
		// load rdf/xml file using Jena.

		// Create an empty model

//		final String inputFileName = "output_part00.owl";
//		String extension = getFileExtension(inputFileName).toLowerCase();
//		
//		
//		Model model = ModelFactory.createDefaultModel();
//
//		InputStream in = FileManager.get().open(inputFileName);	
//		 if (extension.equals("rdf") || extension.equals("owl")) {
//	            model.read(in,  "");
//	        }
		String in = "lov.trig";
		String outputFile = "lov.dlgp";
		ARQ.init();
		Model model = processRDFFile(in);

		// Read a RDF/XML file
		System.out.println("Start converting...");
		StmtIterator iter = model.listStatements();

//		while (iter.hasNext()) {
//			System.out.println("statement " + iter.next());
//		}

		// Transfer from statement to atom
		List<Atom> atomSet = new ArrayList<>();

		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();
			Term termObject = null;
			Term termSubject = null;

			// Consider subject as term 0
			if (subject.isAnon()) {
				continue;
			} else {
				if (predicate.getURI().compareTo("http://www.w3.org/2000/01/rdf-schema#label") == 0) {
					System.out.println(predicate.getURI());
					continue;

				}

				if (predicate.getURI().compareTo("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") == 0) {
					Predicate predicate1 = new Predicate("<" + object.asResource() + ">", 1);
					if (subject.isResource()) {
						termSubject = DefaultTermFactory.instance().createConstant("<" + subject + ">");
					}
					Atom a = new DefaultAtom(predicate1, termSubject);
					atomSet.add(a);
				} else {
					Predicate predicate2 = new Predicate("<" + predicate + ">", 2);
					if (subject.isResource()) {
						termSubject = DefaultTermFactory.instance().createConstant("<" + subject + ">");
					}

					// Consider Object as Term 1
					// System.out.println(object.toString());

					// Case1 : Objects are literal
					if (object.isLiteral()) {
						// System.out.println("literal: " + object.toString());
						termObject = DefaultTermFactory.instance().createLiteral(object.toString());
					}
					// Case2: Objects are resource
					if (object.isResource()) {

						if (object.asResource().getLocalName() == null) {
							termObject = DefaultTermFactory.instance().createConstant("<" + object.asResource() + ">");
						} else if (object.asResource().getLocalName() != null) {
							// System.out.println("resoursce: " + object.toString());
							termObject = DefaultTermFactory.instance().createConstant("<" + object.asResource() + ">");
						}

					}
//					if (object.asNode()!= null) {
//						System.out.println("stmt: " + object.toString());
//					}
					Atom a = new DefaultAtom(predicate2, termSubject, termObject);
					if (a != null || !atomSet.contains(a)) {
						atomSet.add(a);
					}
				}

			}
		}
		System.out.println("Done!");

		PrintWriter out = new PrintWriter(outputFile);
		for (Atom at : atomSet) {
			out.println(App1.AtomWithoutArity(at) + ".");
		}
		out.close();
	}

	public static Predicate valueToPredicate(Property predicate) {
		return new Predicate(predicate.getLocalName(), 2);
	}

	public static Term objectToTerm(RDFNode object) {
		return null;

	}

	public static String handleIRI(OWLNamedObject obj) {
		if ((obj.getIRI().getFragment() != null)) {
			return obj.getIRI().getFragment().toString();
		}
		return obj.toString();
	}

}
