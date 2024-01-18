package com.example.electronic.booth.service.controller;

import com.example.electronic.booth.service.bean.Response;
import com.example.electronic.booth.service.bean.UserRequest;
import com.example.electronic.booth.service.bean.Vote;
import com.example.electronic.booth.service.interfaces.VoterInterface;
import com.example.electronic.booth.service.security.TokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voter")
public class VoterController {

    @Autowired
    TokenManager tokenManager;
    VoterInterface voterInterface;

    @Value("${ADMIN_USERNAME}")
    String adminUsername;

    VoterController(VoterInterface voterInterface) {
        this.voterInterface = voterInterface;
    }
    @PostMapping("/createVoter")
    public ResponseEntity<Response> createVoter(@RequestHeader("Authorization") String authorization, @RequestBody UserRequest user) {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(adminUsername)) {
            return new ResponseEntity<>(new Response("Unauthorized", 401), HttpStatus.UNAUTHORIZED);
        }
        return voterInterface.createVoter(user);
    }

    @PostMapping("/castVote")
    public ResponseEntity<Response> castVote(@RequestHeader("Authorization") String authorization, @RequestBody Vote vote) throws JsonProcessingException {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);

        if (!username.equals(vote.getEmailId())) {
            return new ResponseEntity<>(new Response("Unauthorized", 401), HttpStatus.UNAUTHORIZED);
        }
        return voterInterface.castVote(vote);
    }

    @GetMapping("/viewVoters")
    public ResponseEntity<Response> viewVoters(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(adminUsername)) {
            return new ResponseEntity<>(new Response("Unauthorized", 401), HttpStatus.UNAUTHORIZED);
        }
        return voterInterface.viewVoters();
    }

    @GetMapping("/getNomineesByCity")
    public ResponseEntity<Response> getNomineesByCity(@RequestHeader("Authorization") String authorization, @RequestParam String city, @RequestParam String id) throws JsonProcessingException {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(id)) {
            return new ResponseEntity<>(new Response("Unauthorized", 401), HttpStatus.UNAUTHORIZED);
        }
        ResponseEntity<Response> responseEntity = voterInterface.getNomineesByCity(city);
        return responseEntity;
    }

    @DeleteMapping("/deleteVoter")
    public ResponseEntity<Response> deleteVoter(@RequestHeader("Authorization") String authorization, @RequestParam String voterId) {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(adminUsername)) {
            return new ResponseEntity<>(new Response("Unauthorized", 401), HttpStatus.UNAUTHORIZED);
        }
        return voterInterface.deleteVoter(voterId);
    }

    @PutMapping("/updateVoter")
    public ResponseEntity<Response> updateVoter(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String authorization, @RequestParam Integer voterId) {
        String token = authorization.substring(7);
        String username = tokenManager.getUsernameFromToken(token);
        if (!username.equals(adminUsername)) {
            return new ResponseEntity<>(new Response("Unauthorized", 401), HttpStatus.UNAUTHORIZED);
        }
        return voterInterface.updateVoter(voterId, userRequest);
    }
}
