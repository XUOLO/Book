package com.example.BookMangement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * BookCategory
 *
 * @author benvo
 * @version 01-00
 * @since 5/13/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOOK_CATEGORY")
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "UPDATE_TIME")
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "IS_DELETE")
    private Boolean isDelete;
}
