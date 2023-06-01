import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Ramka extends JFrame {
    Ramka(){
        super("Space Journey");
        Image icon = new ImageIcon(getClass().getClassLoader().getResource("player2.png")).getImage();
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(C.FRAME_WIDTH,C.FRAME_HEIGHT);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        GamePanel gamePanel=new GamePanel();
        setContentPane(gamePanel);
        gamePanel.setFocusable(true);
        setVisible(true);
    }
    public static void main(String[] args) {
        //wczytanie ustawień z pliku ustawień
        try {
            File config = new File("ustawienia.txt");
            String absolutePath = config.getAbsolutePath();
            Scanner scanner = new Scanner(new FileInputStream(absolutePath),"UTF-8");

            // sprawdzanie czy linia tekstu istnieje
            while(scanner.hasNextLine()){
                //kazda linia pliku odpowiada za inne ustawienie
                //pierwsza za glosnosc muzyki
                C.musicVolume=Integer.parseInt(scanner.nextLine());
                //druga za glosnosc dzwiekow
                C.soundVolume=Integer.parseInt(scanner.nextLine());
                //trzecia calkowite wyciszenie
                C.isMuted=Integer.parseInt(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("nie znaleziono pliku");
        }
        //wczytanie najlepszego wyniku z ustawień
        try {
            File config = new File("highscore.txt");
            String absolutePath = config.getAbsolutePath();
            Scanner scanner = new Scanner(new FileInputStream(absolutePath),"UTF-8");
            // sprawdzanie czy linia tekstu istnieje
            while(scanner.hasNextLine()){
                //ilosc punktow
                C.highscorePunkty=Integer.parseInt(scanner.nextLine());
                //jaki poziom
                C.highscoreLevel=Integer.parseInt(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("nie znaleziono pliku");
        }

        new Ramka();
    }
}

