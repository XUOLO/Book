package com.example.BookMangement.Service;

import com.example.BookMangement.Entity.BookCategory;
import com.example.BookMangement.Entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MemberService
 *
 * @author benvo
 * @version 01-00
 * @since 5/15/2024
 */
public interface MemberService {
    public void save(User user);
    public int countMember( );

}
