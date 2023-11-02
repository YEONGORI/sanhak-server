package sanhak.shserver.cad;

import sanhak.shserver.cad.dto.SaveCadDatasReqDTO;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;

import java.util.List;

public interface CadService {
    void saveCadData(SaveCadDatasReqDTO reqDTO);
    List<Cad> searchCadFile(String searchText);

    List<Cad> getSimilarData(SimilarDatasReqDTO reqDTO);
}
