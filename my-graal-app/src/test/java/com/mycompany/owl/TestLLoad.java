package com.mycompany.owl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/*import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;*/

import fr.lirmm.graphik.util.Prefix;
import fr.lirmm.graphik.util.stream.ArrayBlockingStream;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.graal.common.rdf4j.RDFTypeAtomMapper;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import fr.lirmm.graphik.graal.common.rdf4j.RDF4jUtils;
import fr.lirmm.graphik.NAry.App1;
import fr.lirmm.graphik.graal.api.core.AbstractAtom;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Literal;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.kb.Approach;
import fr.lirmm.graphik.graal.api.kb.KnowledgeBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//import org.eclipse.rdf4j.rio.RDFFormat;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.graal.io.rdf.RDFParser;
import fr.lirmm.graphik.graal.io.rdf.RDFWriter;
import fr.lirmm.graphik.graal.kb.KBBuilder;
import fr.lirmm.graphik.util.Prefix;
import fr.lirmm.graphik.graal.api.store.Store;




import fr.lirmm.graphik.graal.api.core.AtomSetException;

import fr.lirmm.graphik.graal.api.core.RuleSetException;

import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;

import fr.lirmm.graphik.graal.api.kb.KnowledgeBaseException;

import fr.lirmm.graphik.graal.kb.KBBuilderException;

import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;
import fr.lirmm.graphik.graal.common.rdf4j.*;


import java.io.PrintWriter;

//import fr.lirmm.graphik.graal.store.triplestore.rdf4j;
//import fr.lirmm.graphik.graal.store.triplestore.rdf4j.RDF4jStore;
//import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
// POM for 
/* <dependency>
			<groupId>org.eclipse.rdf4j</groupId>
			<artifactId>rdf4j-repository-sparql</artifactId>
			<version>4.2.2</version>
		</dependency>
		<dependency>
			<groupId>fr.lirmm.graphik</groupId>
			<artifactId>graal-store-rdf4j</artifactId>
			<version>1.3.1</version>
		</dependency>*/



@SuppressWarnings("deprecation")
public class TestLLoad {
	private static Set<String> conceptNames;
	//static final String inputFileName  = "/Users/tho310/Data test/vc-db-1.rdf";
	//static final String inputFileName  = "/Users/tho310/Data test/Birthdate.rdf";
	//static final String inputFileName  = "/Users/tho310/Data test/test.rdf";
	//static final String inputFileName = "/Users/tho310/surfdrive/Data test 2/BIOGRAPHY BENCHMARK/BIO-5000-Birthdate.rdf";
	//static final String inputFileName  = "/Users/tho310/surfdrive/Data test 2/test.nt";
	  static final String inputFileName  = "/Users/tho310/surfdrive/Data test 2/Lumb Ontology/u1conf50.nt";
	//	public static Term term0
	//public static Term term1;

	public static DefaultAtom atom;

