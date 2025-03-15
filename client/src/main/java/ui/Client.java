package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import net.ClientCommunicator;
import static ui.EscapeSequences.*;

// Draws the Menu
public class Client {

    private static final String BG = SET_BG_COLOR_BLACK;
    private boolean loggedIn;
    private boolean exit;
    PrintStream out;

    ClientCommunicator clientCom;

    public Client() {
        loggedIn = false;
        clientCom = new ClientCommunicator();
        exit = false;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void run() {
        displayWelcome();
        preLoginMenu();
        if (exit) {
            out.print("Bye!");
        }
    }

    private void displayWelcome() {
//        out.print(BG);
        out.println("Welcome to 240 chess. Register or login to get started.");
    }

    private void preLoginMenu() {
        while (!loggedIn && !exit) {
            out.println("\n1. Register\n2. Login\n3. Quit\n4. Help");
            out.print("\n[LOGGED OUT] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (!isValidInput(input, 4)) {
                out.println("\nPlease input valid menu option number");
                continue;
            }
            evalPreLogin(input);
        }
        if (!exit) {
            postLoginMenu();
        }
    }

    private void evalPreLogin(String input) {
        switch(input) {
            case "1":
                loggedIn = clientCom.register();
                if (!loggedIn) {
                    out.println("\nFailed to login, please try again:");
                }
                break;
            case "2":
                loggedIn = clientCom.login();
                // TODO: Remove after implementing server calls
//                loggedIn = true;
                if (!loggedIn) {
                    out.println("\nFailed to login, please try again:");
                }
                break;
            case "3":
                exit=true;
                break;
            case "4":
                out.println("\nEnter one of the option numbers to get started.");
        }
    }

    private void postLoginMenu() {
        while (loggedIn) {
            out.println("\n1. Help\n2. Logout\n3. Create Game\n4. List Games\n5. Play Game\n6. Observe Game");
            out.print("\n[LOGGED IN] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (!isValidInput(input, 6)) {
                out.println("\nPlease input valid menu option number");
                continue;
            }
            evalPostLogin(input);
        }
        preLoginMenu();
    }

    private void evalPostLogin(String input) {
        switch(input) {
            case "1": // Help
                out.println("\nEnter one of the option numbers to get started.");
            case "2": // Logout
                loggedIn = !clientCom.logout();
                // TODO: Remove after implementing server calls
                loggedIn = false;
                if (loggedIn) {
                    out.println("\nFailed to logout.");
                }
                break;
            case "3": // Create Game
                if (clientCom.createGame()) {
                    out.println("\nGame Created!");
                } else {
                    out.println("\nGame was not created.");
                }
                break;
            case "4": // List Games
                if (!clientCom.listGames()) {
                    out.println("Failed to list games.");
                }
                break;
            case "5": // Play Game
                clientCom.playGame();
                break;
            case "6": // Observe Game
                clientCom.observeGame();
                break;
        }
    }



    private boolean isValidInput(String input, int max) {
        if (!isNumeric(input)) {
            return false;
        }
        int num = Integer.parseInt(input);
        return num >= 1 && num <= max;
    }

    private boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
