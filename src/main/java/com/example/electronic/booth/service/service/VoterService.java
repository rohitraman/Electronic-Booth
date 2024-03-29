package com.example.electronic.booth.service.service;

import com.example.electronic.booth.service.bean.*;
import com.example.electronic.booth.service.interfaces.VoterInterface;
import com.example.electronic.booth.service.repository.LoginRepository;
import com.example.electronic.booth.service.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VoterService implements VoterInterface {
    LoginRepository loginRepository;
    @Value("${BLOCKCHAIN_VIEW_URL}")
    String viewVotesURL;

    @Value("${VOTER_ROLE}")
    String voterRole;

    @Value("${PASSWORD}")
    String defaultPassword;

    @Value("${BLOCKCHAIN_VOTE_URL}")
    String blockChainURL;

    @Value("${ADMIN_VIEW_NOMINEES}")
    String viewNomineesURL;

    @Value("${NOMINEE_TOKEN_URL}")
    String tokenUrl;

    @Value("${MASTER_API_USERNAME}")
    String masterUsername;

    @Value("${MASTER_API_PASSWORD}")
    String masterPassword;

    @Value("${NOMINEE_VOTE_TOKEN_URL}")
    String voteTokenUrl;

    @Value("${VOTE_TOKEN_USERNAME}")
    String voteUsername;

    @Value("${VOTE_TOKEN_PASS}")
    String votePassword;

    @Value("${PRODUCER_USERNAME}")
    String emailUsername;

    @Value("${PRODUCER_PASSWORD}")
    String emailPassword;

    @Value("${PRODUCER_TOKEN_URL}")
    String producerTokenUrl;

    @Value("${PRODUCER_EMAIL_URL}")
    String producerEmailUrl;

    @Value("${NOMINEE_GET_URL}")
    String nomineeGetUrl;
    UserRepository userRepository;
    RestTemplate restTemplate;

    VoterService(UserRepository userRepository, RestTemplate restTemplate, LoginRepository loginRepository) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.loginRepository = loginRepository;
    }
    @Override
    public ResponseEntity<Response> createVoter(UserRequest user) {
        try {
            User entity = new User(user.getName(), user.getPhone(), user.getEmailId(), user.getVoterId(), user.getCity(), user.getState(), user.getImage());
            entity.setRole(voterRole);
            User savedVoter = userRepository.save(entity);
            String password = user.getPhone() + user.getEmailId();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Login login = new Login(user.getEmailId(), bCryptPasswordEncoder.encode(password), savedVoter);
            loginRepository.save(login);

            return new ResponseEntity<>(new Response(savedVoter, 200), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Response> castVote(Vote vote) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String token = getTokenVote();
        httpHeaders.add("Authorization", "Bearer " + token);

        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(vote);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, httpHeaders);
        Response response = restTemplate.exchange(blockChainURL, HttpMethod.POST, httpEntity, Response.class).getBody();
        if (response.getStatus() == 200) {            
            User user = userRepository.findByEmailId(vote.getEmailId());
            Date d = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");

            String masterToken = getToken();
            HttpHeaders masterHeaders = new HttpHeaders();
            masterHeaders.setContentType(MediaType.APPLICATION_JSON);
            masterHeaders.add("Authorization", "Bearer " + masterToken);
            HttpEntity<String> masterEntity = new HttpEntity<>(masterHeaders);

            ResponseEntity<Response> responseEntity = restTemplate.exchange(nomineeGetUrl + "?id=" + vote.getCandidateId(), HttpMethod.GET, masterEntity, Response.class);
            Object obj = responseEntity.getBody().getObj();
            Nominee nominee = objectMapper.readValue(objectMapper.writeValueAsString(obj), Nominee.class);

            Email email = new Email(vote.getEmailId(), user.getName(), nominee.getName(), nominee.getParty(), simpleDateFormat.format(d), user.getState(), user.getCity());
            
            String emailToken = getEmailToken();
            HttpHeaders emailHeaders = new HttpHeaders();
            emailHeaders.setContentType(MediaType.APPLICATION_JSON);
            emailHeaders.add("Authorization", "Bearer " + emailToken);
            HttpEntity<String> emailEntity = new HttpEntity<>(objectMapper.writeValueAsString(email), emailHeaders);

            ResponseEntity<Void> emailResponse = restTemplate.exchange(producerEmailUrl, HttpMethod.POST, emailEntity, Void.class);
            if (emailResponse.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(new Response("Error occurred", 400), HttpStatus.BAD_REQUEST);
    }

    public List<Vote> getVotes() throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String token = getTokenVote();
        httpHeaders.add("Authorization", "Bearer " + token);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        Response response = restTemplate.exchange(viewVotesURL, HttpMethod.GET, httpEntity, Response.class).getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Vote> voteList = objectMapper.readValue(objectMapper.writeValueAsString(response.getObj()), new TypeReference<>() {});
        return voteList;
    }
    @Override
    public ResponseEntity<Response> viewVoters() {
        List<User> userList = userRepository.findByRole(voterRole);
        List<UserResponse> userResponses = new ArrayList<>();
        try {
            List<Vote> voteList = getVotes();
            for (User user : userList) {
                UserResponse userResponse = new UserResponse(user.getName(), user.getPhone(), user.getEmailId(), user.getVoterId(), user.getEmailId(), user.getCity(), user.getState(), user.getImage());
                userResponse.setId(user.getId());
                for (Vote vote : voteList) {
                    if (user.getEmailId().equals(vote.getEmailId())) {
                        userResponse.setVoted(true);
                        break;
                    }
                }
                userResponses.add(userResponse);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(new Response(userResponses, 200), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getNomineesByCity(String city) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + getToken());
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(viewNomineesURL + "?city=" + city, HttpMethod.GET, httpEntity, Response.class);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Response> deleteVoter(String voterId) {
        try {
            loginRepository.deleteByUserName(voterId);
            userRepository.deleteByEmailId(voterId);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(viewVoters().getBody(), HttpStatus.OK);
    }

    public String getToken() throws JsonProcessingException {
        LoginRequest login = new LoginRequest(masterUsername, masterPassword, "ROLE_VOTER");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(login);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, httpEntity, Response.class);
        return responseEntity.getBody().getObj().toString();
    }

    public String getTokenVote() throws JsonProcessingException {
        LoginRequest login = new LoginRequest(voteUsername, votePassword, "ROLE_VOTER");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(login);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(voteTokenUrl, HttpMethod.POST, httpEntity, Response.class);
        return responseEntity.getBody().getObj().toString();
    }

    @Override
    public ResponseEntity<Response> updateVoter(Integer voterId, UserRequest userRequest) {
        User n = userRepository.findById(voterId).get();
        Login l = loginRepository.findByUserName(n.getEmailId());
        if (userRequest.getCity() != null && !userRequest.getCity().isEmpty()) {
            n.setCity(userRequest.getCity());
        }
        if (userRequest.getState() != null && !userRequest.getState().isEmpty()) {
            n.setState(userRequest.getState());
        }
        if (userRequest.getName() != null && !userRequest.getName().isEmpty()) {
            n.setName(userRequest.getName());
        }
        if (userRequest.getEmailId() != null && !userRequest.getEmailId().isEmpty()) {
            n.setEmailId(userRequest.getEmailId());
            l.setUserName(userRequest.getEmailId());
            loginRepository.save(l);
        }
        if (userRequest.getImage() != null && !userRequest.getImage().isEmpty()) {
            n.setImage(userRequest.getImage());
        }
        if (userRequest.getVoterId() != null && !userRequest.getVoterId().isEmpty()) {
            n.setVoterId(userRequest.getVoterId());
        }
        if (userRequest.getPhone() != null && !userRequest.getPhone().isEmpty()) {
            n.setPhone(userRequest.getPhone());
        }
        userRepository.save(n);
        return new ResponseEntity<>(viewVoters().getBody(), HttpStatus.OK);
    }

    public String getEmailToken() throws JsonProcessingException {
        LoginRequest login = new LoginRequest(emailUsername, emailPassword, "ROLE_VOTER");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(login);

        HttpEntity<String> httpEntity = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(producerTokenUrl, HttpMethod.POST, httpEntity, Response.class);
        return responseEntity.getBody().getObj().toString();
    }
}
