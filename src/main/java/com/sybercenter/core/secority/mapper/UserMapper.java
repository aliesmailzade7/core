package com.sybercenter.core.secority.mapper;

import com.sybercenter.core.secority.Entity.User;
import com.sybercenter.core.secority.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserDTO toDTO(User entity);

    User ToEntity(UserDTO userDTO);
}
