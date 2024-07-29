package com.example.libraryManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 100, message = "Author should not exceed 100 characters")
    private String author;

    private int publicationYear;

    @NotBlank(message = "ISBN is mandatory")
    @Size(max = 13, message = "ISBN should not exceed 13 characters")
    private String isbn;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BorrowingRecord> borrowingRecords;
}