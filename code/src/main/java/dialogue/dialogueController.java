package dialogue;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.tweetyproject.graphs.HyperDirEdge;

import fr.lirmm.graphik.NAry.App1;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentNode;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentTree;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.util.stream.IteratorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class dialogueController extends MainViewController implements Initializable {
	@FXML
	private Button btnEndDialogue;

	@FXML
	private Button btnStartDialogue;

	@FXML
	private Button btnTreeView;

	@FXML
	private ComboBox<ArgumentNode> cbCounterArgument;

	@FXML
	private ComboBox<ArgumentNode> cbRoot;

	@FXML
	private TextArea txtDialogue;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	void seeAnExplanation(ActionEvent event) throws IteratorException {
		txtDialogue.setText("");

		// display information
		if (scepticalAns != null && scepticalAns) {
			txtDialogue.appendText(
					"Explainer: " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " because ");
			Iterator<AtomSet> iterator = causes.iterator();
			while (iterator.hasNext()) {
				AtomSet s = iterator.next();
				txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(s).toString());

				// Add " or " only if it's not the last element
				if (iterator.hasNext()) {
					txtDialogue.appendText(" or ");
				}
			}

			txtDialogue.appendText(". \n The answer is plausible, regardless of your argument. \n");
			txtDialogue.appendText("Please select a topic to begin a dialogue. \n");
			cbRoot.getItems().clear();
//			for (ArrayList<StructuredArgument> set : argsForQuery) {
//				for (StructuredArgument r : set) {
//					cbRoot.getItems().add(r);
//				}
//
//			}
			for (ArgumentTree t : proTrees) {
				cbRoot.getItems().add(t.getRoot());
			}
		}
		if (credulousAns != null && credulousAns) {
			txtDialogue.appendText(
					"Explainer: " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + " because ");
			Iterator<AtomSet> iterator = causes.iterator();
			while (iterator.hasNext()) {
				AtomSet s = iterator.next();
				txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(s).toString());

				// Add " or " only if it's not the last element
				if (iterator.hasNext()) {
					txtDialogue.appendText(" or ");
				}
			}
			txtDialogue.appendText(
					". \n But the answer is not certain because neither of the options would be consistent with the other known facts. \n");
			Iterator<AtomSet> iterator1 = conflicts.iterator();
			while (iterator1.hasNext()) {
				AtomSet c = iterator1.next();
				txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(c).toString());

				// Add " or " only if it's not the last element
				if (iterator1.hasNext()) {
					txtDialogue.appendText(" or ");
				}
			}

