package com.example.BookMangement.Repository;

import com.example.BookMangement.Entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AuthorRepository
 *
 * @author xuanl
 * @version 01-00
 * @since 5/13/2024
 */
@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {



}
