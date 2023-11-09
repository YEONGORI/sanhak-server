package sanhak.shserver.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import sanhak.shserver.cad.CadLabel;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonUtils {
    @Value("${cloud.aws.tf-idf.lambda}")
    private String tfIdfUrl;

    @Value("${cloud.aws.cnn.lambda}")
    private String cnnUrl;

    public void saveTfIdf(String folder) {
        WebClient webClient = WebClient.builder().baseUrl(tfIdfUrl).build();

        String block = webClient.get().retrieve().bodyToMono(String.class).block();
        log.info(block);
    }

    public CadLabel startCNN(String s3Url) {
        WebClient webClient = WebClient.builder().baseUrl(cnnUrl).build();

        String block = webClient.get().retrieve().bodyToMono(String.class).block();
        return CadLabel.valueOf(block);
    }
}
