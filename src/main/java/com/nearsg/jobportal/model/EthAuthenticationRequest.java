package com.nearsg.jobportal.model;

import lombok.Data;

@Data
public class EthAuthenticationRequest {
    String publicAddress;
    String signature;
}
