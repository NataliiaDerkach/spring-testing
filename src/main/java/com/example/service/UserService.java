package com.example.service;

import com.example.dao.UserRepository;
import com.example.entity.User;
import com.example.service.exeptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;


    public User createUser(User user) {
        Optional<User> savedUser = userRepository.findById(user.getId());
        if (savedUser.isPresent()) {
            throw new ResourceNotFoundException("User already exist with given ID!: " + user.getId());
        }
        return userRepository.save(user);
    }

    public List<User> createUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        User oldUser = null;
        Optional<User> optionaluser = userRepository.findById(user.getId());
        if (optionaluser.isPresent()) {
            oldUser = optionaluser.get();
            oldUser.setName(user.getName());
            userRepository.save(oldUser);
        } else {
            return new User();
        }
        return oldUser;
    }

    public String deleteUserById(int id) {
        userRepository.deleteById(id);
        return "User got deleted";
    }

    public String deleteAllUsers() {
        userRepository.deleteAll();
        return "List of Users is empty!";
    }
}
