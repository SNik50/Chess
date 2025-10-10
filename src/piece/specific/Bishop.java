package piece.specific;

import main.ChessColor;
import main.Type;
import piece.Piece;

public class Bishop extends Piece {
    public Bishop(ChessColor color, int col, int row) {
        super(color, col, row);
        type= Type.BISHOP;
        if (color == ChessColor.WHITE) {
            image = getImage("/piece/w-bishop");
        } else {
            image = getImage("/piece/b-bishop");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            if (calcolaDistanza(targetCol, preCol) == calcolaDistanza(targetRow, preRow)) {
                return (isValidSquare(targetCol, targetRow)&&!checkCollisionDiagonally(targetCol, targetRow));
            }
        }
        return false;
    }
}
