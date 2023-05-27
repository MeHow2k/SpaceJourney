import javax.swing.*;
import java.awt.*;

public class MenuAutor {
    Image imgPANS = new ImageIcon(getClass().getClassLoader().getResource("logoPANS.jpg")).getImage();
    public MenuAutor() {}
    public void draw (Graphics2D g){
        Font title = new Font("arial",Font.BOLD,60);
        g.setFont(title);
        g.setColor(Color.white);
        g.drawString("O Programie",C.FRAME_WIDTH/2-200,50);

        Font txt = new Font("arial",Font.BOLD,18);
        g.setFont(txt);
        g.drawString("Autor projektu: Michał Pasieka",C.FRAME_WIDTH/2-200,100);
        g.drawString("Student II roku Informatyki w PANS w Krośnie",C.FRAME_WIDTH/2-200,130);

        if(imgPANS !=null)
            g.drawImage(imgPANS,C.FRAME_WIDTH/2-260 ,140,256*2,58*2, null);


        g.drawString("Gra została napisana jako projekt zaliczeniowy przedmiotu:",30,330);
        g.drawString("Programowanie w języku Java",30,360);
        g.drawString("Data rozpoczęcia projektu: 1.03.2023",30,390);
        g.drawString("Data zakończenia projektu: 25.05.2023",30,420);
        g.drawString("Grafika została wykonana w programie Paint/GIMP,gify: https://ezgif.com/maker",30,460);
        g.drawString("Dźwięki wykonane za pomocą: www.beepbox.co ,pobrane ze stron (no copyright)",30,490);
        g.drawString("Obróbka dźwięków/muzyki: https://audiomass.co",30,520);
        g.drawString("Muzyka w menu: Ducktales Remastered Soundtrack - Moon Theme",30,570);
        g.drawString("Muzyka w grze: Mega Man 2 - Dr. Wily's Castle Theme",30,600);
        g.drawString("Muzyka przy bossach: Pokémon Omega Ruby/Alpha Sapphire - Deoxys Battle Theme",30,630);
        g.drawString("Dźwięk wygranej: Megaman 7- Victory Theme",30,660);

        g.drawString("Link do projektu:",30,C.FRAME_HEIGHT-150);
        g.drawString("https://github.com/MeHow2k/SpaceJourney",30,C.FRAME_HEIGHT-120);

        g.setFont(new Font("arial",Font.BOLD,15));
        g.drawString("Aby powrócić do menu naciśnij ENTER",C.FRAME_WIDTH-300,C.FRAME_HEIGHT-70);
    }

}