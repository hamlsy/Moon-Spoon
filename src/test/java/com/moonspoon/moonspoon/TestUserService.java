package com.moonspoon.moonspoon;

import com.moonspoon.moonspoon.exception.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service

public class TestUserService {
    @Autowired
    private TestUserRepository repository;
    @Transactional
    public void signup(TestUser testUser){
        testUser.setCreateDate(LocalDateTime.now());
        if(repository.existsByName(testUser.getName())){
            throw new DuplicateUserException("존재하는 이름입니다.");
        }
        try{
            repository.save(testUser);
        }catch (ObjectOptimisticLockingFailureException e){
            throw new IllegalStateException("동시성 문제 발견됨", e);
        }
    }
    @Transactional
    public void signupPessimistic(TestUser testUser){
        testUser.setCreateDate(LocalDateTime.now());
        if(repository.findByNameForUpdate(testUser.getName()).isPresent()){
            throw new DuplicateUserException("존재하는 이름입니다.");
        }
        repository.save(testUser);
    }

}
