package sanhak.shserver.cad;

import org.springframework.stereotype.Service;
import sanhak.shserver.cad.dto.SaveCadDatasReqDTO;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;

import java.util.Set;

@Service
public interface CadService {
    void saveCadData(SaveCadDatasReqDTO reqDTO);
    Set<Cad> searchCadFile(String searchText);
    Set<Cad> getSimilarData(SimilarDatasReqDTO reqDTO);
}
