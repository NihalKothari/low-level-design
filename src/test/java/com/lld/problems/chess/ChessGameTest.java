package com.lld.problems.chess;

import com.lld.common.ErrorCode;
import com.lld.problems.chess.model.Move;
import com.lld.problems.chess.model.PieceColor;
import com.lld.problems.chess.model.PieceType;
import com.lld.problems.chess.service.ChessGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChessGameTest {

    private ChessGameService game;

    @BeforeEach
    void setUp() {
        game = new ChessGameService();
    }

    @Test
    void whitePawnCanAdvanceOneSquare() {
        Move move = new Move(1, 4, 2, 4);
        assertTrue(game.makeMove(move).isSuccess());
        assertEquals(PieceColor.BLACK, game.getCurrentTurn());
        assertTrue(game.getBoard().getPiece(2, 4).isPresent());
    }

    @Test
    void rejectMoveOnEmptySquare() {
        Move move = new Move(2, 2, 3, 2);
        assertEquals(ErrorCode.INVALID_INPUT, game.makeMove(move).getError().orElseThrow());
    }

    @Test
    void rejectMoveOutOfTurn() {
        Move blackMove = new Move(6, 4, 5, 4);
        assertEquals(ErrorCode.INVALID_INPUT, game.makeMove(blackMove).getError().orElseThrow());
    }

    @Test
    void knightCanMoveLShape() {
        game.makeMove(new Move(1, 4, 2, 4));
        game.makeMove(new Move(6, 4, 5, 4));
        Move knightMove = new Move(0, 1, 2, 2);
        assertTrue(game.makeMove(knightMove).isSuccess());
        assertEquals(PieceType.KNIGHT, game.getBoard().getPiece(2, 2).orElseThrow().getType());
    }

    @Test
    void resetRestoresInitialPosition() {
        game.makeMove(new Move(1, 4, 2, 4));
        game.resetGame();
        assertEquals(PieceColor.WHITE, game.getCurrentTurn());
        assertTrue(game.getMoveHistory().isEmpty());
        assertTrue(game.getBoard().getPiece(1, 4).isPresent());
    }

    @Test
    void rejectPawnMovingTwoSquaresInStub() {
        Move twoSquares = new Move(1, 4, 3, 4);
        assertEquals(ErrorCode.INVALID_INPUT, game.makeMove(twoSquares).getError().orElseThrow());
    }
}
