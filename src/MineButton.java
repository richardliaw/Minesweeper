
import javax.swing.JButton;


public class MineButton extends JButton{
    
    public int x, y;
    
    public MineButton(){
        super();
    }
//    public MineButton(int cols, int rows){
//        
//        super(); //forgot why it needed super
//        x = cols;
//        y = rows;
//    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

}