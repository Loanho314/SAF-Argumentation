package fr.lirmm.graphik.NAry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.tweetyproject.graphs.HyperDirEdge;
import org.tweetyproject.graphs.HyperGraph;
import org.tweetyproject.graphs.util.GraphUtil;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.ArgumentationFramework.Argument;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentNode;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentTree;
import fr.lirmm.graphik.NAry.ArgumentationFramework.Attack;
import fr.lirmm.graphik.graal.api.backward_chaining.QueryRewriter;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.backward_chaining.pure.PureRewriter;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.homomorphism.SmartHomomorphism;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;
import fr.lirmm.graphik.util.stream.IteratorException;

public class Experiment1 {

	// static private String file = "C:/Users/tho310/Data
	// test/DBpedia/3Classes-1DisjointWith.dlgp";
	// static private String file = "C:/Users/tho310/Data
	// test/Ex-two-variables.dlgp";
	// static private String file = "C:/Users/tho310/Data test/Lum test.dlgp";
	static private String file = "C:/Users/tho310/Data test/16facts-DBpedia.dlgp";
	public static ArrayList<Argument> listArguments;
	// Initialise an attack set
	public static Set<Attack> attackSet;

	private static int count;

	public static <T> void main(String[] args)
			throws AtomSetException, ChaseException, HomomorphismException, IOException {
		// TODO Auto-generated method stub
		DefeasibleKB kb = new DefeasibleKB(file);
		DefeasibleKB kbArgs = new DefeasibleKB(file);
		AtomSet initialFacts = new LinkedListAtomSet();
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();
		InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();

		attackSet = new HashSet<Attack>();

		kbArgs.saturate();
		saturatedAtoms.addAll(kbArgs.facts);
		// System.out.println("Saturated Facts: " + saturatedAtoms);
		// kbArgs.unsaturate();

		// System.out.println("Chase" + saturatedAtoms);

		initialFacts.addAll(kb.facts);
		// System.out.println("Facts: " + initialFacts);
		negativeRuleSet = kb.negativeConstraintSet;
		// System.out.println("Negative rules:" + negativeRuleSet);
		positiveRuleSet = kb.rules;
		// System.out.println("Positive rules:" + positiveRuleSet);

		// Generate arguments deduced from a KB
		// ArrayList<Argument> listArguments = new ArrayList<Argument>();

		long startTime = System.currentTimeMillis(); // Get the start time
		listArguments = App1.generateArgs(kbArgs);
		// kb.unsaturate();

		// Check whether premises of arguments are consistent
		// If Yes, remove it from ListArgument, otherwise, keep it.

		AtomSet Test;
		for (int i = listArguments.size() - 1; i >= 0; i--) {
			Test = new LinkedListAtomSet();
			for (Atom p : ((Argument) listArguments.get(i)).getPremises()) {
				Test.add(p);
			}
			kbArgs.strictAtomSet = Test;
			if (App1.RIncosistent(kbArgs)) {
				listArguments.remove(i);
			}
		}

		long endTime = System.currentTimeMillis(); // Get the end time
		long duration = endTime - startTime; // Calculate the duration

		// Compute attacks for arguments

		// ArrayList<AtomSet> allMinimalConflicts = new ArrayList<AtomSet>();
		// allMinimalConflicts = FindMinIncSets.findMinimalIncSubsets(kb);
		// System.out.println("All minimal conflicts: " + allMinimalConflicts);

		/*
		 * String queryString = "? :- postdoc(ann)."; ConjunctiveQuery query =
		 * DlgpParser.parseQuery(queryString); AtomSet qAtom = query.getAtomSet();
		 * ArrayList<ArrayList<Argument>> proArgs = App1.getArgsForAtomSet(qAtom,
		 * listArguments);
		 * 
		 * System.out.println("Proponent arguments: " + proArgs); Argument argRoot =
		 * proArgs.get(0).get(0); System.out.println(argRoot);
		 * 
		 */

		Set<ArgumentTree> trees = new HashSet<ArgumentTree>();
		ArrayList<Integer> heights = new ArrayList<Integer>();

		// compute all minimal conflicts
		ArrayList<AtomSet> minInconSets = FindMinIncSets.findMinimalIncSubsets(kb);
		// System.out.println("minimal inconsistent subsets: \n " + minInconSets + " \n
		// size: " + minInconSets.size());

		int number = 0;
		long startTime1 = System.currentTimeMillis();
		for (Argument argRoot : listArguments) {
			number++;
			if (number % 100 == 0) {
				System.out.println("Running so far " + number);
			}

			Set<ArgumentTree> argTrees = new HashSet<ArgumentTree>();
			argTrees = getArgumentTree(argRoot, minInconSets);
			trees.addAll(argTrees);
		}

		long endTime1 = System.currentTimeMillis();
		long duration1 = endTime - startTime;

		FileWriter writer = null;
		try {
			writer = new FileWriter("execution_time.txt", true); // Append mode
			for (ArgumentTree t : trees) {
				writer.write("\n Argument tree for " + t.getRoot() + "\n");
				// System.out.println(" \n Argument tree for " + t.getRoot());
				writer.write(t.toString());
				// System.out.println(t);
				writer.write("Nodes: " + t.getNumberOfNodes() + " \n Edges: " + t.getNumberOfEdges() + "\n");
				// System.out.println("Nodes: " + t.getNumberOfNodes() + " \n Edges: " +
				// t.getNumberOfEdges() + "\n");
				writer.write("Height: " + t.getHeight() + "\n");
				// System.out.println("Height: " + t.getHeight());
				heights.add(t.getHeight());
				writer.write("Width: " + t.getMaxWidth() + "\n");
				// System.out.println("Width: " + t.getMaxWidth());
				// t.printTree(t.getRoot());
				// System.out.println();
			}

			writer.write("Number of trees " + trees.size() + "\n");
			writer.write("Time for computing trees " + duration1 + "\n");

			int[] result = findMaxAndMin(heights);
			writer.write("Max: " + result[0] + "\n");
			writer.write("Min: " + result[1] + "\n");

			double averageHeight = calculateAverageHeight(heights);
			writer.write("Average Tree Height: " + averageHeight + "\n");

			System.out.println("Number of trees " + trees.size());
			// System.out.println("Max: " + result[0]);
			// System.out.println("Min: " + result[1]);

			// System.out.println("Average Tree Height: " + averageHeight);

			// System.out.println(".......List of arguments.......");
			// for (Argument A : listArguments) {
			// System.out.println(A);
			// }
			// Execution time in nanoseconds
			writer.write("Number of args: " + listArguments.size() + " - Time for translating arguemnts: " + duration + "\n");
			writer.write("Attacks: " + attackSet + " " + attackSet.size());
		} finally {
			// close resources
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * for (Argument argRoot : listArguments) {
	 * 
	 * // return a set of AtomSet that includes arg Set<AtomSet> firstLevelSets =
	 * new HashSet<AtomSet>(); firstLevelSets = firstLevel(argRoot, minInconSets);
	 * System.out.println("first level conflict sets: " + firstLevelSets + "\n");
	 * 
	 * for (AtomSet set : firstLevelSets) { count = 0;
	 * 
	 * // initialise an argument tree for a given argument (root). ArgumentNode root
	 * = new ArgumentNode(argRoot); ArgumentTree argTree = new ArgumentTree(root);
	 * argTree.add(root);
	 * 
	 * // Construct Atoms of Undercut (Counter-Argument) for root. Set<Atom>
	 * undercutAtoms = new HashSet<Atom>();
	 * 
	 * // Convert AtomSet to Set<Atom> CloseableIterator<Atom> it = set.iterator();
	 * while (it.hasNext()) { undercutAtoms.add(it.next()); }
	 * 
	 * // remove all atoms in the root from the current minimal conflicts. for (Atom
	 * premise : argRoot.getPremises()) { undercutAtoms.remove(premise); }
	 * 
	 * // Get arguments that have the atom of "undercut" in their premises
	 * ArrayList<ArrayList<Argument>> setOfUndercuts = new
	 * ArrayList<ArrayList<Argument>>();
	 * setOfUndercuts.addAll(getArgsForAtomSet1(undercutAtoms, listArguments));
	 * 
	 * // Create and Add the Undercut Node: for (ArrayList<Argument> undercut :
	 * setOfUndercuts) { count = 1;
	 * 
	 * // Initialise a defence set and a culprit Set<Argument> defenceSet = new
	 * HashSet<Argument>(); Set<Argument> culprit = new HashSet<Argument>();
	 * defenceSet.add(argRoot); culprit.addAll(undercut); // add undercut to culprit
	 * // System.out.println("Defence set: " + defenceSet); //
	 * System.out.println("Culprit: " + culprit + " " + count);
	 * 
	 * // Process undercuts and ensure undercutNodes are only added once
	 * Set<ArgumentNode> undercutNodes = new HashSet<ArgumentNode>(); for (Argument
	 * argument : undercut) { ArgumentNode newNode = new ArgumentNode(argument);
	 * argTree.add(newNode); // Add the new node to the tree
	 * undercutNodes.add(newNode); // Add node to the set to create hyperedegs }
	 * 
	 * // Create hyperedges String label = ".."; argTree.add(new
	 * HyperDirEdge<ArgumentNode>(undercutNodes, root, label));
	 * 
	 * // add to the attack set. Attack attack = new Attack(undercut, argRoot);
	 * attackSet.add(attack);
	 * 
	 * // Work with remaining sets, but avoid modifying the original list
	 * ArrayList<AtomSet> remainingMins = new ArrayList<>(minInconSets);
	 * remainingMins.remove(set);
	 * 
	 * for (ArgumentNode node : undercutNodes) { subcuts(node, remainingMins, set,
	 * new HashSet<>(root.getPremises()), argTree, defenceSet, culprit); //
	 * subcuts(node, remainingMins, set, new HashSet<>(node.getPremises()), //
	 * argTree); } }
	 * 
	 * System.out.println(" \n Argument tree for " + argTree.getRoot());
	 * trees.add(argTree); System.out.println(argTree); System.out.println(
	 * "Nodes: " + argTree.getNumberOfNodes() + " \n Edges: " +
	 * argTree.getNumberOfEdges() + "\n"); System.out.println("Height: " +
	 * argTree.getHeight()); heights.add(argTree.getHeight());
	 * argTree.printTree(argTree.getRoot()); System.out.println();
	 * 
	 * } }
	 */

