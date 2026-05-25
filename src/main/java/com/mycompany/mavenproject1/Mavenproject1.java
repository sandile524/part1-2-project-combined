package com.mycompany.mavenproject1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import java.io.FileWriter;
import java.io.IOException;

public class Mavenproject1 {

    String username;
    String password;
    String cellphone;

    public boolean checkUsername(String username) {
        return username.contains("_") && username.length() >= 5;
    }

    public boolean checkPasswordComplex(String password) {
        String pattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
        return password.matches(pattern);
    }

    public boolean cellPhoneno(String cellphone) {
        return cellphone.startsWith("+27") && cellphone.length() == 12;
    }

    public String registerUser(String username, String password, String cellphone) {
        if (!checkUsername(username)) {
            return "Username is not correctly formatted. Please ensure your username contains an _ and is at least 5 characters long.";
        }
        if (!checkPasswordComplex(password)) {
            return "Password is not correctly formatted. Please ensure the password contains at least 8 characters, a capital letter, a number and a special character.";
        }
        if (!cellPhoneno(cellphone)) {
            return "Cellphone number incorrectly formatted or does not contain international code.";
        }
        this.username = username;
        this.password = password;
        this.cellphone = cellphone;
        return "User registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        return this.username != null
                && this.username.equals(username)
                && this.password.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + username + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
//part 2 of my poe class message
    public class Message {

        String MessageID;
        String messageText;
        String cellPhoneno;
//array to store sent message.
        ArrayList<String> sentMessages = new ArrayList<>();
        //counter for total messages sent
        int totalMessagesSent = 0;

        public Message(String cellPhoneno, String messageText) {
            //generation of the unique id that must be on the message.
            this.MessageID = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 10);
            this.cellPhoneno = cellPhoneno;
            this.messageText = messageText;
        }
//check if the message id lenght is equal to lenght of 10 charecters.
        public boolean checkMessageID(String MessageID) {
            return MessageID.length() == 10;
        }
//cellphone number must be 12 numbers and contains a internationl code 
        public boolean cellPhoneno(String cellphone) {
            return cellphone.startsWith("+27") && cellphone.length() == 12;
        }
//user must enter number that meets conditions of cellphone method.
        public String checkRecipientCell(String cellPhoneno) {
            if (cellPhoneno(cellPhoneno)) {
                return "Cellphone number successfully captured.";
            } else {
                return "Cellphone number is incorrectly formatted or does not contain international code.";
            }
        }
//creating message hash that must 
        public String createMessageHash() {
            //the two first charecters of id
            String firstTwo = MessageID.substring(0, 2);
            //splitting the message into words
            String[] words = messageText.split(" ");
            //getting the first two and last two words of the message
            String firstWord = words[0];
            String lastWord = words[words.length - 1];
            return firstTwo + ":" + firstWord.toUpperCase() + ":" + lastWord.toUpperCase();
        }
//storing messages in json file.
        public void storeMessage() {
            try {
                // // Write message details to file
                FileWriter audit = new FileWriter("messages.json", true);
                audit.write("{\n");
                audit.write("\"MessageID\": \"" + MessageID + "\",\n");
                audit.write("\"user\": \"" + cellPhoneno + "\",\n");
                audit.write("\"MessageHash\": \"" + createMessageHash() + "\",\n");
                audit.write("\"Message\": \"" + messageText + "\"\n");
                audit.write("}\n\n");
                audit.close();
                System.out.println("Message stored in file.");
            } catch (IOException e) {
                System.out.println("Error writing to file.");
            }
        }
//Method to send, store or disregard a message
        public String sentMessage(Scanner scan1) {
            System.out.println("\nOptions:");
            System.out.println("1. Send");
            System.out.println("2. Store");
            System.out.println("3. Disregard");
            int choice = scan1.nextInt();
            scan1.nextLine();
            if (choice == 1) {
                sentMessages.add(messageText);
                totalMessagesSent++;
                return "Message sent.";
            } else if (choice == 2) {
                storeMessage();
                return "Message stored.";
            } else if (choice == 3) {
                return "Message disregarded.";
            } else {
                return "Invalid option.";
            }
        }
// print messages basically displays sent messages
        public void printMessages() {
            System.out.println("\nSent Messages:");
            for (String message : sentMessages) {
                System.out.println(message);
            }
        }

        public int returnTotalMessages() {
            return totalMessagesSent;
        }

        public void printMessageDetails() {
            System.out.println("\n Message Details ");
            System.out.println("Message ID: " + MessageID);
            System.out.println("user: " + cellPhoneno);
            System.out.println("Message: " + messageText);
            System.out.println("Message Hash: " + createMessageHash());
        }
    }
//main class
    public static void main(String[] args) {
        //scanner to take users input
        Scanner Scan = new Scanner(System.in);
        //object of main class
        Mavenproject1 obj = new Mavenproject1();

        System.out.println("Register Your Details");
        System.out.println("Enter username:");
        String username = Scan.nextLine();
        System.out.println("Enter your cellphone number:");
        String cellPhoneno = Scan.nextLine();
        System.out.println("Enter your password:");
        String password = Scan.nextLine();

        String regResult = obj.registerUser(username, password, cellPhoneno);
        System.out.println(regResult);
//registering your details
        if (regResult.equals("User registered successfully.")) {
            System.out.println(obj.returnLoginStatus(username, password));

            System.out.println("Enter messenger cellphone number:");
            String recipientCell = Scan.nextLine();

            System.out.println("Enter your message:");
            String text = Scan.nextLine();

            Message msg = obj.new Message(recipientCell, text);

            if (msg.checkMessageID(msg.MessageID)) {
                System.out.println("Message ID successfully created.");
            } else {
                System.out.println("Invalid Message ID.");
            }

            System.out.println(msg.checkRecipientCell(recipientCell));
            msg.printMessageDetails();
            System.out.println(msg.sentMessage(Scan));
            msg.printMessages();
            //display total message 
            System.out.println("\nTotal Messages Sent: " + msg.returnTotalMessages());
        }

        Scan.close();
    }
}