//			for (AtomSet c : conflicts) {
//				txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(c).toString());
//			}

			txtDialogue.appendText(". \n Please select a topic to begin a dialogue. \n");
			cbRoot.getItems().clear();
			for (ArgumentTree t : proTrees) {
				cbRoot.getItems().add(t.getRoot());
			}

		}
		if (groundedAns != null && groundedAns) {
			txtDialogue
					.appendText("Explainer: " + App1.AtomSetIntoArrayWithoutArity(atomAnswers).toString() + "because ");
			Iterator<AtomSet> iterator = causes.iterator();
			while (iterator.hasNext()) {
				AtomSet s = iterator.next();
				txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(s).toString());

				// Add " or " only if it's not the last element
				if (iterator.hasNext()) {
					txtDialogue.appendText(" or ");
				}
			}
			if (conflicts.isEmpty()) {
				txtDialogue.appendText(
						".\n The answer is the surest because it does not conflict with any of the facts. \n");
			} else {
				txtDialogue.appendText(". \n The answer is the surest because because it is strongly supported by  \n");
				for (AtomSet c : conflicts) {
					txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(c).toString() + " \n");
				}
			}
			txtDialogue.appendText("\n Please select a topic to begin an argumentative dialogue. \n");
			txtDialogue.appendText("");
			cbRoot.getItems().clear();
			for (ArgumentTree t : proTrees) {
				cbRoot.getItems().add(t.getRoot());
			}
		}
		if (nonAns != null && nonAns) {
			txtDialogue.appendText("Explainer: " + "This cannot be the answer because it contradicts certain facts.");
			txtDialogue.appendText("\n Please select a topic to begin an argumentative dialogue. \n");
			if (!conflicts.isEmpty()) {
				for (AtomSet c : conflicts) {
					txtDialogue.appendText(App1.AtomSetIntoArrayWithoutArity(c).toString() + " \n");
				}
			}
		}

	}

	@FXML
	void startDialogue(ActionEvent event) {
		cbRoot.getValue();
		ArgumentTree tree = null;
		for (ArgumentTree t : proTrees) {
			if (t.getRoot().myID == cbRoot.getValue().myID) {
				tree = t;
			}
		}
		ArgumentNode rootNode = tree.getRoot();
		HashSet<ArgumentNode> nextNodes = new HashSet<>();
		for (HyperDirEdge<ArgumentNode> e : tree.getEdges()) {
			if (e.getNodeB().equals(rootNode)) {
				nextNodes.addAll(e.getNodeA());
			}
		}
		for (ArgumentNode aNode : nextNodes) {
			cbCounterArgument.getItems().add(aNode);
		}

		System.out.println(rootNode.getPremises());

		txtDialogue.appendText("Explainer: " + App1.AtomWithoutArity(rootNode.getHead()).toString() + " is plausible ");
		if (!rootNode.getPremises().isEmpty()) {
			int index = 0;
			txtDialogue.appendText("because ");
			for (Atom a : rootNode.getPremises()) {
				txtDialogue.appendText(App1.AtomWithoutArity(a).toString());

				// Add " or " only if it's not the last element
				if (index < rootNode.getPremises().size() - 1) {
					txtDialogue.appendText(" or ");
				}
				index++;
			}
		}
	}

	@FXML
	void submit(ActionEvent event) {

		ArgumentTree tree = null;
		for (ArgumentTree t : proTrees) {
			if (t.getRoot().myID == cbRoot.getValue().myID) {
				tree = t;
			}
		}
		ArgumentNode counterArg = cbCounterArgument.getValue();

		txtDialogue.appendText("\n Explainee: But it is uncertain because it would be inconsistent with "
				+ App1.AtomWithoutArity(counterArg.getHead()).toString());

		HashSet<ArgumentNode> nextNodes = new HashSet<>();
		for (HyperDirEdge<ArgumentNode> e : tree.getEdges()) {
			if (e.getNodeB().equals(counterArg)) {
				nextNodes.addAll(e.getNodeA());
			}
		}
		if (!nextNodes.isEmpty()) {
			cbCounterArgument.getItems().clear();
			txtDialogue.appendText("\n Explainer: " + App1.AtomWithoutArity(counterArg.getHead()).toString()
					+ " is uncertain because it would be consistent with ");
			HashSet<ArgumentNode> nextNextNodes = new HashSet<>();
			for (ArgumentNode aNode : nextNodes) {
				txtDialogue.appendText(App1.AtomWithoutArity(aNode.getHead()).toString() + " ");
				for (HyperDirEdge<ArgumentNode> e : tree.getEdges()) {
					if (e.getNodeB().equals(aNode)) {
						nextNextNodes.addAll(e.getNodeA());
					}
				}
			}

			if (!nextNextNodes.isEmpty()) {
				for (ArgumentNode next : nextNextNodes) {
					cbCounterArgument.getItems().add(next);
				}
			} else {
				cbCounterArgument.getItems().clear();
				txtDialogue.appendText("\n");
				for (ArgumentNode aNode : nextNodes) {
					txtDialogue.appendText(App1.AtomWithoutArity(aNode.getHead()).toString() + " ");
				}

				txtDialogue.appendText(
						" are certain because no arguments contradict them. \nDo you agree? If yes, you may end the dialogue or start with a new one.");
			}

		} else {

			ArgumentTree oppTree = null;
			for (ArgumentTree o : oppTrees) {
				if (o.getRoot().myID == counterArg.myID) {
					oppTree = o;
				}
			}
			cbCounterArgument.getItems().clear();
			ArgumentNode r = oppTree.getRoot();
			HashSet<ArgumentNode> nextNodesOpp = new HashSet<>();
			for (HyperDirEdge<ArgumentNode> e : oppTree.getEdges()) {
				if (e.getNodeB().equals(r)) {
					nextNodesOpp.addAll(e.getNodeA());
				}
			}
			
			txtDialogue.appendText("It is uncertain because it would be consistent with ");
			for (ArgumentNode n : nextNodesOpp) {
				txtDialogue.appendText(App1.AtomWithoutArity(n.getHead()).toString() + " ");
				cbCounterArgument.getItems().add(n);
			}

		}

	}

}
