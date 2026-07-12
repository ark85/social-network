package io.github.ark85.study.network.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String firstName;
    private String secondName;
    private String password;
    private String birthDate;
    private String biography;
    private String city;
}
