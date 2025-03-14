package net;

import servicemodel.*;

import java.io.PrintStream;
import java.util.Scanner;

public class ClientCommunicator {

    ServerFacade serverFacade;
    Scanner scanner;
    PrintStream out;

    public ClientCommunicator() {
        serverFacade = new ServerFacade();
        scanner = new Scanner(System.in);
        out = new PrintStream(System.out);
    }

    public boolean register() {
        out.print("Please Input Username: ");
        String username = scanner.nextLine();
        out.print("Please Input Password: ");
        String password = scanner.nextLine();
        out.print("Please Input Email: ");
        String email = scanner.nextLine();
        var req = new RegisterRequest(username, password, email);
        RegisterResult res = serverFacade.register(req);
        return res.authToken() != null;
    }

    public boolean login() {
        out.print("Please Input Username: ");
        String username = scanner.nextLine();
        out.print("Please Input Password: ");
        String password = scanner.nextLine();
        var req = new LoginRequest(username, password);
        LoginResult res = serverFacade.login(req);
        return res.authToken() != null;
    }

}
