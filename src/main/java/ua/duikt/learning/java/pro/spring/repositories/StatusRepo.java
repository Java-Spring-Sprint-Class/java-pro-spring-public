package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.Status;

import java.util.List;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Repository
public interface StatusRepo extends JpaRepository<Status, Long> {

    List<Status> findAllByProjectIdOrderByPositionAsc(Long projectId);

    @Query("SELECT MAX(s.position) FROM Status s WHERE s.projectId = :projectId")
    Integer findMaxPositionByProjectId(Long projectId);
}
