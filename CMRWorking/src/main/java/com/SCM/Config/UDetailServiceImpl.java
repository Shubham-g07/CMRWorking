package com.SCM.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.SCM.Entities.User;
import com.SCM.Repository.userRepository;

public class UDetailServiceImpl implements UserDetailsService {

    @Autowired
    userRepository userrepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userrepository.getUserByUserName(username);

        if(user == null) {
            throw new UsernameNotFoundException("Could not found user !!");
        }

        CustomUserDetails CUD = new CustomUserDetails(user);


        return CUD;
    }

}
