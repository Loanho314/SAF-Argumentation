package dialogue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.tweetyproject.arg.dung.syntax.Argument;
import org.tweetyproject.arg.setaf.reasoners.SimpleGroundedSetAfReasoner;
import org.tweetyproject.arg.setaf.reasoners.SimplePreferredSetAfReasoner;
import org.tweetyproject.arg.setaf.syntax.SetAf;
import org.tweetyproject.arg.setaf.syntax.SetAttack;
import org.tweetyproject.arg.dung.semantics.Extension;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.App1;
import fr.lirmm.graphik.NAry.Experiment1;
import fr.lirmm.graphik.NAry.FindMinIncSets;
import fr.lirmm.graphik.NAry.computeArguments;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentTree;
import fr.lirmm.graphik.NAry.ArgumentationFramework.Attack;
import fr.lirmm.graphik.NAry.ArgumentationFramework.StructuredArgument;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;
import javafx.event.ActionEvent;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewController implements Initializable {
	public static DefeasibleKB kb;
	public static DefeasibleKB kbSaturated;
	public static AtomSet initialFacts;
	public static RuleSet negativeRules;
	public static RuleSet positiveRules;
	public static RuleSet functionalRules;
	public static Set<Atom> saturatedAtoms;
	public static Set<AtomSet> minIncSets;
	public static InMemoryAtomSet bottomAtomset = new LinkedListAtomSet();
	public static InMemoryAtomSet equalityAtomset = new LinkedListAtomSet();
	public static ArrayList<StructuredArgument> listArguments;
	public static Set<StructuredArgument> arguments;
	public static Set<Attack> attacks;
	public static Set<ArgumentTree> proTrees;
	public static Set<ArgumentTree> oppTrees;
	public static ArrayList<ArrayList<StructuredArgument>> argsForQuery;
	public static Set<AtomSet> conflicts;
	public static Set<AtomSet> causes;
	public static Boolean groundedAns;
	public static Boolean nonAns;
	public static Boolean scepticalAns;
	public static Boolean credulousAns;
	public static AtomSet atomAnswers;
	

	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Button btnGotoDia;

	@FXML
	private Button btnLoad;

	@FXML
	private Button btnRun;

	@FXML
	private Button btnSetExp;

	@FXML
	private TextArea txtKB;

	@FXML
	private TextField txtQuery;

	@FXML
	private TextArea txtResult;

	public void initialize(URL location, ResourceBundle resources) {
		txtKB.prefWidthProperty().bind(anchorPane.widthProperty().divide(2).subtract(60));
		txtResult.prefWidthProperty().bind(anchorPane.widthProperty().divide(2).subtract(60));
	}



	@FXML
	public void Load_KB(ActionEvent event)
			throws FileNotFoundException, IteratorException, AtomSetException, ChaseException, HomomorphismException {

		initialFacts = new LinkedListAtomSet();
		negativeRules = new LinkedListRuleSet();
		positiveRules = new LinkedListRuleSet();
		functionalRules = new LinkedListRuleSet();
		saturatedAtoms = new HashSet<Atom>();

		FileChooser openFileChooser = new FileChooser();
		openFileChooser.setTitle("Open File");

		FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
		openFileChooser.getExtensionFilters().add(allFilesFilter);

		// Show the file chooser dialog
		File selectedFile = openFileChooser.showOpenDialog(new Stage());

		// If a file is selected, read its content
		if (selectedFile != null) {
			// listArguments = new ArrayList<StructuredArgument>();
			attacks = new HashSet<Attack>();
			String filename = selectedFile.getAbsolutePath();

			// Load to KB
			FileReader reader = new FileReader(filename);
			kb = new DefeasibleKB(reader);
			initialFacts.addAll(kb.facts);
			negativeRules = kb.negativeConstraintSet;

			// get functional rules (equality-EGD)
			Iterator it = kb.rules.iterator();
			while (it.hasNext()) {
				Rule r1 = (Rule) it.next();
				if (r1.getHead().iterator().next().getPredicate().equals(Predicate.EQUALITY)) {
					functionalRules.add(r1);
				} else {
					positiveRules.add(r1);
				}

			}

			// Compute arguments
			FileReader reader2 = new FileReader(filename);
			kbSaturated = new DefeasibleKB(reader2);
			kbSaturated.saturate();

			try (CloseableIterator<Atom> it1 = kbSaturated.facts.iterator()) {
				while (it1.hasNext()) {
					saturatedAtoms.add(it1.next());
				}
			}

			listArguments = computeArguments.generateArgs(kbSaturated);

			AtomSet Test;
			for (int i = listArguments.size() - 1; i >= 0; i--) {
				Test = new LinkedListAtomSet();
				for (Atom p : ((StructuredArgument) listArguments.get(i)).getPremises()) {
					Test.add(p);
				}
				kbSaturated.strictAtomSet = Test;
				kbSaturated.saturate();
				if (computeArguments.RIncosistent(kbSaturated)) {
					listArguments.remove(i);
				}
			}

			for (StructuredArgument a : listArguments) {
				System.out.println(a);
			}

			// Compute minimal inconsistent sets
			minIncSets = FindMinIncSets.findMinimalIncSubsets(kb);
			System.out.println(minIncSets);

			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				String line;
				StringBuilder contents = new StringBuilder();

				// Read the file line by line
				while ((line = br.readLine()) != null) {
					contents.append(line).append("\n");
				}

				// Display the content in the TextArea
				txtKB.setText(contents.toString());

			} catch (IOException e) {
				// Show an error dialog if something goes wrong
				showAlert("Error", "An error occurred while reading the file: " + e.getMessage());
			}
		} else {
			// Clear the TextArea and show a message if no file is selected
			txtKB.setText("");
			showAlert("Information", "No file was selected.");
		}

	}

	public void computeArguments(ActionEvent event) throws IteratorException, ChaseException, AtomSetException {

		if (listArguments == null) {
			System.out.println("kbSaturated is empty or null. Please check initialization.");
		} else if (listArguments.isEmpty()) {
			System.out.println("List is empty");
		} else {
			System.out.println("List is not empty " + listArguments.size());
			for (StructuredArgument a : listArguments) {
				System.out.println("Arguments: " + a.toString());
			}
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	public void Run_Query(ActionEvent event)
			throws AtomSetException, IteratorException, ChaseException, HomomorphismException {
		// Clear the result text fields before running a new query
		txtResult.clear();

		String text = txtQuery.getText();
		boolean matchFound = false;

		if (text == null || text.trim().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Input Error");
			alert.setHeaderText("Empty Field");
			alert.setContentText("Please enter a query!");
			alert.showAndWait();
		} else if (kb == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Input Error");
			alert.setHeaderText("Empty Knowledge Base");
			alert.setContentText("Please load a knowledge base!");
			alert.showAndWait();

		} else if (text != null && kb != null && kbSaturated != null) {

			// 1. Get a query from the text field
			String queryString = null;
			// CloseableIterator<Atom> it = initialFacts.iterator();
			Iterator<Atom> it = saturatedAtoms.iterator();
			while (it.hasNext()) {
				Atom at = it.next();
				String st = App1.AtomWithoutArity(at);
				if (text.equalsIgnoreCase(st)) {
					queryString = "? :- " + st + ".";
					System.out.println("Query string " + queryString);
					matchFound = true;
					break;

				}
			}

			if (!matchFound) {
				System.out.println("No match found for text: " + text);
				// Perform another task here
			}

			// 2. Get answers for the query
			ConjunctiveQuery query = DlgpParser.parseQuery(queryString);
			atomAnswers = new LinkedListAtomSet();
			atomAnswers = query.getAtomSet();

			// 3. Get arguments for the answer
			argsForQuery = App1.getArgsForAtomSet(atomAnswers, listArguments);
			System.out.println("Args for Query: " + argsForQuery);

			// 4. Get all argument trees for all pro arguments
			causes = new HashSet<AtomSet>();
			conflicts = new HashSet<AtomSet>();
			arguments = new HashSet<StructuredArgument>();
			attacks = new HashSet<Attack>();
			proTrees = new HashSet<ArgumentTree>();

			// 5. get all argument trees for all opp arguments
			oppTrees = new HashSet<ArgumentTree>();
			ArrayList<ArrayList<StructuredArgument>> oppArguments = new ArrayList<ArrayList<StructuredArgument>>();

			for (ArrayList<StructuredArgument> setOfProArgs : argsForQuery) {
				for (StructuredArgument proArgument : setOfProArgs) {

					// Compute support of the current argument that includes facts
					AtomSet support = computeSupportWithFacts(proArgument, initialFacts);
					causes.add(support);
					System.out.println("causes " + causes);

					// compute opponent arguments
					// 5.1 Get minimal conflicts for each proponent argument
					Set<AtomSet> conflictsForProArg = Experiment1.firstLevel(proArgument, minIncSets);
					conflicts.addAll(conflictsForProArg);
					// 5.2 Collect all possible counter-Arguments for root.
					Set<Atom> atomsForOppArg = new HashSet<>();
					for (AtomSet set : conflictsForProArg) {
						try (CloseableIterator<Atom> it1 = set.iterator()) {
							while (it1.hasNext()) {
								atomsForOppArg.add(it1.next());
							}
						}
					}
					// 5.3 Remove all atoms in the root's premises from the current MICs.
					atomsForOppArg.removeAll(proArgument.getPremises());

					// 5.4 Get arguments that have the atom of "undercut" in their premises
					oppArguments.addAll(Experiment1.findArgumentSets(atomsForOppArg, listArguments));

					// Compute proponent trees
					ArgumentTree proTree = Experiment1.getArgumentTree(proArgument, minIncSets, listArguments,
							arguments, attacks, kb);
					proTrees.add(proTree);
				}
			}

			System.out.println("Conflicts: " + conflicts);

			System.out.println("ProTrees: " + proTrees);
			for (ArgumentTree p : proTrees) {
				System.out.println(p.printTreeGUI(p.getRoot()));

			}
			System.out.println("oppArguments : " + oppArguments);

			// 5.5 Get trees
			// Deduplicate each inner list
			Set<StructuredArgument> merg = new HashSet<StructuredArgument>();
			for (ArrayList<StructuredArgument> innerList : oppArguments) {
				for (StructuredArgument oppArgument : innerList) {
					merg.add(oppArgument);
				}
			}
			for (StructuredArgument oppArgument : merg) {
				oppTrees.add(
						Experiment1.getArgumentTree(oppArgument, minIncSets, listArguments, arguments, attacks, kb));
			}

//			oppArguments.stream().flatMap(List::stream).map(arg -> {
//				try {
//					return Experiment1.getArgumentTree(arg, minIncSets, listArguments, arguments, attacks, kb);
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//			}).filter(Objects::nonNull).forEach(oppTrees::add);

			System.out.println("OppTrees: " + oppTrees);
			for (ArgumentTree o : oppTrees) {
				System.out.println(o.printTreeGUI(o.getRoot()));
			}

			System.out.println("attacks: " + attacks);

			// 6. Compute preferred extensions by arguments and attacks
			SetAf s = new SetAf();
			s = convertToSetAf(arguments, attacks);
			SimpleGroundedSetAfReasoner gr = new SimpleGroundedSetAfReasoner();
			SimplePreferredSetAfReasoner pr = new SimplePreferredSetAfReasoner();
			System.out.println("grounded: " + gr.getModel(s));
			System.out.println("preferred: " + pr.getModels(s));
			

			// 7. Return answers

			// Check grounded and sceptical semantics
			Set<Argument> argsTransfered = new HashSet<Argument>();
			for (ArrayList<StructuredArgument> args : argsForQuery) {
				for (StructuredArgument currentArgument : args) {
					String stringA = "a" + currentArgument.myID;
					Argument newA = new Argument(stringA);
					argsTransfered.add(newA);
				}
			}

			System.out.println("argsTransfered " + argsTransfered);
			groundedAns = false;
			nonAns = false;
			scepticalAns = false;
			credulousAns = false;
			

			// Check if the grounded model is not empty
			if (!gr.getModel(s).isEmpty()) {
				boolean isGroundedAnswer = containsAllInAllGroundedSet(argsTransfered, gr.getModel(s));

				if (isGroundedAnswer) {
					txtResult.appendText("Yes! " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is a surest answer");
					groundedAns = true;
				} else {
					// Check preferred models if grounded answer is not found
					boolean isScepticalAnswer = containsAllInAllPreferredSets(argsTransfered, pr.getModels(s));
					boolean isNonAccepted = containsNoneInAnySet(argsTransfered, pr.getModels(s));
					if (isNonAccepted) {
						txtResult.appendText("No! " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is not an answer");
						nonAns = true;
					} else if (isScepticalAnswer) {
						txtResult.appendText("Yes! " + 
								App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is a plausible answer");
						scepticalAns = true;
					} else if (!isNonAccepted) {
						txtResult.appendText("Yes! " + 
								App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is a possible answer");
						credulousAns = true;
					}
				}

			}
			// If the grounded model is empty, check the preferred models
			else if (!pr.getModels(s).isEmpty()) {
				boolean isScepticalAnswer = containsAllInAllPreferredSets(argsTransfered, pr.getModels(s));
				boolean isNonAccepted = containsNoneInAnySet(argsTransfered, pr.getModels(s));

				if (isScepticalAnswer) {
					txtResult
							.appendText("Yes! " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is a plausible answer");
					scepticalAns = true;
				} else if (isNonAccepted) {
					txtResult.appendText("No! " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is not an answer");
					nonAns = true;
				} else {
					txtResult.appendText("Yes! " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " is a possible answer");
					credulousAns = true;
				}
			}
		}

	}

	protected static boolean containsNoneInAnySet(Set<Argument> set, Collection<Extension<SetAf>> preferredSets) {
		for (Argument element : set) {
			// Check if the element exists in any set in `sets`
			for (Extension<SetAf> currentSet : preferredSets) {
				if (currentSet.contains(element)) {
					return false; // Element is found in at least one set
				}
			}
		}

		return true; // No elements in "set" are in any set in "preferredSets"
	}

	protected static boolean containsAllInAllGroundedSet(Set<Argument> set, Extension<SetAf> groundedSet) {
		for (Argument element : set) {
			// Check if the element exists in every set in groundedSet
			if (!groundedSet.contains(element)) {
				return false; // Element is missing in at least one set
			}
		}

		return true; // All elements in "set" are in every set in "preferredSets"
	}

	protected static boolean containsAllInAllPreferredSets(Set<Argument> set,
			Collection<Extension<SetAf>> preferredSets) {
		for (Extension<SetAf> currentSet : preferredSets) {
			boolean containsAtLeastOne = false;

			// Check if the current set contains at least one element from preferredSets
			for (Argument element : set) {
				if (currentSet.contains(element)) {
					containsAtLeastOne = true;
					break; // No need to check further for this set
				}
			}

			// If the current set does not contain any element from preferredSets, return
			// false
			if (!containsAtLeastOne) {
				return false;
			}
		}

		// All sets contain at least one element from preferredSets
		return true;
	}

	// Helper method to compute support for an argument
	private static AtomSet computeSupportWithFacts(StructuredArgument argument, AtomSet facts) throws AtomSetException {
		AtomSet support = new LinkedListAtomSet();
		for (Atom premise : argument.getPremises()) {
			if (facts.contains(premise)) {
				support.add(premise);
			}
		}
		return support;
	}

	protected static SetAf convertToSetAf(Set<StructuredArgument> listArguments, Set<Attack> attackSet) {
		SetAf s = new SetAf();

		// Map to store arguments for quick lookup
		Map<Integer, Argument> argumentMap = new HashMap<>();

		// Add arguments to SetAf
		for (StructuredArgument a : listArguments) {
			String sArg = "a" + a.myID;
			Argument arg = new Argument(sArg);
			s.add(arg);
			argumentMap.put(a.myID, arg); // Store argument in map for quick access
		}

		// Add attacks
		for (Attack att : attackSet) {
			Set<Argument> source = new HashSet<>();
			for (StructuredArgument b : att.source) {
				Argument bArg = argumentMap.get(b.myID); // Lookup argument from map
				if (bArg != null) {
					source.add(bArg);
				}
			}

			// Find target argument
			Argument target = argumentMap.get(att.target.myID); // Lookup target from map
			if (target != null) {
				s.add(new SetAttack(source, target));
			}
		}

		return s;
	}

//	protected static SetAf convertToSetAf(ArrayList<StructuredArgument> listArguments, Set<Attack> attackSet) {
//		SetAf s = new SetAf();
//
//		// add arguments to SetAf
//		for (StructuredArgument a : listArguments) {
//			String sArg = "a" + a.myID;
//			Argument arg = new Argument(sArg);
//			s.add(arg);
//		}
//
//		// add attacks
//		Collection<Argument> argsInSaf = s.getNodes();
//		for (Attack att : attackSet) {
//			Set<Argument> source = new HashSet<Argument>();
//			for (StructuredArgument b : att.source) {
//				String bString = "a" + b.myID;
//				Argument bArg = new Argument(bString);
//				if (s.getNodes().contains(bArg))
//					source.add(bArg);
//			}
//			// tim arguments in saf, assign with target.
//			String targetString = "a" + att.target.myID;
//			Argument target = new Argument(targetString);
//			s.add(new SetAttack(source, target));
//		}
//		return s;
//	}

	@FXML
	public void getSetbasedExplanations(ActionEvent event) throws IOException, AtomSetException {

		if (argsForQuery == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Input Error");
			alert.setContentText("Please run a query!");
			alert.showAndWait();

		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SetExpView.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Set-Based Explanations");
			stage.setScene(new Scene(root));
			// specyfies the modality for a new window
			// stage.initModality(Modality.WINDOW_MODAL); //defaukt
			stage.initOwner(btnSetExp.getScene().getWindow()); // specifies the owner window for new window - must close
																// new window first
			stage.show();
		}

	}

	@FXML
	public void displayDialogue(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogueView.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle("Dialogical Explanations");
		stage.initOwner(btnGotoDia.getScene().getWindow());
		stage.show();
	}

}
