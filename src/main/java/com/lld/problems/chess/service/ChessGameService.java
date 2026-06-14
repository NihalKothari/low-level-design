package com.lld.problems.chess.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.chess.model.Board;
import com.lld.problems.chess.model.Move;
import com.lld.problems.chess.model.Piece;
import com.lld.problems.chess.model.PieceColor;
import com.lld.problems.chess.model.PieceType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessGameService {

    private final Board board = new Board();
    private final List<Move> moveHistory = new ArrayList<>();
    private PieceColor currentTurn = PieceColor.WHITE;

    public Result<Move> makeMove(Move move) {
        if (!board.isInBounds(move.getFromRow(), move.getFromCol())
                || !board.isInBounds(move.getToRow(), move.getToCol())) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        Piece movingPiece = board.getPiece(move.getFromRow(), move.getFromCol()).orElse(null);
        if (movingPiece == null) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        if (movingPiece.getColor() != currentTurn) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        Piece target = board.getPiece(move.getToRow(), move.getToCol()).orElse(null);
        if (target != null && target.getColor() == movingPiece.getColor()) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        if (!isValidPieceMovement(movingPiece, move)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        if (target != null) {
            move.setCapturedPiece(target);
        }

        board.clearSquare(move.getFromRow(), move.getFromCol());
        board.setPiece(move.getToRow(), move.getToCol(), movingPiece);
        moveHistory.add(move);
        currentTurn = currentTurn.opposite();

        return Result.success(move);
    }

    /**
     * Basic move validation stub — only enforces pawn forward-one-square for now.
     * TODO: implement full piece-specific rules (rook, bishop, knight, king, queen).
     * TODO: add check / checkmate detection.
     */
    boolean isValidPieceMovement(Piece piece, Move move) {
        int rowDelta = move.getToRow() - move.getFromRow();
        int colDelta = Math.abs(move.getToCol() - move.getFromCol());

        if (piece.getType() == PieceType.PAWN) {
            int direction = piece.getColor() == PieceColor.WHITE ? 1 : -1;
            boolean emptyTarget = board.getPiece(move.getToRow(), move.getToCol()).isEmpty();

            if (colDelta == 0 && rowDelta == direction && emptyTarget) {
                return true;
            }
            // TODO: two-square initial pawn move, diagonal capture, en passant, promotion
            return false;
        }

        // TODO: delegate to Strategy per PieceType
        return true;
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    public List<Move> getMoveHistory() {
        return Collections.unmodifiableList(moveHistory);
    }

    public void resetGame() {
        board.reset();
        moveHistory.clear();
        currentTurn = PieceColor.WHITE;
    }
}
