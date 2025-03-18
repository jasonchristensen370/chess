package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import net.ClientCommunicator;
import static ui.EscapeSequences.*;

import static ui.InputChecker.isNotValidInput;

// Draws the Menu
public class Client {

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
            out.print(SET_MESSAGE_TEXT+"Bye!"+RESET_TEXT);
        }
    }

    private void displayWelcome() {
//        out.print(BG);
        out.println(SET_MESSAGE_TEXT+"Welcome to 240 chess. Register or login to get started."+RESET_TEXT);
    }

    private void preLoginMenu() {
        while (!loggedIn && !exit) {
            out.println(SET_MENU_TEXT+"\n1. Register\n2. Login\n3. Quit\n4. Help"+RESET_TEXT);
            out.print("\n[LOGGED OUT] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (isNotValidInput(input, 4)) {
                out.println(SET_ERROR_TEXT+"\nPlease input valid menu option number"+RESET_TEXT);
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
                out.println(SET_MESSAGE_TEXT+"\nEnter the number of one of the options to get started."+RESET_TEXT);
        }
    }

    private void postLoginMenu() {
        while (loggedIn) {
            out.println(SET_MENU_TEXT+"\n1. Help\n2. Logout\n3. Create Game\n4. List Games\n5. Play Game\n6. Observe Game"+RESET_TEXT);
            out.print("\n[LOGGED IN] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (isNotValidInput(input, 6)) {
                out.println(SET_ERROR_TEXT+"\nPlease input valid menu option number"+RESET_TEXT);
                continue;
            }
            evalPostLogin(input);
        }
        preLoginMenu();
    }

    private void evalPostLogin(String input) {
        switch(input) {
            case "1": // Help
                out.println(SET_MESSAGE_TEXT+"\nEnter the number of the option you wish to select."+RESET_TEXT);
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
