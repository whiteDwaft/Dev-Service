package com.whitedwaft.devservice.controllers;


import com.whitedwaft.devservice.JwtTokenProvider;
import com.whitedwaft.devservice.dao.MiniUserDao;
import com.whitedwaft.devservice.dao.UserDao;
import com.whitedwaft.devservice.repositories.UsersRepository;
import com.whitedwaft.devservice.services.MongoService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController

public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UsersRepository users;

    @Autowired
    private MongoService mongoService;


    @SuppressWarnings("rawtypes")
    @PostMapping("/logon")
    public ResponseEntity login(@RequestBody UserDao data) {
        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.users.findByEmail(username).getRole());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }
    @GetMapping("/")
    public ResponseEntity hello()
    {
        return new ResponseEntity("hello",HttpStatus.OK);
    }
    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody MiniUserDao user) {
        UserDao userExists = mongoService.findByEmail(user.getEmail());
        if (userExists != null) {
            throw new BadCredentialsException("User with username: " + user.getEmail() + " already exists");
        }
        mongoService.saveUser(new UserDao(user.getEmail(),user.getPassword()),user.getRole());
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User registered successfully");
        return ok(model);
    }

    @GetMapping("/git")
    public String message(HttpServletRequest request1, HttpServletResponse response1) {
        String s = request1.getParameter("code");
        try {
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

                HttpPost request = new HttpPost("https://github.com/login/oauth/access_token");

                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("client_id", "3d52c807e7ca3ffcfa98"));
                urlParameters.add(new BasicNameValuePair("client_secret", "085d169f708c3784f33559c685719e22b277f9e2"));
                urlParameters.add(new BasicNameValuePair("code", s));


                request.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = client.execute(request);
                String res = EntityUtils.toString(response.getEntity());

                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "qwert";
    }


    @GetMapping("/welcome")
    public ResponseEntity findAllUsers()
    {
        return new ResponseEntity(mongoService.findAllUsers(),HttpStatus.OK);
    }
    @GetMapping("/start")
    public void logout(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }
}
