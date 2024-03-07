//package com.groofycode.GroofyCode.security;
//
//import com.groofycode.GroofyCode.model.UserModel;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//@Setter
//@Getter
//public class UserModelDetails extends User {
//    private Long id;
//    private String email;
//    private String firstname;
//    private String lastname;
//    private String country;
//    private String city;
//    private String bio;
//    private Long clanId;
//
//
//    public UserModelDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
//    }
//
//    public UserModelDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//    }
//}
