package com.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClazzPages {
    String name;
    LocalDate begin_date;
    LocalDate end_date;
    private Long total;
    private List rows;
}
