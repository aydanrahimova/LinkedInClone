package az.matrix.linkedinclone.service.specification;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dto.request.JobFilterDto;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JobSpecification implements Specification<Job> {
    private final JobFilterDto jobFilterDto;

    @Override
    public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        if (jobFilterDto.getOrganizationName() != null && !jobFilterDto.getOrganizationName().isEmpty()) {
            Join<Job, Organization> organizationJoin = root.join("organization", JoinType.INNER);
            predicates.add(builder.equal(organizationJoin.get("name"), jobFilterDto.getOrganizationName()));
        }

        if (jobFilterDto.getTitle() != null && !(jobFilterDto.getTitle().isEmpty())) {
            predicates.add(builder.like(builder.lower(root.get("title")), "%" + jobFilterDto.getTitle() + "%"));
        }

        if (jobFilterDto.getEmploymentType() != null) {
            predicates.add(builder.equal(root.get("employmentType"), jobFilterDto.getEmploymentType()));
        }

        if (jobFilterDto.getSkills() != null && !jobFilterDto.getSkills().isEmpty()) {
            for (String skill : jobFilterDto.getSkills()) {
                Join<Job, Skill> skillJoin = root.join("skills", JoinType.INNER);
                predicates.add(builder.equal(skillJoin.get("name"), skill));
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
