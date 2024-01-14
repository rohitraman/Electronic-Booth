package com.example.electronic.booth.service.repository;

import com.example.electronic.booth.service.bean.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {
    Login findByUserNameAndPassword(String username, String password);
    Login findByUserName(String username);
    void deleteByUserName(String username);
}
