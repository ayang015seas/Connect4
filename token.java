public class token {
    private String color;
    private int arrayRow, arrayCol;
    private double xPos, yPos;
    
    
    public token(String color, int arrayRow, int arrayCol) {
        this.arrayRow = arrayRow;
        this.arrayCol = arrayCol;
        this.color = color;
        
        this.xPos = 0.07 + arrayCol * 0.143;
        this.yPos = 0.83 - arrayRow * 0.143;
    }
    
    public void draw() {
        if (this.color.equals("RED")) {
            PennDraw.setPenColor(PennDraw.RED);
        }
        else {
            PennDraw.setPenColor(PennDraw.BLUE);
        }
        this.xPos = 0.07 + arrayCol * 0.143;
        this.yPos = 0.83 - arrayRow * 0.143;
        
        PennDraw.filledCircle(xPos, yPos, 0.065);
    }
    
    public String getColor() {
        return color;
    }
    
}
