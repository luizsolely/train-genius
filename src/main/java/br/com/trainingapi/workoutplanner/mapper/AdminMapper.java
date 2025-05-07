package br.com.trainingapi.workoutplanner.mapper;

import br.com.trainingapi.workoutplanner.dto.AdminRequest;
import br.com.trainingapi.workoutplanner.dto.AdminResponse;
import br.com.trainingapi.workoutplanner.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin toEntity(AdminRequest request);

    AdminResponse toResponse(Admin admin);

}