	public static void main(String[] args) throws OWLOntologyCreationException, IOException, AtomSetException, KBBuilderException, KnowledgeBaseException {
		// TODO Auto-generated method stub

		// load owl file

		//	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		//File file = new File("/Users/tho310/my-graal-app/Bioturle.owl");
		//	OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(file));
		//OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("/Users/tho310/my-graal-app/BioGraph.owl"));

		// load ontology from IRI

		/*	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//IRI docIRI = IRI.create("http://www.w3.org/2009/08/skos-reference/skos.rdf");
		//IRI docIRI = IRI.create("https://raw.githubusercontent.com/wiki/ontop/ontop/attachments/Example_MovieOntology/movieontology.owl");
	    OWLOntology skos = manager.loadOntologyFromOntologyDocument(docIRI);
	    System.out.println("My class is working");
	    System.out.println("Loaded ontology: " + skos);
	    System.out.println();*/

		/*	File inputOntologyFile = new File("univ-bench.owl");
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);
		OWLDataFactory factory = manager.getOWLDataFactory();

		System.out.println("My class is working!");

		//print owl file
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
		System.out.println(ontologyIRI == null ? "anonymous" : ontologyIRI.toQuotedString());
		System.out.println(" from " + documentIRI.toQuotedString());
		System.out.println("......");
		System.out.println(ontology);*/

		//get class		

		/*	System.out.println("get name of class");
		List<String> listofClass = new ArrayList<String>();		
		Collection<OWLClass> classes = ontology.getClassesInSignature();

		//getting class names in the ontology
		    for (OWLClass owlClass : classes) {
		            System.out.println(owlClass.getIRI().getShortForm());
		            listofClass.add(owlClass.getIRI().getShortForm());
		            for (OWLIndividual indiv : owlClass.getIndividualsInSignature()) {
		                System.out.println(indiv.asOWLNamedIndividual().getIRI().getFragment());
		            }		            
		      }
		    //System.out.println(listofClass);
		    for (OWLClass owlClass : classes) {
		    Set<OWLNamedIndividual> inds = owlClass.getIndividualsInSignature();
	          for(OWLNamedIndividual ind : inds)
	          {
	                System.out.println(ind.toString());
	          }
		    }*/




		// load rdf/xml file using Jena.

		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open(inputFileName );
		
		if (in == null) {
			throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		String extension = FileNameUtils.getExtension(inputFileName);
		if (extension.equals("nt")) {
			model.read(in, null, "N-TRIPLES");
		}
		if (extension.equals("rdf")) {
			model.read(in, "");
		}

		//model.read(in, null);
		
		//model.read(in, "");

		// write it to standard out
		//	model.write(System.out);

		AtomSet InitialFacts = new LinkedListAtomSet();	
		// list of statement in rdf

		System.out.println("begining statement");
		StmtIterator it = model.listStatements(null,RDF.type, (RDFNode)null);
		if (it.hasNext()){
			System.out.println("rdf:type " + it.next());
		}

		StmtIterator iter = model.listStatements();


		// print out the predicate, subject and object of each statement
		/*	while (iter.hasNext()) {
			Statement stmt      = iter.nextStatement();         // get next statement
			Resource  subject   = stmt.getSubject();   // get the subject
			Property  predicate = stmt.getPredicate(); // get the predicate
			RDFNode   object    = stmt.getObject(); // get the object

			// print subject
			if (subject.isLiteral()) {
				System.out.print("Literal: " + subject.listProperties());
			}
			if (subject.isAnon()) {
				System.out.print("Anon: " + subject.getId());
			} 
			if (subject.isResource()) {
				System.out.print("RES: " + subject.getLocalName());
				}			
			// print predicate			
			System.out.print(" - " + predicate.getLocalName() + " - ");
			// print object
			// object is literal
			if (object.isLiteral()) {
				System.out.println("LIT: " + predicate.getLocalName() + "  " + object.toString());
			}
			// object is resource
			if (object.isResource()) {
				if (object.asResource().getLocalName() == null) {
					System.out.println("Null: " + object.asResource().getLocalName());
					StmtIterator stmts = object.asResource().listProperties();
					while (stmts.hasNext()) {
						Statement s = stmts.next();
						System.out.println(s.getPredicate().getLocalName() +" " + s.getObject());
					}
				}
				else {
					System.out.println("RES: " + object.asResource().getLocalName() + " URI: " + object.asResource().getURI() );
				}	

			}
		}*/
		// transform from statement to atom
		//InMemoryAtomSet atomSet = new LinkedListAtomSet();
		ArrayList<Atom> atomSet = new ArrayList<>();
		List<Term> listTerms = new ArrayList<Term>();


		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			Resource  subject   = stmt.getSubject();
			Property  predicate = stmt.getPredicate();
			RDFNode   object    = stmt.getObject();
			Term term1 = null;
			Term term0 = null;



			// subject to term 0
			if (subject.isAnon()) {
				System.out.println("Anon: " + stmt + " " + subject.asResource().getURI());				
				//StmtIterator stmts1 = object.asResource().listProperties();
				//continue;
			} else {
				// value to Prdicate
				//	String predicatLabel = predicate.getLocalName();
				//Predicate pre = new Predicate(predicate.getLocalName(), 2);
				// the case of rdf:type states that an individual belongs to a class

				if (predicate.getURI().compareTo("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") == 0) {
					System.out.println("URI: " + predicate.getURI());
					Predicate pre = new Predicate("<" + object.asResource() + ">", 1);
					System.out.println(pre);
					if (subject.isResource()) {
						term0 = DefaultTermFactory.instance().createConstant("<"+ subject + ">");
					}
					Atom a = new DefaultAtom(pre, term0);
					atomSet.add(a);
					System.out.println("atom: " + a);
					System.out.println(App1.AtomWithoutArity(a) +".");
				} else {
					Predicate pre = new Predicate("<" + predicate + ">",2);
					if (subject.isResource()) {

						//term0 = DefaultTermFactory.instance().createConstant(subject.getLocalName());
						term0 = DefaultTermFactory.instance().createConstant("<"+ subject + ">");
						//	new DefaultTermFactory.instance().createConstant(subject.getLocalName().toString());
						System.out.println("RES-term0: " + term0.toString());
						//System.out.println(subject.isResource());
					}

					// object to Term 1

					// object is literal
					if (object.isLiteral()) {
						term1 = DefaultTermFactory.instance().createLiteral(object.toString());
						System.out.println("LIT-term1: " +  term1);

						//System.out.println("LIT: " + predicate.getLocalName() + "  " + object.toString());
					}
					// object is resource
					if (object.isResource()) {
						if (object.asResource().getLocalName() == null) {
							System.out.println("Null: " + object.asResource().getLocalName());
							//term1 = DefaultTermFactory.instance().createVariable("N");

							StmtIterator stmts = object.asResource().listProperties();
							while (stmts.hasNext()) {
								Statement s = stmts.next();
								System.out.println("statement: "+ s.getPredicate().getLocalName() +" " + s.getObject());
								//if (s.getPredicate().getLocalName().contentEquals("type")) {
								if (s.getObject().toString().contentEquals("birth")) {
									//predicatLabel = "hasBirthdate";
									pre = new Predicate("hasBirthdate",2);
									//System.out.println(predicatLabel);
								}
								if (s.getObject().toString().contentEquals("death")) {
									//predicatLabel = "hasBirthdate";
									pre = new Predicate("hasDeathdate",2);
									//System.out.println(predicatLabel);
								}

								if (s.getPredicate().getLocalName().contentEquals("when")) {

									term1 = DefaultTermFactory.instance().createLiteral(s.getObject());

								}
								//System.out.println(s.getPredicate().getLocalName() +" " + s.getObject());
							}
						}
						else {
							//term1 = DefaultTermFactory.instance().createConstant(object.asResource().getLocalName());
							term1 = DefaultTermFactory.instance().createConstant("<" +object.asResource()+ ">");
							//System.out.println("RES: " + object.asResource().getLocalName() + " URI: " + object.asResource().getURI() );
							System.out.println("RES-term1: " +  term1);
							//listTerms.add(term1);
						}	

					}
					//Predicate pre = new Predicate(predicatLabel, 2);

					System.out.println("Atom: " + pre + "-" + term0 + "-" + term1);

					Atom a = new DefaultAtom(pre, term0, term1);
					//check whether a new atom is in the set of atoms.
					if (!atomSet.contains(a)) {				
						atomSet.add(a);
					}
					else
						System.out.println("da ton tai");

				}
			}
		}

		/*System.out.println("Set of atoms:");
		for (Atom a : atomSet) {
			System.out.println(App1.AtomWithoutArity(a) +".");
		}*/
		PrintWriter out = new PrintWriter("/Users/tho310/surfdrive/Data test 2/RESULT/instance.dlgp");
		//CloseableIterator<Atom> it1 = atomSet.iterator();
		for (Atom at : atomSet) {
			out.println(App1.AtomWithoutArity(at) +".");
		}
		out.close();
		/*	System.out.println("Set of terms:");
		for (int i=0; i<listTerms.size(); i++) {
			System.out.println(listTerms.get(i));
		}*/


	}
	//}
	public static Predicate valueToPredicate(Property predicate) {
		return new Predicate(predicate.getLocalName(), 2);
	}
	/*public static Term subjectToTerm(Resource subject) {
		if (subject.isAnon()) {
			continue;
		}
		if (subject.isResource()) {
			Term te = DefaultTermFactory.instance().createConstant(subject.getLocalName());			
			return te;
		}
	}*/

