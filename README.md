# SAF-Argumentation
Logic-based argumentation framework with collective attacks. We are building a new Darg system to allow human-AI interactions, in which it allows users to query and receive explanatory dialogue about why the query is (not) accepted under certain/ credulous/ grounded semantics. The Darg system is built based on the SAF framework. At the first step of building the Darg system, we provide a graphical representation of explanatory dialogues, i.e., dialogue trees.

# Requirements
- This is a Java implementation: Full java 11 support, java 11 is a requirement.
  
- Install OWL API
  
- It currently supports: DLGP input that supports Datalog+-, logic programming facts.
  
- For OWL2 and RDF/XML(Turtle), you need to run the following command to translate into DLGP files:

  java -jar owl2dlgp-*.jar -f ./example-owl.ttl -o ./example.dlp

or download the owl2dlgp tool: 

Link: https://graphik-team.github.io/graal/downloads/owl2dlgp

# Status
This application is still under development. We will update a new version to support OWL2, RDF and DLGP input without perform 

# Useage
- To use, create a location where you store the DLGP. You can store datasets in the created folder.
- Download and install Eclipse
- Clone a repository to your local computer using a Github link.
- Go to the directory where your source code is, i.e., .\SAF-Argumentation\code
- Use the command to run: javac Experiment1.java



# Support
If you are having issues, please let us know via github.



