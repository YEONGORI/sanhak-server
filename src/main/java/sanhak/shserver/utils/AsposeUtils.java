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

@Slf4j
@Component
@RequiredArgsConstructor
public class AsposeUtils {
    private final S3Utils s3Utils;

    public static String setDataPath() {
        File dir = new File(System.getProperty("user.dir"));
        return dir + File.separator;
    }

    public Map<String, String[]> searchCadFile(String fileName) {
        Map<String, String[]> fileInfo = new HashMap<>();

        String filePath = setDataPath() + fileName;
        String fileIndex = extractTextInCadFile(filePath);
        ByteArrayOutputStream os = CadToJpeg(filePath);
        String s3Url = s3Utils.uploadS3("image/", fileName, os);
        fileInfo.put(fileIndex, new String[]{"", fileName, s3Url});
        log.info("CAD file data start");
        log.info("CAD file data finish");
        return fileInfo;
    }

    public Map<String, String[]> searchCadFileInDataDir(String dir) {
        String projectDir = setDataPath() + dir + File.separator;
        Map<String, String[]> fileInfo = new HashMap<>();

        try {
            Files.walkFileTree(Paths.get(projectDir + dir), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (!Files.isDirectory(file) && file.getFileName().toString().contains(".dwg")) {
                        String fileName = file.getFileName().toString();
                        String filePath = file.toAbsolutePath().toString();
                        String fileIndex = extractTextInCadFile(filePath);
                        ByteArrayOutputStream bos = CadToJpeg(filePath);
                        String S3url = s3Utils.uploadS3("image/", fileName, bos);
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
        StringBuilder index = new StringBuilder();
        try {
            CadImage cadImage = (CadImage) CadImage.load(fileName);
            for (CadBlockEntity blockEntity : cadImage.getBlockEntities().getValues()) {
                for (CadBaseEntity entity : blockEntity.getEntities()) {
                    if (entity.getTypeName() == CadEntityTypeName.TEXT) {
                        CadText childObjectText = (CadText)entity;
                        index.append(childObjectText.getDefaultValue()).append("| ");
                    }
                    
                    else if (entity.getTypeName() == CadEntityTypeName.MTEXT) {
                        CadMText childObjectText = (CadMText)entity;
                        index.append(childObjectText.getText());
                        index.append("| ");
                    }

                }
            }
            return index.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
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
}
