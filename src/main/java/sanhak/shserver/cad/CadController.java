package sanhak.shserver.cad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanhak.shserver.cad.dto.SaveCadsReq;
import sanhak.shserver.cad.service.CadService;

import java.util.Set;

@ResponseBody
@RestController
@RequiredArgsConstructor
@RequestMapping("/cad")
public class CadController {
    private final CadService cadService;

    @PostMapping
    public ResponseEntity<?> saveCadData(@RequestBody SaveCadsReq saveCadsReq) {
        cadService.saveCadData(saveCadsReq.projectFolder(), saveCadsReq.author());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCadData(@RequestParam String searchText) {
        Set<Cad> cads = cadService.searchCadFile(searchText);
        return new ResponseEntity<>(cads, HttpStatus.OK);
    }
}
