package sanhak.shserver.cad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanhak.shserver.cad.dto.SaveCadDatasReqDTO;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;
import sanhak.shserver.cad.service.CadServiceImpl;

import java.util.List;
import java.util.Set;

@ResponseBody
@RestController
@RequiredArgsConstructor
@RequestMapping("/cad/data")
public class CadController {
    private final CadService cadService;

    @GetMapping
    public ResponseEntity<?> getCadData(@RequestParam String searchText) {
        Set<Cad> cads = cadService.searchCadFile(searchText);
        return new ResponseEntity<>(cads, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveCadData(@RequestBody SaveCadDatasReqDTO reqDTO) {
        cadService.saveCadData(reqDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
