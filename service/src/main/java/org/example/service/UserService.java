package org.example.service;


import org.example.domain.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id));
    }

    public User save(User user) {
        //since we are creating a user object
        //if an id was set on the user object we set it to null
        user.setId(null);
        return userRepository.save(user);
    }

    /**
     * updates user entity after checking one exists
     * @param updatedUser
     * @return
     */
    public Optional<User> update(User updatedUser) {

        Optional<User> existingUser = getUserById(updatedUser.getId());

        //update a user only if the user exists and is not flagged as deleted
        if (existingUser.isPresent() && !existingUser.get().isDeleted()) {
            //update user
            return Optional.ofNullable(userRepository.save(updatedUser));
        } else {
            return Optional.empty();
        }
    }

    /**
     * delete a user by id
     * return  true  if a user was deleted
     * returns false if a user was not deleted
     * @param id
     * @return
     */
    public boolean delete(Long id) {
        //get user by id
        Optional<User> existingUser = getUserById(id);
        //if user is found set isremoved flag to 0
        if (existingUser.isPresent()) {
            User objectToDelete = existingUser.get();
            objectToDelete.delete();
            userRepository.save(objectToDelete);
            return true;
        } else {
            //else return false
            return false;
        }
    }
}
