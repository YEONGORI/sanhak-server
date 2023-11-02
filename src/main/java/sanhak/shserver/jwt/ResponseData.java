package sanhak.shserver.jwt;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseData {

    private String message;
    private String access_token;

    private String password;

    private String username;
    private String phoneNumber;
    private String email;
    private String birthday;



}
