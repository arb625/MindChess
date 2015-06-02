package MindChess;

public class Board {
    
    Piece[][] pieces;
    private String turn = "white";
    private boolean hasSelected = false;
    private boolean hasMoved = false;
    private boolean inCheck = false;
    private Piece lastSelectedPiece;
    private int lastSSx;
    private int lastSSy;
    
    public void drawBoard() {
        Piece keyPiece;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
                } else {
                    StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
                }
                StdDrawPlus.filledSquare(i + .5, j + .5, .5);
                keyPiece = pieces[i][j];
                if (keyPiece != null && keyPiece.isWhite()) {
                    StdDrawPlus.picture(i + .5, j + .5, "img/White" + keyPiece.getType() + ".png", 1, 1);
                } else if (keyPiece != null && !keyPiece.isWhite()) {
                    StdDrawPlus.picture(i + .5, j + .5, "img/Black" + keyPiece.getType() + ".png", 1, 1);
                }
                
            }
        }      
    }

    public Board(boolean shouldBeEmpty) {
        pieces = new Piece[8][8];
        boolean color;
        if (!shouldBeEmpty) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces.length; j++) {
                    if (j == 0) {
                        color = true;
                    } else {
                        color = false;
                    }
                    if (j == 0 || j == 7) {
                        switch (i) {
                            case 0:
                                pieces[i][j] = new Piece(color, this, i, j, "Rook");
                                break;
                            case 1:
                                pieces[i][j] = new Piece(color, this, i, j, "Knight");
                                break;
                            case 2:
                                pieces[i][j] = new Piece(color, this, i, j, "Bishop");
                                break;
                            case 3:
                                pieces[i][j] = new Piece(color, this, i, j, "Queen");
                                break;
                            case 4:
                                pieces[i][j] = new Piece(color, this, i, j, "King");
                                break;
                            case 5:
                                pieces[i][j] = new Piece(color, this, i, j, "Bishop");
                                break;
                            case 6:
                                pieces[i][j] = new Piece(color, this, i, j, "Knight");
                                break;
                            case 7:
                                pieces[i][j] = new Piece(color, this, i, j, "Rook");
                                break;
                        }
                    }
                    if (j == 1) {
                        pieces[i][j] = new Piece(true, this, i, j, "Pawn"); 
                    }
                    if (j == 6) {
                        pieces[i][j] = new Piece(false, this, i, j, "Pawn"); 
                    }
                }
            }
        }
    }

    public Piece pieceAt(int x, int y) {
        return pieces[x][y];
    }

    public boolean canSelect(int x, int y) {
        if (pieces[x][y] != null) {
            if (inCheck && !pieces[x][y].getType().equals("King")) {
                return false;
            }
            if (turn.equals("white") && !hasSelected && pieces[x][y].isWhite()) {
                return true;
            } else if (turn.equals("black") && !hasSelected && !pieces[x][y].isWhite()) {
                return true;
            } else if (turn.equals("white") && hasSelected && !pieces[x][y].isWhite()) {
                if (validMove(lastSelectedPiece, lastSSx, lastSSy, x, y)) {
                    return true;
                }
            } else if (turn.equals("black") && hasSelected && pieces[x][y].isWhite()) {
                if (validMove(lastSelectedPiece, lastSSx, lastSSy, x, y)) {
                    return true;
                }
            }
        } else {
            if (!hasSelected) {
                return false;
            } else if (hasSelected && !hasMoved) {
                if (validMove(lastSelectedPiece, lastSSx, lastSSy, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validMove(Piece p, int xi, int yi, int xf, int yf) {
        if (p.getType().equals("Pawn")) {
            if (p.isWhite()) {
                if (yf - yi == 1 && Math.abs(xf - xi) == 1 && pieces[xf][yf] != null && !pieces[xf][yf].isWhite()) {
                    return true;
                } else if (yf - yi == 1 && xf - xi == 0 && pieces[xf][yf] == null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (yf - yi == -1 && Math.abs(xf - xi) == 1 && pieces[xf][yf] != null && pieces[xf][yf].isWhite()) {
                    return true;
                } else if (yf - yi == -1 && xf - xi == 0 && pieces[xf][yf] == null) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (p.getType().equals("Rook")) {
            if (p.isWhite()) {
                if ((Math.abs(xf - xi) > 0 ^ Math.abs(yf - yi) > 0) && pieces[xf][yf] == null) {
                    return clearPath(xi, yi, xf, yf);
                } else if ((Math.abs(xf - xi) > 0 ^ Math.abs(yf - yi) > 0) && pieces[xf][yf] != null && !pieces[xf][yf].isWhite()) {
                    return clearPath(xi, yi, xf, yf);
                }
            } else {
                if ((Math.abs(xf - xi) > 0 ^ Math.abs(yf - yi) > 0) && pieces[xf][yf] == null) {
                    return clearPath(xi, yi, xf, yf);
                } else if ((Math.abs(xf - xi) > 0 ^ Math.abs(yf - yi) > 0) && pieces[xf][yf] != null && pieces[xf][yf].isWhite()) {
                    return clearPath(xi, yi, xf, yf);
                }
            }
            return false;
        } else if (p.getType().equals("Bishop")) {
            if (p.isWhite()) {
                if ((xf - xi == 0 ^ yf - yi == 0) || (xf - xi == 0 ^ yf - yi == 0)) {
                    return false;
                } else {
                    if (pieces[xf][yf] == null || (pieces[xf][yf] != null && !pieces[xf][yf].isWhite())) {
                        return clearPath(xi, xf, yi, yf);
                    }
                }
            } else {
                if ((xf - xi == 0 ^ yf - yi == 0) || (xf - xi == 0 ^ yf - yi == 0)) {
                    return false;
                } else {
                    if (pieces[xf][yf] == null || (pieces[xf][yf] != null && pieces[xf][yf].isWhite())) {
                        return clearPath(xi, xf, yi, yf);
                    }
                }
            }
            return false;
        } else if (p.getType().equals("Queen")) {
            if (validMove(new Piece(p.isWhite(), this, p.getX(), p.getY(), "Rook"), xi, yi, xf, yf)) {
                return true;
            } else if (validMove(new Piece(p.isWhite(), this, p.getX(), p.getY(), "Bishop"), xi, yi, xf, yf)) {
                return true;
            }
        } else if (p.getType().equals("King")) {
            if (Math.abs(xf - xi) == 1 || Math.abs(yf - yi) == 1) {
                if (p.isWhite()) {
                    if (pieces[xf][yf] == null || (pieces[xf][yf] != null && !pieces[xf][yf].isWhite())) {
                        return true;
                    }
                } else {
                    if (pieces[xf][yf] == null || (pieces[xf][yf] != null && pieces[xf][yf].isWhite())) {
                        return true;
                    }
                }
            }
            return false;
        } else if (p.getType().equals("Knight")) {
            boolean capturable;
            if (p.isWhite()) {
                capturable = false;
            } else {
                capturable = true;
            }
            if (Math.abs(xf - xi) == 2 && Math.abs(yf - yi) == 1) {
                if (pieces[xf][yf] == null || (pieces[xf][yf] != null && pieces[xf][yf].isWhite() == capturable)) {
                    return true;
                }
            } else if (Math.abs(yf - yi) == 2 && Math.abs(xf - xi) == 1) {
                if (pieces[xf][yf] == null || (pieces[xf][yf] != null && pieces[xf][yf].isWhite() == capturable)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    private boolean clearPath(int xi, int xf, int yi, int yf) {
        if (yi == yf) {
            if (xi == xf) {
                return true;
            } else if (xf > xi) {
                for (int i = 1; i < xf - xi; i++) {
                    if (pieces[xi+i][yi] != null) {
                        return false;
                    }
                }
            } else {
                for (int i = 1; i < xi - xf; i++) {
                    if (pieces[xf+i][yi] != null) {
                        return false;
                    }
                }
            }
        } else if (xi == xf) {
            if (yf > yi) {
                for (int i = 1; i < yf - yi; i++) {
                    if (pieces[xi][yi+i] != null) {
                        return false;
                    }
                }
            } else {
                for (int i = 1; i < yi - yf; i++) {
                    if (pieces[xf][yf+i] != null) {
                        return false;
                    }
                }
            }
        } else {
            if (Math.abs(xf - xi) != Math.abs(yf - yi)) {
                System.out.println("Not moving diagonally.");
                return false;
            }
            int deltax = 1;
            int deltay = 1;
            if (xi < xf) {
                deltax *= -1;
            }
            if (yi < yf) {
                deltay *= -1;
            }
            for (int i = 1; i < Math.abs(xf - xi); i++) {
                if (pieces[xi+deltax*i][yi+deltay*i] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean causeCheck(String turn) {
        return false;
    }
    
    public void select(int x, int y) {
        boolean match;
        if (turn.equals("white")) {
            match = true;
        } else {
            match = false;
        }
        hasSelected = true;
        if (pieces[x][y] == null) {
            hasMoved = true;
        } else if (lastSelectedPiece == null && pieces[x][y] != null) {
            lastSelectedPiece = pieces[x][y];
        } else if (lastSelectedPiece != null && pieces[x][y] != null && pieces[x][y].isWhite() == match) {
            lastSelectedPiece = pieces[x][y];
        } else if (lastSelectedPiece != null && pieces[x][y] != null && pieces[x][y].isWhite() != match) {
            lastSelectedPiece.move(x, y);
            hasMoved = true;
        }
        lastSSx = x;
        lastSSy = y;
    }

    public void place(Piece p, int x, int y) {
        if (p != null) {
            for(int i = 0; i < pieces.length; i++) {
                for(int j = 0; j < pieces.length; j++) {
                    if (pieces[i][j] == p) {
                        pieces[i][j] = null;
                    }
                }
            }
        }
        pieces[x][y] = p;
    }
    

    public Piece remove(int x, int y) {
        Piece tempPiece = pieces[x][y];
        pieces[x][y] = null;
        return tempPiece;
    }

    public boolean canEndTurn() {
        return hasMoved;
    }

    public void endTurn() {
        if (turn.equals("white")) {
            turn = "black";
        } else {
            turn = "white";
        }
        hasSelected = false;
        hasMoved = false;  
        if (lastSelectedPiece != null) {
            lastSelectedPiece = null;
        }
    }

    public String winner() {
        return "random";
    }

}