package sanhak.shserver.utils;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import sanhak.shserver.infra.AsposeUtils;
import sanhak.shserver.infra.S3Utils;

import java.io.ByteArrayOutputStream;

@SpringBootTest
class S3UtilsTest {
    @Autowired
    S3Utils s3Utils;
    @Autowired
    AsposeUtils asposeUtils;

    private final static String testDir = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";
    private final static String testFile = "03-ST-BTS-102-001 시스템 계통도.dwg";

    @Test
    @DisplayName("폴더 다운로드 테스트")
    @BeforeTestClass
    void downloadFolder() {
        Assertions.assertDoesNotThrow(() -> s3Utils.downloadFolder(testDir));
        Assertions.assertThrows(AmazonS3Exception.class,
                () -> s3Utils.downloadFolder("null"));
    }

    @Test
    @DisplayName("폴더 / 파일 업로드 테스트")
    void uploadS3() {
        ByteArrayOutputStream os = asposeUtils.CadToJpeg(testFile);
        Assertions.assertDoesNotThrow(() -> s3Utils.uploadS3("image/", testFile, os));
    }


    @Test
    @DisplayName("파일 다운로드 테스트")
    void downloadFile() {
        Assertions.assertDoesNotThrow(() -> s3Utils.downloadFile(testFile));
        Assertions.assertThrows(AmazonS3Exception.class,
                () -> s3Utils.downloadFile("null"));
    }
}
