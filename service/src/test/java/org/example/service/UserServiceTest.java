package org.example.service;

import org.example.domain.User;
import org.example.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private UserService userService;
    private User userObj;

    @Before
    public void setup() {
        userService = new UserService(userRepository);
        userObj = new User();
        userObj.setId(1L);
        userObj.setName("xyz");
    }

    /**
     * Test updateUser returns empty optional for non existent user
     */
    @Test
    public void updateUserForNonExistentUser() {

        //GIVEN a user id that does not exist
        when(userRepository.findById(anyLong())).thenReturn(null);

        //THEN a call to update user must return an empty optional
        Optional<User> updatedUser = userService.update(userObj);

        assertEquals(false, updatedUser.isPresent());
    }

    /**
     * Test updateUser returns empty optional for a deleted user
     */
    @Test
    public void updateUserForDeletedUser() {

        //GIVEN a user id that has been soft deleted
        userObj.delete();
        when(userRepository.findById(anyLong())).thenReturn(userObj);

        //THEN a call to update a deleted user must return an empty optional
        Optional<User> updatedUser = userService.update(userObj);

        assertEquals(false, updatedUser.isPresent());
    }

    /**
     * Test updateUser is saved
     */
    @Test
    public void updateUserIsSaved() {

        //GIVEN a user id to be updated
        when(userRepository.findById(anyLong())).thenReturn(userObj);

        //THEN a call to update user invoke the userRepository save method
        userService.update(userObj);

        verify(userRepository).save(userObj);
    }

    /**
     * Test delete on non existent user returns false
     */
    @Test
    public void deleteOnNonExistentUser() {

        //GIVEN a user id to be deleted
        Long userId = 1L;
        when(userRepository.findById(anyLong())).thenReturn(null);

        //THEN a call to delete user returns false
        boolean userHasBeenDeleted = userService.delete(userId);

        assertEquals(false, userHasBeenDeleted);
    }

    /**
     * Test delete on existent user returns true
     */
    @Test
    public void deleteOnExistentUser() {

        //GIVEN a user id to be deleted
        Long userId = 1L;
        when(userRepository.findById(anyLong())).thenReturn(userObj);

        //THEN a call to delete user returns false
        boolean userHasBeenDeleted = userService.delete(userId);

        assertEquals(true, userHasBeenDeleted);

        //verify userRepository save method is invoked
        verify(userRepository).save(userObj);
    }

}
