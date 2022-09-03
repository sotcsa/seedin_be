package com.nearsg.jobportal.model;

import lombok.Data;

@Data
public class AuthenticationRequest {
    String username;
    String password;
}
