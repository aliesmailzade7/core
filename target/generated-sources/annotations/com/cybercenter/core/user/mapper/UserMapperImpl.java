package com.cybercenter.core.user.mapper;

import com.cybercenter.core.constant.LoginMethodType;
import com.cybercenter.core.dto.RegisterRequestDTO;
import com.cybercenter.core.constant.Education;
import com.cybercenter.core.dto.UserInfoDTO;
import com.cybercenter.core.entity.Role;
import com.cybercenter.core.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

import com.cybercenter.core.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-10T11:06:14+0330",
    comments = "version: 1.5.4.Final, compiler: javac, environment: Java 20.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserInfoDTO toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        userInfoDTO.setId( entity.getId() );
        userInfoDTO.setUsername( entity.getUsername() );
        userInfoDTO.setFirstName( entity.getFirstName() );
        userInfoDTO.setLastName( entity.getLastName() );
        userInfoDTO.setBirthDay( entity.getBirthDay() );
        userInfoDTO.setEmail( entity.getEmail() );
        userInfoDTO.setPhoneNumber( entity.getPhoneNumber() );
        userInfoDTO.setJob( entity.getJob() );
        userInfoDTO.setOrientation( entity.getOrientation() );
        userInfoDTO.setEnable( entity.isEnable() );
        List<Role> list = entity.getRoles();
        if ( list != null ) {
            userInfoDTO.setRoles( new ArrayList<Role>( list ) );
        }

        userInfoDTO.setEducation( !ObjectUtils.isEmpty(entity.getEducation()) ? entity.getEducation().getId() : null );
        userInfoDTO.setEducationTitle( !ObjectUtils.isEmpty(entity.getEducation()) ? entity.getEducation().getTitle() : null );

        return userInfoDTO;
    }

    @Override
    public User ToEntity(RegisterRequestDTO registerRequestDTO) {
        if ( registerRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( registerRequestDTO.getId() );
        user.setUsername( registerRequestDTO.getUsername() );
        user.setPassword( registerRequestDTO.getPassword() );
        user.setFirstName( registerRequestDTO.getFirstName() );
        user.setLastName( registerRequestDTO.getLastName() );
        user.setEmail( registerRequestDTO.getEmail() );
        user.setPhoneNumber( registerRequestDTO.getPhoneNumber() );
        if ( registerRequestDTO.getLoginMethodType() != null ) {
            user.setLoginMethodType( Enum.valueOf( LoginMethodType.class, registerRequestDTO.getLoginMethodType() ) );
        }
        user.setEnable( registerRequestDTO.isEnable() );
        List<Role> list = registerRequestDTO.getRoles();
        if ( list != null ) {
            user.setRoles( new ArrayList<Role>( list ) );
        }

        return user;
    }

    @Override
    public void updateUserInfo(User user, UserInfoDTO userInfoDTO) {
        if ( userInfoDTO == null ) {
            return;
        }

        user.setFirstName( userInfoDTO.getFirstName() );
        user.setLastName( userInfoDTO.getLastName() );
        user.setBirthDay( userInfoDTO.getBirthDay() );
        user.setEmail( userInfoDTO.getEmail() );
        user.setPhoneNumber( userInfoDTO.getPhoneNumber() );
        user.setJob( userInfoDTO.getJob() );
        user.setOrientation( userInfoDTO.getOrientation() );

        user.setEducation( !ObjectUtils.isEmpty(userInfoDTO.getEducation()) ? Education.of(userInfoDTO.getEducation()) : null );
    }
}
