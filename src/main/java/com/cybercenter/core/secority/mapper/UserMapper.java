package com.cybercenter.core.secority.mapper;

import com.cybercenter.core.secority.dto.UserDTO;
import com.cybercenter.core.secority.Entity.User;
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
