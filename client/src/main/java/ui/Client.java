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

    ClientCommunicator clientCom;

    public Client() {
        loggedIn = false;
        clientCom = new ClientCommunicator();
        exit = false;
    }

    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        displayWelcome(out);
        preLoginMenu(out);
        if (exit) {
            out.print("Bye!");
            return;
        }

    }

    private void displayWelcome(PrintStream out) {
//        out.print(BG);
        out.println("Welcome to 240 chess. Register or login to get started.\n");
    }

    private void preLoginMenu(PrintStream out) {
        while (!loggedIn) {
            out.println("1. Register\n2. Login\n3. Quit\n4. Help\n");
            out.print("[LOGGED OUT] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (!isValidInput(input, 4)) {
                out.println("Please input valid menu option number");
                continue;
            }
            switch(input) {
                case "1":
                    loggedIn = clientCom.register();
                    if (!loggedIn) {
                        out.println("\nFailed to login, please try again:\n");
                    }
                    break;
                case "2":
                    loggedIn = clientCom.login();
                    if (!loggedIn) {
                        out.println("Failed to login, please try again:");
                    }
                    break;
                case "3":
                    exit=true;
                    break;
                case "4":
                    out.println("Enter one of the option numbers to get started.\n");
            }
            if (exit) {
                break;
            }

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
