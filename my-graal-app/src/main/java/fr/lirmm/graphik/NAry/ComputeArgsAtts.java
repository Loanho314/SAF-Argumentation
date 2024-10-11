package fr.lirmm.graphik.NAry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.core.DefaultConjunctiveQuery;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;
import fr.lirmm.graphik.NAry.DungAF;

public class ComputeArgsAtts {
	//	private static String file = "bio-bench.dlgp";
	//	private static String file = "KB1.dlgp";
	//	private static String file = "kowalski1.dlgp";
	//	private static String file = "/Users/tho310/Data test/LUMP Example.dlgp";
	//private static String file = "/Users/tho310/Data test/BIO benchmark.dlgp";
	private static String file = "/Users/tho310/Data test/TestDurum.dlgp";
	//private static String file = "/Users/tho310/Desktop/Jar file/Bio example.dlgp";
	private static boolean haveParameters = true;	
	public static InMemoryAtomSet bottomAtomset = new LinkedListAtomSet();
	public static InMemoryAtomSet equalityAtomset = new LinkedListAtomSet();
	public static ConjunctiveQuery bottomQuery = new DefaultConjunctiveQuery(bottomAtomset, Collections.emptyList());
	private static DungAF af;
	private static ArrayList<ArrayList<Argument>> extensions;
	private static ArrayList<Argument> ext;
	private static ArrayList<Attack> Attacks;
	//public static ArrayList<Distance> newDist;
	private static ArrayList<Attack> Visited0;
	private static ArrayList<Attack> Visited;
	private static ArrayList<Argument> Reach;
	private static ArrayList<Distance> Dist;
	private static ArrayList<Distance> NewDist;
	private static Map<Argument, ArrayList<Argument>> adjacencyList;
	//	public static ArrayList<Argument> Path;
	private static ArrayList<Argument> ListArgument;
	private static List<List<Argument>> tree;
	private static String filePath = null;

	//	private static String filePath = "/Users/tho310/Data test/LUMP Preferences.dlgp";
	//private static String filePath = "/Users/tho310/Desktop/Jar file/BioPriority.dlgp";



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.nanoTime();

		if ((haveParameters) && (args.length != 0)) {
			file = args[0];
		}
		/*else if (haveParameters) {
			System.out.println("Wrong parameters, please give a DLGP file");
			System.exit(0);
		}*/

