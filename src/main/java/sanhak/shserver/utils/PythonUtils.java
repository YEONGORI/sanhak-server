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
    private WebClient webClientSim;
    private WebClient webClientCnn;

    @Value("${python.tf-idf.url}")
    public void setTfIdfUrl(String url){
        this.webClientTfIdf = WebClient.builder().baseUrl(url).build();
    }

    @Value("${python.similarity.url}")
    public void setSimUrl(String url){
        this.webClientSim = WebClient.builder().baseUrl(url).build();
    }

    @Value("${python.cnn.url}")
    public void setCnnUrl(String url){
        this.webClientCnn = WebClient.builder().baseUrl(url).build();
    }

    public void saveTfIdf() {
        String block = webClientTfIdf.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(block);
    }

    public Set<Cad> getSimilarCads(String id) {
//        WebClient webClient = WebClient.builder().baseUrl(cnnUrl).build();
        String block = webClientSim.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/" + id)
                                .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(block);

        log.info("PythonUtils.getSimilarCads");
        return null;
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
        }
    }
}
