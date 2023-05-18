package com.cybercenter.core.user.mapper;

import com.cybercenter.core.user.dto.UserDTO;
import com.cybercenter.core.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User entity);

    User ToEntity(UserDTO userDTO);

}
