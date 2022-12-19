package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {


    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View transfer by ID");
        System.out.println("4: View your pending requests");
        System.out.println("5: Send TE bucks");
        System.out.println("6: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("Your current account balance is: $" + balance);
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printTransferHistory(TransferDisplayDTO[] transferHistory, String LoggedInUser){
        if(transferHistory == null){
            System.out.println("No transfers found.");
        }
        System.out.print("-------------------------------------------\n" + "Transfers\n" +
                "ID          From/To                 Amount\n" + "-------------------------------------------\n");
        assert transferHistory != null;
        for (TransferDisplayDTO transferDisplayDTO : transferHistory) {
            System.out.println(transferDisplayDTO.toString(LoggedInUser));
        }
        System.out.println("---------");
    }

    public void printTransferById(TransferDisplayDTO transferById){
        if (transferById == null) {
            System.out.println("Transfer ID not valid.");
            return;
        }
        System.out.print("-------------------------------------------\n" +
                "Transfer Details\n" + "-------------------------------------------\n");
        System.out.print("Id: " + transferById.getTransferID() + "\nFrom: " + transferById.getSenderUsername() +
                "\nTo: " + transferById.getReceiverUsername() + "\nType: " + transferById.getTransferType() +
                "\nStatus: " + transferById.getStatus() + "\nAmount: $" + transferById.getTransferAmount());
    }

    public void printPendingRequests(RequestDTO[] pendingRequests) {
        if(pendingRequests == null){
            System.out.println("No requests found.");
        }
        for (int i = 0; i < pendingRequests.length; i++){
            System.out.println(pendingRequests[i].toString());
        }
    }

    public void printUserList(UserListDTO[] userList) {
        if(userList == null){
            System.out.println("No users found.");
        }
        System.out.print("-------------------------------------------\n" + "Users\n" +
                "ID          Name\n" + "-------------------------------------------\n");
        for (int i = 0; i < userList.length; i++){
            System.out.println(userList[i].toString());
        }
        System.out.println("\n---------");
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }
}
