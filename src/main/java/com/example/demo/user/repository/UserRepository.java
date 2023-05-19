package com.example.demo.user.repository;

import com.example.demo.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);

    Users findUsersByPhoneNum(String phoneNum);

    Users findUsersByUsername(String username);


    @Modifying
    @Query("update Users u set u.fullname = :fullname, u.email = :email, u.phoneNum = :phoneNum, u.birthDate = :birthDate where u.id = :id")
    int updateUser(@Param("fullname") String fullname, @Param("email") String email, @Param("phoneNum") String phoneNum, @Param("birthDate") Date birthDate, @Param("id") long id);

    @Modifying
    @Query("DELETE FROM Users u WHERE u.id = :id")
    int deleteUser(@Param("id") long id);

    @Query(value = "select * from users where id= :id", nativeQuery = true)
    Users findUsersById(@Param("id") Long id);
}
