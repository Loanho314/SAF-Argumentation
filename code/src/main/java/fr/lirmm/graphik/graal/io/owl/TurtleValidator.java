package fr.lirmm.graphik.graal.io.owl;

import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.riot.system.StreamRDFBase;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Quad;

public class TurtleValidator {
    public static void main(String[] args) {
        String turtleFile = "output_part17.ttl";

        try {
            // Parse and validate (throws RiotException if invalid)
            RDFParser.source(turtleFile)
                    .errorHandler(ErrorHandlerFactory.errorHandlerStrict) // Enforce strict mode
                    .lang(Lang.TURTLE)                                   // Explicitly set Turtle format
                    .parse(new SinkRDF());                                          // Triggers validation

            System.out.println("Turtle file is valid!");
        } catch (RiotException e) {
            System.err.println("Invalid Turtle syntax: " + e.getMessage());
        }
    }
    private static class SinkRDF extends StreamRDFBase {
        @Override public void triple(Triple triple) { /* ignore */ }
        @Override public void quad(Quad quad) { /* ignore */ }
    }
}