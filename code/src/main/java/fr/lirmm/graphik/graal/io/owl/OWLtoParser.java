package fr.lirmm.graphik.graal.io.owl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.input.ReaderInputStream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.model.OWLAxiom;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.core.Variable;
import fr.lirmm.graphik.graal.api.io.Parser;
import fr.lirmm.graphik.graal.core.DefaultNegativeConstraint;
import fr.lirmm.graphik.graal.core.TreeMapSubstitution;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.atomset.graph.DefaultInMemoryGraphStore;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomFactory;
import fr.lirmm.graphik.graal.core.factory.DefaultRuleFactory;
import fr.lirmm.graphik.util.Prefix;
import fr.lirmm.graphik.util.stream.AbstractCloseableIterator;
import fr.lirmm.graphik.util.stream.ArrayBlockingStream;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;
import fr.lirmm.graphik.util.stream.transformator.Transformator;

public class OWLtoParser extends AbstractCloseableIterator<Object> implements Parser<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OWL2Parser.class);
	private static final RuleTransformator RULE_TRANSFO = new RuleTransformator();
	private static final InMemoryAtomSet BOTTOM_ATOMSET = new LinkedListAtomSet(
			DefaultAtomFactory.instance().getBottom());

	private ArrayBlockingStream<Object> buffer = new ArrayBlockingStream<>(512);

	private InputStream inputStream = null;
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private OWLOntology ontology;
	private boolean prefixEnable = true;

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor for parsing from the given reader.
	 * 
	 * @param stream
	 * @throws OWL2ParserException
	 */
	public OWLtoParser(InputStream stream) throws OWL2ParserException {
		this.inputStream = stream;
		try {
			this.ontology = this.manager.loadOntologyFromOntologyDocument(stream);
		} catch (OWLOntologyCreationException e) {
			throw new OWL2ParserException(e);
		}
		ShortFormProvider sfp = getShortFormProvider(this.ontology);
		new Thread(new Producer(this.ontology, sfp, buffer)).start();
	}

	/**
	 * Constructor for parsing from the standard input.
	 * 
	 * @throws OWL2ParserException
	 */
	public OWLtoParser() throws OWL2ParserException {
		this(System.in);
	}

	/**
	 * Constructor for parsing from the given file.
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws OWL2ParserException
	 */
	public OWLtoParser(File file) throws FileNotFoundException, OWL2ParserException {
		try {
			this.ontology = this.manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			throw new OWL2ParserException(e);
		}
		ShortFormProvider sfp = getShortFormProvider(this.ontology);
		new Thread(new Producer(this.ontology, sfp, buffer)).start();
	}

	/**
	 * Constructor for parsing the content of the string s as OWL content.
	 * 
	 * @param s
	 * @throws OWL2ParserException
	 */
	public OWLtoParser(String s) throws OWL2ParserException {
		this(new ByteArrayInputStream(s.getBytes()));
	}

	/**
	 * Constructor for parsing the given InputStream.
	 * 
	 * @param in
	 * @throws OWL2ParserException
	 */
	@SuppressWarnings("deprecation")
	public OWLtoParser(Reader in) throws OWL2ParserException {
		this(new ReaderInputStream(in));
	}

	// /////////////////////////////////////////////////////////////////////////
	// METHODS
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Enable or disable prefix short form.
	 * 
	 * @param b Default value is true.
	 */
	public void prefixEnable(boolean b) {
		this.prefixEnable = b;
	}

	@Override
	public boolean hasNext() {
		return buffer.hasNext();
	}

	@Override
	public Object next() {
		return buffer.next();
	}

	/**
	 * Closes the stream and releases any system resources associated with it.
	 * Closing a previously closed parser has no effect.
	 */
	@Override
	public void close() {
		if (this.inputStream != null) {
			try {
				this.inputStream.close();
			} catch (IOException e) {
				LOGGER.error("Error during closing inputStream");
			}
			this.inputStream = null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	// /////////////////////////////////////////////////////////////////////////

	private ShortFormProvider getShortFormProvider(OWLOntology ontology) {
		OWLDocumentFormat format = this.manager.getOntologyFormat(this.ontology);
		DefaultPrefixManager pm = new DefaultPrefixManager();
		if (format.isPrefixOWLDocumentFormat()) {
			 PrefixDocumentFormat prefixFormat = format.asPrefixOWLDocumentFormat();
		//	PrefixOWLOntologyFormat prefixFormat = (PrefixOWLOntologyFormat) format;
			Map<String, String> prefixMap = prefixFormat.getPrefixName2PrefixMap();

			Set<String> forbiddenPrefix = new TreeSet<>();
			forbiddenPrefix.add("xml:");
			forbiddenPrefix.add("rdf:");
			forbiddenPrefix.add("rdfs:");
			forbiddenPrefix.add("owl:");

			for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
				String prefix = entry.getKey();
				if (!forbiddenPrefix.contains(prefix)) {
					pm.setPrefix(prefix, entry.getValue());
					prefix = prefix.substring(0, prefix.length() - 1);
					buffer.write(new Prefix(prefix, entry.getValue()));
				}
			}
		}
		return pm;
	}


	private static class Producer implements Runnable {

		private OWLOntology onto;
		private ArrayBlockingStream<Object> buffer;
		private ShortFormProvider shortForm;

		Producer(OWLOntology onto, ShortFormProvider shortForm, ArrayBlockingStream<Object> buffer) {
			this.onto = onto;
			this.buffer = buffer;
			this.shortForm = shortForm;
		}

		@Override
		public void run() {
			try {

				OWLaxiomparser visitor = new OWLaxiomparser(shortForm,manager);

				// process axioms containing anonymous individuals
				Set<OWLAnonymousIndividual> anonymousIndividuals = onto.getAnonymousIndividuals();

				while (!anonymousIndividuals.isEmpty()) {
					Iterator<OWLAnonymousIndividual> it = anonymousIndividuals.iterator();
					TreeSet<OWLAnonymousIndividual> localAnonymousIndividuals = new TreeSet<>();
					localAnonymousIndividuals.add(it.next());
					it.remove();

					InMemoryAtomSet fact = new LinkedListAtomSet();
					AnonymousProcessor processor = new AnonymousProcessor(fact);

					// keep an anonymous individu and process it
					
	
					
					while (!localAnonymousIndividuals.isEmpty()) {
						OWLAnonymousIndividual i = localAnonymousIndividuals.pollFirst();
						Set<OWLAxiom> axioms = onto.getReferencingAxioms(i, Imports.EXCLUDED);
						for (OWLAxiom a : axioms ) {
							// add individu referenced by this axiom to the
							// local set and remove it from the global set
							for (OWLAnonymousIndividual neestedIndividu : a.getAnonymousIndividuals()) {
								if (anonymousIndividuals.contains(neestedIndividu)) {
									anonymousIndividuals.remove(neestedIndividu);
									localAnonymousIndividuals.add(neestedIndividu);
								}
							}

							// process current axiom
							processAxiom(a, visitor, processor);
						}
					}
					buffer.write(fact);
				}

				// process other axioms
				Set<OWLAxiom> axioms1 = onto.getAxioms();
				for (OWLAxiom a1 : axioms1) {
					if (! a1.getAnonymousIndividuals().isEmpty()) {
						continue;
					}
					processAxiom(a1, visitor, noAnonymousProcessor);
				}

			} finally {
				buffer.close();
			}
		}

		/**
		 * @param a
		 */
		private static void processAxiom(OWLAxiom a, OWLaxiomparser visitor, Processor p) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("### OWLAxiom: " + a.toString());
			}

			Iterable<? extends Object> iterable = a.accept(visitor);
			if (iterable != null) {
				for (Object o : iterable) {
					if (o != null) {
						if (o instanceof Rule) {
							o = RULE_TRANSFO.transform((Rule) o);
						}
						if (o != null) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(" => " + o.toString());
							}
							p.exec(o);
						}
					}
				}
			}
		}

		private static interface Processor {
			void exec(Object o);
		}

		private static class AnonymousProcessor implements Processor {

			private InMemoryAtomSet fact;

			public AnonymousProcessor(InMemoryAtomSet fact) {
				this.fact = fact;
			}

			@Override
			public void exec(Object o) {
				if (!(o instanceof InMemoryAtomSet)) {
					LOGGER.error("AnonymousIndividuals not allowed here: " + o);
				} else {
					fact.addAll((InMemoryAtomSet) o);
				}
			}

		}

		private NoAnonymousProcessor noAnonymousProcessor = new NoAnonymousProcessor();

		private class NoAnonymousProcessor implements Processor {
			@Override
			public void exec(Object o) {
				buffer.write(o);
			}
		}

	}

	/**
	 * This filter some rule into a fact or a negativeConstraint or delete it.
	 * 
	 */
	private static class RuleTransformator implements Transformator<Rule, Object> {

		@Override
		public Object transform(Rule r) {
			InMemoryAtomSet body = r.getBody();
			InMemoryAtomSet head = r.getHead();

			CloseableIteratorWithoutException<Atom> bodyIt = body.iterator();

			// Remove equality in body
			Substitution s = new TreeMapSubstitution();
			InMemoryAtomSet toRemove = new LinkedListAtomSet();
			while (bodyIt.hasNext()) {
				Atom a = bodyIt.next();
				if (a.getPredicate().equals(Predicate.EQUALITY)
						&& (a.getTerm(0).isVariable() || a.getTerm(1).isVariable())) {
					toRemove.add(a);
					if (a.getTerm(0).isVariable()) {
						s.put((Variable) a.getTerm(0), a.getTerm(1));
					} else {
						s.put((Variable) a.getTerm(1), a.getTerm(0));
					}
				}
			}
			body.removeAll(toRemove);

			body = removeUselessTopInBody(removeUselessBottom(s.createImageOf(body)));
			bodyIt = body.iterator();

			head = removeUselessTopInHead(removeUselessBottom(s.createImageOf(head)));
			CloseableIteratorWithoutException<Atom> headIt = head.iterator();

			// USELESS STATEMENT
			if (!headIt.hasNext()) {
				return null;
				// CONSTRAINTS
			} else if (headIt.next().getPredicate().equals(Predicate.BOTTOM)) {
				return new DefaultNegativeConstraint(body);
				// ASSERTIONS
			} else if (!bodyIt.hasNext()) {
				return head;
				// USELESS STATEMENT
			} else if (bodyIt.next().getPredicate().equals(Predicate.BOTTOM)) {
				return null;
			} else {
				return DefaultRuleFactory.instance().create(body, head);
			}
		}
	}

	/**
	 * bottom and A => bottom
	 * 
	 * @param atomset
	 * @return an InMemoryAtomSet logically equivalents to the specified one.
	 */
	private static InMemoryAtomSet removeUselessBottom(InMemoryAtomSet atomset) {
		CloseableIteratorWithoutException<Atom> it = atomset.iterator();
		Atom a;
		while (it.hasNext()) {
			a = it.next();
			if (a.getPredicate().equals(Predicate.BOTTOM)) {
				return BOTTOM_ATOMSET;
			}
		}

		return atomset;
	}

	private static InMemoryAtomSet removeUselessTopInHead(InMemoryAtomSet atomset) {
		InMemoryAtomSet newAtomset = new LinkedListAtomSet();
		CloseableIteratorWithoutException<Atom> it = atomset.iterator();
		Atom a;
		while (it.hasNext()) {
			a = it.next();
			if (!a.getPredicate().equals(Predicate.TOP)) {
				newAtomset.add(a);
			}
		}

		return newAtomset;
	}

	private static InMemoryAtomSet removeUselessTopInBody(InMemoryAtomSet atomset) {
		InMemoryAtomSet newAtomset = new DefaultInMemoryGraphStore();
		CloseableIteratorWithoutException<Atom> it = atomset.iterator();
		InMemoryAtomSet toRemove = new LinkedListAtomSet();
		Atom a;
		while (it.hasNext()) {
			a = it.next();
			if (!a.getPredicate().equals(Predicate.TOP)) {
				newAtomset.add(a);
				toRemove.add(a);
			} else {

			}
		}
		atomset.removeAll(toRemove);

		// for each top predicate
		Set<Term> terms = newAtomset.getTerms();
		it = atomset.iterator();
		while (it.hasNext()) {
			a = it.next();
			if (!terms.contains(a.getTerm(0))) {
				newAtomset.add(a);
			}
		}

		return newAtomset;
	}
}
