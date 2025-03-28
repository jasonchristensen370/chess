package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import net.ServerFacade;
import servicemodel.*;

import static ui.EscapeSequences.*;

import static ui.InputChecker.isNotValidMenuInput;
import static ui.InputChecker.isNotValidStringInput;

// Draws the Menu
public class Client {

    private boolean loggedIn;
    private boolean exit;
    PrintStream out;

    ServerFacade serverFacade;
    Scanner scanner;
    String authToken;
    HashMap<Integer, GameData> listGameData;

    public Client() {
        loggedIn = false;
        exit = false;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        serverFacade = new ServerFacade(8080);
        scanner = new Scanner(System.in);
        authToken = null;
        listGameData = new HashMap<>();
    }

    public void run() {
        displayWelcome();
        preLoginMenu();
        if (exit) {
            out.print(SET_MESSAGE_TEXT+"Bye!"+RESET_TEXT);
        }
    }

    private void displayWelcome() {
        out.println(SET_MESSAGE_TEXT+"Welcome to 240 chess. Register or login to get started."+RESET_TEXT);
    }

    private void preLoginMenu() {
        while (!loggedIn && !exit) {
            out.println(SET_MENU_TEXT+"\n1. Register\n2. Login\n3. Quit\n4. Help"+RESET_TEXT);
            out.print("\n[LOGGED OUT] >>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (isNotValidMenuInput(input, 4)) {
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
                loggedIn = register();
                break;
            case "2":
                loggedIn = login();
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
            if (isNotValidMenuInput(input, 6)) {
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
                loggedIn = !logout();
                break;
            case "3": // Create Game
                createGame();
                break;
            case "4": // List Games
                listGames();
                break;
            case "5": // Play Game
                playGame();
                break;
            case "6": // Observe Game
                observeGame();
                break;
        }
    }

    private boolean register() {
        try {
            printPrompt("Please Input Username");
            String username = scanner.nextLine();
            if (username.isEmpty()) {
                printError("\nUsername cannot be empty");
                return false;
            }
            printPrompt("Please Input Password");
            String password = scanner.nextLine();
            if (password.isEmpty()) {
                printError("\nPassword cannot be empty");
                return false;
            }
            out.print("Please Input Email: ");
            String email = scanner.nextLine();
            if (email.isEmpty()) {
                printError("\nEmail cannot be empty");
                return false;
            }
            var req = new RegisterRequest(username, password, email);
            RegisterResult res = serverFacade.register(req);
            authToken = res.authToken();
            return true;
        } catch (ResponseException e) {
            if (e.statusCode()==403) {
                printError("\nUsername already taken, please try again");
            } else {
                printError("\nFailed to register, please try again");
            }
            return false;
        }
    }

    private boolean login() {
        try {
            printPrompt("Please Input Username");
            String username = scanner.nextLine();
            if (isNotValidStringInput(username)) {
                printError("\nUsername cannot be empty");
                return false;
            }
            printPrompt("Please Input Password");
            String password = scanner.nextLine();
            if (isNotValidStringInput(password)) {
                printError("\nPassword cannot be empty");
                return false;
            }
            var req = new LoginRequest(username, password);
            LoginResult res = serverFacade.login(req);
            authToken = res.authToken();
            printMessage("\nLogged in successfully as "+res.username());
            return res.authToken() != null;
        } catch (ResponseException e) {
            printError("\nUsername or password incorrect, please try again");
            return false;
        }
    }

    private boolean logout() {
        try {
            LogoutRequest req = new LogoutRequest(authToken);
            serverFacade.logout(req);
            authToken = null;
            printMessage("\nLogged out successfully");
            return true;
        } catch (ResponseException e) {
            printError("\nFailed to logout");
            return false;
        }
    }

    private void createGame() {
        try {
            printPrompt("Please Input Game Name");
            String gameName = scanner.nextLine();
            if (isNotValidStringInput(gameName)) {
                printError("\nGame Name cannot be empty");
            }
            CreateGameRequest req = new CreateGameRequest(authToken, gameName);
            CreateGameResult res = serverFacade.createGame(req);
            if (res.message() == null) {
                printMessage("\nGame Created!");
            }
        } catch (ResponseException e) {
            printError("\nGame was not created.");
        }
    }

    private void listGames() {
        try {
            var req = new ListRequest(authToken);
            var res = serverFacade.listGames(req);
            if (res.games() == null || res.games().isEmpty()) {
                printMessage("\nThere are no games to list");
            } else {
                printGameList(res.games());
            }
        } catch (ResponseException e) {
            printError("\nFailed to list games");
        }
    }

    private void printGameList(ArrayList<GameData> games) {
        int counter = 1;
        out.println(SET_MESSAGE_TEXT+SET_TEXT_UNDERLINE+"\nLIST OF GAMES"+RESET_TEXT);
        for (var game : games) {
            listGameData.put(counter, game);
            printGame(game, counter);
            counter++;
        }
    }

    private void printGame(GameData game, int gameID) {
        String gameName = SET_TEXT_BOLD+game.gameName()+RESET_TEXT_BOLD_FAINT;
        String whiteUsername = game.whiteUsername()==null ? "available" : "\""+SET_TEXT_BOLD+game.whiteUsername()+RESET_TEXT_BOLD_FAINT+"\"";
        String blackUsername = game.blackUsername()==null ? "available" : "\""+SET_TEXT_BOLD+game.blackUsername()+RESET_TEXT_BOLD_FAINT+"\"";
        out.println(gameID+": \""+gameName+"\", WHITE: "+whiteUsername+", BLACK: "+blackUsername);
    }

    private void playGame() {
        try {
            printPrompt("Please Input Game Number to Join");
            String gameNumString = scanner.nextLine();
            if (isNotValidMenuInput(gameNumString, listGameData.size())) {
                printError("\nPlease input valid game number in list");
                return;
            }
            int gameNum = Integer.parseInt(gameNumString);
            printPrompt("Please Input Color to Play (WHITE/BLACK)");
            String color = scanner.nextLine();
            if (!color.equalsIgnoreCase("WHITE") && !color.equalsIgnoreCase("BLACK")) {
                printError("\nPlease choose valid player color (WHITE/BLACK)");
                return;
            }
            GameData game = listGameData.get(gameNum);
            var req = new JoinGameRequest(authToken, color, game.gameID());
            serverFacade.joinGame(req);
            ChessGame.TeamColor teamColor = color.equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            ui.ChessBoardGraphics.drawChessBoard(game.game(), teamColor);
            // Display board until they press enter
            scanner.nextLine();
        } catch (ResponseException e) {
            if (e.statusCode() == 403) {
                printError("\nPlayer color already taken");
            } else {
                printError(e.statusCode()+"\nFailed to join the game");
            }
        }
    }

    private void observeGame() {
        listGames();
        printPrompt("\nPlease Input Game Number to Observe");
        String gameNumString = scanner.nextLine();
        if (isNotValidMenuInput(gameNumString, listGameData.size())) {
            printError("\nPlease input valid game number in list");
            return;
        }
        int gameNum = Integer.parseInt(gameNumString);
        ui.ChessBoardGraphics.drawChessBoard(listGameData.get(gameNum).game(), ChessGame.TeamColor.WHITE);
        // Display board until they press enter
        scanner.nextLine();
    }

    private void printError(String text) {
        out.println(SET_ERROR_TEXT+text+RESET_TEXT);
    }

    private void printMessage(String text) {
        out.println(SET_MESSAGE_TEXT+text+RESET_TEXT);
    }

    private void printPrompt(String text) {
        out.print(text+": ");
    }

}
