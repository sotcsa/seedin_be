package com.nearsg.jobportal;

import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.domain.UserNonce;
import com.nearsg.jobportal.jpa.UserNonceRepository;
import com.nearsg.jobportal.jpa.UserRepository;
import com.nearsg.jobportal.model.AuthenticationRequest;
import com.nearsg.jobportal.model.AuthenticationResponse;
import com.nearsg.jobportal.service.MyUserDetailsService;
import com.nearsg.jobportal.util.EthUtil;
import com.nearsg.jobportal.util.JwtUtil;
import com.nearsg.jobportal.util.NearUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;

    private final UserNonceRepository userNonceRepository;

    private final AuthenticationManager authenticationManager;

    private final MyUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, UserNonceRepository userNonceRepository, AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userNonceRepository = userNonceRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/users")
    public List<User> users() {
        return userRepository.findAll();
    }

    @GetMapping("/auth/nonce")
    public ResponseEntity<?> generateNonceForAddress(@RequestParam String address,
                                                     @RequestParam(required = false) String account) {
        Optional<UserNonce> userNonce = userNonceRepository.findById(address);
        String nonce;
        if (userNonce.isPresent()) {
            nonce = userNonce.get().getNonce();
        } else {
            nonce =  UUID.randomUUID().toString();
            userNonceRepository.save(new UserNonce(address, nonce, account));
        }
        return ResponseEntity.ok(nonce);
    }

    @CrossOrigin( origins = "http://localhost:63342")
    @PostMapping("/auth/near")
    public ResponseEntity<?> authenticateOnNear(@RequestBody AuthenticationRequest authenticationRequest) {
        String publicKey = authenticationRequest.getPublicAddress();

        try {
            if (NearUtil.verifySignature(publicKey, authenticationRequest.getSignature(), getNonce(publicKey))) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(publicKey, ""));
            } else {
                logger.warn("Signed message verification failed for address: {}", publicKey);
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Signed message verification failed");
            }
        }
        catch (Exception e) {
            logger.error("Unhandled exception. Reason:", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error during authentication", e);
        }

        String jwt = "";
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @CrossOrigin( origins = "http://localhost:63342")
    @PostMapping("/auth/eth")
    public ResponseEntity<?> authenticateInEthereum(@RequestBody AuthenticationRequest authenticationRequest) {
        String publicAddress = authenticationRequest.getPublicAddress();
        try {
            if (EthUtil.verifyAddressFromSignature(authenticationRequest, getNonce(publicAddress))) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(publicAddress, ""));
            } else {
                logger.warn("Signed message verification failed for address: {}", publicAddress);
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Signed message verification failed");
            }
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (BadCredentialsException e) {
            logger.warn("Incorrect username or password", e);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Incorrect username or password", e);
        }
        catch (Exception e) {
            logger.error("Unhandled exception. Reason:", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error during authentication", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByEthAddress(publicAddress);
        final String jwt = jwtUtil.generateToken(userDetails);

        // Let's clear user's nonce (avoid reusing nonce, forcing re-sign a new message)
        clearNonce(publicAddress);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    private String getNonce(String address) throws Exception {
        return userNonceRepository.findById(address).orElseThrow(() -> new Exception("Nonce not found for address " + address)).getNonce();
    }

    private void clearNonce(String address) {
        Optional<UserNonce> userNonce = userNonceRepository.findById(address);
        userNonce.ifPresent(userNonceRepository::delete);
    }

}
