package com.example.demo.service;

import com.example.demo.dao.model.UserEntity;
import com.example.demo.dto.AllUserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AllUserService {


    public List<UserEntity> findAllUser();

    public UserEntity findByLogin(String email);

    public void save(UserEntity allUser);

    public void update(String email);

    public List<AllUserDTO> findAllUserPagebal(Pageable pageable);

}
