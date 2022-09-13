package com.nearsg.jobportal.endpoint;

import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.exception.DataNotFound;
import com.nearsg.jobportal.jpa.UserRepository;
import com.nearsg.jobportal.model.UserRequest;
import com.nearsg.jobportal.util.EndpointUtil;
import com.nearsg.jobportal.util.EthUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserEndpoint {

    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository userRepository
     */
    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets details of logged-in user.
     *
     * @return User
     */
    @Operation(summary = "Gets details of logged-in user")
    @GetMapping
    public User loggedInUser() {
        User user= getUser();
        if (user == null) {
            throw new DataNotFound();
        }
        return user;
    }

    /**
     * Gets all user details
     *
     * @return list of all users
     */
    @Operation(summary = "Gets all user details")
    @GetMapping("all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates user details.
     * Wallet addresses are read-only.
     *
     * @param userRequest userRequest
     * @return the updated User
     */
    @Operation(summary = "Updates user details (except addresses)")
    @PostMapping
    public User updateUser(@RequestBody UserRequest userRequest) {
        User user = getUser();
        if (user == null) {
            throw new DataNotFound();
        }

        user.setAboutMe(userRequest.getAboutMe());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setNickName(userRequest.getNickName());

        return userRepository.save(user);
    }

    /**
     * Retrieves the logged-in user object or null if not found.
     *
     * @return
     */
    private User getUser() {
        String address = EndpointUtil.getLoggedInAddress();
        if ("anonymousUser".equalsIgnoreCase(address)) {
            return null;
        } else if (EthUtil.isEthAddress(address)) {
            return userRepository.findByEthAddress(address);
        } else {
            return userRepository.findByNearAddress(address);
        }

    }

}
