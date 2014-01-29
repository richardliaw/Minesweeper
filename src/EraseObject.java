/**Richard Liaw
 * Period 6
 * 2/26/2013
 * 
 * This lab took about 3 hours to complete. 
 * 
 * The most difficult part of this lab was trying to think about
 * how the problem was to be solved. I tried using an extra
 * copy of the double array, similar to how the other problem
 * was solved. However, I realized that a double array was not 
 * necessary.
 * 
 * Also, another difficult portion of the lab was catching the outofbounds
 * error in the tracking method. This was solved by moving 
 * the end case to the beginning of the line, taking
 * order of operations into account. 
 **/

import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.*;


public class EraseObject{
    
    public static void main(String[] args){
        EraseMaze x = new EraseMaze();
        x.doTraceMaze();
    }
    
}
class EraseMaze{
     private final static char BLANK = ' ';
     private static final int MAXROW = 19;
     private static final int MAXCOL = 19;
     private int myMaxRow;
     private int myMaxCol;
     private boolean [][] myMaze;

     public EraseMaze(){
            myMaze = new boolean [MAXROW + 1][MAXCOL + 1];
            myMaxRow = myMaze.length - 1;
            myMaxCol = myMaze[0].length - 1;
            for(boolean[] c: myMaze)
                Arrays.fill(c, false);
            
     }
    
     public void doTraceMaze() {
            loadMaze();
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter a X coordinate: ");
            int columns = in.nextInt() -1 ;
            System.out.println("Please enter a Y coordinate: ");
            int rows = in.nextInt() -1;
            traceMaze(myMaze, rows, columns);
     }
     
     private void loadMaze(){
            int pointX, pointY, count;
            Scanner in;
            try{
                in = new Scanner(new File("digital.txt"));
                count = in.nextInt();
                while(in.hasNextInt()){
                        pointX = in.nextInt();
                        pointY = in.nextInt();
                        myMaze[pointX -1][pointY-1] = true;
                }
            }catch(IOException i){
                System.out.println("Error: " + i.getMessage());
            }
    }

     /**
      * Description of the Method
      *
      * @param maze Description of Parameter
      */
     public void printMaze(boolean[][] maze){
         
         //have to add headers and etc
         
            Scanner console = new Scanner(System.in);
            
            System.out.print("\t");
            for(int x = 1; x<10;x++){
                System.out.print(x);
            }
            System.out.print('0');
            for(int x = 1; x<10;x++){
                System.out.print(x);
            }
            System.out.print('0');
            System.out.println();
            for (int row = 0; row <= myMaxRow; row++){
                System.out.print((row+1) + "\t");
                for (int col = 0; col <= myMaxCol; col++){
                    System.out.print("" + conversion(maze[row][col]));
                }
                System.out.println();
            }
     }
     
     private char conversion(boolean check){
         if(check){
             return '@';
         }else{
             return '-';
         }
     }
     
     public void traceMaze(boolean[][] maze, int row, int col){
            //char[][] mazeCopy = (char[][])maze.clone();
//            
//         boolean[][] mazeCopy = new boolean[maze.length][maze[0].length];
//         for (int r = 0; r < mazeCopy.length; r++){
//             for (int c = 0; c < mazeCopy[0].length; c++){
//                 mazeCopy[r][c] = maze[r][c];
//             }
//         }
         //if the box is surrounded by exclamations, then change that box
         //in the real array to exclamation
         
         //
         
         if (row > 0 && row <= myMaxRow && col > 0 && col <= myMaxCol){
             // boundary check, if false, a base case
             if (maze[row][col]){
                 // if false, base case
                 maze[row][col] = false;
                 if(row == myMaxRow || col == myMaxCol )
                     
                 if ((row == myMaxRow || !maze[row+1][col] ) && (col == myMaxCol||!maze[row][col+1] )  &&
                         (row == myMaxRow || !maze[row-1][col] ) && (col == myMaxCol||!maze[row][col-1])){
//                     myMaze[row][col] = false;
                     maze[row][col] = false;
                 }
                     
//                     printMaze(maze);
                     traceMaze(maze, row, col + 1);//right
//                     printMaze(maze);
                     traceMaze(maze, row - 1, col); //down
                     
//                     printMaze(maze);
                     traceMaze(maze, row + 1, col); //up
                     
//                     printMaze(maze);
                     traceMaze(maze, row, col - 1); //left
                 
             }else{
                 //do nothing
             }
         }else{
             
            printMaze(myMaze);
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter a X coordinate: ");
            int columns = in.nextInt() -1 ;
            System.out.println("Please enter a Y coordinate: ");
            int rows = in.nextInt() -1;
            traceMaze(myMaze, rows, columns);
         }
     }

}