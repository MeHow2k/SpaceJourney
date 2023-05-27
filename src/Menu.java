import javax.swing.*;
import java.awt.*;


public class Menu {
    Image imgtitle = new ImageIcon(getClass().getClassLoader().getResource("tytul.png")).getImage();
    public Menu() {}
    public void draw (Graphics2D g){
        Font title = new Font("arial",Font.BOLD,60);
        g.setFont(title);
        g.setColor(Color.white);

        if(imgtitle !=null) g.drawImage(imgtitle, C.FRAME_WIDTH/2-250,100,500,200, null);

        Font buttons = new Font("arial",Font.BOLD,40);
        g.setFont(buttons);
        g.drawString("Start",C.FRAME_WIDTH/2-100,350);
        g.drawString("Jak grać?",C.FRAME_WIDTH/2-100,450);
        g.drawString("Opcje",C.FRAME_WIDTH/2-100,550);
        g.drawString("O Programie",C.FRAME_WIDTH/2-100,650);
        g.drawString("Wyjdź",C.FRAME_WIDTH/2-100,750);

        g.setFont(new Font("arial",Font.BOLD,15));
        g.drawString("Aby zatwerdzić naciśnij ENTER",C.FRAME_WIDTH-250,C.FRAME_HEIGHT-70);
        if(C.highscorePunkty!=0) g.drawString("Najlepszy wynik: "+C.highscorePunkty+" punktów "+C.highscoreLevel+" lvl",C.FRAME_WIDTH-300,20);
        if(C.highscorePunkty!=0 && C.highscoreLevel==31) g.drawString("Najlepszy wynik: "+C.highscorePunkty+" punktów 30 lvl",C.FRAME_WIDTH-300,20);
        g.drawString("Michał Pasieka 2023",10,C.FRAME_HEIGHT-70);
    }

}