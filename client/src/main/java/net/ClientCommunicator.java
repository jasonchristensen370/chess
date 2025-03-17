package net;

import chess.ChessGame.TeamColor;
import exception.ResponseException;
import model.GameData;
import servicemodel.*;
import static ui.InputChecker.isNotValidInput;

import static ui.EscapeSequences.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientCommunicator {

    ServerFacade serverFacade;
    Scanner scanner;
    PrintStream out;
    String authToken;
    HashMap<Integer, GameData> listGameData;

    public ClientCommunicator() {
        serverFacade = new ServerFacade(8080);
        scanner = new Scanner(System.in);
        out = new PrintStream(System.out);
        authToken = null;
        listGameData = new HashMap<>();
    }

    public boolean register() {
        try {
            out.print("Please Input Username: ");
            String username = scanner.nextLine();
            out.print("Please Input Password: ");
            String password = scanner.nextLine();
            out.print("Please Input Email: ");
            String email = scanner.nextLine();
            var req = new RegisterRequest(username, password, email);
            RegisterResult res = serverFacade.register(req);
            authToken = res.authToken();
            return res.authToken() != null;
        } catch (ResponseException e) {
            out.println("\nFailed to register, please try again:");
            return false;
        }
    }

    public boolean login() {
        try {
            out.print("Please Input Username: ");
            String username = scanner.nextLine();
            out.print("Please Input Password: ");
            String password = scanner.nextLine();
            var req = new LoginRequest(username, password);
            LoginResult res = serverFacade.login(req);
            authToken = res.authToken();
            return res.authToken() != null;
        } catch (ResponseException e) {
            out.println("\nUsername or password incorrect, please try again:");
            return false;
        }
    }

    public boolean logout() {
        try {
            LogoutRequest req = new LogoutRequest(authToken);
            LogoutResult res = serverFacade.logout(req);
            authToken = null;
            return res.message() == null;
        } catch (ResponseException e) {
            out.println("\nFailed to logout.");
            return false;
        }
    }

    public void createGame() {
        try {
            out.print("Please Input Game Name: ");
            String gameName = scanner.nextLine();
            CreateGameRequest req = new CreateGameRequest(authToken, gameName);
            CreateGameResult res = serverFacade.createGame(req);
            if (res.message() == null) {
                out.println("\nGame Created!");
            }
        } catch (ResponseException e) {
            out.println("\nGame was not created.");
        }
    }

    public void listGames() {
        try {
            var req = new ListRequest(authToken);
            var res = serverFacade.listGames(req);
            if (res.games() == null || res.games().isEmpty()) {
                out.println("\nThere are no games to list.");
            } else {
                printGameList(res.games());
            }
        } catch (ResponseException e) {
            out.println("Failed to list games.");
        }
    }

    private void printGameList(ArrayList<GameData> games) {
        int counter = 1;
        out.println();
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

    public void playGame() {
        try {
            out.print("Please Input Game Number to Join: ");
            // TODO: Add error handling for bad input
            Integer gameNum = Integer.parseInt(scanner.nextLine());
            out.print("Please Input Color to Play (WHITE/BLACK): ");
            String color = scanner.nextLine();
            GameData game = listGameData.get(gameNum);
            var req = new JoinGameRequest(authToken, color, game.gameID());
            serverFacade.joinGame(req);
            TeamColor teamColor = color.equalsIgnoreCase("WHITE") ? TeamColor.WHITE : TeamColor.BLACK;
            ui.ChessBoardGraphics.drawChessBoard(game.game(), teamColor);
            // Display board until they press enter
            scanner.nextLine();
        } catch (ResponseException e) {
            out.println("Failed to join the game.");
        }
    }

    public void observeGame() {
        out.print("Please Input Game Number to Observe: ");
        String gameNum = scanner.nextLine();
        if (isNotValidInput(gameNum, listGameData.size())) {
            out.println("Please input valid game number in list");
            return;
        }
        ui.ChessBoardGraphics.drawChessBoard(listGameData.get(Integer.parseInt(gameNum)).game(), TeamColor.WHITE);
        // Display board until they press enter
        scanner.nextLine();
        // If you can't observe it, print error message
        // out.println("Failed to observe the game.");
    }
}
