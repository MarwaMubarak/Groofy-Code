package com.groofycode.GroofyCode.service;

import com.groofycode.GroofyCode.dto.ClanDTO;
import com.groofycode.GroofyCode.dto.UserDTO;
import com.groofycode.GroofyCode.mapper.ClanMapper;
import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.repository.ClanRepository;
import com.groofycode.GroofyCode.validator.ClanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClanService {

    @Autowired
    private ClanRepository clanRepository;

    @Autowired
    private ClanMapper clanMapper;

    @Autowired
    private ClanValidator clanValidator;

    public ClanDTO createClan(ClanDTO clanDTO) throws Exception {
        ClanModel clanModel = clanMapper.toModel(clanDTO);
        clanModel = clanRepository.save(clanModel);
        return clanMapper.toDTO(clanModel);
    }

    public List<ClanDTO> getAllClans() {
        List<ClanModel>clans =clanRepository.findAll();

        return clanMapper.toDTOs(clans);
    }

    public ClanDTO getClanById(Long clanId) throws Exception {

        Optional<ClanModel> clanModelOptional = clanRepository.findById(clanId);
        return (clanModelOptional.map(clanModel -> clanMapper.toDTO(clanModel)).orElse(null));
    }

    public ClanDTO updateClan(Long clanId, ClanDTO clanDTO) throws Exception {
        Optional<ClanModel> oldClanModel = clanRepository.findById(clanId);

        //found
        if(oldClanModel.isPresent()){
            ClanDTO oldClanDTO = clanMapper.toDTO(oldClanModel.get());
            ClanDTO newClanDTO = clanValidator.updateValidator(oldClanDTO,clanDTO);
            ClanModel newClanModel = clanMapper.toModel(newClanDTO);
            clanRepository.save(newClanModel);
            return newClanDTO;
        }else
            return null;
    }

    public void deleteClan(Long clanId) {
        clanRepository.deleteById(clanId);
    }

    public ClanDTO joinClan(Long clanId, UserDTO member) throws Exception {
            Optional<ClanModel> clanModel = clanRepository.findById(clanId);
            //found
            if(clanModel.isPresent()) {
                ClanDTO clanDTO = clanMapper.toDTO(clanModel.get());
                ClanDTO newClanDTO = clanValidator.joinValidator(clanDTO,member);
                ClanModel newClanModel = clanMapper.toModel(newClanDTO);
                clanRepository.save(newClanModel);
                return clanDTO;

            }else
                return null;
    }

    public ClanDTO leaveClan(Long clanId, UserDTO member) throws Exception {
        Optional<ClanModel> clanModel = clanRepository.findById(clanId);
        //found
        if(clanModel.isPresent()) {
            ClanDTO clanDTO = clanMapper.toDTO(clanModel.get());
            ClanDTO newClanDTO = clanValidator.leaveValidator(clanDTO,member);
            ClanModel newClanModel = clanMapper.toModel(newClanDTO);
            clanRepository.save(newClanModel);
            return clanDTO;

        }else
            return null;
       }
}
