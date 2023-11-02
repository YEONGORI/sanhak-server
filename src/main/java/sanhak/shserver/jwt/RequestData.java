package sanhak.shserver.jwt;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestData
{

    private int employNumber;
    private String password;
    private String username;
    private String phoneNumber;
    private String email;
    private String birthday;
    private int adminkey;
    private String newPassword;
    private String newPhoneNumber;
    private String newEmail;


}
