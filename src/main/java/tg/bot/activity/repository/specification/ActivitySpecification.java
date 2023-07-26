package tg.bot.activity.repository.specification;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.entity.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class ActivitySpecification implements Specification<Activity> {

    private final ActivityRequestParams params;

    @Override
    public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        SpecificationBuilder<Activity> specificationBuilder = new SpecificationBuilder<>(root, query, builder);
        specificationBuilder.addEqualsPredicateIfNotNull("activityType", params.getActivityTypeId());
        specificationBuilder.addEqualsPredicateIfNotNull("activityFormat", params.getActivityFormatId());
        specificationBuilder.addEqualsPredicateIfNotNull("route", params.getRouteId());

        return builder.and(specificationBuilder.build());
    }
}
