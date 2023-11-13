package sanhak.shserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriBuilder;
import sanhak.shserver.cad.Cad;
import sanhak.shserver.cad.dto.SimilarDatasResDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonUtils {
    private WebClient webClientTfIdf;
    private WebClient webClientCnn;

    @Value("${python.tf-idf.url}")
    public void setTfIdfUrl(String url){
        this.webClientTfIdf = WebClient.builder().baseUrl(url).build();
    }

    @Value("${python.cnn.url}")
    public void setCnnUrl(String url){
        this.webClientCnn = WebClient.builder().baseUrl(url).build();
    }

    public void saveTfIdf() {
        try {
            String block = webClientTfIdf.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(block);
        } catch (WebClientException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void updateCNNClassification(String mainCategory) {
        try {
            Map<String, String> data = new HashMap<>();
            data.put("mainCategory", mainCategory);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);

            String response = webClientCnn.post()
                    .uri(UriBuilder::build)
                    .bodyValue(json)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(response);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
