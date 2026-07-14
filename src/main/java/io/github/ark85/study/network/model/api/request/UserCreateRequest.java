package io.github.ark85.study.network.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String firstName;
    private String secondName;
    private String password;
    private LocalDate birthDate;
    private String biography;
    private String city;
}
