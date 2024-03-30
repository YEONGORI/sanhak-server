package sanhak.shserver.cad.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.dto.SaveCadsReq;
import sanhak.shserver.infra.S3Utils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class CadServiceImplTest {
    @Autowired
    CadService cadService;
    @Autowired
    S3Utils s3Utils;

    private final static String testDir = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";
    private final static String author = "박철준";


    @Test
    void saveCadData() {
        assertDoesNotThrow(() ->
                cadService.saveCadData(testDir, "TEST_사용자")
        );
    }

    @Test
    void searchCadFile() {
        Set<Cad> cads = cadService.searchCadFile(author);
        assertThat(cads.size()).isGreaterThan(0);
    }
}