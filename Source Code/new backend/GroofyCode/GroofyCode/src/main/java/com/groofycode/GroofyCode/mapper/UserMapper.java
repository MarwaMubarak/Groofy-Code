package com.groofycode.GroofyCode.mapper;



import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserMapper {

    public  UserDTO toDTO(UserModel userModel) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userModel.getId());
        userDTO.setUsername(userModel.getUsername());
        userDTO.setEmail(userModel.getEmail());
        userDTO.setFirstname(userModel.getFirstname());
        userDTO.setLastname(userModel.getLastname());
        userDTO.setCountry(userModel.getCountry());
        userDTO.setCity(userModel.getCity());
        userDTO.setBio(userModel.getBio());
        if(userModel.getPassword()!=null){
            userDTO.setPassword(userModel.getPassword());
        }
        if (userModel.getClan() != null) {
            userDTO.setClanId(userModel.getClan().getId());
        }
        return userDTO;
    }

    public  UserModel toModel(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        userModel.setId(userDTO.getId());
        userModel.setUsername(userDTO.getUsername());
        userModel.setEmail(userDTO.getEmail());
        userModel.setFirstname(userDTO.getFirstname());
        userModel.setLastname(userDTO.getLastname());
        userModel.setCountry(userDTO.getCountry());
        userModel.setCity(userDTO.getCity());
        userModel.setBio(userDTO.getBio());
        userModel.setPassword(userDTO.getPassword());
        // Setting clanId requires fetching the ClanModel from the database or similar
        // This is typically done in the service layer, not here
        return userModel;
    }

    public List<UserDTO> toDTOs(List<UserModel> userModels) {
        List<UserDTO> dtos = new ArrayList<>();
        for (int i = 0; i < userModels.size(); i++) {
            UserDTO dto = toDTO(userModels.get(i));
            dtos.add(dto);
        }

        return dtos;
    }

    public List<UserModel> toModels(List<UserDTO> clanDTOs) {
        List<UserModel> models = new ArrayList<>();
        for (int i = 0; i < clanDTOs.size(); i++) {
            UserModel model = toModel(clanDTOs.get(i));
            models.add(model);
        }
        return models;
    }
}
