# SAF-Argumentation
Logic-based argumentation framework with collective attacks. We are building a new Darg system to allow human-AI interactions, in which it allows users to query and receive explanatory dialogue about why the query is (not) accepted under certain/ credulous/ grounded semantics. The Darg system is built based on the SAF framework. At the first step of building the Darg system, we provide a graphical representation of explanatory dialogues, i.e., dialogue trees.

# Requirements
- This is a Java implementation: Full java 19 support, java 19 is a requirement.
- Maven
- It currently supports: DLGP input that supports Datalog+-, logic programming facts.
- For OWL2 and RDF/XML in Turtle format, you need to run the following command to convert into DLGP format:  
  java -jar owl2dlgp-*.jar -f ./example-owl.ttl -o ./example.dlgp

- Or download the owl2dlgp tool to use:
Link: https://graphik-team.github.io/graal/downloads/owl2dlgp

# Datasets
You can download datasets from the links below:

Experiment 1: https://surfdrive.surf.nl/files/index.php/s/zhfEj8cpzvPtswD/download

Experiment 2: https://surfdrive.surf.nl/files/index.php/s/40E05sVJymo19DZ/download

Experiment 3:
1. DBpedia 
2. Yago
3. LOV

# Useage
The code has been made available for reproducing the results we show in our paper. To make sure that it is possible we would refer to the CODE folder.
Though the code is still in active development, it is possible that the system will improve.

Perform the following steps:
- To use, create a location where you store the DLGP. You can store datasets in the created folder, i.e., the "data" folder.
- Clone a repository to your local computer using a Github link.
- Go to the directory where your source code is, i.e., .\SAF-Argumentation\code
- Package the project by using the following command:
  mvn clean install
- After compiling, the jar file is in the target folder, and it contains all the dependencies. Next, use the command to run:
  
   java -jar -Xmx4G target/my-graal-app-1.0-jar-with-dependencies.jar arg0 arg1
  
  where - arg0 is an input, for example, arg0 = "yago.dlgp"  
       - arg1 is an output, for example, arg1 = "output/excutiontime.txt"
  
- An alternative way is to run a command directly without compiling the project:
  
      mvn exec:java -Dexec.mainClass="fr.lirmm.graphik.NAry.Experiment1" arg0 arg1
  
Note: This project can also be used as a Java library. After packing the project, add the project as a Maven dependency to your project and profit.

# Status
This application is still under development. We will update a new version to support OWL2, RDF and DLGP input without performing the translation steps. 

# Support
If you are having issues, please let us know via github.



