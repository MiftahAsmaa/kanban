package org.mql.kanban.repository;

import org.mql.kanban.model.Task;
import org.mql.kanban.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository   extends JpaRepository<User, Long> {

        Optional<User> findByUsername(String username);


}
