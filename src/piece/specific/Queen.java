package piece.specific;

import main.ChessColor;
import main.Type;
import piece.Piece;

public class Queen extends Piece {
    public Queen(ChessColor color, int col, int row) {
        super(color, col, row);

        type= Type.QUEEN;
        if(color == ChessColor.WHITE){
            image = getImage("/piece/w-queen");
        }
        else{
            image = getImage("/piece/b-queen");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {

            //Diagonal Mov
            if (calcolaDistanza(targetCol, preCol) == calcolaDistanza(targetRow, preRow)) {
                return (isValidSquare(targetCol, targetRow)&&!checkCollisionDiagonally(targetCol, targetRow));
            }

            //Directional Mov
            if (targetCol == preCol || targetRow == preRow){
                return isValidSquare(targetCol, targetRow) &&
                        !checkCollisionDirectional(targetCol, targetRow);
            }

        }
        return false;
    }
}
