package sanhak.shserver.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class JwtLoginController {

    @Autowired
    JwtLoginService jwtLoginService;




    @PostMapping("/register")//회원가입API
    public ResponseEntity<ResponseData> registerUserData(@RequestBody UserInform data) {

        try {
            ResponseData responseData = new ResponseData();
            int httpCheck = jwtLoginService.registerUser(data, responseData);

            if (httpCheck == 401) {
                return new ResponseEntity<>(responseData, HttpStatus.UNAUTHORIZED);
            } else if (httpCheck == 200) {
                return new ResponseEntity<>(responseData, HttpStatus.OK);

            } else {
                log.info("Back server error");
                responseData.setMessage("Back server error");
                return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }catch(Exception e)
        {
            ResponseData responseData = new ResponseData();
            log.info("Back server error");
            responseData.setMessage("Back server error");
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/login")//로그인 API
    public ResponseEntity<ResponseData> loginUserData(@RequestBody UserInform data) {
        try {
            ResponseData responseData = new ResponseData();
            log.info("[Controller] data : {}", data);
            int httpCheck = jwtLoginService.loginUser(data, responseData);

            if (httpCheck == 401) {
                return new ResponseEntity<>(responseData, HttpStatus.UNAUTHORIZED);
            } else if (httpCheck == 200) {
                return new ResponseEntity<>(responseData, HttpStatus.OK);

            } else {
                log.info("Back server error");
                responseData.setMessage("Back server error");
                return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }catch(Exception e)
        {
            ResponseData responseData = new ResponseData();
            log.info("Back server error");
            responseData.setMessage("Back server error");
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/reconfirm")//비밀번호 재확인 API(MY홈페이지 들어갈때)
    public ResponseEntity<ResponseData> reconfirmUserData(@RequestBody UserInform data, @RequestHeader(value = "Authorization") String token )
    {
        try {
            log.info("[Controller] data : {}", data);
            log.info("[Controller] token : {}", token);
            ResponseData responseData = new ResponseData();
            int httpCheck = jwtLoginService.reconfirmUser(data, responseData, token);

            if (httpCheck == 401) {
                return new ResponseEntity<>(responseData, HttpStatus.UNAUTHORIZED);
            } else if (httpCheck == 200) {
                return new ResponseEntity<>(responseData, HttpStatus.OK);

            } else {
                log.info("Back server error");
                responseData.setMessage("Back server error");
                return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }catch (Exception e)
        {
            ResponseData responseData = new ResponseData();
            log.info("Back server error");
            responseData.setMessage("Back server error");
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/detail")//MY페이지에 필요한 정보를 받아오는 API
    public ResponseEntity<ResponseData> detailUserData(@RequestBody UserInform data, @RequestHeader(value = "Authorization") String token )
    {
        try
        {
            log.info("[Controller] data : {}", data);
            log.info("[Controller] token : {}", token);
            ResponseData responseData = new ResponseData();
            int httpCheck = jwtLoginService.detailUser(data,responseData,token);

            if(httpCheck==401)
            {
                return new ResponseEntity<> (responseData,HttpStatus.UNAUTHORIZED);
            }
            else if(httpCheck==200)
            {
                return new ResponseEntity<> (responseData,HttpStatus.OK);

            }
            else
            {
                log.info("Back server error");
                responseData.setMessage("Back server error");
                return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

            }

        }catch (Exception e)
        {
            ResponseData responseData = new ResponseData();
            log.info("Back server error");
            responseData.setMessage("Back server error");
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

        }



    }


    @PostMapping("/modify")//MY페이지에 개인 정보를 수정하는 API
    public ResponseEntity<ResponseData> modifyUserData(@RequestBody RequestData requestData, @RequestHeader(value = "Authorization") String token )
    {
        try
        {
            log.info("[Controller] data : {}", requestData);
            log.info("[Controller] token : {}", token);
            ResponseData responseData = new ResponseData();
            int httpCheck = jwtLoginService.modifyUser(requestData,responseData,token);

            if(httpCheck==401)
            {
                return new ResponseEntity<> (responseData,HttpStatus.UNAUTHORIZED);
            }
            else if(httpCheck==200)
            {
                return new ResponseEntity<> (responseData,HttpStatus.OK);

            }
            else
            {
                log.info("Back server error");
                responseData.setMessage("Back server error");
                return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

            }

        }catch (Exception e)
        {
            ResponseData responseData = new ResponseData();
            log.info("Back server error");
            responseData.setMessage("Back server error");
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

        }



    }

    @PostMapping("/find")//MY페이지에 필요한 정보를 받아오는 API
    public ResponseEntity<ResponseData>findUserData(@RequestBody RequestData requestData)
    {
        try
        {
            log.info("[Controller] data : {}", requestData);
            ResponseData responseData = new ResponseData();
            int httpCheck = jwtLoginService.findUser(requestData,responseData);

            if(httpCheck==401)
            {
                return new ResponseEntity<> (responseData,HttpStatus.UNAUTHORIZED);
            }
            else if(httpCheck==200)
            {
                return new ResponseEntity<> (responseData,HttpStatus.OK);

            }
            else
            {
                log.info("Back server error");
                responseData.setMessage("Back server error");
                return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

            }

        }catch (Exception e)
        {
            ResponseData responseData = new ResponseData();
            log.info("Back server error");
            responseData.setMessage("Back server error");
            return new ResponseEntity<>(responseData,HttpStatus.INTERNAL_SERVER_ERROR);

        }



    }





}