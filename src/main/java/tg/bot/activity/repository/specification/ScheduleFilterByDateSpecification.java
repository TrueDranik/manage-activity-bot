package tg.bot.activity.repository.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import tg.bot.activity.model.ScheduleParams;
import tg.bot.activity.model.entity.Schedule;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class ScheduleFilterByDateSpecification implements Specification<Schedule> {
    private final ScheduleParams params;

    @Override
    public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        SpecificationBuilder<Schedule> specificationBuilder = new SpecificationBuilder<>(root, query, builder);
        specificationBuilder.addGreaterThenOrEqualsDatePredicateIfNotNull("eventDate", params.getEventDate());
        specificationBuilder.addEqualsPredicateIfNotNull("isActive", params.getIsActive());

        return builder.and(specificationBuilder.build());
    }
}
