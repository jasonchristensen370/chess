package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import net.ClientCommunicator;

import static ui.InputChecker.isNotValidInput;

// Draws the Menu
public class Client {

//    private static final String BG = SET_BG_COLOR_BLACK;
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
            if (isNotValidInput(input, 4)) {
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
                break;
            case "2":
                loggedIn = clientCom.login();
                break;
            case "3":
                exit=true;
                break;
            case "4":
                out.println("\nEnter the number of one of the options to get started.");
        }
    }

    private void postLoginMenu() {
        while (loggedIn) {
            out.println("\n1. Help\n2. Logout\n3. Create Game\n4. List Games\n5. Play Game\n6. Observe Game");
            out.print("\n[LOGGED IN] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (isNotValidInput(input, 6)) {
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
                out.println("\nEnter the number of the option you wish to select.");
                break;
            case "2": // Logout
                loggedIn = !clientCom.logout();
                break;
            case "3": // Create Game
                clientCom.createGame();
                break;
            case "4": // List Games
                clientCom.listGames();
                break;
            case "5": // Play Game
                clientCom.playGame();
                break;
            case "6": // Observe Game
                clientCom.observeGame();
                break;
        }
    }


}
