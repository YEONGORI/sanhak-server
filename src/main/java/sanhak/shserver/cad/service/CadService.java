package sanhak.shserver.cad.service;

import org.springframework.stereotype.Service;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.dto.SaveCadsReq;

import java.util.Set;

@Service
public interface CadService {
    void saveCadData(String projectFolder, String author);
    Set<Cad> searchCadFile(String searchText);
}
