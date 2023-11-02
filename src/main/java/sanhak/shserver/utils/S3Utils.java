package sanhak.shserver.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Base64;

@Transactional(readOnly = true)
@PropertySource(value = "application.properties")
@RequiredArgsConstructor
@Component
public class S3Utils {
    private final TransferManager transferManager;
    private final AmazonS3Client amazonS3Client;

    private final String key = "computerandinformationengineerin";
    private String iv = "kwangwoonunivers";
    public static String algo = "AES/CBC/PKCS5Padding";



    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public void downloadFolder(String dir) throws IOException, InterruptedException {
        try {
            File localDirectory = new File("s3-download");
            String tmp = URLDecoder.decode(dir,"utf-8");
            MultipleFileDownload downloadDirectory = transferManager.downloadDirectory(bucket, tmp, localDirectory);

            System.out.println("[ test ] download progressing... start");
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            while(!downloadDirectory.isDone()){
                Thread.sleep(1000);
                TransferProgress progress = downloadDirectory.getProgress();
                double percentTransferred = progress.getPercentTransferred();
                System.out.println("[ test ]" + decimalFormat.format(percentTransferred)+"% download progressing...");

            }
            System.out.println("[ test ] download directory from S3 succecss!");
        }
        catch (IOException e) {
            System.out.println("ERR");
            e.getMessage();
        }
    }
    public String putS3(String filePath, String fileName, ByteArrayOutputStream bos)throws IOException{

        byte[] data;
        String encryptStr="";
        if(bos == null){
            try {
                encryptStr = encryptAES256("https://dwg-upload.s3.ap-northeast-2.amazonaws.com/image/images.jpeg");
            }catch (Exception e){
                e.printStackTrace();
            }
            return encryptStr;
        }
        else{
            data = bos.toByteArray();
        }
        ByteArrayInputStream bin = new ByteArrayInputStream(data);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_JPEG_VALUE);
        metadata.setContentLength(data.length);

        String S3_fileName = fileName.substring(0,fileName.length()-4) + ".jpeg";

        amazonS3Client.putObject(bucket,filePath+S3_fileName,bin, metadata);
        String PathUrl = amazonS3Client.getUrl(bucket,filePath).toString();
        bin.close();
        try {
            encryptStr = encryptAES256(PathUrl + S3_fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encryptStr;
    }
    public String encryptAES256(String fileName) throws  Exception{
        Cipher cipher = Cipher.getInstance(algo);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),"AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivParameterSpec);

        byte[] encrypted = cipher.doFinal(fileName.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }
}