package org.mql.kanban.service;

import org.mql.kanban.model.User;
import org.mql.kanban.repository.UserRepository; // Adjust this import based on your project structure
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user); // Save the user to the database
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id); // Return user wrapped in Optional
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        System.out.println("user"+username);
        return userRepository.findByUsername(username); // Custom method to find user by username
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Return list of all users
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id); // Delete user by ID
    }
}
