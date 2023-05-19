package com.example.demo.user.controller;

import com.example.demo.common.Response;
import com.example.demo.user.dto.*;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserJwtService;
import com.example.demo.utils.JwtUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


@RestController
public class LoginController {
    @Autowired
    private UserJwtService userJwtService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepo;

    @ModelAttribute("user")
    public UserGGLoginRequest userLoginDTO() {
        return new UserGGLoginRequest();
    }

    @PostMapping("/login/google")
    private UserLoginResponse loginWithGoogle(@Autowired NetHttpTransport transport, @Autowired GsonFactory factory, @RequestBody UserGGLoginRequest request) throws IOException, GeneralSecurityException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, factory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(JwtUtils.GOOGLE_CLIENT_ID))
                // Or, if multiple clients access the backend:
                .build();

        GoogleIdToken idToken = verifier.verify(request.getGoogleToken());
        UserLoginResponse userLoginResponse = new UserLoginResponse();

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);
            // Get profile information from payload
            boolean emailVerified = payload.getEmailVerified();
            String pictureUrl = (String) payload.get("picture");
            if (emailVerified) {
                UsersRegisteredDTO dto = new UsersRegisteredDTO();
                dto.setFullname((String) payload.get("name"));
                dto.setEmail((String) payload.getEmail());
                dto.setUsername(payload.getEmail());
                SaveAccount.users = userJwtService.save(dto);
                String accessToken = jwtUtils.generateToken(payload.getEmail());
                userLoginResponse.setAccessToken(accessToken);
                userLoginResponse.setStatus(200);

            } else {
                userLoginResponse.setStatus(400);
                userLoginResponse.setMessage("Unverified Token");
            }


        } else {
            userLoginResponse.setStatus(400);
            userLoginResponse.setMessage("Invalid ID Token");
        }

        return userLoginResponse;
    }

    @PostMapping("/registry")
    public ResponseEntity<?> registry(@RequestBody UsersRegisteredDTO registeredDTO) throws ExecutionException{
        Users user = userRepo.findUsersByUsername(registeredDTO.getUsername());
        if(user==null){
            Users newuser = userJwtService.save(registeredDTO);
            return Response.response(newuser, 200, "Success");
        }
        return Response.response(null, 400, "Existed phone number");
    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody LoginDto loginDto, HttpServletResponse response) throws ExecutionException {
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        if (userJwtService.checkLogin(loginDto) != null) {
            try {
                userJwtService.loadUserByUsername(loginDto.getPhoneNum());
            } catch (UsernameNotFoundException e) {
                SaveAccount.users = userJwtService.save(loginDto.getPhoneNum());
            }
            userLoginResponse.setAccessToken(jwtUtils.generateToken(loginDto.getPhoneNum()));
            userLoginResponse.setStatus(200);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            userLoginResponse.setStatus(400);
            userLoginResponse.setMessage("Invalid account");
        }

        return userLoginResponse;
    }

    @GetMapping("/user")
    public Users getCurrentUser() {
        return SaveAccount.users;
    }

    @PatchMapping("/user/update/{id}")
    public CommonResponse updateUser(@PathVariable long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userJwtService.updateUser(updateUserRequest.getFullname(), updateUserRequest.getEmail(), updateUserRequest.getPhoneNum(), updateUserRequest.getBirthDate(),id);
    }

    @DeleteMapping("/user/delete/{id}")
    public CommonResponse deleteUser(@PathVariable long id) {
        return userJwtService.deleteUser(id);
    }
}
