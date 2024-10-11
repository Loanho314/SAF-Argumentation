package windowBuilder.views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.App1;
import fr.lirmm.graphik.NAry.Argument;
import fr.lirmm.graphik.NAry.Attack;
import fr.lirmm.graphik.NAry.Distance;
import fr.lirmm.graphik.NAry.DungAF;
import fr.lirmm.graphik.NAry.Graph;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.DefaultConjunctiveQuery;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.graal.io.owl.OWL2ParserException;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.ComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Font;

public class UserInterface extends JFrame {
	private JPanel mainPane;
	private JPanel topPane;
	private JTextArea txtKB;
	private JTextArea txtArg;
	private JComboBox cbQuery;
	private JComboBox cbExp;
	private static JButton btnChoose;
	private static JButton btnShift;
	private static JButton btnEnd;
	private static JComboBox<Argument> cbQuestioner;
	private static DefaultComboBoxModel subModel;
	private static boolean haveParameters = true;	
	public static InMemoryAtomSet bottomAtomset = new LinkedListAtomSet();
	public static InMemoryAtomSet equalityAtomset = new LinkedListAtomSet();
	public static ConjunctiveQuery bottomQuery = new DefaultConjunctiveQuery(bottomAtomset, Collections.emptyList());
	private static AtomSet defeasibleFacts = new LinkedListAtomSet();
	private static DungAF af;
	private static FileReader fileReader; 
	private static FileReader fileReader1; 
	private static FileReader fileReader2; 
	private static JTextArea txtTree;
	private static JTextArea txtExp;
	private static ArrayList<Attack> SetOfAtts;
	private static DefeasibleKB kb;
	private static DefeasibleKB kb1;
	private static DefeasibleKB kb2;	
	private static AtomSet InitialFacts;		
	private static RuleSet negativeruleset;
	private static RuleSet positiveruleset;
	private static RuleSet rules;
	private static RuleSet functionalruleset;
	private static RuleSet ruleset;

	private static ArrayList<ArrayList<Argument>> extensions;
	private static ArrayList<Argument> ext;
	private static ArrayList<Attack> Visited0;
	private static ArrayList<Attack> Visited;
	private static ArrayList<Argument> Reach;
	private static ArrayList<Distance> Dist;
	private static ArrayList<Distance> NewDist;

