package piece.specific;

import main.ChessColor;
import main.Type;
import piece.Piece;

public class Knight extends Piece {
    public Knight(ChessColor color, int col, int row) {
        super(color, col, row);
        type= Type.KNIGHT;
        if (color == ChessColor.WHITE) {
            image = getImage("/piece/w-knight");
        } else {
            image = getImage("/piece/b-knight");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //check directions ratio 2:1 or 1:2
            if (calcolaDistanza(targetCol, preCol) * calcolaDistanza(targetRow, preRow) == 2) {
                return isValidSquare(targetCol, targetRow);
            }
        }
        return false;
    }
}
