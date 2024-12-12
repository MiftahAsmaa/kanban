package org.mql.kanban.repository;

import org.mql.kanban.model.Project;
import org.mql.kanban.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
    List<Project> findByUserId(Long userId);
}
