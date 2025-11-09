package com.example.chart_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chart_backend.dto.request.ResCreateUserDTO;
import com.example.chart_backend.entity.User;
import com.example.chart_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User handleGetUserByUserNawm(String bhxh) {
        return this.userRepository.findByBhxhNumber(bhxh);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User handleUpdateUser(User user) {
        User userUPdate = this.fetchUserById(user.getId());

        if (userUPdate != null) {

            userUPdate.setUserPhone(user.getUserPhone());

        }
        return this.userRepository.save(userUPdate);
    }

    public User fetchUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public void updateUserToken(String refreshToken, String username) {
        User user = handleGetUserByUserNawm(username);
        if (user != null) {
            userRepository.save(user);
        }
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setUserEmail(user.getUserEmail());
        resCreateUserDTO.setUserFullname(user.getUserFullname());
        resCreateUserDTO.setUserPhone(user.getUserPhone());
        return resCreateUserDTO;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUserFullname(userDetails.getUserFullname());
            user.setUserEmail(userDetails.getUserEmail());
            user.setUserPhone(userDetails.getUserPhone());
            user.setUserPassword(userDetails.getUserPassword());

            user.setBhxhNumber(userDetails.getBhxhNumber());
            user.setCitizenId(userDetails.getCitizenId());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setAddress(userDetails.getAddress());
            user.setAvatarUrl(userDetails.getAvatarUrl());
            user.setCardNumber(userDetails.getCardNumber());
            user.setCardIssuedDate(userDetails.getCardIssuedDate());
            user.setCardExpiryDate(userDetails.getCardExpiryDate());
            user.setHospitalRegistered(userDetails.getHospitalRegistered());
            user.setCardStatus(userDetails.getCardStatus());

            return userRepository.save(user);
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

}
