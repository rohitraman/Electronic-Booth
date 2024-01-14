package com.example.electronic.booth.service.service;

import com.example.electronic.booth.service.bean.Login;
import com.example.electronic.booth.service.bean.LoginResponse;
import com.example.electronic.booth.service.bean.Response;
import com.example.electronic.booth.service.interfaces.LoginInterface;
import com.example.electronic.booth.service.repository.LoginRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginInterface {
    LoginRepository loginRepository;

    LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    @Override
    public ResponseEntity<Response> login(Login login) {
        Login retreivedLogin = loginRepository.findByUserNameAndPassword(login.getUserName(), login.getPassword());
        if (retreivedLogin == null) {
            return new ResponseEntity<>(new Response("User not found.", 404), HttpStatus.NOT_FOUND);
        }
        LoginResponse loginResponse = new LoginResponse(retreivedLogin.getUser().getName(), retreivedLogin.getUser().getEmailId(), retreivedLogin.getUser().getPhone(), retreivedLogin.getUser().getVoterId(), retreivedLogin.getUserName(), retreivedLogin.getUser().getRole());
        return new ResponseEntity<>(new Response(loginResponse, 200), HttpStatus.OK);
    }
}
