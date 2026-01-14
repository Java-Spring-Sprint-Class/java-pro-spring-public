package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.duikt.learning.java.pro.spring.entity.Issue;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
public interface IssueRepo extends JpaRepository<Issue, Long> {
}
