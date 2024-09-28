package nl.tudelft.tbm.eeni.owl2java.model.jenautils;

import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Restriction;

public class RestrictionUtils {

	public static boolean hasRestrictionOnProperty(OntClass cls, OntProperty property) {
		Iterator<Restriction> restrictionIt = property.listReferringRestrictions();
		while (restrictionIt.hasNext()) {
			Restriction restriction = restrictionIt.next();
			Iterator<OntClass> subClassIt = restriction.listSubClasses();
			while (subClassIt.hasNext()) {
				OntClass ontClass = subClassIt.next();
				if (ontClass.equals(cls))
					return true;
			}
		}
		return false;
	}
}
