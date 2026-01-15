package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.IssueHistory;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Repository
public interface IssueHistoryRepo extends JpaRepository<IssueHistory, Long> {
    List<IssueHistory> findAllByIssueId(Long issueId);
}