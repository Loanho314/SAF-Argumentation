package fr.lirmm.graphik.NAry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.setaf.reasoners.SimpleAdmissibleSetAfReasoner;
import org.tweetyproject.arg.setaf.reasoners.SimpleGroundedSetAfReasoner;
import org.tweetyproject.arg.setaf.reasoners.SimplePreferredSetAfReasoner;
import org.tweetyproject.arg.setaf.syntax.SetAf;
import org.tweetyproject.arg.setaf.syntax.SetAttack;
import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.ArgumentationFramework.StructuredArgument;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentTree;
import fr.lirmm.graphik.NAry.ArgumentationFramework.Attack;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;

public class Experiment3 {
	static private String file = "C:/Users/tho310/Data test/test2.dlgp";
	// static private String file = "C:/Users/tho310/Data
	// test/DBpedia/55Classes-4Conflicts-5997.dlgp";
	public static ArrayList<StructuredArgument> listArguments;
	//// Initialise an attack set
	public static Set<Attack> attackSet;

	public static <T> void main(String[] args)
			throws AtomSetException, ChaseException, HomomorphismException, IOException {

		DefeasibleKB kb = new DefeasibleKB(file);
		DefeasibleKB kbArgs = new DefeasibleKB(file);
		AtomSet initialFacts = new LinkedListAtomSet();
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();
		InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();

		kbArgs.saturate();
		saturatedAtoms.addAll(kbArgs.facts);
		// System.out.println("Saturated Facts: " + saturatedAtoms);
		// kbArgs.unsaturate();
		// System.out.println("Chase" + saturatedAtoms);

		initialFacts.addAll(kb.facts);
		negativeRuleSet = kb.negativeConstraintSet;
		positiveRuleSet = kb.rules;

		listArguments = App1.generateArgs(kbArgs);

		// Check whether premises of arguments are consistent
		// If Yes, remove it from ListArgument, otherwise, keep it.

		AtomSet Test;
		for (int i = listArguments.size() - 1; i >= 0; i--) {
			Test = new LinkedListAtomSet();
			for (Atom p : ((StructuredArgument) listArguments.get(i)).getPremises()) {
				Test.add(p);
			}
			kbArgs.strictAtomSet = Test;
			if (App1.RIncosistent(kbArgs)) {
				listArguments.remove(i);
			}
		}

		System.out.println("Argument: " + listArguments);

		for (StructuredArgument a : listArguments) {
			List<StructuredArgument> allArgs = findAllArguments(a);
			System.out.println(a + " Body: " + allArgs);
		}

		Set<ArgumentTree> trees = new HashSet<ArgumentTree>();

		// compute all minimal conflicts
		// ArrayList<AtomSet> minInconSets = FindMinIncSets.findMinimalIncSubsets(kb);
		// System.out.println("minimal inconsistent subsets: \n " + minInconSets + " \n
		// size: " + minInconSets.size());

		// attackSet = new HashSet<Attack>();
		// int number = 0;
		// for (StructuredArgument argRoot : listArguments) {
		// number++;
		// if (number % 100 == 0) {
		// System.out.println("Running so far " + number);
		// }

		// ArgumentTree tree = Experiment1.getArgumentTree(argRoot, minInconSets,
		// listArguments);
		// trees.add(tree);
		// }

		System.out.println("Attack: " + attackSet);

		SetAf s = new SetAf();
		s = convertToSetAf(listArguments, attackSet);
		SimpleGroundedSetAfReasoner gr = new SimpleGroundedSetAfReasoner();
		SimpleAdmissibleSetAfReasoner ad = new SimpleAdmissibleSetAfReasoner();
		SimplePreferredSetAfReasoner pr = new SimplePreferredSetAfReasoner();
		System.out.println("grounded: " + gr.getModel(s));
		System.out.println("admissible: " + ad.getModels(s));
		System.out.println("preferred: " + pr.getModels(s));

	}
	public static List<StructuredArgument> findAllArguments(StructuredArgument argument) {
        List<StructuredArgument> arguments = new ArrayList<>();
        findArgumentsRecursively(argument, arguments);
        return arguments;
    }

	private static void findArgumentsRecursively(StructuredArgument argument, List<StructuredArgument> argBody) {
		if (argument.body.isEmpty()) {
			return; // Base case: Stop if the argument is null (empty body)
		}
		argBody.addAll(argument.body); // Add the current argument to the list
		for (StructuredArgument arg : argument.body) {
			findArgumentsRecursively(arg, argBody); // Recursively process the body
		}
	}

	public static SetAf convertToSetAf(ArrayList<StructuredArgument> listArguments, Set<Attack> attackSet) {
		SetAf s = new SetAf();

		// add arguments to SetAf
		for (StructuredArgument a : listArguments) {
			String sArg = "a" + a.myID;
			Argument arg = new Argument(sArg);
			s.add(arg);
		}

		// add attacks
		Collection<Argument> argsInSaf = s.getNodes();
		for (Attack att : attackSet) {
			Set<Argument> source = new HashSet<Argument>();
			for (StructuredArgument b : att.source) {
				String bString = "a" + b.myID;
				Argument bArg = new Argument(bString);
				if (s.getNodes().contains(bArg))
					source.add(bArg);
			}
			// tim arguments in saf, assign with target.
			String targetString = "a" + att.target.myID;
			Argument target = new Argument(targetString);
			s.add(new SetAttack(source, target));
		}
		return s;
	}
}
