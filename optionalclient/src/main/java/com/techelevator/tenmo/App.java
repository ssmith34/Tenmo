package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService();
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private UserCredentials userCredentials;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            userService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
            accountService.setAuthToken(currentUser.getToken());
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        userCredentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(userCredentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            }else if (menuSelection ==3){
                viewTransferById();
            }else if (menuSelection == 4) {
                viewPendingRequests();
            } else if (menuSelection == 5) {
                sendBucks();
            } else if (menuSelection == 6) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance = userService.getBalance();
        consoleService.printBalance(balance);
	}

	private void viewTransferHistory() {
        TransferDisplayDTO[] transferHistory = transferService.getTransfers();
        String LoggedInUser = this.currentUser.getUser().getUsername();
        consoleService.printTransferHistory(transferHistory, LoggedInUser);
	}

    private void viewTransferById() {
        int transferId =  consoleService.promptForInt("Please enter transfer ID: ");
        TransferDisplayDTO transferById = transferService.getTransferByID(transferId);
        consoleService.printTransferById(transferById);
    }

	private void viewPendingRequests() {
		RequestDTO[] pendingRequests = transferService.getPendingRequests();
        consoleService.printPendingRequests(pendingRequests);
	}

	private void sendBucks() {
        UserListDTO[] userList = userService.getAllUsers();
        consoleService.printUserList(userList);
        int receivingID = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");
		Transfer transfer = new Transfer();
        transfer.setReceiverAccountId(receivingID);
        transfer.setAmount(consoleService.promptForBigDecimal("Enter amount: "));
		boolean success = transferService.sendBucks(transfer);
        if (success)
            System.out.println("Transfer successful.");
        else
            System.out.println("Transfer failed.");
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
