package sanhak.shserver.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3UtilsTest {
    @Autowired
    S3Utils s3Utils;

    @Test
    void downloadFolder() {
    }


    @Test
    void downloadFile() {
        s3Utils.downloadFile("015. T1001026-011 910정거장 출입통제설비 CABLE WIRING DIAGRAM.jpeg");
    }
}