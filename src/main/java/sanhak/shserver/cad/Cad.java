package sanhak.shserver.cad;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Data
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

    @Override
    public String toString() {
        return "Cad File[" +
                "\n\tid=" + id +
                "\n\tmainCategory=" + mainCategory +
                "\n\tsubCategory=" + subCategory +
                "\n\ttitle=" + title +
                "\n\tindex=" + index +
                "\n\ts3Url=" + s3Url +
                "]";
    }
}
