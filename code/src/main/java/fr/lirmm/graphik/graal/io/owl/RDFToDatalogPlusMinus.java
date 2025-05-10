package fr.lirmm.graphik.graal.io.owl;
import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
import org.apache.jena.vocabulary.*;
import org.apache.jena.util.FileManager;
import java.util.*;
import java.io.*;

public class RDFToDatalogPlusMinus {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java RDFToDatalogPlusMinus <input.rdf>");
            return;
        }

        String inputFile = args[0];
        List<String> datalogRules = convertRDFToDatalogPlusMinus(inputFile);

        System.out.println("Generated Datalog± Rules:");
        for (String rule : datalogRules) {
            System.out.println(rule);
        }
    }

    public static List<String> convertRDFToDatalogPlusMinus(String rdfFilePath) {
        List<String> rules = new ArrayList<>();

        // Load RDF/XML file using Jena
        Model model = FileManager.get().loadModel(rdfFilePath);

        // Process classes (rdf:type owl:Class)
        processClasses(model, rules);

        // Process properties (rdf:type owl:ObjectProperty/DatatypeProperty)
        processProperties(model, rules);

        // Process individuals and their assertions
        processIndividuals(model, rules);

        return rules;
    }

    private static void processClasses(Model model, List<String> rules) {
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        
        // Get all classes
        Iterator<OntClass> classes = ontModel.listClasses();
        while (classes.hasNext()) {
            OntClass ontClass = classes.next();
            if (ontClass.isURIResource()) {
                String className = getLocalName(ontClass.getURI());
                
                // Add class as a unary predicate
                rules.add(className + "(X) :- ClassAssertion(" + className + ", X).");
                
                // Process subclass relationships
                Iterator<OntClass> superClasses = ontClass.listSuperClasses();
                while (superClasses.hasNext()) {
                    OntClass superClass = superClasses.next();
                    if (superClass.isURIResource()) {
                        String superClassName = getLocalName(superClass.getURI());
                        rules.add(superClassName + "(X) :- " + className + "(X).");
                    }
                }
            }
        }
    }

    private static void processProperties(Model model, List<String> rules) {
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        
        // Process object properties
        Iterator<ObjectProperty> objectProperties = ontModel.listObjectProperties();
        while (objectProperties.hasNext()) {
            ObjectProperty prop = objectProperties.next();
            if (prop.isURIResource()) {
                String propName = getLocalName(prop.getURI());
                
                // Add property as a binary predicate
                rules.add(propName + "(X, Y) :- PropertyAssertion(" + propName + ", X, Y).");
                
                // Process domain and range
                processPropertyDomain(prop, rules);
                processPropertyRange(prop, rules);
                
                // Process subproperty relationships
                Iterator<ObjectProperty> superProperties = (Iterator<ObjectProperty>) prop.listSuperProperties();
                		
                while (superProperties.hasNext()) {
                    ObjectProperty superProp = superProperties.next();
                    if (superProp.isURIResource()) {
                        String superPropName = getLocalName(superProp.getURI());
                        rules.add(superPropName + "(X, Y) :- " + propName + "(X, Y).");
                    }
                }
            }
        }
        
        // Process datatype properties (similar approach)
        Iterator<DatatypeProperty> datatypeProperties = ontModel.listDatatypeProperties();
        while (datatypeProperties.hasNext()) {
            DatatypeProperty prop = datatypeProperties.next();
            if (prop.isURIResource()) {
                String propName = getLocalName(prop.getURI());
                rules.add(propName + "(X, Y) :- PropertyAssertion(" + propName + ", X, Y).");
                
                processPropertyDomain(prop, rules);
                // Range for datatype properties would involve literals
            }
        }
    }

    private static void processPropertyDomain(Property prop, List<String> rules) {
        Iterator<? extends OntResource> domains = ((OntProperty) prop).listDomain();
        while (domains.hasNext()) {
            OntResource domain = domains.next();
            if (domain.isURIResource()) {
                String domainName = getLocalName(domain.getURI());
                String propName = getLocalName(prop.getURI());
                rules.add(domainName + "(X) :- " + propName + "(X, Y).");
            }
        }
    }

    private static void processPropertyRange(ObjectProperty prop, List<String> rules) {
        Iterator<? extends OntResource> ranges = prop.listRange();
        while (ranges.hasNext()) {
            OntResource range = ranges.next();
            if (range.isURIResource()) {
                String rangeName = getLocalName(range.getURI());
                String propName = getLocalName(prop.getURI());
                rules.add(rangeName + "(Y) :- " + propName + "(X, Y).");
            }
        }
    }

    private static void processIndividuals(Model model, List<String> rules) {
        ResIterator individuals = model.listSubjects();
        while (individuals.hasNext()) {
            Resource individual = individuals.next();
            if (individual.isURIResource()) {
                String individualName = getLocalName(individual.getURI());
                
                // Process types of the individual
                NodeIterator types = model.listObjectsOfProperty(individual, RDF.type);
                while (types.hasNext()) {
                    RDFNode typeNode = types.next();
                    if (typeNode.isURIResource()) {
                        String className = getLocalName(typeNode.asResource().getURI());
                        rules.add(className + "(" + individualName + ").");
                    }
                }
                
                // Process properties of the individual
                StmtIterator properties = individual.listProperties();
                while (properties.hasNext()) {
                    Statement stmt = properties.next();
                    if (stmt.getPredicate().isURIResource()) {
                        String propName = getLocalName(stmt.getPredicate().getURI());
                        if (stmt.getObject().isURIResource()) {
                            String objectName = getLocalName(stmt.getObject().asResource().getURI());
                            rules.add(propName + "(" + individualName + ", " + objectName + ").");
                        } else if (stmt.getObject().isLiteral()) {
                            // For datatype properties
                            String literalValue = stmt.getObject().asLiteral().getLexicalForm();
                            rules.add(propName + "(" + individualName + ", \"" + literalValue + "\").");
                        }
                    }
                }
            }
        }
    }

    private static String getLocalName(String uri) {
        // Simple implementation - for proper handling consider using Jena's ResourceUtils
        int index = uri.lastIndexOf('#');
        if (index < 0) {
            index = uri.lastIndexOf('/');
        }
        return index >= 0 ? uri.substring(index + 1) : uri;
    }
}