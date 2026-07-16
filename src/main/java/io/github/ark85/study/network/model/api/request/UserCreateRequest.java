package io.github.ark85.study.network.model.api.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String secondName;
    @NotEmpty
    private String password;
    private LocalDate birthDate;
    private String biography;
    private String city;
}
