package com.luizsolely.traingenius.mapper;

import com.luizsolely.traingenius.dto.AdminRequest;
import com.luizsolely.traingenius.dto.AdminResponse;
import com.luizsolely.traingenius.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin toEntity(AdminRequest request);

    AdminResponse toResponse(Admin admin);

}
