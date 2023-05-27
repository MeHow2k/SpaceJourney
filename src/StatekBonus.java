import javax.swing.*;
import java.awt.*;

public class StatekBonus extends Thread {
    private int x,y,w=200,h=50, kierX =1,kierY =1,velX=1,velY=1,hp,movingType=0,
            angle=270,circleCenterX=C.FRAME_WIDTH/2-w/2,circleCenterY=C.FRAME_HEIGHT/2-100-h/2,radius=300,time=0;
    boolean itemDropped=false;

    JPanel panel;
   Image imgStatekBonus = new ImageIcon(getClass().getClassLoader().getResource("statekBonus.gif")).getImage();

    public StatekBonus(int x, int y, int w, int h, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        hp=999;
        velX=velY=2;
        this.panel=panel;
    }
    public StatekBonus(int x, int y, int w, int h, int velX, int velY, int movingType, JPanel panel){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.velX=velX;
        this.velY=velY;
        this.movingType=movingType;
        hp=999;
        this.panel=panel;

    }
    public void draw(Graphics2D g){
        if(imgStatekBonus !=null)
        g.drawImage(imgStatekBonus , this.getX(),this.getY(),this.getW(),this.getH(), null);
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
    public int getHP() {
        return hp;
    }
    public int getVelX() {
        return velX;
    }
    public int getVelY() {
        return velY;
    }
    public int getRadius() {
        return radius;
    }
    public int getCircleCenterX() {
        return circleCenterX;
    }
    public int getCircleCenterY() {
        return circleCenterY;
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
    public void setVelX(int velX) {
        this.velX = velX;
    }
    public void setVelY(int velY) {
        this.velY = velY;
    }
    public void setHP(int hp) {
        this.hp = hp;
    }
    public void setMovingType(int movingType) {
        this.movingType = movingType;
    }
    public void setCircleCenterY(int circleCenterY) {this.circleCenterY=circleCenterY;}
    public void setCircleCenterX(int circleCenterX) {this.circleCenterX=circleCenterX;}
    public void setRadius(int radius) {this.radius=radius;}
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
            if (C.PAUSE!=true){
                if(movingType==0)//norrmal x+
                    x=x+1* kierX *velX;
                if(movingType==1) {//x+y+ odbijanie od krawedzi
                    x=x+1* kierX *velX;
                    y=y+1* kierY *velY;
                }
                if(movingType==2) {//po torze kola
                     x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                     y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==3) {//lewo prawo gibanie
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==4) {//gora dol gibanie
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==5) {//poruszanie po torze sinusoidalnym po X
                    x=x+1* kierX *velX;
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==6) {//poruszanie po torze sinusoidalnym po Y
                    x = (int) (circleCenterX + radius * Math.cos(Math.toRadians(angle)));
                    y=y+1* kierY *velY;
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==7) {//poruszanie po torze 8 po X
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(2*angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==8) {//poruszanie po torze 8 po Y
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(2*angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==9) {//poruszanie po torze elipsy po X
                    x = (int) (circleCenterX + radius * 2 * Math.cos(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==10) {//poruszanie skosie z lewo gora na prawy dol w granicach
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==11) {//poruszanie skosie z lewo gora na prawy dol
                    x = (int) (circleCenterX + radius * Math.tan(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.tan(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==12) {//poruszanie skosie z prawo gora na lewy dol
                    x = (int) (circleCenterX + -radius * Math.tan(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.tan(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
                if(movingType==13) {// ?
                    x = (int) (circleCenterX + radius * Math.sin(Math.toRadians(angle)));
                    y = (int) (circleCenterY + radius * Math.sin(Math.toRadians(angle)));
                    if(angle==360) angle=0; else angle++;
                }
            }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
