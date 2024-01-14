package com.example.electronic.booth.service.security;

import com.example.electronic.booth.service.bean.Login;
import com.example.electronic.booth.service.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginRepository.findByUserName(username);
        if (login != null) {
            return new MyUserDetails(new Login(login.getUserName(), login.getPassword(), login.getUser()));
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
