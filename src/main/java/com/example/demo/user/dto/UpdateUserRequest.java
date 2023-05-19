package com.example.demo.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserRequest {
    String fullname;
    String email;
    String phoneNum;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;
}