	public static Term objectToTerm(RDFNode object) {
		return null;

	}





	public static String handleIRI(OWLNamedObject obj)
	{
		if ((obj.getIRI().getFragment() != null)) {
			return obj.getIRI().getFragment().toString();
		}
		return obj.toString();
	}

}









//	statementToAtom(org.eclipse.rdf4j.model.Statement stat);


// tranformer example 

// 0 - Create a RDF writer to print parsed facts
/*	RDFWriter writer = new RDFWriter(System.out, RDFFormat.TURTLE);

		// 1 - Create a RDF parser for the specified rdf file
		File f = new File(rdfFilePath);
		RDFParser parser = new RDFParser(new FileReader(f), RDFFormat.RDFXML);

		// 2 - Parse the file and display the facts
		while (parser.hasNext()) {
			Object o = parser.next();
			if(o instanceof Atom) {
				writer.write((Atom)o);
			} else {
				writer.write((Prefix)o);
			}
			writer.flush();
		}

		// 3 - Close the parser
		writer.close();
		parser.close();*/

/*KBBuilder kbb = new KBBuilder();		
		kbb.setStore(new RDF4jStore(new SPARQLRepository("http://dbpedia.org/sparql")));
		kbb.addRules(new DlgpParser("@prefix dbo: <http://dbpedia.org/ontology/> dbo:bandMember(X,Y) :- dbo:formerBandMember(X,Y)."));
		kbb.setApproach(Approach.REWRITING_ONLY);
		KnowledgeBase kb = kbb.build();

		ConjunctiveQuery query = DlgpParser.parseQuery(
				"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "@prefix dbo: <http://dbpedia.org/ontology/> "
				+ "@prefix dbr: <http://dbpedia.org/resource/> "
				+ "[Q1] ?(X,Y) :- "
				+ "dbo:bandMember(dbr:The_Beatles, X), "
                + "dbo:bandMember(Y, X).");

		CloseableIterator<Substitution> it = kb.query(query);
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		it.close();
		kb.close();*/





