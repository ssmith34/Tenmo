package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private final String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Transfer[] getTransfer(){
        Transfer[] transferHistory = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "history", HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class);
            transferHistory = response.getBody();
        }catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());

        }catch(ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transferHistory;
    }
}


