package sanhak.shserver.cad.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record SaveCadsReq(
        @NotEmpty
        String projectFolder,
        String author
) {}
