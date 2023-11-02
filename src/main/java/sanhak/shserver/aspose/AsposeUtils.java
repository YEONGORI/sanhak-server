package sanhak.shserver.aspose;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sanhak.shserver.s3.S3Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@RequiredArgsConstructor
@Component
public class AsposeUtils {
    private final S3Utils s3Utils;
    public static final String dataDir = setDataPath() + "s3-download" + File.separator;
    public static final String ImagePath = setDataPath() + "testtest" + File.separator;

    public static String setDataPath() {
        File dir = new File(System.getProperty("user.dir"));
        return dir + File.separator;
    }

    public Map<String, String[]> searchCadFleInDataDir(String dir) {
        System.out.println("searchCadFileInDataDir");
        System.out.println(dir);
        Map<String, String[]> fileInfo = new HashMap<>();
        try {
            System.out.println("searchCadFileInDataDir2222");
            Files.walkFileTree(Paths.get(dataDir + dir), new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
                    System.out.println("visitFile");
                    if (!Files.isDirectory(file) && file.getFileName().toString().contains(".dwg")) {
                        String fileName = file.getFileName().toString();
                        String filePath = file.toAbsolutePath().toString();
                        String fileIndex = extractTextInCadFile(filePath);
                        System.out.println("complete extract");
                        ByteArrayOutputStream bos = CadToJpeg(filePath);
                        System.out.println("complete cadToJpeg");
                        String S3url = s3Utils.putS3("image/", fileName, bos);
                        System.out.println(S3url);
                        filePath = filePath.substring(filePath.indexOf(dir) + dir.length(), filePath.indexOf(fileName) - 1);
                        System.out.println(filePath);
                        fileInfo.put(fileIndex, new String[]{filePath, fileName, S3url});
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println("visitFIle error");
            e.printStackTrace();
        }
        return fileInfo;
    }

    public static String extractTextInCadFile(String fileName) {

        System.out.println("extractTextInCadFile");

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
            System.out.println(index);
            return index;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public ByteArrayOutputStream CadToJpeg(String filePath) {
        System.out.println("CadToJpeg");

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
            System.out.println("cadtoimage error");
            e.printStackTrace();
            return null;
        }
    }
}
