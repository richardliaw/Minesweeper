/*Richard Liaw
 * Period 6
 * 
 * This lab took me about 25 hours; I am only having a small bug 
 * at the replaying of the AutoPlayer. After one game, it will
 * not be able to revert back to its original state, which is rather 
 * frustrating. I have in place a temporary solution of returning.
 * 
 * I should have been able to do this lab a bit faster but
 * it was a pretty interesting lab to undertake. It is currently 3 
 * and I need some rest. However, just by looking at the 
 * amount of imports, I was able to further develop some mastery over the language while
 * spurring countless questions. 
 * 
 * I am looking forward to Android Programming.
 */


import java.awt.*;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.lang.Object.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;
import java.awt.image.*;

/* 1. Add the capability to capture mouse clicks using MouseListener
 * 2. Add the capability to capture mouse drags using MouseMotionListener
 */

public class MinesweeperAutoPlay_RichardLiaw_Period6 {

	public static void main(String[] args) throws IOException {    

		MyGUI gui = new MyGUI();
	}
}
class MyGUI implements ActionListener{

	// Attributes
        MineButtons[][] buttonArray;
        JPanel MyDrawingPanel;
        JRadioButton radioButton4;
        JFileChooser choosing = new JFileChooser();
        JMenuItem newGame;
        JMenuItem exits;
        JMenuItem mineCounter;
        JMenuItem autoPlay;
        JMenuItem abouts;
        JMenuItem helper;
        int columns = 20;
        int rows = 20;
        int[][] integral = new int[rows][columns];
        int mineCount, mineCounted, timeCount = 0;
        JLabel countingTime, countingMines;
        JFrame window;
        listenMouse listen = new listenMouse();
        ImageIcon flag, mine;
        Timer time;
        boolean gameOver = false;
        
        int stage;
        
	MyGUI() throws IOException {
                mineCount = 20;
                mineCounted = mineCount;
                
		// Create Java Window
		window= new JFrame("MineSweeper");
                
		window.setBounds(100, 100, 445, 600);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create GUI elements
                
                //menu bar and its items
                JMenuBar bared = new JMenuBar();
                
                JMenu games = new JMenu("Game");
                newGame = new JMenuItem("New Game");
                newGame.addActionListener(this);
                exits = new JMenuItem("Exit");
                exits.addActionListener(this);
                
                JMenu options = new JMenu("Options");
                mineCounter = new JMenuItem("Total Mines");
                mineCounter.addActionListener(this);
                
                
                JMenu helps = new JMenu("Help");
                helper = new JMenuItem("Help");
                helper.addActionListener(this);
                abouts = new JMenuItem("About");
                abouts.addActionListener(this);
//                JMenuItem colorchoose = new JMenuItem("");
                
                games.add(newGame);
                games.add(exits);
                
                options.add(mineCounter);
                
                helps.add(abouts);
                helps.add(helper);
                bared.add(games);
                bared.add(options);
                bared.add(helps);
                
                //buffered image with Image IO read
                
                time = new Timer(1000, this);
                
		// JPanel to draw in
		MyDrawingPanel = new JPanel();
                int widDraw = window.getWidth() - 45;
                int heiDraw = window.getHeight() - 200;
		MyDrawingPanel.setBounds(20, 20, widDraw, heiDraw);
		MyDrawingPanel.setBorder(BorderFactory.createEtchedBorder());
                MyDrawingPanel.setLayout(null);
                
                
                File flags = new File("red_flag.png");
                BufferedImage image = ImageIO.read(flags);
                Image resized = image.getScaledInstance((MyDrawingPanel.getWidth() / columns), (MyDrawingPanel.getHeight())/rows, Image.SCALE_DEFAULT);
                
                File miness = new File("minesweepericon.png");
                BufferedImage image2 = ImageIO.read(miness);
                Image resized2 = image2.getScaledInstance((MyDrawingPanel.getWidth() / columns), (MyDrawingPanel.getHeight())/rows, Image.SCALE_DEFAULT);
                
                
                flag = new ImageIcon(resized);
                mine = new ImageIcon(resized2);
		// Add GUI elements to the Java window's ContentPane
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);

		JPanel timePanel = new JPanel();
                
		window.setVisible(true);
                
		timePanel.setBorder(BorderFactory.createTitledBorder("Time"));
		timePanel.setBounds(120, 425, 100, 70);
                countingTime = new JLabel(""+timeCount);
                timePanel.add(countingTime);
                
                
		JPanel otherPanel = new JPanel();
		otherPanel.setBorder(BorderFactory.createTitledBorder("Mines"));
		otherPanel.setBounds(220, 425, 100, 70);
                countingMines = new JLabel(""+mineCounted);
                otherPanel.add(countingMines);
                
