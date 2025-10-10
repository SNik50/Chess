package piece.specific;

import main.ChessColor;
import main.GamePanel;
import main.Type;
import piece.Piece;

public class Pawn extends Piece {

    public Pawn(ChessColor color, int col, int row) {
        super(color, col, row);
        type = Type.PAWN;

        if(color == ChessColor.WHITE){
            image = getImage("/piece/w-pawn");
        }
        else{
            image = getImage("/piece/b-pawn");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //color based move defining
            int moveValue;
            if(color == ChessColor.WHITE){
                moveValue = -1;
            } else{
                moveValue = 1;
            }

            //check hitting piece
            hittingPiece = getHittingPiece(targetCol, targetRow);

            //1 square mov
            if(targetCol==preCol && targetRow == (preRow+moveValue) && hittingPiece ==null){
                return true;
            }
            //2 square mov (first move rule)
            if(!hasMoved && targetCol==preCol && targetRow == (preRow + moveValue*2) &&
                    hittingPiece ==null && !checkCollisionDirectional(targetCol, targetRow)){
                return true;
            }

            //piece capture handling (diagonal movement)
            if(calcolaDistanza(targetCol, preCol)==1 && targetRow ==preRow+moveValue
                    && hittingPiece!=null && hittingPiece.color!=color){
                return true;
            }

            //En passant
            if(calcolaDistanza(targetCol, preCol)==1 && targetRow ==preRow+moveValue){
                for(Piece piece: GamePanel.simPieces){
                    if(piece.col == targetCol && piece.row ==preRow && piece.twoStepped){
                        hittingPiece=piece;
                        return true;
                    }
                }
            }

        }
        return false;
    }

}
