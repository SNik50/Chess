package piece;

import main.ChessColor;
import main.GamePanel;
import main.Type;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static main.Board.*;

//superclass pieces
public class Piece {
    public BufferedImage image;
    public int x, y;
    public int col, row;
    public int preCol, preRow;
    public ChessColor color;
    public Piece hittingPiece;
    public boolean hasMoved;
    public boolean twoStepped;
    public Type type;

//constr
    public Piece(ChessColor color, int col, int row){
        this.color = color;
        this.row = row;
        this.col = col;
        x = getX(col);
        y = getY(row);
        preCol = col;
        preRow = row;
    }


    public BufferedImage getImage(String imagePath){
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath+ ".png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public int getX(int col){
        return col * SQUARE_SIZE;
    }
    public int getY(int row){
        return row * SQUARE_SIZE;
    }
    //java image are top left size --> update based on center
    public int getCol(int x){
        return (x + HALF_SQUARE_SIZE)/ SQUARE_SIZE;
    }
    public int getRow(int y){
        return (y + HALF_SQUARE_SIZE)/ SQUARE_SIZE;
    }

    //going to get overridden
    public boolean canMove(int targetCol, int targetRow){
        return false;
    }

    public boolean isWithinBoard(int targetCol, int targetRow){
        return targetRow >= 0 && targetRow <= 7 && targetCol >= 0 && targetCol <= 7;
    }

    public void updatePosition(){
        //check EnPassant
        if(type ==Type.PAWN){
            if(calcolaDistanza(row, preRow)==2){
                twoStepped = true;
            }
        }


        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        hasMoved = true;
    }
    public void resetPosition(){
        col =preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }
    public Piece getHittingPiece(int targetCol, int targetRow){
        for(Piece piece : GamePanel.simPieces){
            if(piece.col==targetCol && piece.row ==targetRow && piece !=this){
                return piece;
            }
        }
        return null;
    }

    public boolean isValidSquare(int targetCol, int targetRow){
        hittingPiece = getHittingPiece(targetCol, targetRow);
        if(hittingPiece ==null){ //SPACE IS NOT OCCUPIED
            return true;
        }
        else if(hittingPiece.color !=this.color){ //SPACE OCCUPIED
            return true;
        }
        else{
            hittingPiece=null;
        }
        return false;
    }
    public int getIndex(){
        for(int index=0;index<GamePanel.simPieces.size();index++){
            if(GamePanel.simPieces.get(index)==this) {
                return index;
            }
        }
        return 0;
    }

    public boolean isSameSquare(int targetCol, int targetRow){
        return(targetCol==preCol && targetRow==preRow);
    }


    public boolean checkCollisionDirectional(int targetCol, int targetRow){
        //Moving LEFT
        for(int c = preCol-1; c>targetCol;c--){
            for(Piece piece:GamePanel.simPieces){
                if(piece.col ==c && piece.row == targetRow){
                    hittingPiece = piece;
                    return true;
                }
            }
        }
        //Moving RIGHT
        for(int c = preCol+1; c<targetCol;c++){
            for(Piece piece:GamePanel.simPieces){
                if(piece.col ==c && piece.row == targetRow){
                    hittingPiece = piece;
                    return true;
                }
            }
        }
        //Moving UP
        for(int r = preRow-1; r>targetRow;r--){
            for(Piece piece:GamePanel.simPieces){
                if(piece.col == targetCol && piece.row == r){
                    hittingPiece = piece;
                    return true;
                }
            }
        }
        //Moving DOWN
        for(int r = preRow+1; r<targetRow;r++){
            for(Piece piece:GamePanel.simPieces){
                if(piece.col == targetCol && piece.row == r){
                    hittingPiece = piece;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean checkCollisionDiagonally(int targetCol, int targetRow){
        //Moving UP
        if(targetRow<preRow){
            //UP-LEFT
            for(int c=preCol-1;c>targetCol;c--){
                int diff = calcolaDistanza(c, preCol);
                for(Piece piece: GamePanel.simPieces){
                    if (piece.col == c && piece.row== preRow-diff){
                        hittingPiece = piece;
                        return true;
                    }
                }
            }
            //UP-RIGHT
            for(int c=preCol+1;c<targetCol;c++){
                int diff = calcolaDistanza(c, preCol);
                for(Piece piece: GamePanel.simPieces){
                    if (piece.col == c && piece.row== preRow-diff){
                        hittingPiece = piece;
                        return true;
                    }
                }
            }
        }
        //Moving DOWN
        if(targetRow>preRow) {
            //DOWN-LEFT
            for(int c=preCol-1;c>targetCol;c--){
                int diff = calcolaDistanza(c, preCol);
                for(Piece piece: GamePanel.simPieces){
                    if (piece.col == c && piece.row==preRow+diff){
                        hittingPiece = piece;
                        return true;
                    }
                }
            }
            //DOWN-RIGHT
            for(int c=preCol+1;c<targetCol;c++){
                int diff = calcolaDistanza(c, preCol);
                for(Piece piece: GamePanel.simPieces){
                    if (piece.col == c && piece.row==preRow+diff){
                        hittingPiece = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public void draw(Graphics2D g2){
        g2.drawImage(image, x, y, SQUARE_SIZE, SQUARE_SIZE, null);
    }
    public int calcolaDistanza(int x, int y){
        return Math.abs(x-y);
    }

}
