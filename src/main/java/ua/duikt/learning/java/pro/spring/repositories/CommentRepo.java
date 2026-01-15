package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.IssueComment;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Repository
public interface CommentRepo extends JpaRepository<IssueComment, Long> {
    List<IssueComment> findAllByIssueId(Long issueId);
}
