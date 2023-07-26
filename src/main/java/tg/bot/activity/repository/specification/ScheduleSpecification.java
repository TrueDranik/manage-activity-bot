package tg.bot.activity.repository.specification;

import com.bot.sup.model.RouteActivityRequestParams;
import com.bot.sup.model.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class ScheduleSpecification implements Specification<Schedule> {
    private final RouteActivityRequestParams params;

    @Override
    public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        SpecificationBuilder<Schedule> specificationBuilder = new SpecificationBuilder<>(root, query, builder);
        specificationBuilder.addEqualsPredicateIfNotNull("activity", params.getActivityId());
        specificationBuilder.addEqualsPredicateIfNotNull("route", params.getRouteId());

        return builder.and(specificationBuilder.build());
    }
}
