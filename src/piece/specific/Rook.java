package piece.specific;

import main.ChessColor;
import main.Type;
import piece.Piece;

public class Rook extends Piece {

    public Rook(ChessColor color, int col, int row) {
        super(color, col, row);
        type= Type.ROOK;
        if(color == ChessColor.WHITE){
            image = getImage("/piece/w-rook");
        }
        else{
            image = getImage("/piece/b-rook");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //check directions
            if (targetCol == preCol || targetRow == preRow){
                return isValidSquare(targetCol, targetRow) &&
                        !checkCollisionDirectional(targetCol, targetRow);
            }
        }
        return false;
    }

}
