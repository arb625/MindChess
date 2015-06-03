package Game;

public class MindChess {
    public static void main(String[] args) {
        Board b = new Board(false);
        StdDrawPlus.setXscale(0, 8);
        StdDrawPlus.setYscale(0, 8);
        while (true) {
            b.drawBoard();
            if (StdDrawPlus.mousePressed()) {
                int tempxcoord = (int) StdDrawPlus.mouseX();
                int tempycoord = (int) StdDrawPlus.mouseY();
                if (b.canSelect(tempxcoord, tempycoord)) {
                    b.select(tempxcoord, tempycoord);
                    StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
                    StdDrawPlus.filledSquare(tempxcoord + .5, tempycoord + .5,
                            .5);
                }
            }
            if (StdDrawPlus.isSpacePressed()) {
                if (b.canEndTurn()) {
                    b.endTurn();
                }
            }
            StdDrawPlus.show(100);
        }
    }

}
