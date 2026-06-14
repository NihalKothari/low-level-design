package com.lld.problems.chess.model;

import java.util.Optional;

public class Board {

    public static final int SIZE = 8;

    private final Piece[][] squares = new Piece[SIZE][SIZE];

    public Board() {
        initializeStartingPosition();
    }

    public Optional<Piece> getPiece(int row, int col) {
        if (!isInBounds(row, col)) {
            return Optional.empty();
        }
        return Optional.ofNullable(squares[row][col]);
    }

    public void setPiece(int row, int col, Piece piece) {
        if (!isInBounds(row, col)) {
            throw new IndexOutOfBoundsException("Square out of bounds: " + row + "," + col);
        }
        squares[row][col] = piece;
    }

    public void clearSquare(int row, int col) {
        setPiece(row, col, null);
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                squares[row][col] = null;
            }
        }
        initializeStartingPosition();
    }

    private void initializeStartingPosition() {
        placeBackRank(0, PieceColor.WHITE);
        for (int col = 0; col < SIZE; col++) {
            squares[1][col] = new Piece(PieceType.PAWN, PieceColor.WHITE);
            squares[6][col] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        }
        placeBackRank(7, PieceColor.BLACK);
    }

    private void placeBackRank(int row, PieceColor color) {
        squares[row][0] = new Piece(PieceType.ROOK, color);
        squares[row][1] = new Piece(PieceType.KNIGHT, color);
        squares[row][2] = new Piece(PieceType.BISHOP, color);
        squares[row][3] = new Piece(PieceType.QUEEN, color);
        squares[row][4] = new Piece(PieceType.KING, color);
        squares[row][5] = new Piece(PieceType.BISHOP, color);
        squares[row][6] = new Piece(PieceType.KNIGHT, color);
        squares[row][7] = new Piece(PieceType.ROOK, color);
    }
}
