package com.example.electronic.booth.service.interfaces;

import com.example.electronic.booth.service.bean.Login;
import com.example.electronic.booth.service.bean.Response;
import org.springframework.http.ResponseEntity;

public interface LoginInterface {
    ResponseEntity<Response> login(Login login);
}
