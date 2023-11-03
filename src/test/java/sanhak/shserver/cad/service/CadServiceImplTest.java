package sanhak.shserver.cad.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.CadService;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CadServiceImplTest {
    @Autowired
    CadService cadService;


    @Test
    void saveCadData() {
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