package sanhak.shserver.utils;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadconsts.CadEntityTypeName;
import com.aspose.cad.fileformats.cad.cadobjects.CadBaseEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadBlockEntity;
import com.aspose.cad.fileformats.cad.cadobjects.CadMText;
import com.aspose.cad.fileformats.cad.cadobjects.CadText;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.JpegOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsposeUtils {
    private final S3Utils s3Utils;
    public static final String dataDir = setDataPath() + "s3-download" + File.separator;

    public static String setDataPath() {
        File dir = new File(System.getProperty("user.dir"));
        return dir + File.separator;
    }

    public Map<String, String[]> searchCadFile(String fileName) {
        Map<String, String[]> fileInfo = new HashMap<>();

        String filePath = setDataPath() + fileName;
        try {
            String fileIndex = extractTextInCadFile(filePath);
            ByteArrayOutputStream os = CadToJpeg(filePath);
            String s3Url = s3Utils.putS3("image/", fileName, os);
            fileInfo.put(fileIndex, new String[]{"", fileName, s3Url});
            log.info("CAD file data start");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("CAD file data finish");
        return fileInfo;
    }

    public Map<String, String[]> searchCadFileInDataDir(String dir) {
        Map<String, String[]> fileInfo = new HashMap<>();
        try {
            Files.walkFileTree(Paths.get(dataDir + dir), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
                    if (!Files.isDirectory(file) && file.getFileName().toString().contains(".dwg")) {
                        String fileName = file.getFileName().toString();
                        String filePath = file.toAbsolutePath().toString();
                        String fileIndex = extractTextInCadFile(filePath);
                        ByteArrayOutputStream bos = CadToJpeg(filePath);
                        String S3url = s3Utils.putS3("image/", fileName, bos);
                        filePath = filePath.substring(filePath.indexOf(dir) + dir.length(), filePath.indexOf(fileName) - 1);
                        fileInfo.put(fileIndex, new String[]{filePath, fileName, S3url});
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return fileInfo;
    }

    public static String extractTextInCadFile(String fileName) {
        String index = "";
        try {
            CadImage cadImage = (CadImage) CadImage.load(fileName);
            for (CadBlockEntity blockEntity : cadImage.getBlockEntities().getValues()) {
                for (CadBaseEntity entity : blockEntity.getEntities()) {
                    if (entity.getTypeName() == CadEntityTypeName.TEXT) {
                        CadText childObjectText = (CadText)entity;
                        index = index + childObjectText.getDefaultValue() + "| ";
                    }
                    
                    else if (entity.getTypeName() == CadEntityTypeName.MTEXT) {
                        CadMText childObjectText = (CadMText)entity;
                        index += childObjectText.getText();
                        index += "| ";
                    }

                }
            }
            return index;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public ByteArrayOutputStream CadToJpeg(String filePath) {
        Image image = Image.load(filePath);

        CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
        rasterizationOptions.setPageWidth(200);
        rasterizationOptions.setPageHeight(200);

        JpegOptions options = new JpegOptions();
        options.setVectorRasterizationOptions(rasterizationOptions);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.save(os, options);
        return os;
    }

    public ByteArrayOutputStream CadToJpeg2(String filePath) {
        try {
            Image cadImage = Image.load(filePath);

            CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();
            rasterizationOptions.setPageHeight(200);
            rasterizationOptions.setPageWidth(200);

            JpegOptions options = new JpegOptions();

            options.setVectorRasterizationOptions(rasterizationOptions);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ExecutorService executor = Executors.newCachedThreadPool();
            Callable<Object> task = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    cadImage.save(bos, options);
                    System.out.println("success");
                    return bos;
                }
            };
            Future<Object> future = executor.submit(task);
            executor.shutdown();
            try {
                Object result = future.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException | ExecutionException | InterruptedException time_e) {
                time_e.printStackTrace();
                return null;
            }
            return bos;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
