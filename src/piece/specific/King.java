package piece.specific;

import main.ChessColor;
import main.GamePanel;
import main.Type;
import piece.Piece;

public class King extends Piece {
    public King(ChessColor color, int col, int row) {
        super(color, col, row);
        type= Type.KING;
        if (color == ChessColor.WHITE) {
            image = getImage("/piece/w-king");
        } else {
            image = getImage("/piece/b-king");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //check directions
            if (calcolaDistanza(targetCol, preCol) + calcolaDistanza(targetRow, preRow) == 1
                    || calcolaDistanza(targetCol, preCol) * calcolaDistanza(targetRow, preRow) == 1) {
                return isValidSquare(targetCol, targetRow);
            }

        }
        //CASTLING
        if (!hasMoved) {
            //Right castling
            if (targetCol == preCol + 2 && targetRow == preRow && !checkCollisionDirectional(targetCol, targetRow)) {
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == preCol + 3 && piece.row == preRow && !hasMoved) {
                        GamePanel.castlingP = piece;
                        return true;
                    }

                }
            }
            //Left castling
            if (targetCol == preCol - 2 && targetRow == preRow && !checkCollisionDirectional(targetCol, targetRow)) {
                Piece[] p = new Piece[2];
                for (Piece piece : GamePanel.simPieces) {
                    if (piece.col == preCol - 3 && piece.row == targetRow) {
                        p[0] = piece;
                    }
                    if (piece.col == preCol - 4 && piece.row == targetRow) {
                        p[1] = piece;
                    }
                    if (p[0] == null&& p[1]!=null && !p[1].hasMoved) {
                        GamePanel.castlingP = p[1];
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
