package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.User;
import org.springframework.data.domain.Page;

/**
 * fff
 *
 * @author xuanl
 * @version 01-00
 * @since 5/08/2024
 */

public interface UserService {

    public void save(User user);
    public User findByUsername(String username);
    public User getUserById(Long id);
    public Page<User> findPaginatedEmployee(int pageNo, int pageSize, String sortField, String sortDirection);

}
