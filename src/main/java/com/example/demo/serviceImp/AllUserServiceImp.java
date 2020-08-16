package com.example.demo.serviceImp;

import com.example.demo.dao.model.UserEntity;
import com.example.demo.dao.model.StudentEntity;
import com.example.demo.dao.model.TeacherEntity;
import com.example.demo.dao.model.enums.RoleUser;
import com.example.demo.dao.repository.AllUserRepository;
import com.example.demo.dao.repository.StudentRepository;
import com.example.demo.dao.repository.TeacherRepository;
import com.example.demo.dto.AllUserDTO;
import com.example.demo.service.AllUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Service
public class AllUserServiceImp implements AllUserService {

    @Autowired
    AllUserRepository allUserRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    private static final String ROLE = "STUDENT";

    @Transient
    @Override
    public List<UserEntity> findAllUser() {
        return allUserRepository.findAll();
    }

    @Transient
    @Override
    public void save(UserEntity allUser) {
        if (allUserRepository.existsByEmail(allUser.getEmail()))
            return;
        allUserRepository.save(allUser);
    }

    @Transient
    @Override
    public void update(String email) {

        RoleUser roleUser = allUserRepository.findByEmail(email).getRole();
        String rool = roleUser.toString();

        if (rool.equals(ROLE)) {
            UserEntity userEntity = allUserRepository.findByEmail(email);
            userEntity.setRole(RoleUser.TEACHER);

            StudentEntity studentEntity = studentRepository.findByEmail(email);
            TeacherEntity teacherEntity = new TeacherEntity(userEntity.getEmail(),
                    userEntity.getName(), userEntity.getPictureURL());

            teacherRepository.save(teacherEntity);

            studentRepository.delete(studentEntity);
        } else {
            UserEntity userEntity = allUserRepository.findByEmail(email);
            userEntity.setRole(RoleUser.STUDENT);

            TeacherEntity teacherEntity = teacherRepository.findByEmail(email);
            StudentEntity studentEntity = new StudentEntity(userEntity.getEmail(),
                    userEntity.getName(), userEntity.getPictureURL());
            studentRepository.save(studentEntity);

            teacherRepository.delete(teacherEntity);
        }
    }

    @Transient
    @Override
    public UserEntity findByLogin(String email) {

        return allUserRepository.findByEmail(email);
    }

    @Transient
    @Override
    public List<AllUserDTO> findAllUserPagebal(Pageable pageable) {

        List<AllUserDTO> allUserDTOS = new ArrayList<>();

        allUserRepository.findAllUserEntitiesBy(pageable).forEach(x -> allUserDTOS.add(x.toAllUserDTO()));
        return allUserDTOS;
    }
}
