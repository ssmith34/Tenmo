package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.RequestDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
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

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferDTO[] getTransfers(){
        TransferDTO[] transferHistory = null;
        try {
            ResponseEntity<TransferDTO[]> response = restTemplate.exchange(baseUrl + "history", HttpMethod.GET,
                    makeAuthEntity(), TransferDTO[].class);
            transferHistory = response.getBody();
        } catch(RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());

        } catch(ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferHistory;
    }

    public Transfer getTransfer(int transferId) {
        Transfer transferById = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "history/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transferById = response.getBody();

        } catch(RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
       return transferById;
    }

    public RequestDTO[] getPendingRequests() {
        RequestDTO[] pendingRequests = null;
        try {
            ResponseEntity<RequestDTO[]> response = restTemplate.exchange(baseUrl + "view-pending-requests",
                    HttpMethod.GET, makeAuthEntity(), RequestDTO[].class);
            pendingRequests = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingRequests;
    }

    public boolean sendBucks(Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
        boolean success = false;
        try {
            restTemplate.put(baseUrl + "transfer",  entity);
            success = true;
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }
}


