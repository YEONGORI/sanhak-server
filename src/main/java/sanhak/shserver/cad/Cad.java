package sanhak.shserver.cad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "cad")
public class Cad {
    @Id
    private String id;
    private String author;
    private String mainCategory;
    private String subCategory;
    private String title;
    private String index;
    private String s3Url;
    private String createdAt;


    public Cad(String author, String mainCategory, String subCategory, String title, String index, String s3Url, String createdAt) {
        this.author = author;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.title = title;
        this.index = index;
        this.s3Url = s3Url;
        this.createdAt = createdAt;
    }
}
