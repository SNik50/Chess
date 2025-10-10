package main;

public enum ChessColor {
        WHITE, BLACK;

        public static ChessColor swapColor(ChessColor color){
                if(color.equals(ChessColor.WHITE)){
                        return ChessColor.BLACK;
                }
                else {
                        return ChessColor.WHITE;
                }
        }
}