		try
		{
			DefeasibleKB kb = new DefeasibleKB(file);
			DefeasibleKB kb1 = new DefeasibleKB(file);
			DefeasibleKB kb2 = new DefeasibleKB();
			AtomSet InitialFacts = new LinkedListAtomSet();		
			RuleSet negativeruleset = new LinkedListRuleSet();
			RuleSet positiveruleset = new LinkedListRuleSet();
			RuleSet rules = new LinkedListRuleSet();
			RuleSet functionalruleset = new LinkedListRuleSet();
			RuleSet ruleset = new LinkedListRuleSet();			
			InitialFacts.addAll(kb.facts);

			/*	CloseableIterator<Atom> inte = (CloseableIterator<Atom>) InitialFacts.iterator();
			while (inte.hasNext()) {	
				String inthu = new String();
				inthu = AtomWithoutArity(inte.next());
				System.out.println(inthu);
			}*/


			// Read all prioritized instances
			//	System.out.println ("Initial: " + InitialFacts);
			CloseableIterator<Atom> iter1 = InitialFacts.iterator();
			//while(iter1.hasNext()) {
			//	Atom atom = iter1.next();
			//	System.out.println(atom);
			//}

			if (filePath != null) {
				HashMap<String, Integer> preAtoms = App1.readPreferenceAtoms(filePath, InitialFacts);
				System.out.println(preAtoms);
			}

			//read all rules
			negativeruleset = kb.negativeConstraintSet;	
			//System.out.println(negativeruleset);					
			positiveruleset = kb.rules;

			// get functional rules (equality-EGD) 
			Iterator ck = positiveruleset.iterator();
			while (ck.hasNext()) {
				Rule r1 = (Rule) ck.next();
				if (r1.getHead().iterator().next().getPredicate().equals(Predicate.EQUALITY)) {
					functionalruleset.add(r1);
				}
				else {
					rules.add(r1);
				}

			}

			ruleset.addAll(positiveruleset.iterator());
			ruleset.addAll(negativeruleset.iterator());	


			// create another KB2 that is used for computing arguments
			//	System.out.println("rule set: " + ruleset);
			CloseableIterator<Atom> it = (CloseableIterator<Atom>) InitialFacts.iterator();
			while (it.hasNext()) {
				Atom a = it.next();				
				kb2.addAtom(a);
			}
			//System.out.println(kb2.facts);
			Iterator it2 = positiveruleset.iterator();
			while (it2.hasNext()) {
				Rule r2 = (Rule) it2.next();
				if (!r2.getHead().iterator().next().getPredicate().equals(Predicate.EQUALITY)) {
					kb2.addRule(r2);;
				}

			}
			//	System.out.println("kb2 rules: " + kb2.rules);
			kb2.saturate();

			kb.saturate();
			//kb1.saturateWithNegativeConstraint();
			InMemoryAtomSet saturatedAtom = new LinkedListAtomSet();			
			saturatedAtom.addAll(kb2.facts);


			// Compute a set of arguments.
			ListArgument = App1.generateArgs(kb2);
			AtomSet Test;
			for (int i = ListArgument.size() - 1; i >= 0; i--)
			{
				Test = new LinkedListAtomSet();
				for (Atom p : ((Argument)ListArgument.get(i)).getPremises()) {
					Test.add(p);
				}
				kb2.strictAtomSet = Test;
				kb2.saturate();

				if (App1.RIncosistent(kb2)) {
					ListArgument.remove(i);
				}

			}

			System.out.println(".......List of arguments.......");
			for (Argument A : ListArgument) {				
				System.out.println(A);// + " get Premises: "+ A.getPremises());
			}

			/*
			 * Compute all attackers for an argument
			 */

			//ArrayList Attacks = new ArrayList();
			Attacks = new ArrayList();

			/*compute attacks under equality rule*/
			if (!functionalruleset.isEmpty()) {

				for(Argument a: ListArgument) {
					ArrayList<Atom> supportsA = a.getPremises();
					for (Argument b : ListArgument){
						Atom conB = b.head;

						// compare conB to supportsA
						ArrayList<Argument> temp = new ArrayList<Argument>();
						if (App1.checkInequality(supportsA,conB,functionalruleset) == true) {
							temp.add(b);						
						}
						//System.out.println(a);
						// check b co trong cau truc cua mot argument khac ko? - co => ko add, ko =>add
						if (!temp.isEmpty()) {
							Attack add = new Attack(temp, a);						
							if (App1.checkAttacks(Attacks, add) == false) {
								Attacks.add(add);
							}
						}				
					}				
				}
			}

			if (!negativeruleset.isEmpty()) {
				for (Argument a : ListArgument) {
					ArrayList<Atom> supA = a.getPremises();

					for (Argument b : ListArgument) {
						if (!b.equals(a)) {	
							AtomSet u = new LinkedListAtomSet();
							for (Atom atom : supA)
								u.add(atom);
							u.add(b.head);
							kb.facts = u;
							kb.saturateWithNegativeConstraint();
							InMemoryAtomSet newSaturatedAtoms = new LinkedListAtomSet();
							newSaturatedAtoms.addAll(kb.facts);
							Boolean inconsistent = App1.RIncosistent(kb);
							if (inconsistent) {
								ArrayList<Argument> temp = new ArrayList<>();
								temp.add(b);
								Attack at = new Attack(temp, a);						

								Attacks.add(at);

							}
						}
					}
				}	
			}




			System.out.println("...............");
			Iterator At = Attacks.iterator();
			while(At.hasNext()){
				Attack a = (Attack)((Iterator)At).next();
				System.out.println(a);
			}
			System.out.println("There are " + ListArgument.size() + " arguments and " + Attacks.size() + " attacks."); 
		//	System.out.println("There are " + ListArgument.size()); 



		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IteratorException e) {
			e.printStackTrace();
		}
		catch (AtomSetException e) {
			e.printStackTrace();
		}
		catch (ChaseException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}


		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println(duration / 1000000L + " ms");
	}

}
