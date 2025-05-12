package com.luizsolely.traingenius.mapper;

import com.luizsolely.traingenius.dto.UserRequest;
import com.luizsolely.traingenius.dto.UserResponse;
import com.luizsolely.traingenius.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

}
