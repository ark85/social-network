package io.github.ark85.study.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String firstName;
    private String secondName;
    private String passwordHash;
    private String birthDate;
    private String biography;
    private String city;
}
