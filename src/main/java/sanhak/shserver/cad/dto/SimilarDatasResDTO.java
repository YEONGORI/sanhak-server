package sanhak.shserver.cad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SimilarDatasResDTO {
    private List<String> similar_cad_ids;
}
