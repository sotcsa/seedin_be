package com.nearsg.jobportal.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class EndpointUtil {

    public static String getLoggedInAddress() {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}
