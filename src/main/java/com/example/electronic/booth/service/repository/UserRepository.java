package com.example.electronic.booth.service.repository;

import com.example.electronic.booth.service.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailId(String emailID);
    List<User> findByRole(String role);
    void deleteByEmailId(String emailId);
}
