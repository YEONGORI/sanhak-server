package sanhak.shserver.utils;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3UtilsTest {
    @Autowired
    S3Utils s3Utils;

    private final static String testDir = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";
    private final static String testFile = "03-ST-BTS-102-001 시스템 계통도.dwg";

    @Test
    void downloadFolder() {
        Assertions.assertDoesNotThrow(() -> s3Utils.downloadFolder(testDir));
        Assertions.assertThrows(AmazonS3Exception.class,
                () -> s3Utils.downloadFolder("null2"));
    }

    @Test
    void uploadS3() {

//        Assertions.assertThrows()
    }

    @Test
    void downloadFile() {
        Assertions.assertDoesNotThrow(() -> s3Utils.downloadFile(testFile));
        Assertions.assertThrows(AmazonS3Exception.class,
                () -> s3Utils.downloadFile("null"));
    }

    @Test
    void encryptAES256() {
        s3Utils.encryptAES256
    }
}
