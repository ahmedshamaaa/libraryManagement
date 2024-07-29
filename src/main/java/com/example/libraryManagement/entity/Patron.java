package com.example.libraryManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Contact info is mandatory")
    private String contactInfo;


    @OneToMany(mappedBy = "patron", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BorrowingRecord> borrowingRecords;
}

