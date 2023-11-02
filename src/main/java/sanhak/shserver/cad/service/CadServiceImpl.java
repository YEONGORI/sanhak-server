package sanhak.shserver.cad.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.CadRepository;
import sanhak.shserver.cad.CadService;
import sanhak.shserver.cad.dto.SaveCadDatasReqDTO;
import sanhak.shserver.cad.dto.SimilarDatasReqDTO;
import sanhak.shserver.utils.AsposeUtils;
import sanhak.shserver.utils.S3Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CadServiceImpl implements CadService {
    private final CadRepository cadRepository;
    private final MongoTemplate mongoTemplate;
    private final S3Utils s3Utils;
    private final AsposeUtils asposeUtils;


    public void saveCadData(SaveCadDatasReqDTO reqDTO) {
        String folder = reqDTO.getProjectFolder();
        String author = reqDTO.getAuthor();

        s3Utils.downloadFolder(folder);

        Map<String, String[]> fileInfo = asposeUtils.searchCadFleInDataDir(folder);

        for (Map.Entry<String, String[]> entry: fileInfo.entrySet()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Cad cad = new Cad(author, folder, entry.getValue()[0], entry.getValue()[1], entry.getKey(), entry.getValue()[2], date);

            cadRepository.save(cad);
//                mongoTemplate.insert(cad, "cad");
        }
    }

    public List<Cad> searchCadFile(String searchText) {
        try {
            if (Objects.equals(searchText, ""))
                return null;
            String[] eachText = searchText.split(" ");

            String Col[] = {"title", "mainCategory" ,"subCategory", "index"};
            Query query_qrr[][] = new Query[Col.length][eachText.length];

            for(int i=0;i<Col.length;i++){
                for(int j=0;j<eachText.length;j++){
                    query_qrr[i][j] = new Query();
                    query_qrr[i][j].addCriteria(Criteria.where(Col[i]).regex(eachText[j]));
                }

            }
            List<Cad> list = mongoTemplate.find(query_qrr[0][0],Cad.class,"cad");
            for(int i=0;i<eachText.length;i++){
                list = Stream.concat(list.stream(),mongoTemplate.find(query_qrr[0][i],Cad.class,"cad").stream()).distinct().toList();
                list = Stream.concat(list.stream(),mongoTemplate.find(query_qrr[1][i],Cad.class,"cad").stream()).distinct().toList();
                list = Stream.concat(list.stream(),mongoTemplate.find(query_qrr[2][i],Cad.class,"cad").stream()).distinct().toList();
                list = Stream.concat(list.stream(),mongoTemplate.find(query_qrr[3][i],Cad.class,"cad").stream()).distinct().toList();
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cad> getSimilarData(SimilarDatasReqDTO reqDTO) {

    }
}
