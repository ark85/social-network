package io.github.ark85.study.socialnetwork.model.api.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    @NotEmpty
    private UUID id;
    @NotEmpty
    private String text;
}
