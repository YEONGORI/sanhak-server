package sanhak.shserver.cad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cad")
public class CadController {
    private final CadServiceImpl cadService;

    @GetMapping("/data")
    public ResponseEntity<List<Cad>> getCadDatas(@RequestParam String searchText) {
        try {
            if (searchText == null)
                System.out.println("검색어 입력 필요");
            List<Cad> result = cadService.searchCadFile(searchText);
            if (result.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/data")
    public ResponseEntity<HttpStatus> createCadDatas(@RequestBody String s3Url) {
        try {
            System.out.println("Cad Controll");
            cadService.saveCadFile(s3Url);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            System.out.println("save Error");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
