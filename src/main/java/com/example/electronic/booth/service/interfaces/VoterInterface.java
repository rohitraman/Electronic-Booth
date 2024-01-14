package com.example.electronic.booth.service.interfaces;

import com.example.electronic.booth.service.bean.Response;
import com.example.electronic.booth.service.bean.UserRequest;
import com.example.electronic.booth.service.bean.Vote;
import com.example.electronic.booth.service.bean.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface VoterInterface {
    ResponseEntity<Response> createVoter(UserRequest user);
    ResponseEntity<Response> castVote(Vote vote) throws JsonProcessingException;

    ResponseEntity<Response> viewVoters();

    ResponseEntity<Response> getNomineesByCity(String city) throws JsonProcessingException;

    ResponseEntity<Response> deleteVoter(String voterId);

    ResponseEntity<Response> updateVoter(Integer voterId, UserRequest userRequest);
}
