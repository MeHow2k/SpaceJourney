import javax.swing.*;
import java.awt.*;

public class MenuJakGrac {
    Image imgPunkt = new ImageIcon(getClass().getClassLoader().getResource("punkt.gif")).getImage();
    Image imgRakieta = new ImageIcon(getClass().getClassLoader().getResource("rakietaRight.gif")).getImage();
    Image imgWrog = new ImageIcon(getClass().getClassLoader().getResource("wrog.gif")).getImage();
    Image imgWrogLaser = new ImageIcon(getClass().getClassLoader().getResource("wrogLaserLeft.gif")).getImage();
    Image imgStrzalWrog = new ImageIcon(getClass().getClassLoader().getResource("strzalWrog.gif")).getImage();
    Image imgMeteor = new ImageIcon(getClass().getClassLoader().getResource("meteor.gif")).getImage();
    Image imgStatekBonus = new ImageIcon(getClass().getClassLoader().getResource("statekBonus.gif")).getImage();
    Image imgZycie = new ImageIcon(getClass().getClassLoader().getResource("zycie.gif")).getImage();
    Image imgBron = new ImageIcon(getClass().getClassLoader().getResource("upgradeBron.png")).getImage();
    Image imgBateria = new ImageIcon(getClass().getClassLoader().getResource("punktBateria.png")).getImage();
    Image imgTarcza = new ImageIcon(getClass().getClassLoader().getResource("punktTarcza.png")).getImage();
    Image imgPANS = new ImageIcon(getClass().getClassLoader().getResource("punktPANS.jpg")).getImage();
    public MenuJakGrac() {}
    public void draw (Graphics2D g){
        Font title = new Font("arial",Font.BOLD,60);
        g.setFont(title);
        g.setColor(Color.white);
        g.drawString("Jak grać?",C.FRAME_WIDTH/2-200,50);

        Font txt = new Font("arial",Font.BOLD,18);
        g.setFont(txt);
        g.drawString("Poruszasz się za pomocą STRZAŁEK. Strzelasz za pomocą SPACJI.",30,100);
        g.drawString("Jeżeli posiadasz rakietę możesz ją wystrzelić za pomocą klawisza \"R\".",30,130);
        if(imgRakieta !=null) g.drawImage(imgRakieta, 650,110,30,30, null);

        g.drawString("Podczas gry musisz niszczyć statki wrogów, unikając ich strzałów.",30,170);
        if(imgWrog !=null) g.drawImage(imgWrog, 630,140,50,50, null);
        if(imgWrogLaser !=null)g.drawImage(imgWrogLaser, 680,140,50,50, null);
        if(imgMeteor !=null)g.drawImage(imgMeteor, 680,200,50,50, null);
        if(imgStrzalWrog !=null)g.drawImage(imgStrzalWrog, 630,200,20,20, null);
        g.drawString("Gra kończy się po utracie wszystkich żyć.",30,210);

        g.drawString("Podczas gry można zebrać rożne bonusy:",180,270);

        g.drawString("Dodatkowe punkty",30,330);
        if(imgPunkt !=null)g.drawImage(imgPunkt, 30,340,30,30, null);

        g.drawString("Dodatkowe życie",230,330);
        if(imgZycie !=null)g.drawImage(imgZycie, 230,340,30,30, null);

        g.drawString("Zwiększenie szybkostrzelności",400,330);
        if(imgBateria !=null)g.drawImage(imgBateria, 400,340,30,30, null);

        g.drawString("Ulepszenie broni",30,430);
        if(imgBron !=null)g.drawImage(imgBron, 30,440,30,30, null);

        g.drawString("Tarcza",230,430);
        if(imgTarcza !=null)g.drawImage(imgTarcza, 230,440,30,30, null);

        g.drawString("Wezwanie sojuszniczego statku",400,430);
        if(imgPANS !=null)g.drawImage(imgPANS, 410,440,30,30, null);

        g.drawString("Każdy z nich po zebraniu dodaje określoną liczbę punktów.",30,540);
        g.drawString("Aby je zdobyć, należy niszczyć wrogów lub spotkać sojuszniczy statek.",30,580);
        if(imgStatekBonus !=null)g.drawImage(imgStatekBonus, 250,600,200,50, null);

        g.drawString("Aby zapauzować/odpauzować naciśnij \"P\".   Aby wyjść z gry naciśnij \"ESC\".",30,700);

        g.setFont(new Font("arial",Font.BOLD,15));
        g.drawString("Aby powrócić do menu naciśnij ENTER",C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);

    }

}