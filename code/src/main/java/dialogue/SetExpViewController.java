package dialogue;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import fr.lirmm.graphik.NAry.App1;
import fr.lirmm.graphik.NAry.Experiment1;
import fr.lirmm.graphik.NAry.ArgumentationFramework.StructuredArgument;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.io.owl.OWL2ParserException;
import fr.lirmm.graphik.util.stream.CloseableIterator;

public class SetExpViewController extends MainViewController implements Initializable {

	@FXML
	private TextArea txtCause;

	@FXML
	private TextArea txtConflict;

	@FXML
	private TextArea txtMessage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void DisplaySetbasedExplanation(ActionEvent event)
			throws AtomSetException, ChaseException, HomomorphismException, OWL2ParserException, IOException {
		txtMessage.setText("");
		txtCause.setText("");
		txtConflict.setText("");

		
		ArrayList<AtomSet> repairs = App1.ComputeAllRepairs(kb);
		for (int i = 0; i < repairs.size(); i++) {
		    AtomSet repair = repairs.get(i);
		    AtomSet filteredRepair = new LinkedListAtomSet(); // Create a new AtomSet for filtered elements

		    // Iterate through the elements of the current AtomSet
		    try (CloseableIterator<Atom> iterator = repair.iterator()) {
		        while (iterator.hasNext()) {
		            Atom element = iterator.next();
		            
		            // Add the element to the new AtomSet if it is in initialFacts
		            if (initialFacts.contains(element)) {
		                filteredRepair.add(element);
		            }
		        }
		    } // Close the iterator automatically

		    // Replace the original AtomSet with the filtered one
		    repairs.set(i, filteredRepair);
		}
		System.out.println("\n repairs: " + repairs);
		txtMessage.setText("Repairs (Maiximal consistent subsets): " + "\n");
		
		for (AtomSet r : repairs) {			
			txtMessage.appendText(App1.AtomSetIntoArrayWithoutArity(r).toString() + "\n");
		}

		// display information
		if (scepticalAns != null && scepticalAns) {
			txtMessage.appendText("The answer is plausible because: ");
			txtCause.appendText("The facts in the causes make the answer acceptable under all repairs." + "\n");
			for (AtomSet s : causes) {
				txtCause.appendText(App1.AtomSetIntoArrayWithoutArity(s).toString() + " \n");
			}
			txtConflict.appendText("");
		}
		if (credulousAns != null && credulousAns) {
			txtMessage.appendText("The answer is possible because: ");
			txtCause.appendText("The facts in the causes make the answer acceptable under some repairs." + "\n");
			for (AtomSet s : causes) {
				txtCause.appendText(App1.AtomSetIntoArrayWithoutArity(s).toString() + " \n");
			}

			txtConflict.appendText(
					"The facts in the set of minimal conflicts prevent the answer from being accepted in some repair."
							+ "\n");
			for (AtomSet c : conflicts) {
				txtConflict.appendText(App1.AtomSetIntoArrayWithoutArity(c).toString() + " \n");
			}
		}
		if (groundedAns != null && groundedAns) {
			txtMessage.appendText("The answer is surest because: ");
			txtCause.appendText(
					"The facts in the causes make the answer acceptable under the intersection of the repairs." + "\n");
			for (AtomSet s : causes) {
				txtCause.appendText(App1.AtomSetIntoArrayWithoutArity(s).toString() + " \n");
			}
			txtConflict.appendText("");

		}
		if (nonAns != null && nonAns) {
			txtMessage.appendText("It is not the answer because: ");
			txtConflict.appendText(
					"The facts in the set of minimal conflicts prevent the answer from being accepted in any repair."
							+ "\n");
			if (!conflicts.isEmpty()) {
				for (AtomSet c : conflicts) {
					txtConflict.appendText(App1.AtomSetIntoArrayWithoutArity(c).toString() + " \n");
				}
			}
		}

	}

}