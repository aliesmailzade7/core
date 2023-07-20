package com.cybercenter.core.mapper;

import com.cybercenter.core.dto.RegisterRequestDTO;
import com.cybercenter.core.constant.Education;
import com.cybercenter.core.dto.UserInfoDTO;
import com.cybercenter.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.persistence.PreUpdate;

@Mapper(componentModel = "spring", imports = {Education.class, ObjectUtils.class})
@Component
public interface UserMapper {

    @Mapping(target = "education", expression = "java(!ObjectUtils.isEmpty(entity.getEducation()) ? entity.getEducation().getId() : null)" )
    @Mapping(target = "educationTitle", expression = "java(!ObjectUtils.isEmpty(entity.getEducation()) ? entity.getEducation().getTitle() : null)" )
    UserInfoDTO toDTO(User entity);

    User ToEntity(RegisterRequestDTO registerRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "education", expression = "java(!ObjectUtils.isEmpty(userInfoDTO.getEducation()) ? Education.of(userInfoDTO.getEducation()) : null)" )
    void updateUserInfo(@MappingTarget User user, UserInfoDTO userInfoDTO);
}
