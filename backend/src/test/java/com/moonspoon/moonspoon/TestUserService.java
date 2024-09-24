package com.moonspoon.moonspoon;

import com.moonspoon.moonspoon.common.exception.custom.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service

public class TestUserService {
    @Autowired
    private TestUserRepository repository;

    @Transactional
    public void signupUnique(TestUser testUser){
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

    public synchronized void signupSynchronized(TestUser testUser){
        testUser.setCreateDate(LocalDateTime.now());
        if(repository.existsBySynName(testUser.getSynName())){
            throw new DuplicateUserException("존재하는 이름입니다.");
        }
        try{
            repository.save(testUser);
        }catch (ObjectOptimisticLockingFailureException e){
            throw new IllegalStateException("동시성 문제 발견됨", e);
        }
    }
}
