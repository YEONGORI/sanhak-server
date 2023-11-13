package sanhak.shserver.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class PythonUtilsTest {
    @Autowired
    PythonUtils pythonUtils;

    @Test
    void saveTfIdf() {
        pythonUtils.saveTfIdf();
    }

    @Test
    void getSimilarCads() {
        pythonUtils.getSimilarCads("60ab8405-a8bd-41e7-8c25-33f6a349ba1f");
    }

    @Test
    void updateCNNClassification() {
        pythonUtils.updateCNNClassification("[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/");
    }
}