package sanhak.shserver.cad.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.CadService;
import sanhak.shserver.cad.dto.SaveCadDatasReqDTO;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;
import sanhak.shserver.utils.S3Utils;

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
    private final static String testFile = "03-ST-BTS-102-001 시스템 계통도.dwg";
    private final static String author = "홍길동";


    @Test
    void saveCadData() {
        SaveCadDatasReqDTO reqDTO = SaveCadDatasReqDTO.builder()
                .projectFolder(testDir)
                .author("이상평")
                .build();

        assertDoesNotThrow(() ->
                cadService.saveCadData(reqDTO)
        );
    }

    @Test
    void searchCadFile() {
        Set<Cad> cads = cadService.searchCadFile(author);
        assertThat(cads.size()).isGreaterThan(0);
    }

    @Test
    void getSimilarData() {
        SimilarDatasReqDTO reqDTO = SimilarDatasReqDTO.builder()
                .id("015. T1001026-011 910정거장 출입통제설비 CABLE WIRING DIAGRAM.jpeg")
                .build();
        Set<Cad> cads = cadService.getSimilarData(reqDTO);
    }
}