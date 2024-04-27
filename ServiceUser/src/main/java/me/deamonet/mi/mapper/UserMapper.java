package me.deamonet.mi.mapper;

import me.deamonet.mi.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectUserById(Integer uid);
    List<User> selectUserByIds(List<Integer> uids);
    Integer insertUser(User user);
    Integer updateUser(User user);
    Integer deleteUserById(Integer uid);
}
