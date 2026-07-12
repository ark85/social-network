package io.github.ark85.study.network.model;

import io.github.ark85.study.network.model.api.request.UserCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String firstName;
    private String secondName;
    private String passwordHash;
    private String birthDate;
    private String biography;
    private String city;

    public User(UserCreateRequest userCreateRequest) {
        this.firstName = userCreateRequest.getFirstName();
        this.secondName = userCreateRequest.getSecondName();
        this.passwordHash = userCreateRequest.getPassword();
        this.birthDate = userCreateRequest.getBirthDate();
        this.biography = userCreateRequest.getBiography();
        this.city = userCreateRequest.getCity();
    }
}
