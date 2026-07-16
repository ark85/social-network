package io.github.ark85.study.network.model.api.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @NotEmpty
    private String id;
    @NotEmpty
    private String password;
}
