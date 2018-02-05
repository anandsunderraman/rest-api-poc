package org.example.rest;

import cc.api.model.v1.model.Apimessage;
import cc.api.model.v1.model.User;
import cc.api.model.v1.resource.UserResource;
import org.example.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    UserService userService = mock(UserService.class);
    UserResourceImpl userResource = new UserResourceImpl(userService);

    @Test
    public void shouldReturnBadRequestForNegativeId() throws Exception {

        //when get user with negative id is invoked it must throw bad request
        UserResource.GetUserByIdResponse response = userResource.getUserById(-1);

        //then response must be 400
        assertEquals(response.getStatus(), 400);
        Apimessage apimessage = (Apimessage)response.getEntity();
        assertEquals(apimessage.getStatus(), Apimessage.Status.BADREQUEST);
    }

    @Test
    public void shouldReturnValidUserObject() throws Exception {

        org.example.domain.User result = new org.example.domain.User();
        result.setId(1L);
        result.setName("a");
        result.setAge(10);

        Optional<org.example.domain.User> userOptional = Optional.of(result);

        //given no user exists for id 1
        when(userService.getUserById(1L)).thenReturn(userOptional);

        //when get user is called on existent id
        UserResource.GetUserByIdResponse response = userResource.getUserById(1);

        //then response must be 200
        assertEquals(response.getStatus(), 200);
        User responseEntity = (User)response.getEntity();
        assertEquals(result.getAge(), responseEntity.getAge());
        assertEquals(result.getId(), responseEntity.getId());
        assertEquals(result.getName(), responseEntity.getName());
    }

    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        //given no user exists for id 1
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        //when get user is called on non existent id
        UserResource.GetUserByIdResponse response = userResource.getUserById(1);

        //then response must be 404
        assertEquals(response.getStatus(), 404);
    }

    @Test
    public void shouldReturnNotFoundWhenUserIsDeleted() throws Exception {

        org.example.domain.User result = new org.example.domain.User();
        result.setId(1L);
        result.setName("a");
        result.setAge(10);
        result.delete();

        Optional<org.example.domain.User> userOptional = Optional.of(result);

        //given deleted user exists
        when(userService.getUserById(1L)).thenReturn(userOptional);

        //when get user is called on non existent id
        UserResource.GetUserByIdResponse response = userResource.getUserById(1);

        //then response must be 404
        assertEquals(response.getStatus(), 404);
    }

}
