import javax.swing.*;
import java.awt.*;

public class Gracz{
    private int x,y,w=50,h=50;
       Image imgGracz = new ImageIcon(getClass().getClassLoader().getResource("player.gif")).getImage();

    Gracz(int x,int y){
        this.x=x;
        this.y=y;
    }

    public void draw(Graphics2D g){
        if(imgGracz!=null)
            g.drawImage(imgGracz, x,y,w,h, null);
        if(C.tarczaActivated){
            g.setColor(Color.green);
            g.drawOval(x-5,y-5,w+10,h+10);
        }
    }

    public int getY() {
        return y;
    }
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }
    public int getX() {
        return x;
    }

    public void setY(int y) { this.y = y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }
//metoda poruszajaca obiekt o zadana wartosc
    public void moveX(int mx) {
        this.x+=mx;
    }
    public void moveY(int my) {
        this.y+=my;
    }

}
