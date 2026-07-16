package io.github.ark85.study.socialnetwork.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private String token;
}
