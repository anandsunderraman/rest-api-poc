package org.example.rest;

import cc.api.model.v1.model.Error;
import cc.api.model.v1.model.User;
import cc.api.model.v1.resource.UserResource;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserResourceImpl implements UserResource {

    private final UserService userService;

    @Autowired
    public UserResourceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public GetUserByIdResponse getUserById(Integer id) throws Exception {

        if (id < 0) {
            Error error = new Error()
                    .withMessage("Id cannot be negative")
                    .withErrorType(Error.ErrorType.BADREQUEST);

            return GetUserByIdResponse.withJsonNotFound(error);
        }

        Optional<org.example.domain.User> domainUser = userService.getUserById(new Long(id));

        if (domainUser.isPresent()) {
            return GetUserByIdResponse.withJsonOK(from(domainUser.get()));
        } else {
            String errorMessage = String.format("Unable to find with user with id: %d", id);
            Error error = new Error()
                    .withMessage(errorMessage)
                    .withErrorType(Error.ErrorType.BADREQUEST);
            return GetUserByIdResponse.withJsonNotFound(error);
        }
    }

    /**
     * Convert domain object to REST response object
     * @param domainUser
     * @return
     */
    private User from(org.example.domain.User domainUser) {
        User restResponseUser =  new User();
        restResponseUser.setAge(domainUser.getAge());
        restResponseUser.setId(domainUser.getId());
        restResponseUser.setName(domainUser.getName());
        return restResponseUser;
    }

    @Override
    public PostUserResponse postUser(User userPayload) throws Exception {
        //convert the user entity to domain user object
        org.example.domain.User domainUser = new org.example.domain.User(userPayload);

        //save the user via the service
        org.example.domain.User savedUser = userService.save(domainUser);

        //return the saved user as the response
        return PostUserResponse.withJsonOK(savedUser.convertToRestResponse());
    }

    @Override
    public PutUserByIdResponse putUserById(Integer id, User entity) throws Exception {
        return null;
    }
}
