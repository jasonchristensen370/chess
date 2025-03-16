package net;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import exception.ResponseException;
import servicemodel.*;

import java.io.PrintStream;
import java.util.Scanner;

public class ClientCommunicator {

    ServerFacade serverFacade;
    Scanner scanner;
    PrintStream out;
    String authToken;

    public ClientCommunicator() {
        serverFacade = new ServerFacade(8080);
        scanner = new Scanner(System.in);
        out = new PrintStream(System.out);
        authToken = null;
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
            } else if (res.games() == null) {
                out.println("There are no games to list.");
            } else {
                for (var game : res.games()) {
                    out.println(game);
                }
            }
            return true;
        } catch (ResponseException e) {
            out.println(e.statusCode()+": "+e.getMessage());
            return false;
        }
    }

    public boolean playGame() {
        try {
            out.print("Please Input Game Number to Join: ");
            // TODO: Add error handling for bad input
            Integer gameNum = Integer.parseInt(scanner.nextLine());
            out.print("Please Input Color to Play (WHITE/BLACK): ");
            String color = scanner.nextLine();
            var req = new JoinGameRequest(authToken, color, gameNum);
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
