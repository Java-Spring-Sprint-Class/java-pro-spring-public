package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.Label;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 15.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Repository
public interface LabelRepo extends JpaRepository<Label, Long> {

    @Query("SELECT l FROM Label l WHERE l.id IN (SELECT il.labelId FROM IssueLabel il WHERE il.issueId = :issueId)")
    List<Label> findAllByIssueId(Long issueId);
}