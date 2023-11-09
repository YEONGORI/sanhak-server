package sanhak.shserver.cad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SimilarDatasReqDTO {
    private String fileName;
}
