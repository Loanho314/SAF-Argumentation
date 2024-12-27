# SAF-Argumentation
Logic-based argumentation framework with collective attacks. We are building a new Darg system to allow human-AI interactions, in which it allows users to query and receive explanatory dialogue about why the query is (not) accepted under certain/ credulous/ grounded semantics. The Darg system is built based on the SAF framework. At the first step of building the Darg system, we provide a graphical representation of explanatory dialogues, i.e., dialogue trees.

# Requirements
- This is a Java implementation: Full java 11 support, java 11 is a requirement.  
- Install OWL API
- Download and install Eclipse
- It currently supports: DLGP input that supports Datalog+-, logic programming facts.
- For OWL2 and RDF/XML in Turtle format, you need to run the following command to convert into DLGP format:
  
  java -jar owl2dlgp-*.jar -f ./example-owl.ttl -o ./example.dlp

- Or download the owl2dlgp tool to use:
Link: https://graphik-team.github.io/graal/downloads/owl2dlgp

# Useage
The code has been made available for reproducing the results we show in our paper. To make sure that it is possible we would refer to the CODE folder.
Though the code is still in active development, it is possible that the software will improve over time.
Perform the following steps:
- To use, create a location where you store the DLGP. You can store datasets in the created folder.
- Clone a repository to your local computer using a Github link.
- Go to the directory where your source code is, i.e., .\SAF-Argumentation\code
- Use the command to run:
   javac Experiment1.java

# Status
This application is still under development. We will update a new version to support OWL2, RDF and DLGP input without performing the translation steps. 

# Support
If you are having issues, please let us know via github.



