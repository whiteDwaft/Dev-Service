package com.whitedwaft.devservice.services;

import com.whitedwaft.devservice.dao.RoleDao;
import com.whitedwaft.devservice.dao.UserDao;
import com.whitedwaft.devservice.repositories.RolesRepository;
import com.whitedwaft.devservice.repositories.UsersRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MongoService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;


    public UserDao findByEmail(String email)
    {
        return usersRepository.findByEmail(email);
    }

    public void saveUser(UserDao user,String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        RoleDao userRole = rolesRepository.findByRole(role);
        user.setRole(new HashSet<>(Arrays.asList(userRole)));
        usersRepository.save(user);
    }

    public List<UserDao> findAllUsers()
    {
        return usersRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDao user = usersRepository.findByEmail(email);
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<RoleDao> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(UserDao user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
