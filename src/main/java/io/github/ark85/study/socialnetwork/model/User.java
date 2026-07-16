package io.github.ark85.study.socialnetwork.model;

import io.github.ark85.study.socialnetwork.model.api.request.UserCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private UUID id;
    private String firstName;
    private String secondName;
    private String passwordHash;
    private LocalDate birthDate;
    private String biography;
    private String city;

    public User(UserCreateRequest userCreateRequest) {
        this.firstName = userCreateRequest.getFirstName();
        this.secondName = userCreateRequest.getSecondName();
        this.birthDate = userCreateRequest.getBirthDate();
        this.biography = userCreateRequest.getBiography();
        this.city = userCreateRequest.getCity();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }
}
