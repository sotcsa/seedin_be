package com.nearsg.jobportal.endpoint;

import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.jpa.UserRepository;
import com.nearsg.jobportal.model.UserRequest;
import com.nearsg.jobportal.util.EthUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserEndpoint {

    private final UserRepository userRepository;

    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User loggedInUser() {
        return getUser();
    }

    @PostMapping
    public User updateUser(@RequestBody UserRequest userRequest) {
        User user = getUser();

        user.setAboutMe(userRequest.getAboutMe());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setNickName(userRequest.getNickName());

        return userRepository.save(user);
    }

    private User getUser() {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String address = user.getUsername();
        if (EthUtil.isEthAddress(address)) {
            return userRepository.findByEthAddress(address);
        } else {
            return userRepository.findByNearAddress(address);
        }

    }

    /**
     * TODO remove this endpoint, only for debugging purpose
     * @return all users
     */
    @GetMapping("/events")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
