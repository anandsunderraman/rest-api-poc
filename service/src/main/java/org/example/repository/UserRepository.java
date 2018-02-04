package org.example.repository;

import org.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findById(Long id);

    Page<User> findByNameAndAgeBetween(String name, Integer startAge, Integer endAge, Pageable pageRequest);
    Page<User> findByNameContainingAndAgeBetween(String name, Integer startAge, Integer endAge, Pageable pageRequest);
    Page<User> findByAgeBetween(Integer startAge, Integer endAge, Pageable pageRequest);
}
