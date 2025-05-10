package fr.lirmm.graphik.graal.io.owl;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.util.ShortFormProvider;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.core.Variable;
import fr.lirmm.graphik.graal.api.core.VariableGenerator;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.factory.DefaultAtomFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

public class OWLEquivalentClassExpressionVisitorImpl1 extends
OWLEquivalentClassExpressionVisitor {
	
	private static final OWLDataFactory DF = new OWLDataFactoryImpl();

	private Term glueVariable;
	private VariableGenerator varGen;
	private ShortFormProvider prefixManager;

	// /////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	// /////////////////////////////////////////////////////////////////////////

	public OWLEquivalentClassExpressionVisitorImpl1(
			ShortFormProvider prefixManager, VariableGenerator varGen,
			Term glueVariable) {
		this.prefixManager = prefixManager;
		this.varGen = varGen;
		this.glueVariable = glueVariable;
	}

	// /////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public InMemoryAtomSet visit(OWLClass arg) {
		Predicate p = GraalUtils.createPredicate(arg);
		Atom a = DefaultAtomFactory.instance().create(p, glueVariable);
		return GraalUtils.createAtomSet(a);
	}

	@Override
	public InMemoryAtomSet visit(OWLObjectIntersectionOf arg) {
		InMemoryAtomSet atomset = GraalUtils.createAtomSet();
		for (OWLClassExpression c : arg.getOperands()) {
			atomset.addAll(c.accept(this));
		}
		return atomset;
	}

	//@Override
	public InMemoryAtomSet visit(OWLObjectSomeValuesFrom arg) {
		Term newGlueVariable = varGen.getFreshSymbol();

		InMemoryAtomSet atomset = arg.getProperty().accept(
				new OWLPropertyExpressionVisitorImpl(glueVariable,
						newGlueVariable));

		if (!arg.getFiller().equals(DF.getOWLThing())) {
			atomset.addAll(arg.getFiller().accept(
					new OWLEquivalentClassExpressionVisitorImpl1(
							this.prefixManager, varGen, newGlueVariable)));
		}

		return atomset;
	}

	@Override
	public InMemoryAtomSet visit(OWLDataSomeValuesFrom arg) {
		Variable newGlueVariable = varGen.getFreshSymbol();

		InMemoryAtomSet atomset = arg.getProperty().accept(
				new OWLPropertyExpressionVisitorImpl(glueVariable,
						newGlueVariable));

		if (!arg.getFiller().equals(DF.getTopDatatype())) {
			atomset.addAll(arg.getFiller().accept(
					new OWLEquivalentDataRangeVisitorImpl(newGlueVariable)));
		}

		return atomset;
	}

	@Override
	public InMemoryAtomSet visit(OWLObjectHasValue arg) {
		return arg.getProperty()
				.accept(new OWLPropertyExpressionVisitorImpl(glueVariable,
						GraalUtils.createTerm(arg.getFiller())));

	}

	@Override
	public InMemoryAtomSet visit(OWLDataHasValue arg) {
		return arg.getProperty().accept(
				new OWLPropertyExpressionVisitorImpl(glueVariable, GraalUtils
						.createLiteral(arg.getFiller())));
	}

	@Override
	public InMemoryAtomSet visit(OWLObjectHasSelf arg) {
		return arg.getProperty()
				.accept(new OWLPropertyExpressionVisitorImpl(glueVariable,
						glueVariable));
	}

	@Override
	public InMemoryAtomSet objectMinCardinality0(OWLObjectMinCardinality arg) {
		InMemoryAtomSet atomset = GraalUtils.createAtomSet();
		atomset.add(new DefaultAtom(Predicate.TOP, glueVariable));
		return atomset;
	}

	@Override
	public InMemoryAtomSet dataMinCardinality0(OWLDataMinCardinality arg) {
		InMemoryAtomSet atomset = GraalUtils.createAtomSet();
		atomset.add(DefaultAtomFactory.instance().getTop());
		return atomset;
	}

	@Override
	public InMemoryAtomSet objectMinCardinality1(OWLObjectMinCardinality arg) {
		Term newGlueVariable = varGen.getFreshSymbol();

		InMemoryAtomSet atomset = arg.getProperty().accept(
				new OWLPropertyExpressionVisitorImpl(glueVariable,
						newGlueVariable));

		atomset.addAll(arg.getFiller().accept(
				new OWLEquivalentClassExpressionVisitorImpl1(this.prefixManager,
						varGen, newGlueVariable)));

		return atomset;
	}

	@Override
	public InMemoryAtomSet dataMinCardinality1(OWLDataMinCardinality arg) {
		Variable newGlueVariable = varGen.getFreshSymbol();

		InMemoryAtomSet atomset = arg.getProperty().accept(
				new OWLPropertyExpressionVisitorImpl(glueVariable,
						newGlueVariable));

		atomset.addAll(arg.getFiller().accept(
				new OWLEquivalentDataRangeVisitorImpl(newGlueVariable)));

		return atomset;
	}

	@Override
	public InMemoryAtomSet objectOneOf1(OWLIndividual i) {
		InMemoryAtomSet atomset = GraalUtils.createAtomSet();
		atomset.add(DefaultAtomFactory.instance().create(Predicate.EQUALITY, glueVariable,
				GraalUtils.createTerm(i)));
		return atomset;
	}

}




