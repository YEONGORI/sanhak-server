package sanhak.shserver.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
class PythonUtilsTest {
    @Autowired
    PythonUtils pythonUtils;

    private static final String id = "5663a8e6-5d5a-479a-8151-954fe9dea6b6";
    private static final String mainCategory = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";

    @Test
    void saveTfIdf() {
        pythonUtils.saveTfIdf();
    }

    @Test
    void updateCNNClassification() {
        pythonUtils.updateCNNClassification(mainCategory);
    }
}