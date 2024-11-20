package by.zemich.userms.service.mapper;

import by.zemich.userms.controller.request.RegisterRequest;
import by.zemich.userms.controller.request.UpdateUserRequest;
import by.zemich.userms.controller.response.UserFullResponse;
import by.zemich.userms.dao.entity.Role;
import by.zemich.userms.dao.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = ".", target = "active", qualifiedByName = "mapToActive")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapToRole")
    User mapToExistingEntity(RegisterRequest registerRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapToExistingEntity(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    UserFullResponse mapToFullResponse(User user);

    @Named("mapToActive")
    default boolean isActive(RegisterRequest registerRequest){
        Role role = Role.valueOf(registerRequest.getRole());
        return switch (role){
            case ADMIN, JOURNALIST -> false;
            case SUBSCRIBER -> true;
        };
    }

    @Named("mapToRole")
    default Role mapToRole(String role){
        return Role.valueOf(role.toUpperCase());
    }
}
