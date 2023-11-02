package sanhak.shserver.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "user")
public class UserInform {

    @Id
    private String id;
    private int employNumber;
    private String password;
    private String username;
    private String phoneNumber;
    private String email;
    private String birthday;
    private int adminkey;
}