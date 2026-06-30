package main;

import piece.Piece;
import piece.specific.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;

//customizable jpanel (does not extend jframe but jpanel)
public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 640;
    public int FPS = 60;
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();


    //PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>(); //backup list
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    Piece activeP, checkingP;
    public static Piece castlingP;
    ArrayList<Piece> promoPieces = new ArrayList<>();

    //BOOLEANS
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameOver;
    boolean staleMate;

    //COLOR
    ChessColor currentColor = ChessColor.WHITE;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        //MOUSE (detect action)
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        setPieces();
        copyPieces(pieces, simPieces);
    }


    //Thread start
    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces(){
        placePawns();
        placeRooks();
        placeKings();
        placeQueen();
        placeBishops();
        placeKnights();
    }
    private void placePawns(){
        for(int i=0; i<8;i++){
            pieces.add(new Pawn(ChessColor.WHITE, i, 6));
            pieces.add(new Pawn(ChessColor.BLACK, i, 1));
        }
    }
    private void placeRooks(){
        pieces.add(new Rook(ChessColor.WHITE, 0, 7));
        pieces.add(new Rook(ChessColor.WHITE, 7, 7));
        pieces.add(new Rook(ChessColor.BLACK, 0, 0));
        pieces.add(new Rook(ChessColor.BLACK, 7, 0));

    }
    private void placeKnights(){
        pieces.add(new Knight(ChessColor.WHITE, 1, 7));
        pieces.add(new Knight(ChessColor.WHITE, 6, 7));
        pieces.add(new Knight(ChessColor.BLACK, 1, 0));
        pieces.add(new Knight(ChessColor.BLACK, 6, 0));
    }
    private void placeBishops(){
        pieces.add(new Bishop(ChessColor.WHITE, 2, 7));
        pieces.add(new Bishop(ChessColor.WHITE, 5, 7));
        pieces.add(new Bishop(ChessColor.BLACK, 2, 0));
        pieces.add(new Bishop(ChessColor.BLACK, 5, 0));

    }
    private void placeKings(){
        pieces.add(new King(ChessColor.WHITE, 4, 7));
        pieces.add(new King(ChessColor.BLACK, 4, 0));
    }
    private void placeQueen(){
        pieces.add(new Queen(ChessColor.WHITE, 3, 7));
        pieces.add(new Queen(ChessColor.BLACK, 3, 0));
    }
    public void copyPieces( ArrayList<Piece> source, ArrayList<Piece> target){
        target.clear();
        target.addAll(source);
    }

    //changing turns
    private void changePlayer(){
       currentColor = ChessColor.swapColor(currentColor);
       //reset two stepped status
        if(currentColor.equals(ChessColor.BLACK)) {
            for (Piece piece : pieces) {
                if (piece.color.equals(ChessColor.BLACK)) {
                    piece.twoStepped = false;
                }
            }
        }
        else{
            for(Piece piece: pieces){
                if(piece.color.equals(ChessColor.WHITE)){
                    piece.twoStepped = false;
                    }
                }

            }
        activeP = null;
        }
    private void checkCastling(){
        if(castlingP!=null){
            if(castlingP.col ==0){
                castlingP.col +=3;
            }
            if(castlingP.col ==7){
                castlingP.col -=2;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }
    private boolean canPromote(){
        if(activeP.type == Type.PAWN){
            if((currentColor==ChessColor.WHITE && activeP.row==0) ||
                    (currentColor==ChessColor.BLACK && activeP.row ==7)){
                promoPieces.clear();
                promoPieces.add((new Rook(currentColor,9,3)));
                promoPieces.add((new Knight(currentColor,9,2)));
                promoPieces.add((new Bishop(currentColor,9,4)));
                promoPieces.add((new Queen(currentColor,9,5)));
            return true;
            }
        }
       return false;
    }
    private boolean isIllegalKingMove(Piece king){
        if(king.type ==Type.KING){
            for(Piece piece : simPieces){
                if(piece !=king && piece.color!=king.color&&piece.canMove(king.col, king.row)){
                return true;
                }
            }
        }

       return false;
    }
    private boolean isKingInCheck(){
        Piece king = getKing(true);

        if(activeP.canMove(king.col, king.row)){
            checkingP = activeP;
            return true;
        }
        else {
            checkingP = null;
        }

        return false;

    }
    private Piece getKing(boolean opponent){
        Piece king = null;

        for(Piece piece : simPieces){
            if(opponent) { //opponent king
                if (piece.type == Type.KING && piece.color != currentColor) {
                    king = piece;
                }
            }
            else {
                if (piece.type == Type.KING && piece.color == currentColor) {
                    king = piece;
                }

            }
        }
        return king;
    }
    private boolean opponentCanCaptureKing(){
        Piece king = getKing(false);

        for(Piece piece : simPieces){
            if(piece.color != king.color && piece.canMove (king.col, king.row)){
                return true;
            }
        }



        return false;
    }
    private boolean isCheckmate(){
        Piece king = getKing(true);

        //not checkmate
        if (kingCanMove(king)) {
            return false;
        }
        else{
            //check block attack w pieces

            //check pos checking piece and king
            int colDiff = Math.abs(checkingP. col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);

            if(colDiff == 0){
                //attacco verticale
                if(checkingP.row < king.row){
                    //case above
                    for(int row = checkingP.row; row < king.row; row++){
                        for(Piece piece : simPieces){
                            if(piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)){
                                return false;
                            }

                        }
                    }
                }
                if(checkingP.row > king.row){
                    //case: below
                    for(int row = checkingP.row; row > king.row; row--){
                        for(Piece piece : simPieces){
                            if(piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)){
                                return false;
                            }

                        }
                    }
                }

            }
            if(rowDiff ==0) {
                //attacco orizzontale
                if (checkingP.col < king.col) { //to the left

                    for(int col = checkingP.row; col < king.row; col++){
                        for(Piece piece : simPieces){
                            if(piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)){
                                return false;
                            }

                        }
                    }
                }
                if (checkingP.col > king.col) { // to the right

                    for(int col = checkingP.col; col > king.col; col--){
                        for(Piece piece : simPieces){
                            if(piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)){
                                return false;
                            }

                        }
                    }

                }
            }
            if(colDiff == rowDiff){
                //attacco diagonale
                if(checkingP.row < king.row){
                    // P above king
                    if(checkingP.col < king.col){
                        //upper left atk
                        for(int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++){
                            for(Piece piece : simPieces){
                                if( piece != king && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;
                                }
                            }
                        }

                    }
                    if(checkingP.col > king.col){
                        //upper right atk
                        for(int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++){
                            for(Piece piece : simPieces){
                                if( piece != king && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;
                                }
                            }
                        }
                    }

                }
                if(checkingP.row > king.row){
                    // P below king
                    if(checkingP.col < king.col){
                        //lower left atk
                        for(int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--){
                            for(Piece piece : simPieces){
                                if( piece != king && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;
                                }
                            }
                        }

                    }
                    if(checkingP.col > king.col){
                        //lower right atk
                        for(int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--){
                            for(Piece piece : simPieces){
                                if( piece != king && piece.color != currentColor && piece.canMove(col, row)){
                                    return false;
                                }
                            }
                        }
                    }

                }

            }

        }




    return true;
    }
    private boolean kingCanMove(Piece king){
        //simulate if exists square king can move
        if(isValidMove(king, -1, -1)) return true;
        if(isValidMove(king, 0, -1)) return true;
        if(isValidMove(king, 1, -1)) return true;
        if(isValidMove(king, -1, 0)) return true;
        if(isValidMove(king, 1, 0)) return true;
        if(isValidMove(king, -1, 1)) return true;
        if(isValidMove(king, 0, 1)) return true;
        if(isValidMove(king, 1, 1)) return true;

        return false;
    }
    private boolean isValidMove(Piece king, int colPlus, int rowPlus){
        boolean isValidMove = false;
        // update king position temp
        king.col += colPlus;
        king.row += rowPlus;

        //check safe spot
        if(king.canMove(king.col, king.row)){
            if(king.hittingPiece != null){
                simPieces.remove(king.hittingPiece.getIndex());
            }
            if(!isIllegalKingMove(king)){
                isValidMove = true;
            }
        }
        //reset king pos and restore removed
        king.resetPosition();
        copyPieces(pieces, simPieces);

        return false;
    }
    private boolean isStaleMate(){
        int count = 0;
        //count num pieces
        for(Piece piece : simPieces) {
            if(piece.color != currentColor){
                count++;
            }
            //if only king left
            if(count==1){
                if(!kingCanMove(getKing(true))){
                    return true;
                }
            }
        }
        return false;
    }



    //gameLoop
    public void run(){
    double drawInterval = (double) 1000000000 /FPS;
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;

    while(gameThread !=null){
        currentTime = System.nanoTime();
        delta += (currentTime-lastTime)/drawInterval;
        lastTime = currentTime;
        if(delta>=1){
            update();
            repaint();
            delta--;
        }
    }

    }
    //update information
    private void update(){
        //STOP the game for promotion
        if(promotion){
            promoting();

        }
        else if(!gameOver && !staleMate){
            //MOUSE BUTTON PRESSED
            if(mouse.pressed) {
                if(activeP==null){
                    //check if you can pick up a piece
                    for(Piece piece :simPieces){
                        //ally piece? it becomes active p
                        if(piece.color ==currentColor && piece.col == mouse.x/Board.SQUARE_SIZE &&
                                piece.row == mouse.y/Board.SQUARE_SIZE){
                            activeP = piece;
                        }
                    }
                }
                else{
                    //player already holding a piece
                    simulate();
                }

            }
            //MUOSE BUTTON RELEASED
            if(!mouse.pressed){
                if(activeP !=null){
                    if(validSquare) {
                        //MOVE CONFIRMED

                        //update piece list when capture happens, remove ti during simulation
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        if (castlingP != null) {
                            castlingP.updatePosition();
                        }

                        if (isKingInCheck() && isCheckmate()) {
                            gameOver = true;
                        }
                        else if(isStaleMate() && !isKingInCheck()){
                            staleMate = true;
                        }
                        else { // continue game
                            if (canPromote()) {
                                promotion = true;
                            } else {
                                changePlayer();
                            }
                        }
                    }
                    else{
                        //move not valid
                        copyPieces(pieces,simPieces);
                        activeP.resetPosition();
                        activeP = null;
                    }
                }
            }
        }

    }
    private void promoting(){
        if(mouse.pressed){
            for(Piece piece:promoPieces){
                if(piece.col ==mouse.x/Board.SQUARE_SIZE && piece.row == mouse.y/Board.SQUARE_SIZE){
                    switch(piece.type){
                        case ROOK: simPieces.add(new Rook(currentColor, activeP.col, activeP.row)); break;
                        case KNIGHT: simPieces.add(new Knight(currentColor, activeP.col, activeP.row)); break;
                        case QUEEN: simPieces.add(new Queen(currentColor, activeP.col, activeP.row)); break;
                        case BISHOP: simPieces.add(new Bishop(currentColor, activeP.col, activeP.row)); break;
                        default: break;
                    }
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);
                    activeP = null;
                    promotion = false;
                    changePlayer();
                }
            }
        }

    }
    private void simulate(){
        canMove =false;
        validSquare = false;
        //reset piece list every loop
        //restores removed piece during simulation
        copyPieces(pieces, simPieces);

        //reset castling piece position
        if(castlingP !=null){
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }


        //thinking stage
        //if player holding a piece, update its postiion
        activeP.x = mouse.x -Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y-Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);

        //checking if hovering over a valid move square
        if(activeP.canMove(activeP.col, activeP.row)){
            canMove = true;
            //hitting a piece --> remove it
            if(activeP.hittingPiece != null){
                simPieces.remove(activeP.hittingPiece.getIndex());
            }
            checkCastling();
            if(!isIllegalKingMove(activeP) && !opponentCanCaptureKing()){
                validSquare = true;
            }
        }
    }

    //draw obj on the panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = ((Graphics2D) g);

        //BOARD
        board.draw(g2);
        //PIECES
        for (Piece p : simPieces) {
            p.draw(g2);
        }

        if (activeP != null) {
            if (canMove) {
                if (isIllegalKingMove(activeP) || opponentCanCaptureKing()) {
                    g2.setColor(Color.red);

                } else {
                    g2.setColor(Color.white);
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE,
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); //reset the alpha
            }
            activeP.draw(g2);
        }

        //STATUS MESSAGE
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua", Font.PLAIN, 25));
        g2.setColor(Color.white);

        if (promotion) {
            g2.drawString("Promote to:", 650, 75);
            for (Piece piece : promoPieces) {
                g2.drawImage(piece.image, piece.getX(piece.col) - 40, piece.getY(piece.row),
                        Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        } else {
            if (currentColor == ChessColor.WHITE) {
                g2.drawString("White's turn", 650, 450);
                if (checkingP != null && checkingP.color == ChessColor.BLACK) {
                    g2.setColor(Color.red);
                    g2.drawString("The King", 650, 550);
                    g2.drawString("Is in check", 650, 600);
                }
            } else {
                g2.drawString("Black's turn", 650, 150);
                if (checkingP != null && checkingP.color == ChessColor.WHITE) {
                    g2.setColor(Color.red);
                    g2.drawString("The King", 650, 50);
                    g2.drawString("Is in check", 650, 100);
                }
            }
        }
        if(gameOver){
            String s = "";
            if(currentColor == ChessColor.WHITE){
                s = "White wins";
            }
            else{
                s = "Black wins";
            }
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.green);
            g2.drawString(s, 200, 420);
        }
        if(staleMate){
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.lightGray);
            g2.drawString("Stalemate", 200, 420);
        }
    }


    }



