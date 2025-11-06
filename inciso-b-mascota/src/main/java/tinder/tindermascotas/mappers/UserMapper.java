package tinder.tindermascotas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tinder.tindermascotas.UserDetailDto;
import tinder.tindermascotas.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDetailDto dto);

    UserDetailDto toDto(User entity);
}