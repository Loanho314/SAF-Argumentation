package fr.lirmm.graphik.graal.io.owl;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.factory.TermFactory;
import fr.lirmm.graphik.graal.core.DefaultNegativeConstraint;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomFactory;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import fr.lirmm.graphik.graal.io.dlp.Directive;
import fr.lirmm.graphik.graal.io.dlp.DlgpWriter;
import fr.lirmm.graphik.util.DefaultURI;
import fr.lirmm.graphik.util.Prefix;
import java.io.File;
import java.io.IOException;


public class Owl2Dlgp {
	private static Predicate THING = new Predicate(new DefaultURI("http://www.w3.org/2002/07/owl#Thing"), 1);
	private static Atom NOTHING = DefaultAtomFactory.instance().create(
			new Predicate(new DefaultURI("http://www.w3.org/2002/07/owl#Nothing"), 1),
			new Term[] { DefaultTermFactory.instance().createVariable("X") });

	//private String inputFile = "/Users/tho310/Data/DBpedia/Data + Ontology/DBPedia-ontology.owl";
	private String inputFile = "/Users/tho310/my-graal-app/test1.owl";
	private String outputFile = "/Users/tho310/my-graal-app/data.dlgp";
	// @Parameter(names = { "-b", "--base" }, description = "specify dlgp base
	// directive")
	private String base = "";

	public static void main(String[] args) throws OWL2ParserException, IOException {

		Owl2Dlgp options = new Owl2Dlgp();
		OWLtoParser parser;
		parser = new OWLtoParser(new File(options.inputFile));
		DlgpWriter writer;
		writer = new DlgpWriter(new File(options.outputFile));
		
		if (!options.base.isEmpty()) {
			writer.write(new Directive(Directive.Type.BASE, options.base));
		}

		while (parser.hasNext()) {
			System.out.println("parser: " + parser.next().toString());
			Object o = parser.next();
			if (!(o instanceof Prefix)) {
				writer.write(new Directive(Directive.Type.TOP, THING));
				writer.write(new DefaultNegativeConstraint(new LinkedListAtomSet(new Atom[] { NOTHING })));
				writer.write(o);
				break;
			}
			writer.write(o);
		}

		while (parser.hasNext()) {
			writer.write(parser.next());
		}

		writer.close();
		System.out.println("Completed!");

	}

}
