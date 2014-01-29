import java.util.Random;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.*;


public class MineTest {
    /*0 Represents nothing there, 9 represents mine
     add extra method to initiate sweeping*/
    
    static int rowY = 10;
    static int colX = 10;
    int mineNumbers = 10;
    boolean gameContinue = true;
    
    int[][] integral = new int[rowY][colX];
    char[][] cover = new char[rowY][colX];
    
    public MineTest(){
        
        for(int y = 0; y < cover.length; y++){
            for(int x = 0; x<cover[0].length; x++){
                cover[y][x] = '*';
            }
        }
        createMines(mineNumbers);
        drawStuff(integral);
        System.out.println();
        drawStuff(cover);
        playGame();
        
    }
    
    public void playGame(){
        while(gameContinue){
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter x coordinate: ");
            int x = in.nextInt();
            System.out.println("Please enter y coordinate: ");
            int y = in.nextInt();
            sweepStart(x-1, y-1);
            if(!checkWin()){
                gameContinue = checkWin();
                System.out.print("GG");
            }
        }
        
    }

    public static void main(String[] args) {
        MineTest m = new MineTest();
    }
    private boolean checkWin(){
        int counter = 0;
        for(char[] chars: cover){
            for(char c: chars){
                if(c == '*'){
                    counter++;
                }
            }
        }
        if(counter == mineNumbers){
            return false;
        }else{
            return true;
        }
    }
    
    private void createMines(int count){
        for(int x = 0; x < count; x++){
            Random r = new Random();
            int wRow = r.nextInt(rowY);
            int wCol = r.nextInt(colX);
            System.out.println(wRow + " , " + wCol);
            if(integral[wRow][wCol] == 9){ //to check for repeats
                x--;
            }
            integral[wRow][wCol] = 9;
        } //set mines
        for(int y = 0; y < integral.length; y++){
            for(int x = 0; x<integral[0].length; x++){
                if(integral[y][x] == 9){
                }else{
                    integral[y][x] = surroundMines(y, x);
                }
            }
        }//surround mines
        
        
    }
    
    private int surroundMines(int row, int column){
        int counter = 0; //check surroundings to determine what will happen
        int a = row - 1;
        int b = column - 1;
        if(a == -1){
            a = 0;
        }
        if(b == -1){
            b = 0;
        }
        int temp = b;
        for( ; a <= row + 1; a++){ //boundaries
            b = temp;
            for(; b <= column + 1; b++){
                if(a > integral.length - 1 || b > integral[row].length - 1){
                    //skip out of bounds
                }else{
                    if(integral[a][b]== 9){
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
    
    
    public void sweepStart(int x, int y){
        int init = integral[y][x];
        if (init == 9){
            System.out.println("GAME OVER");
            gameContinue = false;
            return;
        }else{
            sweepMines(x , y);
        }
        drawStuff(cover);
    }
    
    private void sweepMines(int x, int y){
        
        if(y < 0 || x < 0 || y > (rowY - 1) || x > (colX - 1)){
            return;
        }
        if(cover[y][x] != '*'){ 
            return;
        }
        
        int init = integral[y][x];
        if(init > 0 && init < 9){
            cover[y][x] = (char)(integral[y][x]+48);
            return;
        }else if(init == 0){
            integral[y][x] = -1;
            cover[y][x] = '_';
        }else if(init == 9){
            return;
        }
        sweepMines(x, y-1);
        sweepMines(x+1, y-1);
        sweepMines(x+1, y);
        sweepMines(x+1, y+1);
        sweepMines(x, y+1);
        sweepMines(x-1, y+1); //goes clockwise
        sweepMines(x-1, y);
        sweepMines(x-1, y-1);
    }
    
    
    public void drawStuff(int[][] arrays){
        int counter = 1, count = 0;
        System.out.print("\t");
        for(int x = 1; x <= arrays.length; x++){
            
            System.out.print(x - 10*count);
            if(x % 10 == 9){
                count++;
            }
        }
        System.out.println();
        for(int[] jack: arrays){
            System.out.print(counter + "\t");
            counter++;
            for(int jacky: jack){
                if(jacky == 9){
                    System.out.print("*");
                }else{
                    System.out.print(jacky);
                }
            }
            System.out.println();
        }
    }
    
    public void drawStuff(char[][] arrays){
        int counter = 1, count = 0;
        System.out.print("\t");
        for(int x = 1; x <= arrays.length; x++){
            
            System.out.print(x - 10*count);
            if(x % 10 == 9){
                count++;
            }
            
        }
        System.out.println();
        for(char[] jack: arrays){
            System.out.print(counter + "\t");
            counter++;
            for(char jacky: jack){
                System.out.print(jacky);
            }
            System.out.println();
        }
    }
}
