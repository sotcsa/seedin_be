package com.nearsg.jobportal.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = "foo";
        List<? extends GrantedAuthority> authorities = new ArrayList<>();
        return new User(username, password, authorities);
    }

    public UserDetails loadUserByEthAddress(String publicAddress) throws UsernameNotFoundException {
        String password = "";
        List<? extends GrantedAuthority> authorities = new ArrayList<>();
        return new User(publicAddress, password, authorities);
    }
}
