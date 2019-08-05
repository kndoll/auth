package com.example.auth.service;

import com.example.auth.domain.Members;
import com.example.auth.domain.LoginUser;
import com.example.auth.repository.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private MembersRepository membersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Members members = membersRepository.findById(username).get();

        return new LoginUser(members);
    }
}
