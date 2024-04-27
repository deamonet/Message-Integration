package me.deamonet.mi.service.implement;

import me.deamonet.mi.entity.User;
import me.deamonet.mi.mapper.UserMapper;
import me.deamonet.mi.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImplement implements UserService {

    @Resource
    UserMapper mapper;

    @Override
    public User getUserById(int uid) {
        return mapper.selectUserById(uid);
    }
}
