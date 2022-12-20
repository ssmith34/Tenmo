package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.RequestDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDisplayDTO;
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

    private HttpEntity<Integer> makeIntegerEntity(Integer number) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(number, headers);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferDisplayDTO[] getTransfers(){
        TransferDisplayDTO[] transferHistory = null;
        try {
            ResponseEntity<TransferDisplayDTO[]> response = restTemplate.exchange(baseUrl + "transfer-history", HttpMethod.GET, makeAuthEntity(), TransferDisplayDTO[].class);
            transferHistory = response.getBody();
        } catch(RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());

        } catch(ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferHistory;
    }

    public TransferDisplayDTO getTransferByID(int transferId) {
        TransferDisplayDTO transferById = null;
        try {
            ResponseEntity<TransferDisplayDTO> response = restTemplate.exchange(baseUrl + "transfer-history/" + transferId,
                    HttpMethod.GET, makeAuthEntity(), TransferDisplayDTO.class);
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

    public boolean sendBucks(Transfer sendingTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(sendingTransfer);
        boolean success = false;
        try {
            restTemplate.put(baseUrl + "send-money", entity);
            success = true;
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean requestBucks(Transfer requestingTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(requestingTransfer);
        boolean success = false;
        try {
            restTemplate.put(baseUrl + "request-money", entity);
            success = true;
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean approveRequest(Integer transferID) {
        boolean success = false;
        try {
            restTemplate.put(baseUrl + "approve-request/" + transferID, makeAuthEntity());
            success = true;
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean denyRequest(int transferID) {
        boolean success = false;
        try {
            restTemplate.put(baseUrl + "deny-request/" + transferID, makeAuthEntity());
            success = true;
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }
}


