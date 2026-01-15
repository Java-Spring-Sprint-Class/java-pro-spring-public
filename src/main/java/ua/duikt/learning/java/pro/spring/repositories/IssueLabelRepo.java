package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.IssueLabel;

@Repository
public interface IssueLabelRepo extends JpaRepository<IssueLabel, Long> {

    void deleteByIssueIdAndLabelId(Long issueId, Long labelId);

    boolean existsByIssueIdAndLabelId(Long issueId, Long labelId);
}