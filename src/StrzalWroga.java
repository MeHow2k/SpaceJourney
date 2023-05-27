import javax.swing.*;
import java.awt.*;

public class StrzalWroga extends Thread {
    private int x,y,w=10,h=10,movingType=0;
    JPanel panel;
    Image imgStrzal = new ImageIcon(getClass().getClassLoader().getResource("strzalWrog.gif")).getImage();
    public StrzalWroga(int x, int y, JPanel panel){
        this.x=x;
        this.y=y;
        this.panel=panel;
    }
    public StrzalWroga(int x, int y,int w,int h, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.panel=panel;
    }
    public StrzalWroga(int x, int y,int w,int h,int m, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.movingType=m;
        this.panel=panel;
    }
    public void draw(Graphics2D g){
        if(imgStrzal!=null)
            g.drawImage(imgStrzal, this.getX(),this.getY(),this.getW(),this.getH(), null);
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
    public int getMovingType() {
        return movingType;
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
    public void setMovingType(int m) {
        this.movingType = m;
    }
//metody zmieniajace polozenie o zadana wartosc
    public void moveX(int move_x) {
        this.x+=move_x;
    }
    public void moveY(int move_y) {
        this.x+=move_y;
    }

    //samoczynny ruch w dól movinType 0
    //w prawo i dół =1
    //w lewo i dół=2
    @Override
    public void run() {
        while (true){
            if(y>C.FRAME_HEIGHT || y<-40) break;
                if (C.PAUSE!=true && movingType==0)//w dol
                 y=y+2;
            else if (C.PAUSE!=true && movingType==1){//prawo dol
                y=y+2;
                x=x+2;
            }else if (C.PAUSE!=true && movingType==2){//lewo dol
                    y=y+2;
                    x=x-2;
                }else if (C.PAUSE!=true && movingType==3){//lewo gora
                    y=y-2;
                    x=x-2;
                }else if (C.PAUSE!=true && movingType==4){//prawo gora
                    y=y-2;
                    x=x+2;
                }else if (C.PAUSE!=true && movingType==5){//prawo
                    x=x+2;
                }else if (C.PAUSE!=true && movingType==6){//lewo
                    x=x-2;
                }else if (C.PAUSE!=true && movingType==7){//gora
                    y=y-2;
                }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
