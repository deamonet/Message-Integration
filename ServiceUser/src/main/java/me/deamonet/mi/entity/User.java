package me.deamonet.mi.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer uid;
    private String name;
    private String phoneNumber;
    private String password;
    private String identity;
    private Date registration;
}
