package com.example.springtestpractice.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String birthdate;
}
