public class connect4 {
    public static void main(String[] args) {
        boolean startScreen = true;
        boolean singlePlayer = true;
        
        // menu screen
        PennDraw.setFontSize(25);
        PennDraw.picture(0.5, 0.5, "startScreen.png", 1200, 600);
        PennDraw.text(0.5, 0.60, "CONNECT 4");
        PennDraw.text(0.5, 0.50, "Press 1 for Singleplayer");
        PennDraw.text(0.5, 0.4, "Press 2 for 2 Players");
        
        while (startScreen) {
            if (PennDraw.hasNextKeyTyped()) {
                char choice = PennDraw.nextKeyTyped();
                int conversion = choice - 48;
                if (conversion == 1) {
                    singlePlayer = true;
                    startScreen = false;
                }
                else if (conversion == 2) {
                    singlePlayer = false;
                    startScreen = false;
                }
                else {
                }
            }
        }
        // clear the board
        PennDraw.clear();
        
        PennDraw.setCanvasSize(600, 600);
        PennDraw.picture(0.5, 0.4, "board.png", 600, 600);
        
        PennDraw.setFontSize(25);
        PennDraw.text(0.5, 0.95, "CONNECT 4");
        
        grid board = new grid();
        board.drawColor();
        board.drawColumns();
        
        
        
        while(true) {
            if (board.tokenGetter() % 2 == 1 && singlePlayer) {
                // int bestOption = board.greedyEvaluator() + 49;
                int bestOption = board.playerBlocker() + 49;
                char move = (char) bestOption;
                board.updatePosition(move);
                System.out.println("CPU: " + move);
                System.out.println(board.tokenGetter());
            }
            
            if (PennDraw.hasNextKeyTyped()) {
                board.updatePosition(PennDraw.nextKeyTyped());
            }
            if (board.gameEnd()) {
                break;
            }
        }
    }
}
