package sanhak.shserver.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sanhak.shserver.infra.PythonUtils;

@SpringBootTest
class PythonUtilsTest {
    @Autowired
    PythonUtils pythonUtils;

    private static final String mainCategory = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";

    @Test
    void saveTfIdf() {
        Assertions.assertDoesNotThrow(
                () -> pythonUtils.saveTfIdf());
    }

    @Test
    void updateCNNClassification() {
        Assertions.assertDoesNotThrow(
                () -> pythonUtils.updateCNNClassification(mainCategory));
    }
}