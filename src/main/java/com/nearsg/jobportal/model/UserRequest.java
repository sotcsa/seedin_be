package com.nearsg.jobportal.model;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String nickName;
    private String aboutMe;
//    private String ethAddress;
//    private String nearAddress;
}
