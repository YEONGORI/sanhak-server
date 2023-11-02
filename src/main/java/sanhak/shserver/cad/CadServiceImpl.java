package sanhak.shserver.cad;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sanhak.shserver.utils.AsposeUtils;
import sanhak.shserver.utils.S3Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;



@RequiredArgsConstructor
@Service
@Component
public class CadServiceImpl implements CadService {

    private final CadRepository cadRepository;
    @Autowired
    public final MongoTemplate mongoTemplate;
    private final S3Utils s3Utils;
    private final AsposeUtils asposeUtils;


    public void saveCadFile(String dir) {
        try {
            System.out.println("cadServiceimpl");
            System.out.println(dir);
            String[] mainCategory = dir.split("\"");
            String folder = mainCategory[3];
            String author = mainCategory[7];
            //cadRepository.deleteAll();

            s3Utils.downloadFolder(folder);

            String existDir = AsposeUtils.dataDir+folder;

            Map<String, String[]> fileInfo = asposeUtils.searchCadFleInDataDir(folder);

            System.out.println("cadServiceimpl222");
            for (Map.Entry<String, String[]> entry: fileInfo.entrySet()) {
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Cad cad = new Cad(author, folder, entry.getValue()[0], entry.getValue()[1], entry.getKey(), entry.getValue()[2], date);

                //cadRepository.save(cad);
                mongoTemplate.insert(cad, "cad");
            }
            /*System.out.println("cadserviceimpl333");
            try{
                File file = new File(AsposeUtils.dataDir);
                FileUtils.deleteDirectory(file);
            }catch (IOException e){
                e.printStackTrace();
            }*/



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void remove(File file) throws IOException {
        System.out.println(file.toString());
        if (file.isDirectory()) {
            removeDirectory(file);
        } else {
            file.delete();
        }
    }
    public void removeDirectory(File directory) throws IOException {
        System.out.println(directory.toString());
        File[] files = directory.listFiles();
        for (File file : files) {
            System.out.println(file.toString());
            file.delete();
        }
        directory.delete();
    }



    public List<Cad> searchCadFile(String searchText) {

        try {
            if (searchText == "")
                return null;
            String[] eachText = searchText.split(" ");

            Query query = new Query();
            Criteria criteria = new Criteria();

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
            e.printStackTrace();
        }
        return null;
    }
}
