package org.example.service;


import org.example.domain.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public UserQueryResponse queryUsers(UserQuery userQuery) {

        Page<User> resultPage;

        if (userQuery.name != null) {
            if (userQuery.exactMatch) {
                resultPage = userRepository.findByNameAndAgeBetween(userQuery.name, userQuery.startAge, userQuery.endAge, userQuery.getPageRequest());
            } else {
                resultPage = userRepository.findByNameContainingAndAgeBetween(userQuery.name, userQuery.startAge, userQuery.endAge, userQuery.getPageRequest());
            }
        } else {
            resultPage = userRepository.findByAgeBetween(userQuery.startAge, userQuery.endAge, userQuery.getPageRequest());
        }
        return new UserQueryResponse(resultPage);
    }

    public static class UserQuery {

        public int pageSize = 50;
        public int pageNumber = 0;
        public int startAge = 0;
        public int endAge = Integer.MAX_VALUE;
        public String name = null;
        public boolean exactMatch = false;

        public PageRequest getPageRequest() {
            return new PageRequest(pageNumber, pageSize);
        }

    }

    /**
     * class used to represent user response
     * we could have returned the Page<User> object to the consumer
     * but this ties the consumer of the service to spring Page implementation
     * used this inner class abstracts the implementation detail from the consumer
     */
    public static class UserQueryResponse {

        final long totalItems;
        final List<User> userList;
        final int pageSize;
        final int pageNumber;

        public UserQueryResponse(Page<User> userPage) {
            totalItems = userPage.getTotalElements();
            userList = userPage.getContent();
            pageSize = userPage.getSize();
            pageNumber = userPage.getNumber();
        }

        public long getTotalItems() {
            return totalItems;
        }

        public List<User> getUserList() {
            return userList;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }
    }
}
