package MindChess;

public class Piece {
    
    private boolean isWhite;
    private Board bd;
    private int c1;
    private int c2;
    private String type;
    private boolean hasCaptured;

    public Piece(boolean white, Board b, int x, int y, String kind) {
        isWhite = white;
        bd = b;
        c1 = x;
        c2 = y;
        type = kind;
    }

    public boolean isWhite() {
        return isWhite;
    }
    
    public String getType() {
        return type;
    }
    
    public int getX() {
        return c1;
    }
    
    public int getY() {
        return c2;
    }

    public void move(int x, int y) {
        bd.place(this, x, y);
        c1 = x;
        c2 = y;
        bd.winner();
    }

    public boolean hasCaptured() {
        return hasCaptured;
    }

}