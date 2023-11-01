package com.w4kened.RefHelper.security;

import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("username="+email);

        UserEntity user = userRepository.findByEmail(email);
        System.out.println(user);

        if (user != null) {
            System.out.println(
                    user.getEmail()+" "+
                            user.getPassword());
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleEntity().getName());
            User authUser = new User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(authority)
            );
            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