	// function finds an average height of argumentation trees

	public static double calculateAverageHeight(ArrayList<Integer> heights) {
		if (heights == null || heights.isEmpty()) {
			throw new IllegalArgumentException("List cannot be null or empty");
		}

		int sum = 0;
		for (int height : heights) {
			sum += height;
		}

		return (double) sum / heights.size();
	}

	/**
	 * 
	 * @param list of heights of argumentation trees
	 * @return maximal and minimal heights.
	 */

	public static int[] findMaxAndMin(ArrayList<Integer> list) {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("List cannot be null or empty");
		}

		int max = Collections.max(list);
		int min = Collections.min(list);

		return new int[] { max, min };
	}

	/**
	 * 
	 * @param an   argument
	 * @param myID
	 * @return the argument tree for a given argument
	 * @throws IteratorException
	 */

	public static Set<ArgumentTree> getArgumentTree(Argument argRoot, ArrayList<AtomSet> minInconSets)
			throws IteratorException {

		// return a set of AtomSet that includes arg
		Set<AtomSet> firstLevelSets = new HashSet<AtomSet>();
		firstLevelSets = firstLevel(argRoot, minInconSets);
		Set<ArgumentTree> argTrees = new HashSet<>();

		for (AtomSet set : firstLevelSets) {
			count = 0;

			// initialise an argument tree for a given argument (root).
			ArgumentNode root = new ArgumentNode(argRoot);
			ArgumentTree argTree = new ArgumentTree(root);
			argTree.add(root);

			// Construct Atoms of Undercut (Counter-Argument) for root.
			Set<Atom> undercutAtoms = new HashSet<Atom>();

			// Convert AtomSet to Set<Atom>
			CloseableIterator<Atom> it = set.iterator();
			while (it.hasNext()) {
				undercutAtoms.add(it.next());
			}

			// remove all atoms in the root from the current minimal conflicts.
			for (Atom premise : argRoot.getPremises()) {
				undercutAtoms.remove(premise);
			}

			// Get arguments that have the atom of "undercut" in their premises
			ArrayList<ArrayList<Argument>> setOfUndercuts = new ArrayList<ArrayList<Argument>>();
			setOfUndercuts.addAll(getArgsForAtomSet1(undercutAtoms, listArguments));

			// Create and Add the Undercut Node:
			for (ArrayList<Argument> undercut : setOfUndercuts) {
				count = 1;

				// Initialise a defence set and a culprit
				Set<Argument> defenceSet = new HashSet<Argument>();
				Set<Argument> culprit = new HashSet<Argument>();
				defenceSet.add(argRoot);
				culprit.addAll(undercut); // add undercut to culprit

				// Process undercuts and ensure undercutNodes are only added once
				Set<ArgumentNode> undercutNodes = new HashSet<ArgumentNode>();
				for (Argument argument : undercut) {
					ArgumentNode newNode = new ArgumentNode(argument);
					argTree.add(newNode); // Add the new node to the tree
					undercutNodes.add(newNode); // Add node to the set to create hyperedegs
				}

				// Create hyperedges
				String label = "..";
				argTree.add(new HyperDirEdge<ArgumentNode>(undercutNodes, root, label));

				// add to the attack set.
				Attack attack = new Attack(undercut, argRoot);
				attackSet.add(attack);

				// Work with remaining sets, but avoid modifying the original list
				ArrayList<AtomSet> remainingMins = new ArrayList<>(minInconSets);
				remainingMins.remove(set);

				for (ArgumentNode node : undercutNodes) {
					subcuts(node, remainingMins, set, new HashSet<>(root.getPremises()), argTree, defenceSet, culprit);
				}
			}
			argTrees.add(argTree);

		}
		return argTrees;
	}

	/*
	 * private static ArgumentNode findOrCreateNode(Argument argument, ArgumentTree
	 * argTree) { for (ArgumentNode node : argTree.getNodes()) { if (node.getID() ==
	 * argument.myID) { return node; // Return existing node if found } }
	 * ArgumentNode newNode = new ArgumentNode(argument); argTree.add(newNode); //
	 * Add the new node to the tree return newNode; // Return the newly created node
	 * }
	 */

	private static ArgumentNode findNodeByID(ArgumentTree argTree, int myID) {
		for (ArgumentNode node : argTree.getNodes()) {
			if (node.getID() == myID) {
				return node; // Return the node if found
			}
		}
		return null; // Return null if not found
	}

	/**
	 * This method returns the minimal conflicts that can be used to construct
	 * undercuts to the given argument.
	 * 
	 * @param arg some argument.
	 * @return a set of minimal conflicts.
	 * @throws IteratorException
	 */
	private static Set<AtomSet> firstLevel(Argument arg, ArrayList<AtomSet> allMins) throws IteratorException {
		Stack<AtomSet> candidates = new Stack<AtomSet>();
		for (AtomSet min : allMins) {
			ArrayList<Atom> set = new ArrayList<Atom>();

			CloseableIterator<Atom> it = min.iterator();
			while (it.hasNext()) {
				set.add(it.next());
			}

			set.retainAll(arg.getPremises());
			if (!set.isEmpty())
				candidates.add(min);
		}
		Set<AtomSet> result = new HashSet<AtomSet>();
		while (!candidates.isEmpty()) {
			AtomSet element = candidates.pop();
			boolean addToResult = true;
			for (AtomSet element2 : candidates) {
				Set<Atom> set1 = new HashSet<Atom>();
				Set<Atom> set2 = new HashSet<Atom>();

				// Convert AtomSet to Set<Atom>

				CloseableIterator<Atom> it = element.iterator();
				while (it.hasNext()) {
					set1.add(it.next());
				}

				CloseableIterator<Atom> it2 = element2.iterator();
				while (it2.hasNext()) {
					set1.add(it2.next());
				}

				set1.removeAll(arg.getPremises());
				set2.removeAll(arg.getPremises());
				if (set2.containsAll(set1)) {
					addToResult = false;
					break;
				}
			}
			if (addToResult)
				for (AtomSet element2 : result) {
					Set<Atom> set1 = new HashSet<Atom>();
					Set<Atom> set2 = new HashSet<Atom>();

					// Convert AtomSet to Set<Atom>

					CloseableIterator<Atom> it = element.iterator();
					while (it.hasNext()) {
						set1.add(it.next());
					}

					CloseableIterator<Atom> it2 = element2.iterator();
					while (it2.hasNext()) {
						set1.add(it2.next());
					}

					set1.removeAll(arg.getPremises());
					set2.removeAll(arg.getPremises());
					if (set2.containsAll(set1)) {
						addToResult = false;
						break;
					}
				}
			if (addToResult)
				result.add(element);
		}
		return result;
	}

	/**
	 * This method return sets of arguments whose the supports equal to a set of
	 * atoms
	 * 
	 */

	public static ArrayList<ArrayList<Argument>> getArgsForAtomSet1(Set<Atom> set, ArrayList<Argument> listOfArgs)
			throws IteratorException {
		ArrayList<ArrayList<Argument>> result = new ArrayList<ArrayList<Argument>>();
		for (Atom a : set) {

			for (int i = 0; i < listOfArgs.size(); i++) {
				ArrayList<Argument> argGroup = new ArrayList<Argument>();
				Argument arg = listOfArgs.get(i);
				if (arg.head.equals(a)) {
					argGroup.add(arg);
					result.add(argGroup);
				}
			}
		}
		return result;
	}

	/**
	 * This method recursively builds up the argument tree from the given argument.
	 * 
	 * @param argNode        an argument.
	 * @param remainingNodes the non-visited minimal conflict sets.
	 * @param current        the current node.
	 * @param currentSupport the union of the supports of the current path.
	 * @param argTree        the argument tree.
	 * @throws IteratorException
	 */
	private static void subcuts(ArgumentNode currentNode, ArrayList<AtomSet> remainingMins, AtomSet current,
			Set<Atom> supportOfCurrentNode, ArgumentTree argTree, Set<Argument> defenceSet, Set<Argument> culprit)
			throws IteratorException {

		for (AtomSet min : remainingMins) {

			// Convert Atomset to a Set to optimize intersection check
			Set<Atom> minSet = new HashSet<Atom>();
			CloseableIterator<Atom> it = min.iterator();
			while (it.hasNext()) {
				minSet.add(it.next());
			}

			// check current and node have a non-empty intersection
			if (hasNonEmptyIntersection(current, minSet)) {

				// Check whether the support of the current node is present in the MinSets.
				if (!supportOfCurrentNode.containsAll(minSet)) {
					Set<Atom> set = new HashSet<Atom>(currentNode.getPremises());
					// System.out.println("SET: " + set);
					set.retainAll(minSet);
					// System.out.println("SET after retain " + set);

					if (!set.isEmpty()) {
						boolean properUndercut = true;

						/**
						 * The idea is to ensure that the current set (current) is not contradicted by
						 * other supporting evidence elsewhere, which would invalidate the undercut.
						 */

						for (AtomSet min2 : remainingMins) {
							if (!min2.equals(min) && current.equals(min2)) {
								// convert AtomSet to a Set to optimize intersection check
								Set<Atom> minSet2 = new HashSet<Atom>();
								CloseableIterator<Atom> it2 = min2.iterator();
								while (it2.hasNext()) {
									minSet2.add(it2.next());
								}

								Set<Atom> set1 = new HashSet<Atom>(minSet);
								Set<Atom> set2 = new HashSet<Atom>(minSet2);
								set1.retainAll(currentNode.getPremises());
								set2.retainAll(currentNode.getPremises());
								if (set1.containsAll(set2)) {
									properUndercut = false;
									System.out.println("BREAK");
									break;
								}
							}
						}

						if (properUndercut) {
							count++;
							Set<Atom> atomsUndercut = new HashSet<Atom>(minSet);
							atomsUndercut.removeAll(currentNode.getPremises());
							// System.out.println("SUPPORT of undercutNode of " + currentNode + " " +
							// atomsUndercut);

							// get a set of arguments that collectively attacks argNode
							ArrayList<ArrayList<Argument>> undercuts = new ArrayList<ArrayList<Argument>>();
							undercuts = getArgsForAtomSet1(atomsUndercut, listArguments);
							// System.out.println("UNDERCUTS: " + undercuts);

							// create argumentNodes and hyperdeges of the argTree.
							for (ArrayList<Argument> undercut : undercuts) {
								Set<ArgumentNode> undercutNodes = new HashSet<ArgumentNode>();

								// System.out.println("Count" + count);
								if (count % 2 != 0 && defenceSet.containsAll(undercut)) {
									// System.out.println("These arguments exist in the defence set.");
									break;
								}

								// add nodes
								for (Argument arg : undercut) {
									ArgumentNode newNode = new ArgumentNode(arg);
									undercutNodes.add(newNode);
									argTree.add(newNode);
								}
								// add hyperedges
								String label2 = "...";
								argTree.add(new HyperDirEdge<ArgumentNode>(undercutNodes, currentNode, label2));
								// System.out.println("NODES:" + argTree.getNodes());
								// System.out.println("HYPEREDGES:" + argTree.getEdges());

								// System.out.println("UDERCUT NODE " + undercutNodes);

								// add to the culprit or the defence set
								int currentHeight = argTree.getHeight();
								for (ArgumentNode node : undercutNodes) {

									if (currentHeight % 2 == 0) {
										defenceSet.add(returnArgForNode(node, listArguments));
										// System.out.println("New DEFECE SET: " + defenceSet + " " + currentHeight);
										// System.out.println("Culprit" + culprit);
									} else {
										culprit.add(returnArgForNode(node, listArguments));
										// System.out.println("New CULPRIT: " + culprit + " " + currentHeight);
										// System.out.println("defence set" + defenceSet);
									}
								}

								// add to the attack set.
								Argument currentArg = returnArgForNode(currentNode, listArguments);
								Attack attack = new Attack(undercut, currentArg);
								attackSet.add(attack);

								ArrayList<AtomSet> newRemainingMins = new ArrayList<AtomSet>(remainingMins);
								newRemainingMins.remove(min);
								// System.out.println("NEW REMAININGMINS: " + newRemainingMins);

								for (ArgumentNode node : undercutNodes) {
									Set<Atom> newSupport = new HashSet<Atom>(atomsUndercut); // Create the new support
									newSupport.addAll(node.getPremises());
									subcuts(node, newRemainingMins, min, newSupport, argTree, defenceSet, culprit);
								}
							}
						} // End IF

					} // End IF

				} // End IF

			} // End FOR
		}
	}

	public static boolean hasNonEmptyIntersection(AtomSet set1, Set<Atom> set2) throws IteratorException {
		// Convert one of the LinkedLists to a Set to optimize intersection check
		Set<Atom> set1AsSet = new HashSet<>();
		CloseableIterator<Atom> it1 = set1.iterator();
		while (it1.hasNext()) {
			set1AsSet.add(it1.next());
		}

		Set<Atom> temp = new HashSet<>(set1AsSet);

		// Retain elements that are common in both sets
		temp.retainAll(set2);

		// Check if the intersection is non-empty
		return !temp.isEmpty();
	}

	private static Argument returnArgForNode(ArgumentNode node, ArrayList<Argument> listOfArgs) {
		Argument result = new Argument();
		for (Argument arg : listOfArgs) {
			if (arg.myID == node.getID()) {
				result = arg;
				break;
			}
		}
		return result;
	}

	/*
	 * for (Argument arg : undercut) { boolean nodeExists = false; // Reset
	 * nodeExists for each argument
	 * 
	 * /** Check whether an argument from undercut is present in tree. If yes, then
	 * add it to undercutNodes to create hyperdeges. If No, then create a new
	 * ArgumentNode and add it to both of the tree and undercutNodes to create
	 * hyperedges.
	 */

	/*
	 * for (ArgumentNode node : argTree.getNodes()) { if (node.getID() == arg.myID)
	 * { System.out.println("NODE IN Tree: " + node); undercutNodes.add(node);
	 * nodeExists = true; break; } }
	 * 
	 * if (!nodeExists) { System.out.println("NOT IN Tree - Argument " + arg);
	 * ArgumentNode newNode = new ArgumentNode(arg); undercutNodes.add(newNode);
	 * nodesToAdd.add(newNode); } }
	 */

	/*
	 * for (ArgumentNode newNode : nodesToAdd) { argTree.add(newNode); // Add the
	 * new node to the tree }
	 */

}
