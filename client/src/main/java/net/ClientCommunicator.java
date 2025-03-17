package net;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import exception.ResponseException;
import model.GameData;
import servicemodel.*;
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
    HashMap<Integer, Integer> listGameIDs;

    public ClientCommunicator() {
        serverFacade = new ServerFacade(8080);
        scanner = new Scanner(System.in);
        out = new PrintStream(System.out);
        authToken = null;
        listGameIDs = new HashMap<>();
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
            out.println(e.statusCode()+": "+e.getMessage());
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
            out.println(e.statusCode()+": "+e.getMessage());
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
            out.println(e.statusCode()+": "+e.getMessage());
            return false;
        }
    }

    public boolean createGame() {
        try {
            out.print("Please Input Game Name: ");
            String gameName = scanner.nextLine();
            CreateGameRequest req = new CreateGameRequest(authToken, gameName);
            CreateGameResult res = serverFacade.createGame(req);
            return res.message() == null;
        } catch (ResponseException e) {
            out.println(e.statusCode()+": "+e.getMessage());
            return false;
        }
    }

    public boolean listGames() {
        try {
            var req = new ListRequest(authToken);
            var res = serverFacade.listGames(req);
            if (res.message() != null) {
                return false;
            } else if (res.games() == null || res.games().isEmpty()) {
                out.println("\nThere are no games to list.");
            } else {
                printGameList(res.games());
            }
            return true;
        } catch (ResponseException e) {
            out.println(e.statusCode()+": "+e.getMessage());
            return false;
        }
    }

    private void printGameList(ArrayList<GameData> games) {
        int counter = 1;
        for (var game : games) {
            listGameIDs.put(counter, game.gameID());
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

    public boolean playGame() {
        try {
            out.print("Please Input Game Number to Join: ");
            // TODO: Add error handling for bad input
            Integer gameNum = Integer.parseInt(scanner.nextLine());
            out.print("Please Input Color to Play (WHITE/BLACK): ");
            String color = scanner.nextLine();
            int gameID = listGameIDs.get(gameNum);
            var req = new JoinGameRequest(authToken, color, gameID);
            var res = serverFacade.joinGame(req);
            TeamColor teamColor = color.equalsIgnoreCase("WHITE") ? TeamColor.WHITE : TeamColor.BLACK;
            ui.ChessBoardGraphics.drawChessBoard(new ChessGame(), teamColor);
            return res.message() == null;
        } catch (ResponseException e) {
            out.println(e.statusCode()+": "+e.getMessage());
            return false;
        }
    }

    public boolean observeGame() {
        out.print("Please Input Game Number to Observe: ");
        String gameNum = scanner.nextLine();
        ui.ChessBoardGraphics.drawChessBoard(new ChessGame(), TeamColor.WHITE);
        return true;
    }
}