		JButton button = new JButton("AutoPlayer");
		button.setBounds(window.getWidth()/2 - 75, window.getHeight()/2+200, 150, 20);
                button.addActionListener(this);
                
                setUp();
                
                
                
		mainPanel.add(MyDrawingPanel);
		mainPanel.add(otherPanel);
		mainPanel.add(timePanel);
                mainPanel.add(button);
                
                
                window.setJMenuBar(bared);
		window.getContentPane().add(mainPanel);

		// Let there be light
		window.setVisible(true);

	}
        
        
        private void setUp(){
            stage = 1;
            buttonArray = new MineButtons[rows][columns];
            for(int y = 0; y< buttonArray.length; y++){
                int temp = MyDrawingPanel.getHeight() / rows;
                int temp2 = MyDrawingPanel.getWidth() / columns;
                for(int side = 0; side< buttonArray[y].length; side++){
                    MineButtons x = new MineButtons(side, y);
                    x.setBounds((temp2*side), (temp*y), (temp2), temp);
                    x.setMargin(new Insets(0,0,0,0)); //TOOK A LONNNG TIME TO FIND OUT HOW TO WORK THIS
                    x.setIcon(null);
                    x.setAs(false);
                    MyDrawingPanel.add(x);
                    buttonArray[y][side] = x;
                    buttonArray[y][side].addActionListener(this);
                    buttonArray[y][side].addMouseListener(listen);
                }
            }
            createMines(mineCount);
            drawStuff(integral);
        }
        
        
        private void createMines(int count){
            stage = 1;
            countingMines.setText(""+count);
            timeCount = 0;
            integral = new int[rows][columns];
            for(int x = 0; x < count; x++){
                Random r = new Random();
                int wRow = r.nextInt(rows);
                int wCol = r.nextInt(columns);
                System.out.println(wRow + " , " + wCol);
                if(integral[wRow][wCol] == 9){ //to check for repeats
                    x--;
                }
                integral[wRow][wCol] = 9;
            } //set mines
            for(int y = 0; y < integral.length; y++){
                for(int x = 0; x<integral[0].length; x++){
                    if(integral[y][x] != 9){
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
        
        private void cleanAndWrite(){
            time.stop();
            stage = 1;
            for(int y = 0; y< buttonArray.length; y++){
                int temp = MyDrawingPanel.getHeight() / rows;
                int temp2 = MyDrawingPanel.getWidth() / columns;
                for(int side = 0; side< buttonArray[y].length; side++){
                    buttonArray[y][side].setBounds((temp2*side), (temp*y), (temp2), temp);
                    buttonArray[y][side].setMargin(new Insets(0,0,0,0)); //TOOK A LONNNG TIME TO FIND OUT HOW TO WORK THIS
                    buttonArray[y][side].setIcon(null);
                    buttonArray[y][side].setAs(false);
                    buttonArray[y][side].setText("");
                    buttonArray[y][side].setSelected(false);
                }
            }
            createMines(mineCount);
            drawStuff(integral);
        }
        
        
	public void actionPerformed(ActionEvent e) {

//		System.out.println("Action -> " + e.getActionCommand());
                if(gameOver == true){
                    gameOver = false;
                    if(e.getSource() instanceof MineButtons){
                        ((MineButtons)e.getSource()).setSelected(true);
                        time.stop();
                        stage = 1;
                    }
                    cleanAndWrite();
                }
                else if (e.getActionCommand() != null) {
                        if(e.getActionCommand().equals("AutoPlayer")){
                            if(stage == 1){
                                System.out.println("Stage 1");
                                stepOnePlayer();
                                System.out.println("Clicking randomly until obtaining"
                                        + " 20% of mine buttons selected");
                                    
                                if(countSelected() > rows*columns / 5){
                                    stage = 2;
                                    System.out.println("Stage 2");
                                }
                                
                            }else if(stage == 2){
                                stepTwoPlayer();
                                System.out.println("Stage 3");
                                if(countSelected() == 0){
                                    stage = 1;
                                    return;
                                }
                                stage = 3;
                            }else{
                                stepThreePlayer();
                                System.out.println("Back to Stage Two");
                                if(countSelected() == 0){
                                    stage = 1;
                                    return;
                                }
                                stage = 2;
                            }
                            
                        }
                        if(e.getActionCommand().equals("About")){
                            JEditorPane aboutContent;
                            try{
                                aboutContent = new JEditorPane(new URL("file:about.html"));
                                JScrollPane helpPane = new JScrollPane(aboutContent);
                                JOptionPane.showMessageDialog(null, helpPane, "About", JOptionPane.PLAIN_MESSAGE, null);
                            }catch(IOException x){
                                System.out.println("Error: " + x.getMessage());
                            }
			}
                        if(e.getActionCommand().equals("Help")){
                            JEditorPane helpContent;
                            try{
                                helpContent = new JEditorPane(new URL("file:howto.html"));
//                                JScrollPane helpPane = new JScrollPane(helpContent);
                                helpContent.setBounds(200,200,400,400);
                                JOptionPane.showMessageDialog(null, helpContent, "How To Play", JOptionPane.PLAIN_MESSAGE, null);
                                
                            }catch(IOException x){
                                System.out.println("Error: " + x.getMessage());
                            }
			}
                        if(e.getActionCommand().equals("Exit")){
                            window.dispose();
			}
                        if(e.getActionCommand().equals("New Game")){
                            cleanAndWrite();
			}
                        if(e.getActionCommand().equals("Total Mines")){
                            JPanel mines = new JPanel();
                            mines.setLayout(null);
                            JLabel area = new JLabel("Please enter number of Mines");
                            mines.add(area);
                            JTextField fields = new JTextField();
                            mines.add(fields);
                            String str = JOptionPane.showInputDialog(null, area, "Mine Count", JOptionPane.QUESTION_MESSAGE); //component?
                            if(!str.equals(null)||!str.equals("")){
                                mineCount = Integer.parseInt(str); //learned this
                            }
                            newGame.doClick();//learned this
			}
                        if(e.getSource() instanceof MineButtons){
                            countingMines.setText(""+mineCounted);

                            if(((MineButtons)e.getSource()).isSelected()){
                                time.start();
                                ((MineButtons)e.getSource()).setSelected(false);
                                int x = ((MineButtons)e.getSource()).getXs();
                                int o = ((MineButtons)e.getSource()).getYs();
                                int temp = integral[o][x];
                                if (temp == 9){
                                    System.out.println("GAME OVER");
                                    time.stop();
                                    gameOver = true;
                                    countingMines.setText("Game Over");
                                    showMines();
                                    return;
                                }
                                sweepMines(x,o);
                                if(gameEND()){
                                    gameOver = true;
                                    JLabel test = new JLabel("Congratulations!");
                                    JOptionPane.showMessageDialog(null, test);
                                    stage = 1;
                                }
                            }else if(!((MineButtons)e.getSource()).isSelected()){
                                ((MineButtons)e.getSource()).setSelected(true);
                            }
                        }
                }else{
                    ++timeCount;
                    countingTime.setText(""+timeCount);
                }
                
	}
        
        
        private void sweepMines(int x, int y){
            
            if(y < 0 || x < 0 || y > (rows - 1) || x > (columns - 1)){
                return;
            }
            if(buttonArray[y][x].isSelected()){  //WILL
                return;
            }

            int init = integral[y][x];
            if(buttonArray[y][x].setFlagger()){
                return;
            }
            if(init > 0 && init < 9){
                buttonArray[y][x].setText(""+integral[y][x]);
                buttonArray[y][x].setSelected(true);
                return;
            }else if(init == 0){
//                integral[y][x] = -1;
                buttonArray[y][x].setSelected(true);
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
            //check GAME
        }
        
        public boolean gameEND(){
            boolean games;
            int amount = 0;
            for(int y = 0; y < rows; y++){
                for(int x = 0; x<columns; x++){
                    if(!buttonArray[y][x].isSelected()){
                        amount++;
                    }
                }
            }
            if(amount==mineCount){
                return true;
            }else{
                return false;
            }
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
        
        //show mines when dead
        private void showMines(){
            for(int y = 0; y < rows; y++){
                for(int side = 0; side < columns; side++){
                    if(integral[y][side] == 9){
                        buttonArray[y][side].setIcon(mine);
                    }
                }
            }
            //show dialog for starting over, showing YOU LOSE
        }
        
        
        //for right click
        private class listenMouse implements MouseListener, MouseMotionListener{
            
            public void mouseClicked(MouseEvent me){
//                System.out.println("clicked");
//                System.out.println(me.getX() + ", " + me.getY());
                painter(me);
            }

            public void mousePressed(MouseEvent me){
//                System.out.println("press");
                painter(me);
            }

            public void mouseReleased(MouseEvent me){
//                System.out.println("release");
            }

            public void mouseEntered(MouseEvent me){
//                System.out.println("entered");
            }

            public void mouseExited(MouseEvent me){ 
//                System.out.println("exited");
            }
            
            public void mouseDragged(MouseEvent me){
//                System.out.println("drag");
                painter(me);
            }

            public void mouseMoved(MouseEvent me){
          //      System.out.println("move");
            }
            
            public void painter(MouseEvent me){

                if((me.getModifiersEx()) == MouseEvent.BUTTON3_DOWN_MASK){
                    if(me.getSource() instanceof MineButtons){
                        if(!((MineButtons)me.getSource()).isSelected()){
                            ((MineButtons)me.getSource()).setSelected(false);
                            if(((MineButtons)me.getSource()).setFlagger()){
                                ((MineButtons)me.getSource()).setIcon(null);
                                ((MineButtons)me.getSource()).setAs(false);
                                mineCounted++;
                                countingMines.setText(""+mineCounted);

                            }else{
                                ((MineButtons)me.getSource()).setIcon(flag);
                                mineCounted--;
                                countingMines.setText(""+mineCounted);

                                ((MineButtons)me.getSource()).setAs(true);
                            }
                        }
                    }
                }
            }
        }
        
        
        //<ARTIFICIAL INTELLIGENCE>
        
        private int countSelected(){
            int count = 0;
            for(int y = 0; y < rows; y++){
                for(int x = 0; x < columns; x++){
                    if(buttonArray[y][x].isSelected()){
                        count++;
                    }
                }
            }
            return count;
        }
        
        
         //will serve as both counter and flagger
         private int checkSurround(int row, int column, boolean clicker){
                
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
                        if(a > buttonArray.length - 1 || b > buttonArray[row].length - 1){
                            //skip out of bounds
                        }else{
                            if(!buttonArray[a][b].isSelected()){
                                counter++;
                                if(clicker){
                                    buttonArray[a][b].setIcon(flag);
                                    buttonArray[a][b].setAs(true);
                                }
                            }
                        }
                    }
                }
                return counter;
        }
         private int countFlags(int row, int column){
                
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
                        if(a > buttonArray.length - 1 || b > buttonArray[row].length - 1){
                            //skip out of bounds
                        }else{
                            if(buttonArray[a][b].setFlagger()){
                                counter++;
                            }
                        }
                    }
                }
                return counter;
        }
         
         private void clickThrough(int row, int column){
                System.out.println("Checking the surroundings and clicking where"
                        + " there are determined to be no mines.");
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
                        if(a > buttonArray.length - 1 || b > buttonArray[row].length - 1){
                            //skip out of bounds
                        }else{
                            if(!buttonArray[a][b].setFlagger()){
                                if(!buttonArray[a][b].isSelected()){
                                    buttonArray[a][b].doClick();
                                }
                            }
                        }
                    }
                }
         }
         
        public void stepOnePlayer(){
            //run through and click randomly
                //if amount of cells selected > 20% of row * columns
                //move onto step two
                //will change among stages
            Random r = new Random();
            int wRow = 0, wCol = 0;
            //stage one

            wRow = r.nextInt(rows);
            wCol = r.nextInt(columns);
            buttonArray[wRow][wCol].doClick();
            System.out.println("Clicking randomly");
        }
        
        public void stepTwoPlayer(){
            //step two
                //Run through and stop at each untoggled button
                    //check if number of surrounding untoggled buttons is equal to number of box
                    //If number is equivalent, color as green, 
                    //mark all with flag
            for(int y = 0; y < rows; y++){
                for(int x = 0; x < columns; x++){
                    int countOf = 0;
                    if(buttonArray[y][x].isSelected()){
                        String str = buttonArray[y][x].getText();
                        if((!str.equals(null))&&(!str.equals(""))){
                            countOf = Integer.parseInt(str);
                            int xs = buttonArray[y][x].getXs();
                            int ys = buttonArray[y][x].getYs();
                            int count = checkSurround(ys, xs, false); //counts amount unselected
                            if(count == countOf){
                                checkSurround(ys, xs, true);//will flag surroundings
                            }  
                        }
                    }
                }
            }
            System.out.println("Checking if there are any places where a mine is determined.");
        }
        
        public void stepThreePlayer(){  
            //step three
                //Run through to see if a box has same number of surrounding flags as its given number
                    //if there are equal amounts,
                    //check with green click all other boxes
                    //loop back to step 2
            for(int y = 0; y < rows; y++){
                for(int x = 0; x < columns; x++){
                    if(buttonArray[y][x].isSelected()){
                        int countOf = 0;
                        String str = buttonArray[y][x].getText();
                        if((!str.equals(null))&&(!str.equals(""))){
                            countOf = Integer.parseInt(str);
                        }
                        int xs = buttonArray[y][x].getXs();
                        int ys = buttonArray[y][x].getYs();
                        if(countFlags(y , x ) == countOf){
                            clickThrough(y, x);
                        }
                    }
                }
            }
            System.out.println("The points have been determined and are beginning to be cleared.");
        }

}


//button for special data
class MineButtons extends JToggleButton{
    
    public int x, y;
    public boolean setFlag = false;
    
    public MineButtons(int xVal , int yVal){
        super();
        x = xVal;
        y = yVal;
    }
    public boolean setFlagger(){
        return setFlag;
    }
    public void setAs(boolean reset){
        setFlag = reset;
    }
    public int getXs(){
        return x;
    }
    public int getYs(){
        return y;
    }
    
    
}
        