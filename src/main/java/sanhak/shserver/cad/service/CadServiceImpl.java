package sanhak.shserver.cad.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.repository.CadRepository;
import sanhak.shserver.cad.dto.SaveCadsReq;
import sanhak.shserver.infra.AsposeUtils;
import sanhak.shserver.infra.PythonUtils;
import sanhak.shserver.infra.S3Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CadServiceImpl implements CadService {
    private final CadRepository cadRepository;
    private final S3Utils s3Utils;
    private final AsposeUtils asposeUtils;
    private final PythonUtils pythonUtils;

    @Override
    public void saveCadData(String projectFolder, String author) {
        Map<String, String[]> fileInfo;
        if (projectFolder.endsWith(".dwg")) {
            s3Utils.downloadFile(projectFolder);
            fileInfo = asposeUtils.searchCadFile(projectFolder);
        } else {
            s3Utils.downloadFolder(projectFolder);
            fileInfo = asposeUtils.searchCadFileInDataDir(projectFolder);
        }
        
        log.info("mongo atlas upload start");
        saveDatasInMongo(projectFolder, author, fileInfo);
        log.info("mongo atlas upload finish");

        pythonUtils.saveTfIdf();
        log.info("save Tf-Idf is done");

        pythonUtils.updateCNNClassification(projectFolder);
        log.info("update CNN Classification is done");
    }

    private void saveDatasInMongo(String projectFolder, String author, Map<String, String[]> fileInfo) {
        for (Map.Entry<String, String[]> entry: fileInfo.entrySet()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Cad cad = Cad.builder()
                    .id(UUID.randomUUID().toString())
                    .author(author)
                    .mainCategory(projectFolder)
                    .subCategory(entry.getValue()[0])
                    .title(entry.getValue()[1])
                    .index(entry.getKey())
                    .s3Url(entry.getValue()[2])
                    .createdAt(date)
                    .cadLabel("")
                    .tfidf("")
                    .build();
            cadRepository.save(cad);
        }
    }

    @Override
    public Set<Cad> searchCadFile(String searchText) {
        String[] split = searchText.split(" ");
        Set<Cad> cads = new HashSet<>();

        for (String st : split) {
            cads.addAll(cadRepository.findAllByAuthor(st));
            cads.addAll(cadRepository.findAllByTitle(st));
            cads.addAll(cadRepository.findAllByIndex(st));
            cads.addAll(cadRepository.findAllByMainCategory(st));
            cads.addAll(cadRepository.findAllBySubCategory(st));
            cads.addAll(cadRepository.findAllByCadLabel(st));
        }
        return cads;
    }
}
