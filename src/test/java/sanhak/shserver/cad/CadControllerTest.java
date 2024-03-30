package sanhak.shserver.cad;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sanhak.shserver.cad.dto.SaveCadsReq;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String testAuthor = "박철준";
    private final static String testDir = "[18ED17] 완주삼봉지구스마트도시 정보통신 설계용역/";

    @Test
    void getCadData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cad/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchText", "박철준"))
                .andExpect(status().isOk());
    }

    @Test
    void saveCadData() throws Exception {
        SaveCadsReq reqDTO = SaveCadsReq.builder()
                .author(testAuthor)
                .projectFolder(testDir)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cad/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO)))
                .andExpect(status().isOk());
    }
}