package sanhak.shserver.cad.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaveCadDatasReqDTO {
    private String projectFolder;

    private String author;
}
