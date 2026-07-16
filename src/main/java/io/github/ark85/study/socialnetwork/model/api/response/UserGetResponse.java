package io.github.ark85.study.socialnetwork.model.api.response;

import io.github.ark85.study.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGetResponse {
    private UUID id;
    private String firstName;
    private String secondName;
    private LocalDate birthDate;
    private String biography;
    private String city;

    public UserGetResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
        this.birthDate = user.getBirthDate();
        this.biography = user.getBiography();
        this.city = user.getCity();
    }
}
