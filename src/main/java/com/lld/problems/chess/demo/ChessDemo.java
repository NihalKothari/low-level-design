package com.lld.problems.chess.demo;

import com.lld.problems.chess.model.Move;
import com.lld.problems.chess.model.PieceColor;
import com.lld.problems.chess.service.ChessGameService;

public class ChessDemo {

    public static void main(String[] args) {
        ChessGameService game = new ChessGameService();

        System.out.println("Starting turn: " + game.getCurrentTurn());

        Move e2e4 = new Move(1, 4, 2, 4);
        game.makeMove(e2e4);
        System.out.println("After e2-e4, turn: " + game.getCurrentTurn());

        Move e7e5 = new Move(6, 4, 5, 4);
        game.makeMove(e7e5);
        System.out.println("After e7-e5, turn: " + game.getCurrentTurn());
        System.out.println("Move count: " + game.getMoveHistory().size());
        System.out.println("White to move: " + (game.getCurrentTurn() == PieceColor.WHITE));
    }
}
