package com.yildirimog.walletapi.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yildirimog.walletapi.auth.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    
    Optional<User> findByEmail(String email);
}
