package nl.tudelft.tbm.eeni.owlstructure.processor;

import org.apache.jena.ontology.OntModel;

public interface IOntologyProcessor {
	OntModel process(OntModel ontModel);
}
