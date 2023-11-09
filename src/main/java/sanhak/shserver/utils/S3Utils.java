package sanhak.shserver.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Utils {
    private final TransferManager transferManager;
    private final AmazonS3Client amazonS3Client;

    private static final String key = "computerandinformationengineerin";
    private static final String iv = "kwangwoonunivers";
    private static final String algo = "AES/CBC/PKCS5Padding";

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public void downloadFolder(String dirName) {
        try {
            File localDirectory = new File(dirName);
            dirName = URLDecoder.decode(dirName, StandardCharsets.UTF_8);

            log.info("Download folder start");
            MultipleFileDownload downloadDirectory = transferManager.downloadDirectory(bucket, dirName, localDirectory);
            while(!downloadDirectory.isDone())
                Thread.sleep(1000);
            log.info("Download folder finish");
        } catch (InterruptedException |  AmazonS3Exception e) {
            log.error(e.getMessage());
            throw new AmazonS3Exception(e.getMessage());
        }
    }

//    public String putS3(String filePath, String fileName, ByteArrayOutputStream bos)throws IOException{
//
//        byte[] data;
//        String encryptStr="";
//        if(bos == null){
//            try {
//                encryptStr = encryptAES256("https://dwg-upload.s3.ap-northeast-2.amazonaws.com/image/images.jpeg");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return encryptStr;
//        }
//        else{
//            data = bos.toByteArray();
//        }
//        ByteArrayInputStream bin = new ByteArrayInputStream(data);
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        metadata.setContentLength(data.length);
//
//        String S3_fileName = fileName.substring(0,fileName.length()-4) + ".jpeg";
//
//        amazonS3Client.putObject(bucket,filePath+S3_fileName,bin, metadata);
//        String PathUrl = amazonS3Client.getUrl(bucket,filePath).toString();
//        bin.close();
//        try {
//            encryptStr = encryptAES256(PathUrl + S3_fileName);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        log.info("upload image finish");
//        return encryptStr;
//    }

    public String uploadS3(String filePath, String fileName, ByteArrayOutputStream bos) {
        try {
            String defaultUrl = "https://dwg-upload.s3.ap-northeast-2.amazonaws.com/image/images.jpeg";

            String fileUrl = (bos == null) ? defaultUrl : uploadFiles(filePath, fileName, bos);
            String encryptedUrl = encryptAES256(fileUrl);

            log.info("upload image finish");
            return encryptedUrl;
        } catch (IOException e) {
            log.error("Error occurred while uploading to S3 or encrypting the URL", e);
            throw new RuntimeException(e);
        }
    }

    private String uploadFiles(String filePath, String fileName, ByteArrayOutputStream bos) throws IOException {
        byte[] data = bos.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(data);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
        metadata.setContentLength(data.length);

        String S3FileName = fileName.substring(0, fileName.length() - 4) + ".jpeg";

        amazonS3Client.putObject(bucket, filePath + S3FileName, bin, metadata);
        bin.close();

        String pathUrl = amazonS3Client.getUrl(bucket, filePath).toString();
        return pathUrl + S3FileName;
    }

    public void downloadFile(String fileName) {
        try {
            fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, fileName);
            File file = new File(fileName);

            log.info("Download file start");
            Download download = transferManager.download(getObjectRequest, file);
            while (!download.isDone())
                Thread.sleep(1000);
            log.info("Download file finish");
            log.debug("Downloaded file name: " + file.getName());
        } catch (InterruptedException | AmazonS3Exception e) {
            log.error(e.getMessage());
        }
    }

    private String encryptAES256(String fileName) {
        try {
            Cipher cipher = Cipher.getInstance(algo);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(fileName.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error("Cipher getInstance Error: " + e.getMessage());
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.error("IvParameterSpec Error: " + e.getMessage());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("Cipher init Error: " + e.getMessage());
        }
        throw new RuntimeException();
    }
}
