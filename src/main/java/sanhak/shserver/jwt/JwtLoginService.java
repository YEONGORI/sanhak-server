package sanhak.shserver.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component

public class JwtLoginService {



    @Autowired
    JwtLoginRepository jwtLoginRepository;
    @Autowired
    JwtProvider jwtProvider;


/*
    public String selectUser(String name) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (mongoDBTestRepository.findByName(name) == null) {
                log.info("[Service] user name : {} not exist!!", name);
                return String.format("user name : %s not exist!!", name);
            } else {
                return objectMapper.writeValueAsString(mongoDBTestRepository.findByName(name));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

 */

    public int registerUser(UserInform data, ResponseData responseData) {

        log.info("[Controller] data : {}", data);
        if(data.getEmployNumber() == 0 ||
                data.getPassword() == "" ||
                data.getUsername() == "" ||
                data.getPhoneNumber() == "" ||
                data.getEmail() == "" ||
                data.getEmail() == "")
        {
            log.info("Received incorrect data");
            responseData.setMessage("Incorrect data");

            return 401;
        }
        if (data.getAdminkey()== 9999) {
            //관리자권한 부여
            data.setAdminkey(1);
        } else {
            data.setAdminkey(0);

        }

        if (jwtLoginRepository.findByEmployNumber(data.getEmployNumber()) != null)
        {

            log.info("employNumber already exist");
            responseData.setMessage("employNumber already exist");

            return 401;
            //data.setId(mongoDBTestRepository.findByEmployNumber(data.getEmployNumber()).getId());
            //log.info("[Controller] data Id : {}", data.getId());

        }
        log.info("New name received");
        jwtLoginRepository.save(data);
        responseData.setMessage("Success register");
        return 200;




    }
    public int loginUser(UserInform data, ResponseData responseData)
    {
        UserInform checkUserInform=new UserInform();
        checkUserInform=jwtLoginRepository.findByEmployNumber(data.getEmployNumber());


        if (checkUserInform == null)
        {

            log.info("Incorrect employNumber or password");
            responseData.setMessage("Incorrect employNumber or password");
            return 401;

        }
        else if(checkUserInform.getPassword().compareTo(data.getPassword())!=0)
        {
            log.info("Incorrect employNumber or password");
            responseData.setMessage("Incorrect employNumber or password");
            return 401;
        }


        String token = jwtProvider.createToken(checkUserInform.getEmployNumber(),checkUserInform.getPassword(),checkUserInform.getAdminkey());
        responseData.setAccess_token(token);
        responseData.setUsername(checkUserInform.getUsername());
        log.info("token : {}",token);
        log.info("Success login");
        responseData.setMessage("Success login");
        //log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
        return 200;
    }
    public int reconfirmUser(UserInform data, ResponseData responseData,String token) {

        UserInform reconfirmUserInform=new UserInform();
        reconfirmUserInform=jwtLoginRepository.findByEmployNumber(data.getEmployNumber());

        if (reconfirmUserInform.getPassword().compareTo(data.getPassword())!=0)
        {

            log.info("Incorrect password");
            responseData.setMessage("Incorrect password");
            return 401;

        }
        if(jwtProvider.validateToken(token)==false)
        {
            log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
            log.info("Error access_token revoked");
            responseData.setMessage("Error access_token revoked");
            return 401;
        }

        log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
        log.info("Success reconfirm");
        responseData.setMessage("Success reconfirm");
        return 200;

    }

    public int detailUser(UserInform data, ResponseData responseData,String token) {

        UserInform detailUserInform=new UserInform();
        detailUserInform=jwtLoginRepository.findByEmployNumber(data.getEmployNumber());

        if (detailUserInform == null)
        {

            log.info("Incorrect employNumber or password");
            responseData.setMessage("Incorrect employNumber or password");
            return 401;

        }

        if(jwtProvider.validateToken(token)==false)
        {
            log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
            log.info("Error access_token revoked");
            responseData.setMessage("Error access_token revoked");
            return 401;
        }
        responseData.setPhoneNumber(detailUserInform.getPhoneNumber());
        responseData.setEmail(detailUserInform.getEmail());
        responseData.setBirthday((detailUserInform.getBirthday()));
        log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
        log.info("Success finding detail");
        responseData.setMessage("Success finding detail");
        return 200;

    }

    public int modifyUser(RequestData requestData, ResponseData responseData,String token) {

        UserInform modifyUserInform=new UserInform();
        modifyUserInform=jwtLoginRepository.findByEmployNumber(requestData.getEmployNumber());

        if (modifyUserInform == null)
        {

            log.info("No exist employNumber");
            responseData.setMessage("No exist employNumber");
            return 401;

        }
        else if(modifyUserInform.getPassword().compareTo(requestData.getPassword())!=0)
        {
            log.info("No exist employNumber");
            responseData.setMessage("No exist employNumber");
            return 401;
        }

        if(jwtProvider.validateToken(token)==false)
        {
            log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
            log.info("Error access_token revoked");
            responseData.setMessage("Error access_token revoked");
            return 401;
        }
        if (requestData.getNewPassword() != "") {
            modifyUserInform.setPassword(requestData.getNewPassword());
        }
        if (requestData.getNewPhoneNumber() != "") {
            modifyUserInform.setPhoneNumber(requestData.getNewPhoneNumber());
        }
        if (requestData.getNewEmail() != "") {
            modifyUserInform.setEmail(requestData.getNewEmail());
        }

        jwtLoginRepository.save(modifyUserInform);
        log.info("modifyUserInform : {}",modifyUserInform);
        log.info("jwtProvider.getSubject(token) : {}",jwtProvider.validateToken(token));
        log.info("Success modifying user imformation");
        responseData.setMessage("Success modifying user imformation");
        return 200;

    }


    public int findUser(RequestData requestData, ResponseData responseData) {

        UserInform findUserInform=new UserInform();
        findUserInform=jwtLoginRepository.findByEmployNumber(requestData.getEmployNumber());

        if (findUserInform == null)
        {

            log.info("No exist information in DB");
            responseData.setMessage("No exist imformation in DB");
            return 401;

        }
        else if(findUserInform.getUsername().compareTo(requestData.getUsername())!=0)
        {
            log.info("No exist information in DB");
            responseData.setMessage("No exist imformation in DB");
            return 401;
        }
        else if(findUserInform.getPhoneNumber().compareTo(requestData.getPhoneNumber())!=0)
        {
            log.info("No exist information in DB");
            responseData.setMessage("No exist imformation in DB");
            return 401;
        }
        else if(findUserInform.getEmail().compareTo(requestData.getEmail())!=0)
        {
            log.info("No exist information in DB");
            responseData.setMessage("No exist imformation in DB");
            return 401;
        }
        else if(findUserInform.getEmail().compareTo(requestData.getEmail())!=0)
        {
            log.info("No exist information in DB");
            responseData.setMessage("No exist imformation in DB");
            return 401;
        }




        log.info("Success finding password");
        responseData.setMessage("Success finding password");
        responseData.setPassword(findUserInform.getPassword());
        return 200;

    }









}