package com.javaweb.myspringcrud.service;

import com.javaweb.myspringcrud.entity.User;
import com.javaweb.myspringcrud.repository.UserRepository;
import com.javaweb.myspringcrud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepository;

    @Override
    public List<User> getAllUser() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findUserById(Long id) {
//        return Optional.empty();
        return userRepository.findById(id);
    }
}
