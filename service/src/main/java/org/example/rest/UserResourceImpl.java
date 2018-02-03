package org.example.rest;

import cc.api.model.v1.model.Apimessage;
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
            Apimessage error = new Apimessage()
                    .withMessage("Id cannot be negative")
                    .withStatus(Apimessage.Status.BADREQUEST);

            return GetUserByIdResponse.withJsonNotFound(error);
        }

        Optional<org.example.domain.User> domainUser = userService.getUserById(new Long(id));

        //return response only if user is not deleted
        if (domainUser.isPresent() && !domainUser.get().isDeleted()) {
            return GetUserByIdResponse.withJsonOK(domainUser.get().convertToRestResponse());
        } else {
            String errorMessage = String.format("Unable to find with user with id: %d", id);
            Apimessage error = new Apimessage()
                    .withMessage(errorMessage)
                    .withStatus(Apimessage.Status.NOTFOUND);
            return GetUserByIdResponse.withJsonNotFound(error);
        }
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
    public PutUserByIdResponse putUserById(Integer id, User userPayLoad) throws Exception {
        //convert user to domain object
        org.example.domain.User domainUser = new org.example.domain.User(userPayLoad);
        domainUser.setId(new Long(id));

        //update user object
        Optional<org.example.domain.User> updatedUser = userService.update(domainUser);

        if (updatedUser.isPresent()) {
            return PutUserByIdResponse.withJsonOK(updatedUser.get().convertToRestResponse());
        } else {
            String errorMessage = String.format("User with id: %d does not exist and hence unable to update it", id);
            Apimessage error = new Apimessage()
                    .withMessage(errorMessage)
                    .withStatus(Apimessage.Status.NOTFOUND);
            return PutUserByIdResponse.withJsonNotFound(error);
        }
    }

    @Override
    public DeleteUserByIdResponse deleteUserById(Integer id) throws Exception {
        //invoke user service to delete user
        boolean isUserDeleted = userService.delete(new Long(id));

        if (isUserDeleted) {
            String successMessage = String.format("Successfully deleted user with id: %d", id);
            Apimessage success = new Apimessage()
                    .withMessage(successMessage)
                    .withStatus(Apimessage.Status.SUCCESS);
            return DeleteUserByIdResponse.withJsonOK(success);
        } else {
            String errorMessage = String.format("Unable to located user with id: %d and hence unable to delete it", id);
            Apimessage error = new Apimessage()
                    .withMessage(errorMessage)
                    .withStatus(Apimessage.Status.NOTFOUND);
            return DeleteUserByIdResponse.withJsonNotFound(error);

        }
    }
}
