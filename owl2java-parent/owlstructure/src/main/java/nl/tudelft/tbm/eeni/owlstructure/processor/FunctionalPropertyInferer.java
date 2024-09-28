package nl.tudelft.tbm.eeni.owlstructure.processor;

import nl.tudelft.tbm.eeni.owlstructure.utils.OntologyUtils;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.shared.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Find functional properties by looking at instances in a given ontology
 */
public class FunctionalPropertyInferer implements IOntologyProcessor {

	Logger log = LoggerFactory.getLogger(FunctionalPropertyInferer.class);

	/**
	 * Creates a new functional property inferer, that tries to detect
	 * function properties by looking at instances in the ontology.
	 */
	public FunctionalPropertyInferer() {
	}

	/**
	 * Run the functional propery inferer on all properties in the given ontology
	 *
	 * @param ontModel	the ontology model to work on
	 */
	@Override
	public OntModel process(OntModel ontModel) {
		// Look for properties that at least one instance uses once and no instances use more than once
		String queryString = OntologyUtils.getSparqlPrefixes(ontModel)
				+ "select ?property (max(?count) as ?max) { "
				+ "  select ?property (count(?o) as ?count) "
				+ "  where { "
				+ "    ?s ?property ?o . "
				+ "    ?property rdf:type ?t "
				+ "  } "
				+ "  group by ?s ?property "
				+ "} "
				+ "group by ?property "
				+ "having (?max = 1) ";

		ontModel.enterCriticalSection(Lock.READ);
		try {
			Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
			QueryExecution qexec = QueryExecutionFactory.create(query, ontModel);

			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				// We've found a property that seems to be functional
				OntProperty property = results.nextSolution().get("property").as(OntProperty.class);

				// Convert the property to a functional property
				property.convertToFunctionalProperty();

				// Output a debug message
				log.info("Functional property inference: " + property.getLocalName() + " is functional");
			}
			qexec.close();
		} finally {
			ontModel.leaveCriticalSection();
		}

		return ontModel;
	}
}
