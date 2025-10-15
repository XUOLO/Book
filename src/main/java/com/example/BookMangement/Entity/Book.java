package com.example.BookMangement.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Book
 *
 * @author xuanl
 * @version 01-00
 * @since 5/10/2024
 */
@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;


    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "publish_year")
    private int publishYear;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String image;

    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "UPDATE_DATE")
    private LocalDate updateDate;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "IS_DELETE")
    private Boolean isDelete;

    private String categoryIds;
    private String type;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_bookcategory",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookcategory_id", referencedColumnName = "id")
    )
    private Set<BookCategory> bookCategories;

    public void clearBookCategories() {
        this.bookCategories.clear();
    }
    public void clearBookImagess() {
        this.images.clear();
    }


    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImage> images = new ArrayList<>();
}
