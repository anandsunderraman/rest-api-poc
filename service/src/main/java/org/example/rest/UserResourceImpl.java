package org.example.rest;

import cc.api.model.v1.model.Error;
import cc.api.model.v1.model.User;
import cc.api.model.v1.resource.UserResource;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        org.example.domain.User user = userService.getUserById(new Long(id));

        return GetUserByIdResponse.withJsonOK(from(user));


    }

    /**
     * Convert domain object to REST response object
     * @param domainUser
     * @return
     */
    private User from(org.example.domain.User domainUser) {
        User resultUser =  new User();
        resultUser.setAge(domainUser.getAge());
        resultUser.setId(domainUser.getId());
        resultUser.setName(domainUser.getName());
        return resultUser;
    }


}
