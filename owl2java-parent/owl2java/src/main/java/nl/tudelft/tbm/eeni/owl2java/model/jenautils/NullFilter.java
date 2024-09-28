package nl.tudelft.tbm.eeni.owl2java.model.jenautils;

import java.util.function.Predicate;

public class NullFilter<T> implements Predicate<T> {

    @Override
    public boolean test(T obj) {
        return obj == null;
    }

}
