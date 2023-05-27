import javax.swing.*;
import java.awt.*;

public class Rakieta extends  Thread{
    // po uzyskaniu liczby pkt dostajemy rakiete ktora niszczy wszystkich wrogow na mapie
    private int x,y,w=50,h=50,delay=100,kier;
    private boolean isDetonated=false;
    JPanel panel;
    Image imgRakietaRight = new ImageIcon(getClass().getClassLoader().getResource("rakietaRight.gif")).getImage();
    Image imgRakietaLeft = new ImageIcon(getClass().getClassLoader().getResource("rakietaLeft.gif")).getImage();
    public Rakieta(int x,int y,JPanel panel){
        this.x=x;
        this.y=y;
        kier=0;
        this.panel=panel;
    }
    public Rakieta(int x,int y,int w,int h,JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        kier=0;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        if(imgRakietaLeft!=null && kier==1) g.drawImage(imgRakietaLeft, this.getX(),this.getY(),this.getW(),this.getH(), null);
        else g.drawImage(imgRakietaRight, this.getX(),this.getY(),this.getW(),this.getH(), null);
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
    public boolean getIsDetonated() {return isDetonated;}
    public int getkier() {
        return kier;
    }
    public int getDelay() {
        return delay;
    }
    public void setY(int y) { this.y = y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setW(int w) {
        this.w = w;
    }
    public void setIsDetonated(boolean isDetonated) {
        this.isDetonated = isDetonated;
    }

    public void setH(int h) {
        this.h = h;
    }
    public void setkier(int kier) {
        this.kier = kier;
    }

    //metody zmieniajace polozenie o zadana wartosc
    public void moveX(int move_x) {
        this.x+=move_x;
    }
    public void moveY(int move_y) {
        this.x+=move_y;
    }

    //samoczynny ruch
    @Override
    public void run() {
        while (true){
            if (C.PAUSE!=true && isDetonated==false){
                y=y-2;
            if (kier==1) x=x-1;
            else x=x+1;
            delay--;
            if (isDetonated==true) break;
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
