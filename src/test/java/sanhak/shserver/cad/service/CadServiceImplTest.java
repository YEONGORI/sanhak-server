package sanhak.shserver.cad.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.CadService;
import sanhak.shserver.cad.dto.SaveCadDatasReqDTO;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CadServiceImplTest {
    @Autowired
    CadService cadService;

    private final static String testDir = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";
    private final static String testFile = "03-ST-BTS-102-001 시스템 계통도.dwg";


    @Test
    void saveCadData() {
        SaveCadDatasReqDTO reqDTO = SaveCadDatasReqDTO.builder()
                .projectFolder(testDir)
                .author("이상평")
                .build();

        Assertions.assertDoesNotThrow(() ->
                cadService.saveCadData(reqDTO)
        );
    }

    @Test
    void searchCadFile() {

    }

    @Test
    void getSimilarData() {
        SimilarDatasReqDTO reqDTO = SimilarDatasReqDTO.builder()
                .fileName("015. T1001026-011 910정거장 출입통제설비 CABLE WIRING DIAGRAM.jpeg")
                .build();
        List<Cad> cads = cadService.getSimilarData(reqDTO);
    }
}