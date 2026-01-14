package ua.duikt.learning.java.pro.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.duikt.learning.java.pro.spring.entity.Label;

/**
 * Created by Mykyta Sirobaba on 14.01.2026.
 * email mykyta.sirobaba@gmail.com
 */
@Repository
public interface LabelRepo extends JpaRepository<Label, Long> {
}