	private static Map<Argument, ArrayList<Argument>> adjacencyList;
	private static ArrayList<Argument> ListArgument;
	private static InMemoryAtomSet saturatedAtom;
	private static ArrayList<Argument> grd;
	private static ArrayList<Attack> tempAtt;
	private static List<List<Argument>> forestTrees;
	private static String filePath;
	private static String filename;
	private static HashMap<Argument, ArrayList<List<Argument>>> argValues;
	private final JFileChooser openFileChooser;
	private BufferedReader bufferedReader;
	private static ConjunctiveQuery query;
	private static ArrayList<Argument> argumentForQuery; 
	private JTextField txtfQuery;
	private static List<List<Argument>> forestTreeFor;
	private static ArrayList<Argument> newArgForQuery;
	private static int index;
	private static List<Argument> previousArgs = new ArrayList<>();
	private static Argument argCr;
	private static List<String> itemCr;
	private static int removedItIndex;
	private static List<Integer> listToRemove = new ArrayList();
	private JButton btnTransfer;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface frame = new UserInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserInterface() {
		openFileChooser = new JFileChooser();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1091,832);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		topPane = new JPanel();
		topPane.setBorder(new TitledBorder(null, "KB to PAF", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JButton btnLoad = new JButton("Load KB");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				try {
					txtArg.setText("");
					DefeasibleKB kb = new DefeasibleKB();
					DefeasibleKB kb1 = new DefeasibleKB();
					DefeasibleKB kb2  = new DefeasibleKB();	
					bottomAtomset = new LinkedListAtomSet();
					equalityAtomset = new LinkedListAtomSet();
					InitialFacts = new LinkedListAtomSet();		
					negativeruleset = new LinkedListRuleSet();
					positiveruleset = new LinkedListRuleSet();
					rules = new LinkedListRuleSet();
					functionalruleset = new LinkedListRuleSet();
					ruleset = new LinkedListRuleSet();
					filePath = null;
					if ((ListArgument == null) && (SetOfAtts == null) && (tempAtt == null)){
						ListArgument = new ArrayList<>();
						SetOfAtts = new ArrayList<>();

					} else {
						ListArgument.removeAll(ListArgument);
						System.out.println("List arg: " + ListArgument);
						System.out.println(kb);
						System.out.println(kb1);
						System.out.println(kb2);
						SetOfAtts.removeAll(SetOfAtts);
						//tempAtt.removeAll(tempAtt);					
						af = new DungAF();
					}


				} catch (IteratorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}




				int returnValue = openFileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = openFileChooser.getSelectedFile();
					filename = selectedFile.getAbsolutePath();
					try {
						FileReader reader = new FileReader(filename);
						BufferedReader br = new BufferedReader(reader);

						String line;
						String contents = "";

						while ((line = br.readLine()) != null) {
							contents += line + "\n";
						}

						reader.close();
						br.close();
						txtKB.setText(contents);

					} catch (IOException c) {
						JOptionPane.showMessageDialog(null, "Error!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
					}
					//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				}
				else {
					txtKB.setText("");
					JOptionPane.showMessageDialog(null, "No file choose!",
							"Message Dialog", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnLoad.setPreferredSize(new Dimension(80, 21));

		btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadArguments();				
			}
			private void loadArguments() {
				try {
					FileReader reader = new FileReader(filename);
					//fileReader	= new FileReader("/Users/tho310/Data test/Lum test.dlgp");
					//fileReader	= new FileReader("bio-bench.dlgp");
					kb= new DefeasibleKB(reader);
					kb1 = new DefeasibleKB(reader);
					kb2 = new DefeasibleKB(); // create a set of arguments 
					InitialFacts = new LinkedListAtomSet();		
					negativeruleset = new LinkedListRuleSet();
					positiveruleset = new LinkedListRuleSet();
					rules = new LinkedListRuleSet();
					functionalruleset = new LinkedListRuleSet();
					ruleset = new LinkedListRuleSet();	


					InitialFacts.addAll(kb.facts);
					defeasibleFacts.addAll(kb.defeasibleAtomSet);
					CloseableIterator<Atom> de = (CloseableIterator<Atom>) defeasibleFacts.iterator();

					negativeruleset = kb.negativeConstraintSet;		
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
					CloseableIterator<Atom> it = (CloseableIterator<Atom>) InitialFacts.iterator();
					while (it.hasNext()) {
						Atom a = it.next();				
						kb2.addAtom(a);
					}
					Iterator it2 = positiveruleset.iterator();
					while (it2.hasNext()) {
						Rule r2 = (Rule) it2.next();
						if (!r2.getHead().iterator().next().getPredicate().equals(Predicate.EQUALITY)) {
							kb2.addRule(r2);;
						}

					}
					kb2.saturate();
					kb.saturate();
					saturatedAtom = new LinkedListAtomSet();			
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





					SetOfAtts = new ArrayList();
					//compute attacks under equality rule
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
									if (App1.checkAttacks(SetOfAtts, add) == false) {
										SetOfAtts.add(add);
									}
								}				

							}				

						}
						//System.out.println(SetOfAtts);
						txtArg.setText("");
						txtArg.append("Set of arguments: \n");
						for (Argument arg : ListArgument) {
							txtArg.append(arg.toString() + "\n");
						}


						if (filePath == null) {
							tempAtt = new ArrayList<Attack>(SetOfAtts);

						} else {
							// Read all prioritized instances
							HashMap<String, Integer> preAtoms = new HashMap<String, Integer>();
							if (!functionalruleset.isEmpty()) {
								preAtoms = App1.readPreferenceAtoms(filePath, InitialFacts);
							} else 	
								if(!negativeruleset.isEmpty())
								{
									preAtoms = App1.readPreferenceAtoms(filePath, InitialFacts);
								}

							// compute attacks with considering preferences

							tempAtt = new ArrayList<Attack>();

							for (int i = 0; i < SetOfAtts.size(); i++) {
								Attack att = SetOfAtts.get(i);
								ArrayList<Argument> sourceAtt = att.source;
								ArrayList<Atom> targetAtom = att.target.getPremises();
								for (int j = 0; j < sourceAtt.size(); j++) {
									ArrayList<Atom> sourceAtom = sourceAtt.get(j).getPremises();
									if (App1.isGPcomparing(sourceAtom, targetAtom, preAtoms) == true ) {
										tempAtt.add(att);
									}
								}
							}
						}


						for (Attack at : tempAtt) {
							txtArg.append(at.toString() + "\n");
						}

						//System.out.println("There are " + ListArgument.size() + " arguments and " + tempAtt.size() + " SetOfAtts.");
						//return;

					}


					/* the case of negative rules*/
					ArrayList<AtomSet> repairs = new ArrayList<AtomSet>();
					if(!negativeruleset.isEmpty()) {
						// Step1: Convert LinkedListAtomSet to ArrayList
						repairs = App1.ComputeAllRepairs(kb);

						//	System.out.println(repairs);

						ArrayList<ArrayList<String>> repairString = new ArrayList();
						Iterator<AtomSet> s = repairs.iterator();
						ArrayList subList = new ArrayList();
						ArrayList<String> tempt = new ArrayList();			
						while(s.hasNext()){
							AtomSet pr = s.next();				
							tempt = App1.AtomSetIntoArrayWithoutArity(pr);				
							repairString.add(tempt);				
						}



						// Convert ArrayList<ArrayList<String>> (repairString) to ArrayList<ArrayList<Atom>> (repairsAtom)
						ArrayList repairsAtom = new ArrayList();
						Iterator localIterator3;
						Iterator localIterator2 = repairString.iterator();
						CloseableIterator T;
						for (int i =0; i < repairString.size(); i++) {
							ArrayList R = (ArrayList) repairString.get(i);
							localIterator3 = R.iterator();
							while(localIterator3.hasNext()) {
								String r = (String)localIterator3.next();
								repairsAtom.add(new ArrayList());
								T = saturatedAtom.iterator();
								while (T.hasNext())
								{
									Atom t = (Atom)T.next();
									if (App1.AtomWithoutArity(t).equals(r)){
										((ArrayList)repairsAtom.get(repairsAtom.size()-1)).add(t);
									}
								}
							} 
						}






						// Step2: Compute a set of arguments apprearing in the repairs.

						HashMap<AtomSet,ArrayList<Argument>> Arg = new HashMap<AtomSet, ArrayList<Argument>>();		
						Iterator<AtomSet> localIterator4 = repairs.iterator();				 
						while(localIterator4.hasNext()) {				
							AtomSet r = localIterator4.next();					
							Arg.put(r, new ArrayList<Argument>());
							Iterator<Argument> T1 = ListArgument.iterator();
							while (T1.hasNext()) {
								Argument Ar = (Argument)T1.next();
								ArrayList<Atom> atom1 = Ar.getPremises();
								Boolean checkAtom = true;
								for (int k=0; k<atom1.size(); k++) {										
									if (!r.contains(atom1.get(k))){
										checkAtom = false;
									}
								}
								if (checkAtom == true) {
									Arg.get(r).add(Ar);
								}
							}

						}	

						for (AtomSet As: repairs) {
							ArrayList<Argument> NotInArg = new ArrayList<Argument>();
							// Step 4: compute a set of arguments that are not in Arg, and them to NotInArg.
							ArrayList<Argument> arrayArg = Arg.get(As);
							for (Argument a : ListArgument) {
								if (!arrayArg.contains(a)) {						
									NotInArg.add(a);
								}
							}

							// Step 5: For each argument that is NotInArg, compute attack relation of arguments
							for (Argument temp : NotInArg) {					
								Argument aInTemp = temp;
								ArrayList newS = new ArrayList();
								newS.add(new ArrayList());
								App1.AllSubset(newS, arrayArg); //get(r)
								newS.remove(0);
								for (int i = newS.size() - 1; i >= 0; i--) {					
									ArrayList Concs = new ArrayList();
									for (Object b1 : (ArrayList)newS.get(i)) {
										Argument b = (Argument)b1;
										Concs.add(b.head);
									}
									// check whether two arguments are conflict (inconsistent)

									int iter = 0;
									boolean inconsistent = false;
									while ((!inconsistent) && (iter < aInTemp.getPremises().size())){						
										AtomSet u = new LinkedListAtomSet();
										for (Object c1 : Concs) {
											Atom c = (Atom)c1;
											u.add(c);
										}
										u.add((Atom)aInTemp.getPremises().get(iter));

										kb.strictAtomSet = u;
										kb.saturate();							
										InMemoryAtomSet newSaturatedAtoms = new LinkedListAtomSet();
										newSaturatedAtoms.addAll(kb.facts);
										inconsistent = App1.IsInconsistent(newSaturatedAtoms, ruleset);
										iter++;
									}
									if (!inconsistent) {
										newS.remove(i);
									}						

								}

								ArrayList truth = new ArrayList();
								for (int i = 0; i < newS.size(); i++)
									truth.add(Boolean.valueOf(true));
								for (int i = newS.size() - 1; i >= 1; i--) {
									for (int j = i - 1; j >= 0; j--) {
										if (((ArrayList)newS.get(i)).containsAll((Collection)newS.get(j)))
										{
											truth.set(i, Boolean.valueOf(false));
										}
										else if (((ArrayList)newS.get(j)).containsAll((Collection)newS.get(i)))
										{
											truth.set(j, Boolean.valueOf(false));
										}
									}
								}

								for (int j = newS.size() - 1; j >= 0; j--) {
									if (!((Boolean)truth.get(j)).booleanValue()) {
										newS.remove(j);
									}
								}

								for (int k = 0; k < newS.size(); k++) {
									Attack toAdd = new Attack((ArrayList)newS.get(k), aInTemp);								
									//check whether an attack is in the set of attacks
									if (App1.checkAttacks(SetOfAtts, toAdd) == false) {							
										SetOfAtts.add(toAdd);
									}						
								} 
							} 
						}
					}


					txtArg.setText("");
					txtArg.append("Set of arguments: \n");
					for (Argument arg : ListArgument) {
						txtArg.append(arg.toString() + "\n");
					}

					// compute attacks with considering preferences

					if (filePath == null) {
						tempAtt = new ArrayList<Attack>(SetOfAtts);

					} else {
						// Read all prioritized instances
						HashMap<String, Integer> preAtoms = new HashMap<String, Integer>();
						if (!functionalruleset.isEmpty()) {
							preAtoms = App1.readPreferenceAtoms(filePath, InitialFacts);
						} else 	
							if(!negativeruleset.isEmpty())
							{
								preAtoms = App1.readPreferenceAtoms(filePath, InitialFacts);
							}

						// compute attacks with considering preferences

						tempAtt = new ArrayList<Attack>();
						for (int i = 0; i < SetOfAtts.size(); i++) {
							Attack att = SetOfAtts.get(i);
							ArrayList<Argument> sourceAtt = att.source;
							ArrayList<Atom> targetAtom = att.target.getPremises();
							for (int j = 0; j < sourceAtt.size(); j++) {
								ArrayList<Atom> sourceAtom = sourceAtt.get(j).getPremises();
								if (App1.isGPcomparing(sourceAtom, targetAtom, preAtoms) == true ) {
									tempAtt.add(att);
								}
							}
						}
					}

					txtArg.append("Set of attacks: \n");
					for (Attack at : tempAtt) {
						txtArg.append(at.toString() + "\n");
					}

					//	System.out.println("There are " + ListArgument.size() + " arguments and " + tempAtt.size() + " SetOfAtts."); 

					// compute stable/ prefered extensions				  
					HashSet<String> argString = new HashSet<String>();
					af = new DungAF();
					/* read arguments from ListArgument (ArrayList<Argument>) to HashSet<String> */
					for (Argument a : ListArgument) {				
						String aString = "A" + a.myID;								
						af.addArgs(aString);
					}
					argString = af.getArgs();

					/* read attacks from Attacks (ArrayList<Attack>) to HashSet<String>[] []*/
					HashSet<String[] []> atts = new HashSet<String[] []>();
					for (int i = 0; i < tempAtt.size(); i++) {
						Attack at = (Attack) tempAtt.get(i);				
						String target = "A" + at.target.myID;			
						String source = new String();
						for (Argument argS : at.source) {
							source = "A" + argS.myID;
						}
						//af.addAtts(new String[][] {{source, target}});
					}
					/*Compute preferred sematics*/
					HashSet<HashSet<String>> preferredExts = new HashSet<HashSet<String>>();
					preferredExts = af.getPreferredExts();
					//	System.out.println("preferred extensions: " + preferredExts);
					txtArg.append("Preferred Extensions: ");
					txtArg.setText(txtArg.getText() + preferredExts.toString() + "\n");

					/*Compute grounded sematics*/
					HashSet<String> groundedExts = new HashSet<String>();
					groundedExts = af.getGroundedExt();
					//System.out.println("Grounded extensions: " + groundedExts);

					txtArg.append("Grounded Extensions: ");
					txtArg.setText(txtArg.getText() + groundedExts.toString() + "\n");

					// convert grounded extentions from HashSet<String> to ArrayList<Argument>

					grd = new ArrayList<Argument>();

					for (String s : groundedExts) {
						for (Argument arg : ListArgument) {
							String id = "A" + arg.myID;
							if (s.contains(id)) {
								grd.add(arg);
							}
						}
					}

					//System.out.println("grounded extension: " + grd);


					/*convert extensions from HashSet<HashSet<String>> to ArrayList<ArrayList<Argument>>*/

					extensions = new ArrayList<ArrayList<Argument>>();
					for(HashSet<String> extString : preferredExts) {
						ext = new ArrayList<Argument>();
						for (Argument arg : ListArgument) {
							String argID = "A" + arg.myID;
							if (extString.contains(argID)) {
								ext.add(arg);
							}

						}
						extensions.add(ext);
					}

					//System.out.println("Extension after convert: " );

					//for (ArrayList<Argument> print : extensions) {			
					//	System.out.println(print);
					//}

					/*Get union of extensions for skeptical semantics*/

					ArrayList<Argument> preferredScepticalExt = new ArrayList<Argument>();
					preferredScepticalExt = App1.getPreferredScepticalExt(extensions);

					//System.out.println("Sceptical extensions: " + preferredScepticalExt);

				} catch (FileNotFoundException ex)
				{
					ex.printStackTrace();
				}
				catch (IteratorException ex) {
					ex.printStackTrace();
				}
				catch (AtomSetException ex) {
					ex.printStackTrace();
				}
				catch (ChaseException ex) {
					ex.printStackTrace();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
				catch (HomomorphismException e) {
					e.printStackTrace();
				}
				catch (OWL2ParserException e) {
					e.printStackTrace();
				}	
			}
		});
		btnTransfer.setPreferredSize(new Dimension(80, 21));

		txtKB = new JTextArea();
		txtKB.setEditable(false);


		JScrollPane scrollPane1 = new JScrollPane(txtKB);
		txtArg = new JTextArea();	
		txtArg.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane(txtArg);

		JButton btnPre = new JButton("Preferences");
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue1 = openFileChooser.showOpenDialog(null);
				if (returnValue1 == JFileChooser.APPROVE_OPTION) {
					File selectedFile = openFileChooser.getSelectedFile();
					filePath = selectedFile.getAbsolutePath();
					try {
						FileReader readerPath = new FileReader(filePath);
						BufferedReader br = new BufferedReader(readerPath);
						JOptionPane.showMessageDialog(null, "File loaded sucessfully!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						readerPath.close();
						br.close();

					} catch (IOException c) {
						JOptionPane.showMessageDialog(null, "Error!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "No file choosen!",
							"Message Dialog", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		GroupLayout gl_topPane = new GroupLayout(topPane);
		gl_topPane.setHorizontalGroup(
				gl_topPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_topPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_topPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnTransfer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnPre, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnLoad, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
						.addGap(18)
						.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		gl_topPane.setVerticalGroup(
				gl_topPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_topPane.createSequentialGroup()
						.addGroup(gl_topPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_topPane.createSequentialGroup()
										.addGap(98)
										.addComponent(btnLoad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnPre)
										.addGap(18)
										.addComponent(btnTransfer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_topPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(20, Short.MAX_VALUE))
				);
		topPane.setLayout(gl_topPane);

		JPanel bottomPane = new JPanel();

		bottomPane.setBorder(new TitledBorder(null, "Dialogue Toolbox", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_mainPane = new GroupLayout(mainPane);
		gl_mainPane.setHorizontalGroup(
				gl_mainPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_mainPane.createParallelGroup(Alignment.LEADING)
								.addComponent(bottomPane, GroupLayout.PREFERRED_SIZE, 1047, GroupLayout.PREFERRED_SIZE)
								.addComponent(topPane, GroupLayout.DEFAULT_SIZE, 1047, Short.MAX_VALUE))
						.addContainerGap())
				);
		gl_mainPane.setVerticalGroup(
				gl_mainPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPane.createSequentialGroup()
						.addComponent(topPane, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bottomPane, GroupLayout.PREFERRED_SIZE, 465, Short.MAX_VALUE)
						.addContainerGap())
				);
		JScrollPane scrollPane3 = new JScrollPane(txtTree);
		txtExp = new JTextArea();
		txtExp.setEditable(false);
		JScrollPane scrollPane4 = new JScrollPane(txtExp);

		subModel = new DefaultComboBoxModel();
		cbQuestioner = new JComboBox<>(subModel);



		cbExp = new JComboBox();
		cbExp.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selected = ((JComboBox)e.getSource())
							.getSelectedItem().toString();
					//	switch(selected) {
					if (selected.equals("Why is the answer certain?")) {
						txtExp.setText("");
						txtExp.append("Questioner: Why is the answer certain?");
						cbQuestioner.removeAllItems();
						for (Argument arg : argumentForQuery) {
							cbQuestioner.addItem(arg);
						}
					}

					if (selected.equals("Why is the answer possible?")) {
						txtExp.setText("");
						txtExp.append("Questioner: Why is the answer possible?");
						cbQuestioner.removeAllItems();
						for (Argument arg : argumentForQuery) {
							cbQuestioner.addItem(arg);
						}
						btnChoose.setVisible(true);
					}


					if (selected.equals("Why is not the answer?")) {
						txtExp.setText("");
						txtExp.append("Questioner: Why is not the answer?");
						cbQuestioner.removeAllItems();
						for (Argument arg : argumentForQuery) {
							cbQuestioner.addItem(arg);
						}
					}

					if (selected.equals("Why is the answer accepted under grounded semantics?")) {
						txtExp.setText("");
						txtExp.append("Questioner: Why is the answer accepted under grounded semantics?");
						cbQuestioner.removeAllItems();
						for (Argument arg : argumentForQuery) {
							cbQuestioner.addItem(arg);
						}

					}

				} 
			}
		});
		cbExp.addItem("Please choose a question for explanation");

		btnChoose = new JButton("Choose case");
		btnChoose.setVisible(false);
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					txtExp.append("\nAnswerer: I claim that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
					txtExp.append(", I also claim that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
					if (cbQuestioner.getSelectedIndex() == -1) {
						JOptionPane.showMessageDialog(null, "Select an utterance!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
					} else
						if (cbQuestioner.getSelectedIndex()>=0) {
							argCr = (Argument) cbQuestioner.getSelectedItem();
							cbQuestioner.removeAllItems();
							itemCr = new ArrayList<>();
							itemCr.add(0, "Why do you think that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
							itemCr.add(1, "Why do you think that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
							for (String st : itemCr) {
								subModel.addElement(st);
							}
							btnChoose.setVisible(false);
							JOptionPane.showMessageDialog(null, "Please select EXPLAIN to start a dialogue!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						}

				} catch (IteratorException e1) {
					e1.printStackTrace();

				}
			}
		});

		cbQuery = new JComboBox<Object>();
		cbQuery.setVisible(false);


		/*argValues = new HashMap<Argument,ArrayList<List<Argument>>>();					
		for (Argument arg : argumentForQuery) {
			argValues.put(arg, new ArrayList<List<Argument>>());
			for (List<Argument> t : forestTrees) {
				if (arg.myID == t.get(0).myID) {
					argValues.get(arg).add(t);
				}
			}
		}*/

		JButton btnExplain = new JButton("Explain");
		btnExplain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					index = 0;
					forestTreeFor = new ArrayList<>();
					newArgForQuery = argumentForQuery;
					if (cbQuestioner.getSelectedIndex() == -1) {
						JOptionPane.showMessageDialog(null, "Select an utterance!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
					}
					if (cbExp.getSelectedItem().equals("Why is the answer possible?")) {
						Object selectedItem = subModel.getSelectedItem();
						if (subModel.getIndexOf(selectedItem) == 0)   {
							removedItIndex = 0;
							txtExp.append("\nQuestioner: Why do you think that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
							Argument arg = argCr;
							forestTreeFor = findPathsforArgument(arg, forestTrees);
							List<List<Argument>> oddPaths = findOddPathsForArg(arg, forestTreeFor);
							List<List<Argument>> evenPaths = findEvenPathsForArg(arg, forestTreeFor);
							if (arg.body.isEmpty()) {
								List<Argument> argsAt = new ArrayList<>();
								argsAt = findArgumentsAt(1, evenPaths);
								//System.out.println(argsAt);
								txtExp.append("\nAnswerer: I am certain that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
								txtExp.append(", because we know that ");
								List<Argument> nextArg = new ArrayList<>();
								List<Argument> newList = new ArrayList<>();

								for (int i = 0; i < argsAt.size(); i++) {
									Argument a = argsAt.get(i);
									// a case that the argument a is the last place in argsAt. Should have a case check whether a.body.isnotempty. In this code, we assume that a.body.isEmpty
									if (i == argsAt.size()-1) {
										if (a.body.isEmpty()) {
											txtExp.append(App1.convertAtom(a.head));
										} else {
											txtExp.append(App1.convertAtom(a.head) + "(This is implied from ");
											for (Atom bodyOfFirstEle : a.getPremises()) {
												if (bodyOfFirstEle.equals(a.getPremises().get(a.getPremises().size()-1))){
													txtExp.append(App1.convertAtom(bodyOfFirstEle) + ")");
												} else 
													txtExp.append(App1.convertAtom(bodyOfFirstEle) + ", and ");
											}
										}
									} else {
										if (a.body.isEmpty()) {
											txtExp.append(App1.convertAtom(a.head));	
											txtExp.append("; or we know that ");
										} 
										else if (!a.body.isEmpty()) {
											txtExp.append(App1.convertAtom(a.head) + "(This is implied from ");	
											for (Atom bodyA : a.getPremises()) {
												if (bodyA.equals(a.getPremises().get(a.getPremises().size()-1))){
													txtExp.append(App1.convertAtom(bodyA) + ")");
												} else 
													txtExp.append(App1.convertAtom(bodyA) + ", and ");		
											}
											txtExp.append("; or we know that ");
										}

									}
								}

								// remove all path has length =2
								List<List<Argument>> newPaths = new ArrayList<>();
								for(int m = 0; m < evenPaths.size(); m++) {
									List<Argument> path = evenPaths.get(m);
									if (path.size() > 2) {
										newPaths.add(path);
									}
								}

								// newList considers the elements at i =1 (i.e., the second place in the path) where path.size() >2
								evenPaths = newPaths;	
								for (List<Argument> path : evenPaths) {
									newList.add(path.get(1));
								}
								if (newList.isEmpty()) {
									txtExp.append(". I was right that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									cbQuestioner.removeAllItems();
									subModel.addElement("Ok! I understood");
									cbQuestioner.setModel(subModel);
								} else {
									cbQuestioner.removeAllItems();
									for (Argument temp : newList) {
										nextArg.addAll(findNextArgs(temp, 1, evenPaths));
									}
									previousArgs.clear();
									previousArgs.addAll(newList);
									//	System.out.println("previous: " + previousArgs);
									//	System.out.println("Next: " + nextArg);
									//add elements in NextArg to cbQuestioner.
									for (Argument next : nextArg) {
										cbQuestioner.addItem(next);
									}
									index = 2; // consider element at the third place in the path
								}
							}

							else if (!arg.body.isEmpty()) {// in this case, the answer includes 
								txtExp.append("\nAnswerer: I am certain that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
								txtExp.append(", because we know that ");
								txtExp.append(printArg(newArgForQuery.get(0)) + ". We also know that ");
								// code below is similar to the case of a.body.isEmpty()
								List<Argument> argsAt = new ArrayList<>();
								argsAt = findArgumentsAt(1, evenPaths);								
								List<Argument> nextArg = new ArrayList<>();
								List<Argument> newList = new ArrayList<>();

								for (int i = 0; i < argsAt.size(); i++) {
									Argument a = argsAt.get(i);
									// a case that the argument a is the last place in argsAt. Should have a case check whether a.body.isnotempty. In this code, we assume that a.body.isEmpty
									if (i == argsAt.size()-1) {
										if (a.body.isEmpty()) {
											txtExp.append(App1.convertAtom(a.head));
										} else {
											txtExp.append(App1.convertAtom(a.head) + "(This is implied from ");
											for (Atom bodyOfFirstEle : a.getPremises()) {
												if (bodyOfFirstEle.equals(a.getPremises().get(a.getPremises().size()-1))){
													txtExp.append(App1.convertAtom(bodyOfFirstEle) + ")");
												} else 
													txtExp.append(App1.convertAtom(bodyOfFirstEle) + ", and ");
											}
										}
									} else {
										if (a.body.isEmpty()) {
											txtExp.append(App1.convertAtom(a.head));	
											txtExp.append("; or we know that ");
										} 
										else if (!a.body.isEmpty()) {
											txtExp.append(App1.convertAtom(a.head) + "(This is implied from ");	
											for (Atom bodyA : a.getPremises()) {
												if (bodyA.equals(a.getPremises().get(a.getPremises().size()-1))){
													txtExp.append(App1.convertAtom(bodyA) + ")");
												} else 
													txtExp.append(App1.convertAtom(bodyA) + ", and ");		
											}
											txtExp.append("; or we know that ");
										}

									}
								}

								// remove all path has length =2
								List<List<Argument>> newPaths = new ArrayList<>();
								for(int m = 0; m < evenPaths.size(); m++) {
									List<Argument> path = evenPaths.get(m);
									if (path.size() > 2) {
										newPaths.add(path);
									}
								}

								// newList considers the elements at i =1 (i.e., the second place in the path) where path.size() >2
								evenPaths = newPaths;	
								for (List<Argument> path : evenPaths) {
									newList.add(path.get(1));
								}
								if (newList.isEmpty()) {
									txtExp.append(". I was right that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									cbQuestioner.removeAllItems();
									subModel.addElement("Ok! I understood");
									cbQuestioner.setModel(subModel);
								} else {
									cbQuestioner.removeAllItems();
									for (Argument temp : newList) {
										nextArg.addAll(findNextArgs(temp, 1, evenPaths));
									}
									previousArgs.clear();
									previousArgs.addAll(newList);
									//	System.out.println("previous: " + previousArgs);
									//	System.out.println("Next: " + nextArg);
									//add elements in NextArg to cbQuestioner.
									for (Argument next : nextArg) {
										cbQuestioner.addItem(next);
									}
									index = 2; // consider element at the third place in the path
								}

								// bla bla
							}
							newArgForQuery.remove(arg);	


						} 

						// acceptance for credulous
						else if (subModel.getIndexOf(selectedItem) == 1) {
							removedItIndex = 1;
							txtExp.append("\nQuestioner: Why do you think that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));						
							Argument arg = argCr;
							forestTreeFor = findPathsforArgument(arg, forestTrees);
							List<List<Argument>> oddPaths = findOddPathsForArg(arg, forestTreeFor);
							List<List<Argument>> evenPaths = findEvenPathsForArg(arg, forestTreeFor);

							List<Argument> nextArgs = new ArrayList<>();
							nextArgs = findNextArgs(arg, 0, oddPaths);
							//	System.out.println("Next at begin: " + nextArgs);
							previousArgs.clear();
							previousArgs.add(arg);
							//	System.out.println("previous: " + previousArgs);
							if (arg.body.isEmpty()) { 
								txtExp.append("\nAnswerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
								// add the next argment to cbQuestioner
								if (!nextArgs.isEmpty()) {
									cbQuestioner.removeAllItems();
									for (Argument a : nextArgs) {
										cbQuestioner.addItem(a);
									}

								} else {
									txtExp.append(". Because there is no conflict information against " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									cbQuestioner.removeAllItems();
									subModel.addElement("Ok! I understood");
									cbQuestioner.setModel(subModel);

								}
							} else 	
								if (!arg.body.isEmpty()) {
									txtExp.append("\nAnswerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));						
									txtExp.append(", because we know that ");							
									txtExp.append(printArg(arg));
									if (!nextArgs.isEmpty()) {
										cbQuestioner.removeAllItems();
										for (Argument a : nextArgs) {
											cbQuestioner.addItem(a);
										}

									} else {
										cbQuestioner.removeAllItems();
										subModel.addElement("Ok! I understood");
										cbQuestioner.setModel(subModel);

									}
								}

							newArgForQuery.remove(arg);
							index = 1;

							JOptionPane.showMessageDialog(null, "Please select SEND to continue the dialogue!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);

						}
					}










					else			
						if(cbQuestioner.getSelectedIndex() >= 0) {

							Argument arg = (Argument) cbQuestioner.getSelectedItem();
							forestTreeFor = findPathsforArgument(arg, forestTrees);
							List<List<Argument>> oddPaths = findOddPathsForArg(arg, forestTreeFor); //le
							//oddPaths = notSubList(oddPaths);
							List<List<Argument>> evenPaths = findEvenPathsForArg(arg, forestTreeFor); // chan
							//	System.out.println("odd paths: " + oddPaths);
							//	System.out.println("even paths: " + evenPaths);



							if ((oddPaths.size() == 0) && (evenPaths.size() != 0)) { // not accepted
								if (arg.body.isEmpty()) {
									List<Argument> argsAt = new ArrayList<>();
									argsAt = findArgumentsAt(1, evenPaths);
									//	System.out.println(argsAt);
									txtExp.append("\nAnswerer: I am certain that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									txtExp.append(", because we know that ");
									List<Argument> nextArg = new ArrayList<>();
									List<Argument> newList = new ArrayList<>();
									//for (Argument a : argsAt) {	
									for (int i = 0; i < argsAt.size(); i++) {
										Argument a = argsAt.get(i);
										// a case that the argument a is the last place in argsAt. Should have a case check whether a.body.isnotempty. In this code, we assume that a.body.isEmpty
										if (i == argsAt.size()-1) {
											txtExp.append(App1.convertAtom(a.head));
										} else {
											txtExp.append(App1.convertAtom(a.head));	
											txtExp.append("; or ");
										}
									}

									// remove all path has length =2
									List<List<Argument>> newPaths = new ArrayList<>();
									for(int m = 0; m < evenPaths.size(); m++) {
										List<Argument> path = evenPaths.get(m);
										if (path.size() > 2) {
											newPaths.add(path);
										}
									}

									// newList considers the elements at i =1 (i.e., the second place in the path) where path.size() >2
									evenPaths = newPaths;	
									for (List<Argument> path : evenPaths) {
										newList.add(path.get(1));
									}
									if (newList.isEmpty()) {
										txtExp.append(". I was right that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
										cbQuestioner.removeAllItems();
										subModel.addElement("Ok! I understood");
										cbQuestioner.setModel(subModel);
									} else {
										cbQuestioner.removeAllItems();
										for (Argument temp : newList) {
											nextArg.addAll(findNextArgs(temp, 1, evenPaths));
										}
										previousArgs.clear();
										previousArgs.addAll(newList);
										//	System.out.println("previous: " + previousArgs);
										//	System.out.println("Next: " + nextArg);
										//add elements in NextArg to cbQuestioner.
										for (Argument next : nextArg) {
											cbQuestioner.addItem(next);
										}
										index = 2; // consider element at the third place in the path
									}
								}

								else if (!arg.body.isEmpty()) {// chua giai quyet duoc
									txtExp.append("\nAnswerer: I am certain that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									txtExp.append(", because we know that ");
									txtExp.append(printArg(newArgForQuery.get(0)));

									// bla bla
								}
								newArgForQuery.remove(arg);

							}
							//else if ((oddPaths.size() != 0) && (evenPaths.size() != 0)) { 
							//do nothing

							//} 
							else if ((oddPaths.size() == 0) && (evenPaths.size() == 0)) { // grounded semantics with no conflict
								if (arg.body.isEmpty()) {
									txtExp.append("\nAnswerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									txtExp.append(". Because there is no conflict information against " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									cbQuestioner.removeAllItems();
									subModel.addElement("Ok! I understood");
									cbQuestioner.setModel(subModel);
								} else {
									txtExp.append("\nAnswerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
									txtExp.append(", because we know that ");							
									txtExp.append(printArg(arg));
									txtExp.append(", we also know that there is no conflict information against " + App1.convertListAtoms(arg.getPremises()));
									cbQuestioner.removeAllItems();
									subModel.addElement("Ok! I understood");
									cbQuestioner.setModel(subModel);

								}
								newArgForQuery.remove(arg);
							}

							else if ((oddPaths.size() != 0) && (evenPaths.size() == 0)) { // sckeptical and grounded semantics
								List<Argument> nextArgs = new ArrayList<>();
								nextArgs = findNextArgs(arg, 0, oddPaths);
								previousArgs.clear();
								previousArgs.add(arg);
								//	System.out.println("previous: " + previousArgs);
								if (arg.body.isEmpty()) { 
									//txtExp.setText("");
									txtExp.append("\nAnswerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));

									if (!nextArgs.isEmpty()) {
										cbQuestioner.removeAllItems();
										for (Argument a : nextArgs) {
											cbQuestioner.addItem(a);
										}

									} else {
										txtExp.append(". Because there is no conflict information against " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
										cbQuestioner.removeAllItems();
										subModel.addElement("Ok! I understood");
										cbQuestioner.setModel(subModel);

									}
								} else 	
									if (!arg.body.isEmpty()) {
										txtExp.append("\nAnswerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));						
										txtExp.append(", because we know that ");							
										txtExp.append(printArg(arg));
										if (!nextArgs.isEmpty()) {
											cbQuestioner.removeAllItems();
											for (Argument a : nextArgs) {
												cbQuestioner.addItem(a);
											}

										} else {
											cbQuestioner.removeAllItems();
											subModel.addElement("Ok! I understood");
											cbQuestioner.setModel(subModel);

										}
									}

								newArgForQuery.remove(arg);
								index = 1;

							}
							JOptionPane.showMessageDialog(null, "Please select SEND to continue the dialogue!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);


						}
				} catch  (IteratorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					if (subModel.getSelectedItem().equals("Ok! I understood")) {
						cbQuestioner.removeAllItems();
						txtExp.append("\nQuestioner: OK! I understood");					
						JOptionPane.showMessageDialog(null, "The dialogue ended!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						subModel.removeAllElements();
					}
					else {
						//get the current items in cbQuestioner
						List<Argument> currentArgs = new ArrayList<>();
						int num = cbQuestioner.getItemCount();
						for (int i = 0; i < num; i++) {
							Argument item = cbQuestioner.getItemAt(i);
							currentArgs.add(item);
						}

						System.out.println("current args: " + currentArgs);

						int curentIndex = index;
						Argument selectedArg = (Argument) cbQuestioner.getSelectedItem();
						System.out.println("curent Index : " + curentIndex + " selectedArg: " + selectedArg );


						if (cbQuestioner.getSelectedIndex() == -1) {
							JOptionPane.showMessageDialog(null, "Select an utterance!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						}
						else if(cbQuestioner.getSelectedIndex() >= 0) {
							Argument arg = (Argument) cbQuestioner.getSelectedItem();
							System.out.print("arg: " + arg + " forestTress: " + forestTrees);

							//List<Argument> nextArgs = findNextArgs(selectedArg, curentIndex, forestTrees);
							List<Argument> nextArgs1 = findNextArgs(arg, curentIndex, forestTrees);
							txtExp.append("\nQuestioner: That is not true. ");
							for (Argument pre : previousArgs) {
								if (previousArgs.lastIndexOf(pre) == previousArgs.size()-1) {
									if (!pre.body.isEmpty()) {
										txtExp.append(App1.convertNegListAtoms(pre.getPremises()) + ", because " + App1.convertAtom(arg.head) );
										if (!arg.body.isEmpty()) {
											txtExp.append("(This is implied from ");
											for (Atom bodyArg : arg.getPremises()) {
												if (bodyArg.equals(arg.getPremises().get(arg.getPremises().size() - 1))) {
													txtExp.append(App1.convertAtom(bodyArg) + ")");
												} else {
													txtExp.append(", and " + App1.convertAtom(bodyArg));
												}
											}
										}
									}
									else {
										txtExp.append(App1.convertNegAtom(pre.head) + ", because " + App1.convertAtom(arg.head) );
										if (!arg.body.isEmpty()) {
											txtExp.append("(This is implied from ");
											for (Atom bodyArg : arg.getPremises()) {
												if (bodyArg.equals(arg.getPremises().get(arg.getPremises().size() - 1))) {
													txtExp.append(App1.convertAtom(bodyArg) + ")");
												} else {
													txtExp.append(", and " + App1.convertAtom(bodyArg));
												}
											}
										}
									}
								} else {
									if (!pre.body.isEmpty()) {
										txtExp.append(App1.convertNegListAtoms(pre.getPremises()) + "; or ");
									}
									else 
										txtExp.append(App1.convertNegAtom(pre.head) + "; or ");

								}
							}
								System.out.println("Next arg1:" + nextArgs1);
							if (nextArgs1.isEmpty()) {
									System.out.println("Next arg empty");
								cbQuestioner.removeAllItems();
								subModel.addElement("Ok! I understood");
								cbQuestioner.setModel(subModel);								
							} else
								if (!nextArgs1.isEmpty()) {
									txtExp.append("\nAnswerer: " + App1.convertNegListAtoms(arg.getPremises()) + ", because " + App1.convertAtom(nextArgs1.get(0).head) );
									currentArgs.remove(selectedArg);
									List<Argument> newArgs1 = currentArgs;
									if (newArgs1.isEmpty()) {
										if(cbExp.getSelectedItem().equals("Why is the answer certain?") || cbExp.getSelectedItem().equals("Why is the answer accepted under grounded semantics?")) {
											txtExp.append(". I was right that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
										} else  if (cbExp.getSelectedItem().equals("Why is not the answer?")) {
											txtExp.append(". I was right that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
										}
										cbQuestioner.removeAllItems();
										subModel.addElement("Ok! I understood");
										cbQuestioner.setModel(subModel);

									} else {
										cbQuestioner.removeAllItems();
										for (int j = 0; j < newArgs1.size(); j++) {
											cbQuestioner.addItem(newArgs1.get(j));
										}
									}
								}
							previousArgs.clear();
							previousArgs.add(arg);
							System.out.println("previousArgs after adding " + arg);
							index = index + 2;		
						}
					}

				} catch (IteratorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});

		btnEnd = new JButton("Terminate current dialogue");
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbQuestioner.removeAllItems();
				subModel.removeAllElements();
				txtExp.setText("");
				txtExp.setText("Questioner: " + cbExp.getSelectedItem().toString());				
				listToRemove.add(removedItIndex);
				if (cbExp.getSelectedItem().equals("Why is the answer possible?")) {
					if (listToRemove.size() == 2) {
						JOptionPane.showMessageDialog(null, "No dialogue!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						listToRemove.clear();
					} else
						if (removedItIndex == 0) {
							subModel.addElement(itemCr.get(1));
							JOptionPane.showMessageDialog(null, "Please select EXPLAIN to start a dialogue!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						} else {
							subModel.addElement(itemCr.get(0));	
							JOptionPane.showMessageDialog(null, "Please select EXPLAIN to start a dialogue!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						}
				}
				else 
					JOptionPane.showMessageDialog(null, "No dialogue!",
							"Message Dialog", JOptionPane.PLAIN_MESSAGE);

				// tra lai List of Args at index = 1


			}
		});

		btnShift = new JButton("Shift another dialogue");
		btnShift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbQuestioner.removeAllItems();
				if (newArgForQuery.size() == 0) {
					txtExp.setText("");
					JOptionPane.showMessageDialog(null, "No dialogue! Please select SHIFT ANOTHER QUERY to start a new query.",
							"Message Dialog", JOptionPane.PLAIN_MESSAGE);
				} else {
					index = 0;
					previousArgs.clear();
					txtExp.setText("");
					txtExp.setText("\nQuestioner: " + cbExp.getSelectedItem().toString());
					if (cbExp.getSelectedItem().equals("Why is the answer possible?")) {
						txtExp.setText("");
						btnChoose.setVisible(true);
					}
					cbQuestioner.removeAllItems();					
					for (Argument arg : newArgForQuery) {
						cbQuestioner.addItem(arg);
					}

				}
			}
		});






		JLabel lblNewLabel = new JLabel("Dispute Trees");

		JLabel lblNewLabel_1 = new JLabel("Dialogue Box");

		JLabel lblUtter = new JLabel("Utterance");

		JButton btnQuery = new JButton("Query");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strQuery = "? :- " + txtfQuery.getText() + ".";
				try {
					if (tempAtt == null &&  ListArgument == null) {
						JOptionPane.showMessageDialog(null, "KB was not converted to Argumentaion. Please choose TRANSFER before querying!",
								"Message Dialog", JOptionPane.PLAIN_MESSAGE);
						txtfQuery.setText("");
					} else {
						query = DlgpParser.parseQuery(strQuery);
						txtTree.setText("");
						argumentForQuery = new ArrayList<Argument>();
						argumentForQuery = App1.GetArgumentForQuery(query, ListArgument, saturatedAtom);
						//	System.out.println("Set of Arguments for Query: " + argumentForQuery);

						if(argumentForQuery.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Error!",
									"Message Dialog", JOptionPane.PLAIN_MESSAGE);
							txtfQuery.setText("");						
						} else {

							// check IAR semantic or Grounded extension
							int count = 0;
							if (App1.includesArrayList(grd, argumentForQuery) == true) {
								txtTree.append(App1.AtomSetIntoArrayWithoutArity(query.getAtomSet()).toString() + " is accepted under grounded extenions" + ".");
							} 
							else {
								// Check credulous, skepcitcal, non-accept for argument			
								for (Argument arg: argumentForQuery) {
									for (int i=0; i<extensions.size(); i++) {
										if (extensions.get(i).contains(arg)){
											count++;
										}
									}
								}
								if (count == 0){//non-acceptance
									txtTree.append(App1.AtomSetIntoArrayWithoutArity(query.getAtomSet()).toString() + " is not an answer" + ".");
								} else // sceptical acceptance
									if ((count == extensions.size()) || (count/extensions.size() ==  extensions.size())) {
										txtTree.append(App1.AtomSetIntoArrayWithoutArity(query.getAtomSet()).toString() + " is a certain answer" + ".");

									}
									else // credulous acceptance
										txtTree.append(App1.AtomSetIntoArrayWithoutArity(query.getAtomSet()).toString() + " is a possible answer" + ".");
							}
							txtTree.append("\n");					

							//	System.out.println();

							Dist = new ArrayList<Distance>();
							for (int i=0; i < ListArgument.size(); i++) {
								Argument a = ListArgument.get(i);
								//Reach.add(a);
								Dist.add(new Distance(a,a,0));
								for (int j=0; j < ListArgument.size(); j++) {
									if (j != i) {
										Argument b = ListArgument.get(j);
										Dist.add(new Distance(a, b, 0 ));
									}
								}
							}

							if ((count == extensions.size()) || (count/extensions.size() ==  extensions.size())) { // certain
								forestTrees = new ArrayList<>();
								ArrayList<ArrayList<Argument>> printDef = new ArrayList<>();
								for (Argument a : argumentForQuery) {
									Visited = new ArrayList<Attack>();
									NewDist = new ArrayList<Distance>();
									Reach = new ArrayList<Argument>();
									NewReReach(a, a, 0, null, SetOfAtts);
									ArrayList<Argument> reachOdd = new ArrayList<>();
									ArrayList<Argument> reachEven = new ArrayList<>();
									reachOdd = App1.getReachOdd(a, NewDist);
									reachEven = App1.getReachEven(a, NewDist);
									ArrayList<Argument> DefBy = reachEven;

									// compute a set of extension for an argument and merge them into one set.
									Set<Argument> merge = new HashSet<Argument>();
									for (int i = 0; i < extensions.size(); i++) {
										ArrayList<Argument> subDef = App1.intersection(DefBy, extensions.get(i));
										merge.addAll(subDef);				
									}
									ArrayList<Argument> DefByExt = new ArrayList<>(merge);
									printDef.add(DefBy);

									//print paths from A to B
									Graph graph = new Graph();
									//addEdge to graph
									for (int i = 0; i < tempAtt.size(); i++) {
										Attack at = (Attack) tempAtt.get(i);
										graph.addEdge(at.target, at.source);
									}
									Argument start = a;
									List<List<Argument>> paths = new ArrayList<>();

									for (Argument b : DefByExt) {
										Argument end = b;
										if (!start.equals(end)) {
											paths = graph.printAllPathsOdd(start, end);
											for(List<Argument> path : paths) {
												forestTrees.add(path);										
											}
										}
									}
								}
								//IN RA EXPLANTION - VIET DOAN NAY
								txtTree.append("Explanation for the certain answer: <{ " + argumentForQuery + "}, { " + printDef);

								txtTree.append("\nDispute Forest: \n");
								for (List<Argument> t : forestTrees) {
									txtTree.append(printPathUser(t) + "\n");
								}

								txtExp.setText("");
								cbExp.addItem("Why is the answer certain?");
							} 
							else	if (App1.includesArrayList(grd, argumentForQuery) == true) { //grounded semantic
								forestTrees = new ArrayList<>();
								ArrayList<ArrayList<Argument>> printDef = new ArrayList<>();

								for (Argument a : argumentForQuery) {
									Visited = new ArrayList<Attack>();
									NewDist = new ArrayList<Distance>();
									Reach = new ArrayList<Argument>();
									NewReReach(a, a, 0, null, SetOfAtts);
									ArrayList<Argument> reachOdd = new ArrayList<>();
									ArrayList<Argument> reachEven = new ArrayList<>();
									reachOdd = App1.getReachOdd(a, NewDist);
									reachEven = App1.getReachEven(a, NewDist);
									printDef.add(reachEven);

									//print paths from A to B
									Graph graph = new Graph();
									//addEdge to graph
									for (int i = 0; i < tempAtt.size(); i++) {
										Attack at = (Attack) tempAtt.get(i);
										graph.addEdge(at.target, at.source);
									}

									Argument start = a;
									List<List<Argument>> paths = new ArrayList<>();

									for (Argument b : grd) {
										Argument end = b;
										if (!start.equals(end)) {											
											paths = graph.printAllPaths(start, end);
											for(List<Argument> path : paths) {
												forestTrees.add(path);
												//	txtTree.append(printPathUser(path) + "\n");
											}
										}
									}				
								}
								// there is no attacks
								if (forestTrees.isEmpty()) {
									txtTree.append("Explanation for acceptance under grounded semantics: <{ " + argumentForQuery + "}, {null}>");
									txtTree.append("\nThere is no tree because " + query.getAtomSet().toString() + " do not have conflicting information.");
								} else {
									txtTree.append("Explanation for acceptance under grounded semantics: <{ " + argumentForQuery + "}, { " + printDef + "}>");
									txtTree.append("\nDispute Trees: \n");
									for (List<Argument> t : forestTrees) {
										txtTree.append(printPathUser(t) + "\n");
									}
								}

								txtExp.setText("");
								//cbExp.removeAllItems();
								cbExp.addItem("Why is the answer accepted under grounded semantics?");
								cbExp.setSelectedItem(null);



							}
							else if (count == 0) { // non-acceptance
								forestTrees = new ArrayList<>();
								ArrayList<ArrayList<Argument>> printAtt = new ArrayList<>();
								for (Argument a : argumentForQuery) {
									Visited = new ArrayList<Attack>();
									NewDist = new ArrayList<Distance>();
									Reach = new ArrayList<Argument>();
									NewReReach(a, a, 0, null, SetOfAtts);

									ArrayList<Argument> reachOdd = new ArrayList<>();
									ArrayList<Argument> reachEven = new ArrayList<>();
									reachOdd = App1.getReachOdd(a, NewDist);
									printAtt.add(reachOdd);

									//Compute NotDef for an argument wrt extensions
									ArrayList<ArrayList<Argument>> NotDefOfA = App1.computeNotDefBy(reachOdd, extensions, NewDist);
									// merge all extensions to one
									Set<Argument> set = new HashSet<Argument>();
									for (int i = 0; i < NotDefOfA.size(); i++) {
										set.addAll(NotDefOfA.get(i));				
									}
									ArrayList<Argument> combinedList = new ArrayList<>(set);

									//print paths from A to B
									Graph graph = new Graph();
									//addEdge to graph
									for (int i = 0; i < tempAtt.size(); i++) {
										Attack at = (Attack) tempAtt.get(i);
										graph.addEdge(at.target, at.source);
									}
									Argument start = a;
									List<List<Argument>> paths = new ArrayList<>();
									for (Argument b : combinedList) {
										Argument end = b;
										if (b.myID != a.myID) {
											paths = graph.printAllPathsEven(start, end);
											for(List<Argument> path : paths) {
												forestTrees.add(path);
											}
										}
									}
								} 
								txtTree.append("Explanation for the non-acceptance answer: <{ " + argumentForQuery + "}, { " + printAtt + " }>");
								txtTree.append("\nDispute Trees: \n");
								for (List<Argument> t : forestTrees) {
									txtTree.append(printPathUser(t) + "\n");
								}

								txtExp.setText("");
								cbExp.addItem("Why is not the answer?");
								cbExp.setSelectedItem(null);
							}
							else {// credulous
								forestTrees = new ArrayList<>();
								ArrayList<ArrayList<Argument>> printAtt = new ArrayList<>();
								ArrayList<ArrayList<Argument>> printDef = new ArrayList<>();
								for (Argument a : argumentForQuery) {
									Visited = new ArrayList<Attack>();
									NewDist = new ArrayList<Distance>();
									Reach = new ArrayList<Argument>();
									NewReReach(a, a, 0, null, SetOfAtts);

									ArrayList<Argument> reachOdd = new ArrayList<>();
									ArrayList<Argument> reachEven = new ArrayList<>();
									reachOdd = App1.getReachOdd(a, NewDist);
									reachEven = App1.getReachEven(a, NewDist);
									ArrayList<Argument> DefBy = reachEven;
									printDef.add(DefBy);
									printAtt.add(reachOdd);
									//Compute Def for an argument wrt extensions

									Set<Argument> merge = new HashSet<Argument>();
									for (int i = 0; i < extensions.size(); i++) {
										ArrayList<Argument> subDef = App1.intersection(DefBy, extensions.get(i));
										merge.addAll(subDef);				
									}
									ArrayList<Argument> DefByExt = new ArrayList<>(merge);

									ArrayList<ArrayList<Argument>> NotDefOfA = App1.computeNotDefBy(reachOdd, extensions, NewDist);
									Set<Argument> set = new HashSet<Argument>();
									for (int i = 0; i < NotDefOfA.size(); i++) {
										set.addAll(NotDefOfA.get(i));				
									}
									ArrayList<Argument> combinedList = new ArrayList<>(set);

									Graph graph = new Graph();
									//addEdge to graph
									for (int i = 0; i < tempAtt.size(); i++) {
										Attack at = (Attack) tempAtt.get(i);
										graph.addEdge(at.target, at.source);
									}

									Argument start = a;

									//print paths to explain the non-acceptance
									List<List<Argument>> pathsForNon = new ArrayList<>();
									for (Argument b : combinedList) {
										Argument end1 = b;
										if (b.myID != a.myID) {
											pathsForNon = graph.printAllPathsEven(start, end1);
											for(List<Argument> path : pathsForNon) {
												forestTrees.add(path);										
											}
										}
									}
									List<List<Argument>>  paths = new ArrayList<>();
									for (Argument b : DefByExt) {
										Argument end = b;	
										if (a.myID != b.myID) {
											paths = graph.printAllPaths(start, end);
											for(List<Argument> path : paths) {
												forestTrees.add(path);										
											}
										} 
									}
								}

								txtTree.append("\nFor the credulous answer, explanation includes: ");
								txtTree.append("\n(1). Explaining why the answer is accepted under some extensions: <{ " 
										+ argumentForQuery 
										+ "}, { "
										+ printDef 
										+ "}>" );

								txtTree.append("\n(2). Explaining why the answer is not accepted under some extensions: <{ " 
										+ argumentForQuery 
										+ "}, { "
										+ printAtt 
										+ "}>" );


								txtTree.append("\nDispute Trees: \n");
								for (List<Argument> t : forestTrees) {
									txtTree.append(printPathUser(t) + "\n");
								}

								txtExp.setText("");	
								cbExp.addItem("Why is the answer possible?");
								cbExp.setSelectedItem(null);

							}
						}
					}

				}	catch (IteratorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		//btnQuery.setVisible(false);

		JLabel lblQuery = new JLabel("Please enter a query for consideration");

		txtfQuery = new JTextField();
		txtfQuery.setColumns(10);

		JButton btnChangeQuery = new JButton("Shift another query");
		btnChangeQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cbExp.removeAllItems();
				cbExp.addItem("Please choose a question to explanation");
				cbQuestioner.removeAllItems();
				txtExp.setText("");
				txtfQuery.setText("");
				txtTree.setText("");
			}
		});




		GroupLayout gl_bottomPane = new GroupLayout(bottomPane);
		gl_bottomPane.setHorizontalGroup(
				gl_bottomPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bottomPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_bottomPane.createSequentialGroup()
										.addGroup(gl_bottomPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_bottomPane.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_bottomPane.createSequentialGroup()
																.addGroup(gl_bottomPane.createParallelGroup(Alignment.LEADING)
																		.addGroup(gl_bottomPane.createSequentialGroup()
																				.addComponent(btnQuery, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
																				.addGap(18)
																				.addComponent(btnChangeQuery))
																		.addComponent(lblQuery, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
																.addGap(125))
														.addGroup(gl_bottomPane.createParallelGroup(Alignment.TRAILING, false)
																.addComponent(txtfQuery, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)
																.addComponent(cbQuery, Alignment.LEADING, 0, 433, Short.MAX_VALUE))))
										.addGap(85))
								.addGroup(gl_bottomPane.createSequentialGroup()
										.addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGap(36)
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane4, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_bottomPane.createSequentialGroup()
										.addComponent(lblUtter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(cbQuestioner, GroupLayout.PREFERRED_SIZE, 372, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_bottomPane.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(btnShift, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(Alignment.LEADING, gl_bottomPane.createSequentialGroup()
												.addComponent(btnChoose)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnExplain, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnEnd, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)))
								.addComponent(cbExp, 0, 446, Short.MAX_VALUE))
						.addContainerGap())
				);
		gl_bottomPane.setVerticalGroup(
				gl_bottomPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bottomPane.createSequentialGroup()
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblQuery)
								.addComponent(cbExp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtfQuery, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUtter)
								.addComponent(cbQuestioner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnQuery)
								.addComponent(btnChangeQuery)
								.addComponent(btnChoose)
								.addComponent(btnExplain)
								.addComponent(btnSend)
								.addComponent(btnEnd))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbQuery, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnShift))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_bottomPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(25, Short.MAX_VALUE))
				);
		txtTree = new JTextArea();
		scrollPane3.setViewportView(txtTree);
		txtTree.setEditable(false);
		bottomPane.setLayout(gl_bottomPane);
		mainPane.setLayout(gl_mainPane);


	}

	private static void credulousAnswer() {
		try {
			txtExp.append("Answerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
			txtExp.append(". Because we know that ");							
			txtExp.append(printArg(argumentForQuery.get(0)));
			int count1 = 1; 		
			while (argumentForQuery.size() > count1) {
				txtExp.append(", we also know ");
				txtExp.append(printArg(argumentForQuery.get(count1)));						
				count1++;
			}
			txtExp.append(". \n");			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static void notAnswer() {
		try {
			txtExp.append("Answerer: I claim that " + App1.convertNegListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));			
			if (argumentForQuery.get(0).body.isEmpty()) {
				txtExp.append(".\n");
			}			
			else {
				txtExp.append(". Because we know that ");							
				txtExp.append(printArg(argumentForQuery.get(0)));
				int count1 = 1; 		
				while (argumentForQuery.size() > count1) {
					if (!argumentForQuery.get(count1).body.isEmpty()) {
						txtExp.append(", we also know ");
						txtExp.append(printArg(argumentForQuery.get(count1)));
					}
					count1++;
				}
				txtExp.append(". \n");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static List<Argument> findArgumentsAt(int index, List<List<Argument>> forestTrees){
		List<Argument> argsAt = new ArrayList<>();
		for (List<Argument> tree : forestTrees) {
			for (int i=0; i<tree.size(); i++) {
				argsAt.add(tree.get(index));
			}
		}

		List<Argument> uniqueArgs = argsAt.stream()
				.distinct()
				.collect(Collectors.toList());
		return uniqueArgs;
	}

	private static void certainAnswer() {
		try {
			txtExp.append("Answerer: I am certain that " + App1.convertListAtoms(App1.convertToArrayListAtom(query.getAtomSet())));
			if(!argumentForQuery.get(0).body.isEmpty()) {
				txtExp.append(". Because we know that ");							
				txtExp.append(printArg(argumentForQuery.get(0)));
				int count1 = 1; 		
				while (argumentForQuery.size() > count1) {
					if (!argumentForQuery.get(count1).body.isEmpty() ) {
						txtExp.append(", we also know ");
						txtExp.append(printArg(argumentForQuery.get(count1)));
					}
					count1++;
				}
			}
			txtExp.append(". \n");	
			cbQuestioner.removeAllItems();
			List<Argument> argsAt = new ArrayList<>();
			argsAt = findArgumentsAt(1,forestTrees);
			for (Argument arg : argsAt) {
				cbQuestioner.addItem(arg);
			}

			/*for (Argument arg : argumentForQuery) {
				if (!arg.body.isEmpty()) {
					cbQuestioner.addItem("But " + App1.convertNegListAtoms(arg.getPremises()));
				}
				else 
					cbQuestioner.addItem("But " + App1.convertNegAtom(arg.head));
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}





	private static List<List<Argument>> findPathsforArgument (Argument arg, List<List<Argument>> forestTrees ){
		List<List<Argument>> newForestTrees = new ArrayList<>();
		for (List<Argument> tree : forestTrees) {
			if (tree.contains(arg)) {
				newForestTrees.add(tree);
			}
		}
		return newForestTrees;
	}




	private static List<List<Argument>> findOddPathsForArg (Argument arg, List<List<Argument>> forestTrees ){
		List<List<Argument>> newForestTrees = new ArrayList<>();
		for (List<Argument> tree : forestTrees) {
			if (tree.contains(arg)) {
				if (tree.size() % 2 != 0 ) {
					newForestTrees.add(tree);
				}
			}
		}
		return newForestTrees;
	}

	private static List<List<Argument>> findEvenPathsForArg (Argument arg, List<List<Argument>> forestTrees ){
		List<List<Argument>> newForestTrees = new ArrayList<>();
		for (List<Argument> tree : forestTrees) {
			if (tree.contains(arg)) {
				if (tree.size() % 2 == 0 ) {
					newForestTrees.add(tree);
				}
			}
		}
		return newForestTrees;
	}

	/*private static int findPathsAtIndex (Argument arg, int index, List<List<Argument>> forestTrees){
		List<List<Argument>> newForestTrees = new ArrayList<>();
		for (List<Argument> tree : forestTrees) {
			tree.
		}

		return newForestTrees;
	}*/

	//private static void loadToComboBox (Argument arg) {

	//}

	private static List<Argument> findNextArgs (Argument arg, int index, List<List<Argument>> Paths) {
		List<Argument> nextArgs = new ArrayList<>();
		for (List<Argument> path : Paths) {
			if(path.size() > 2) {
				for (int i = 0; i < path.size(); i++) {			
					if ( path.get(i).equals(arg) && i == index ) {
						nextArgs.add(path.get(i+1));
					}
				}
			}
		}
		return nextArgs;
	}
	private static boolean checkArgAtPath2 (Argument a, List<List<Argument>> Paths) {
		List<Boolean> result = new ArrayList();
		for (List<Argument> path : Paths) {
			if (path.contains(a)){
				if ((path.size() == 2) && (path.indexOf(a) == path.size()-1)) {
					result.add(true);
				}
			}
			else
				result.add(false);
		}
		for (Boolean value : result) {
			if (value == false) {
				return false;
			}
		}
		return true;
	}



	private static List<List<Argument>> notSubList (List<List<Argument>> evenTrees) {
		List<List<Argument>> newEvenTrees = new ArrayList<>(evenTrees);
		List<List<Argument>> list1 = new ArrayList<>(evenTrees);
		List<List<Argument>> list2 = new ArrayList<>(evenTrees);

		for (int i = 0; i< list1.size() -1; i++) {
			List<Argument> first = evenTrees.get(i);

			for (int j = 0; j <list2.size(); j++) {
				List<Argument> second = evenTrees.get(j);
				if (first.containsAll(second)) {
					if (isSubsetWithCommonPosition(second, first) == true ) {
						newEvenTrees.remove(j);				
					}
				}
			}
		}
		return newEvenTrees;
	}

	public static boolean isSubsetWithCommonPosition(List<Argument> array1, List<Argument> array2) {
		if (array1.size() > array2.size()) {
			return false; // array1 can't be a subset with common positions if it's longer
		}

		for (int i = 0; i < array1.size(); i++) {
			if (array1.get(i) != array2.get(i)) {
				return false; // Elements at the same position are not equal
			}
		}

		return true; // All elements from array1 are found at the same positions in array2
	}



	public static void NewReReach(Argument a, Argument b, int n, ArrayList<Attack> S, ArrayList<Attack> Attacks){
		Visited0 = S;

		//get all attacks having the target as b and the source as c
		ArrayList<Attack> Att = App1.GetAttacksFromArg(b,Attacks);
		//System.out.println("Att: " + Att);
		for(Attack at : Att) {

			for (int i = 0; i < at.source.size(); i++) {
				Argument c = at.source.get(i);
				if ((Visited0 == null) || (!Visited.contains(at))) {
					if (!Reach.contains(c)) {
						Reach.add(c);
					}
					Distance distance = App1.DistFromAtoB(c, a, Dist);
					distance.dist = n+1;
					NewDist.add(distance);
					Visited.add(at);
					if (c.equals(a) ) {
						continue;
					} else {
						NewReReach(a, c , n+1, Visited, Attacks);
						Visited = Visited0;
					}

				}
			}
		}
	}
	private String[] getComboBoxItems(List<Argument> path) {
		String[] items = new String[path.size()];
		for (int i = 0; i < path.size(); i++) {
			items[i] = path.get(i).toString();
		}
		return items;
	}

	private static String printPathUser(List<Argument> path) {
		StringBuilder s = new StringBuilder();

		s.append(stringArg(path.get(0)));
		s.append(" <= ");
		for (int i =1; i < path.size() - 1; i++) {
			s.append(stringArg(path.get(i)));
			s.append(" <= ");
		}
		s.append(stringArg(path.get(path.size()-1)));			
		return s.toString();
	}

	private static String stringArg(Argument a) {	
		String result = "A" + a.myID + " :";
		if (a.body.isEmpty()) {
			result = result + App1.AtomWithoutArity(a.head);
		} else {
			result = result + App1.AtomSetIntoArrayWithoutArity1(a.getPremises()) + "->" + App1.AtomWithoutArity(a.head);
		}
		return result;

	}

	private static String printArg(Argument a) {
		String result = new String();
		if (a.body.isEmpty()) {
			result = "";
		}
		else {								
			for (Atom at : a.getPremises()) {
				result = App1.convertAtom(at);
			}
		}
		return result;
	}
}
