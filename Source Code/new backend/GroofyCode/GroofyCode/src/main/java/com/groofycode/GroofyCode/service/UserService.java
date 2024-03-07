package com.groofycode.GroofyCode.service;
import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.mapper.UserMapper;
import com.groofycode.GroofyCode.model.UserModel;
import com.groofycode.GroofyCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<UserDTO> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    public UserDTO getBadgeById(Long id) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        return userOptional.map(userModel -> userMapper.toDTO(userModel)).orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserModel user = userMapper.toModel(userDTO);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void deleteBadge(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        //getUser
        Optional<UserModel> user = userRepository.findByUsername(username);
        //if not found return exception
        if(!user.isPresent()){
            throw new UsernameNotFoundException("This Username["+username+"] Not Found");
        }
        // else return userDetails contain username, password, authorities
        Collection<GrantedAuthority> grantedAuthorityList= new ArrayList<>();
        //return new User(user.get().getUsername(),user.get().getPassword(),(grantedAuthorityList));
        // return new UserModelDetails(user.get());
        return new UserModel(user.get().getUsername(),user.get().getPassword(),grantedAuthorityList);

    }
}
