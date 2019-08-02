public class grid {
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLUMNS = 7;
    
    private token[][] array;
    private int tokenNumber;
    private boolean endgame;
    
    // constructor function 
    public grid() {
        this.array = new token[NUM_ROWS][NUM_COLUMNS];
        this.tokenNumber = 0;
        this.endgame = false;
    }
    
    // get the token number
    public int tokenGetter() {
        return tokenNumber;
    }
    
    public void updatePosition(char x) {
        x -= 49;
        if (x > -1 && x < 7) {
            insertToken(x);
            drawGrid();
            int row = topRow(x);
            drawColor();
            displayChooser(row, x);
        }
    }
    
    // implement a playerblocker AI 
    public int playerBlocker() {
        int probableMove = greedyEvaluator();
        insertToken(probableMove);
        // evaluate blue move 
        int opposingMove = greedyEvaluator();
        insertToken(opposingMove); 
        int opposingRow = topRow(opposingMove);
        
        if (winCheck(opposingRow, opposingMove)) {
            removeToken(probableMove);
            removeToken(opposingMove);
            return opposingMove;
        }
        else {
            removeToken(probableMove);
            removeToken(opposingMove);
            return probableMove;
        }
    }
    
    public int greedyEvaluator() {
        int[] boardEvaluator = new int[NUM_COLUMNS];
        int[] centerEvaluator = new int[NUM_COLUMNS];
        int[] centerScore = {1, 2, 3, 4, 3, 2, 1};
        int max = 0;
        int numMaxes = 0;
        
        for (int i = 0; i < NUM_COLUMNS; i++) {
            if (!columnIsFull(i)) {
                insertToken(i);
                boardEvaluator[i] = columnEvaluator(i);
                removeToken(i);
            }
        }
        
        for (int i = 0; i < NUM_COLUMNS; i++) {
            if (boardEvaluator[i] > max) {
                max = boardEvaluator[i];
            }
        }
        
        for (int i = 0; i < NUM_COLUMNS; i++) {
            if (boardEvaluator[i] == max) {
                numMaxes++;
            }
        }
        
        if (numMaxes > 1) {
            for (int i = 0; i < NUM_COLUMNS; i++) {
                if (boardEvaluator[i] == max) {
                    centerEvaluator[i] = boardEvaluator[i];
                    centerEvaluator[i] += centerScore[i];
                }
                else {
                    centerEvaluator[i] = 0;
                }
            }
            int centerMax = arrayMax(centerEvaluator);
            return arrayMaxIndex(centerEvaluator, centerMax);
        }
        else {
            return arrayMaxIndex(boardEvaluator, max);
        }
    }
    
    private int arrayMax(int[] array) {
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    private int arrayMaxIndex(int[] array, int max) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == max) {
                return i;
            }
        }
        return 0;
    }
    
    private int columnEvaluator(int col) {
        int topRow = topRow(col);
        int[] possibleDirections = new int[4];
        possibleDirections[0] = checkColumn(topRow, col);
        possibleDirections[1] = checkRow(topRow, col);
        possibleDirections[2] = diagonal1(topRow, col);
        possibleDirections[3] = diagonal2(topRow, col);
        int max = 0;
        for (int i = 0; i < 4; i++) {
            if (possibleDirections[i] > max) {
                max = possibleDirections[i];
            }
        }
        return max;
    }
    
    private void removeToken(int col) {
        for (int i = 0; i < NUM_ROWS; i++) {
            if (array[i][col] != null) {
                array[i][col] = null;
                tokenNumber -= 1;
                break;
            }
        }
    }
    
    public void drawColor() {
        if (tokenNumber % 2 == 0) {
            PennDraw.setPenColor(PennDraw.RED);
        }
        else {
            PennDraw.setPenColor(PennDraw.BLUE);
        }
        PennDraw.filledSquare(0.95, 0.95, 0.05);
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.text(0.84, 0.95, "TURN: ");
    }
    
    public void drawColumns() {
        for (int i = 1; i <= NUM_COLUMNS; i++) {
            PennDraw.text((double) i / 7 - 0.06, 0.02, i + "");
        }
    }
    
    private int topRow(int col) {
        int row = 5;
        for (int i = 0; i < NUM_ROWS; i++) {
            if (array[i][col] != null) {
                return i;
            }
        }
        return row;
    }
    
    // draw grid
    private void drawGrid() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                if (array[i][j] != null) {
                    array[i][j].draw();
                }
            }
        }
    }
    
    
    // display chooser 
    private void displayChooser(int row, int col) {
        if (winCheck(row, col)) {
            String color = array[row][col].getColor();
            this.endgame = true;
            if (color.equals("RED")) {
                displayRed();
            }
            else {
                displayBlue();
            }
        }
        else if (tokenNumber == NUM_ROWS * NUM_COLUMNS) {
            displayDraw();
            this.endgame = true;
        }
        else {
            return;
        }
    }
    
    // display win/drawstates
    private void displayRed() {
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.setFontSize(50);
        PennDraw.text(0.5, 0.5, "Red Wins!");
    }
    
    private void displayBlue() {
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.setFontSize(50);
        PennDraw.text(0.5, 0.5, "Blue Wins!");
    }
    
    private void displayDraw() {
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.setFontSize(50);
        PennDraw.text(0.5, 0.5, "Draw");
    }
    
    private void insertToken(int col) {
        int firstOpenRow = 0;
        
        // find open space
        if (columnIsFull(col)) {
            return;
        }
        else {
            for (int i = 0; i < NUM_ROWS; i++) {
                if (array[i][col] == null) {
                    firstOpenRow = i;
                }
            }
        }
        
        // find turn team 
        if (tokenNumber % 2 == 0) {
            array[firstOpenRow][col] = new token("RED", firstOpenRow, col);
        }
        else {
            array[firstOpenRow][col] = new token("BLUE", firstOpenRow, col);
        }
        tokenNumber += 1;
    }
    
    private void incrementTokens() {
        this.tokenNumber++;
    }
    
    private int checkColumn(int row, int col) {
        int columnCounter = 1;
        String color = array[row][col].getColor();
        
        // check down
        for (int i = row; i < NUM_ROWS - 1; i++) {
            if (array[i + 1][col] == null) {
                break;
            }
            else if (array[i + 1][col].getColor().equals(color)) {
                columnCounter++;
            }
            else {
                break;
            }
        }
        
        for (int i = row; i > 0; i--) {
            if (array[i - 1][col] == null || !(array[i - 1][col].getColor().equals(color))) {
                break;
            }
            else if (array[i - 1][col].getColor().equals(color)) {
                columnCounter++;
            }
            else {
                break;
            }
        }
        return columnCounter;
    }
    
    private int checkRow(int row, int col) {
        int rowCounter = 1;
        String color = array[row][col].getColor();
        
        for (int i = col; i < NUM_COLUMNS - 1; i++) {
            if (array[row][i + 1] == null) {
                break;
            }
            else if (array[row][i + 1].getColor().equals(color)) {
                rowCounter++;
                // System.out.println(i);
            }
            else {
                break;
            }
        }
        
        for (int i = col; i > 0; i--) {
            if (array[row][i - 1] == null) {
                break;
            }
            else if (array[row][i - 1].getColor().equals(color)) {
                rowCounter++;
            }
            else {
                break;
            }
        }
        return rowCounter;
    }
    
    // check the diagonal rows 
    private int diagonal1(int row, int col) {
        int diagonalCounter = 0;
        String color = array[row][col].getColor();
        
        int row1 = row;
        int col1 = col;
        
        while (row < NUM_ROWS && col < NUM_COLUMNS && array[row][col] != null &&
               array[row][col].getColor().equals(color)) {
            row++;
            col++;
            diagonalCounter++;
        }
        
        while (row1 > - 1 && col1 > - 1 && array[row1][col1] != null &&
               array[row1][col1].getColor().equals(color)) {
            row1--;
            col1--;
            diagonalCounter++;
        }
        
        return diagonalCounter - 1;
    }
    
    // check the diagonal other direction
    private int diagonal2(int row, int col) {
        int diagonalCounter = 1;
        String color = array[row][col].getColor();
        
        int row1 = row;
        int col1 = col;
        
        while (row < NUM_ROWS - 1 && col > 0 && array[row + 1][col - 1] != null &&
               array[row + 1][col - 1].getColor().equals(color)) {
            row++;
            col--;
            diagonalCounter++;
        }
        
        while (row1 > 0 && col1 < NUM_COLUMNS - 1 && 
               array[row1 - 1][col1 + 1] != null &&
               array[row1 - 1][col1 + 1].getColor().equals(color)) {
            row1--;
            col1++;
            diagonalCounter++;
        }
        return diagonalCounter;
    }
    
    // incorporate all winstate functions
    private boolean winCheck(int row, int col) {
        if (checkRow(row, col) >= 4 || checkColumn(row, col) >= 4 || 
            diagonal1(row, col) >= 4 || diagonal2(row, col) >= 4) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean columnIsFull(int col) {
        int tokenNumber = 0;
        for (int i = 0; i < 6; i++) {
            if (array[i][col] != null) {
                tokenNumber++;
            }
        }
        return tokenNumber == 6;
    }
    
    public String toString() {
        String out = "[";
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 7; column++) {
                if (array[row][column] != null) {
                    out += array[row][column].getColor() + " , ";
                }
                else {
                    out += "null, ";
                }
            }
            out += '\n';
        }
        out += "]";
        return out;
    }
    
    public boolean gameEnd() {
        return this.endgame;
    }
    
    public static void main(String[] args) {
         grid test = new grid();
         
         /* test.insertToken(3);
         test.insertToken(4);
         test.insertToken(3);
         test.insertToken(4);
         test.insertToken(3);
         test.insertToken(3);
         test.insertToken(3);
         
         test.insertToken(5);
         test.insertToken(4);
         test.insertToken(5);
         test.insertToken(5);
         test.insertToken(6);
         test.insertToken(2);
         */
         // test.removeToken(2);
         
         int move = test.greedyEvaluator();
         System.out.println(move);
         
         // System.out.println(test.diagonal2(4, 3));
         // System.out.println(test.checkColumn(5, 4));
         
         
         System.out.println(test.toString());
    }
}