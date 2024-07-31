package org.apache.fineract.operations;

import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

import static org.springframework.data.jpa.domain.Specifications.where;

public class BatchSpecs {

    public static <T> Specifications<Batch> match(SingularAttribute<Batch, T> attribute, T input) {
        return where((root, query, builder) -> builder.equal(root.get(attribute), input));
    }

    public static Specifications<Batch> between(SingularAttribute<Batch, Date> attribute, Date from, Date to) {
        return where((root, query, builder) -> builder.and(
                builder.greaterThanOrEqualTo(root.get(attribute), from),
                builder.lessThanOrEqualTo(root.get(attribute), to)
        ));
    }

    public static Specifications<Batch> later(SingularAttribute<Batch, Date> attribute, Date from) {
        return where((root, query, builder) -> builder.greaterThanOrEqualTo(root.get(attribute), from));
    }

    public static Specifications<Batch> earlier(SingularAttribute<Batch, Date> attribute, Date to) {
        return where((root, query, builder) -> builder.lessThanOrEqualTo(root.get(attribute), to));
    }
}
