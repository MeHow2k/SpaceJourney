import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener {
    JButton buttonMenu, buttonStart, buttonQuit;
    Menu menu;
    MenuJakGrac menujakgrac;
    MenuOpcje menuOpcje;
    JLabel labelPunktacja, labelZycia, labelUpgrade, labelRakiety, labelTEST,
            labelBossHp, labelPauza,labelMonety,labelRekord;
    Gracz gracz;
    MenuCursor menuCursor;
    MenuAutor menuAutor;
    PunktZycie livesUI;
    Strzal strzalUI;
    Rakieta rakietaUI;
    PunktBateria bateriaUI;
    Punkt monetaUI;
    List<Punkt> listPunkt = new ArrayList<>(20);//lista spadajacych punktow
    List<Strzal> listStrzal = new ArrayList<>(20);//lista strzalow
    List<Rakieta> listRakieta = new ArrayList<>(20);//lista strzalow
    List<StrzalWroga> listStrzalWroga = new ArrayList<>(20);//lista strzalow wroga
    List<LaserWroga> listLaserWroga = new ArrayList<>(20);//lista strzalow lasera wroga
    List<StatekBonus> listStatekBonus = new ArrayList<>(0);//lista statku z bounsem
    List<Wrog> listWrog = new ArrayList<>(20);//lista wrogow
    List<WrogLaser> listWrogLaser = new ArrayList<>(20);//lista wrogow z laserem
    List<Wrog> listBoss1 = new ArrayList<>(10);//lista wrogow
    List<Wrog> listWrogMenu = new ArrayList<>(20);//lista wrogów pojawiajacych sie w menu
    List<Meteor> listMeteor = new ArrayList<>(20);//lista meteorów
    List<PunktBron> listPunktBron = new ArrayList<>(10);//lista punktw ulepszen broni
    List<PunktBateria> listPunktBateria = new ArrayList<>(10);//lista punktw przyspieszenie strzelania
    List<PunktZycie> listPunktZycie = new ArrayList<>(10);//lista punktów dodatkowe życie
    List<PunktTarcza> listPunktTarcza = new ArrayList<>(10);//lista punktów tarcza
    List<PunktPANS> listPunktPANS = new ArrayList<>(10);//lista punktów PANS
    int tick = 0, enemyCreated = 0;
    boolean isBoss = false;
    boolean LEFT_PRESSED, RIGHT_PRESSED, SHOT_PRESSED, DOWN_PRESSED, UP_PRESSED, RAKIETA_PRESSED;
    int strzalCooldown = 60, rakietaCooldown = 100, tarczaCooldown = 100;
    boolean strzalisCooldown = false, rakietaisCooldown = false;
    //zmienne pomocnicze
    int tempX = 0, tempY = 0,tempZ = 0, bossHPtotal = 0, menudelay=50000000;
    boolean  playedMusic = false;


    GamePanel() {
        super(null);
        Color color = new Color(132, 142, 220);
        setBackground(color);


        labelPunktacja = new JLabel("");
        labelPunktacja.setBounds(C.FRAME_WIDTH - 120, C.FRAME_HEIGHT - 80, 100, 30);
        labelPunktacja.setForeground(Color.white);
        add(labelPunktacja);
        labelZycia = new JLabel("");
        labelZycia.setBounds(50, C.FRAME_HEIGHT - 80, 100, 30);
        labelZycia.setForeground(Color.white);
        add(labelZycia);
        labelUpgrade = new JLabel("");
        labelUpgrade.setBounds(90, C.FRAME_HEIGHT - 80, 100, 30);
        labelUpgrade.setForeground(Color.white);
        add(labelUpgrade);
        labelRakiety = new JLabel("");
        labelRakiety.setBounds(150, C.FRAME_HEIGHT - 80, 100, 30);
        labelRakiety.setForeground(Color.white);
        add(labelRakiety);
        labelPauza = new JLabel("");
        labelPauza.setBounds(C.FRAME_WIDTH / 2 - 130, C.FRAME_HEIGHT / 2 - 50, 300, 30);
        labelPauza.setForeground(Color.white);
        add(labelPauza);
        labelRekord = new JLabel("");
        labelRekord.setBounds(C.FRAME_WIDTH - 120, C.FRAME_HEIGHT - 110, 100, 30);
        labelRekord.setForeground(Color.white);
        add(labelRekord);
        labelMonety = new JLabel("");
        labelMonety.setBounds(C.FRAME_WIDTH - 200, C.FRAME_HEIGHT - 80, 300, 30);
        labelMonety.setForeground(Color.white);
        add(labelMonety);
        this.addKeyListener(this);

        labelTEST = new JLabel("");
        labelTEST.setBounds(0, 10, 600, 30);
        labelTEST.setForeground(Color.white);
        add(labelTEST);

        labelBossHp = new JLabel("");
        labelBossHp.setBounds(C.FRAME_WIDTH / 2 - 50, 10, 200, 30);
        labelBossHp.setForeground(Color.white);
        add(labelBossHp);

        //utworzenie obiektu gracza oraz elementow interfejsu
        gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
        livesUI = new PunktZycie(20, C.FRAME_HEIGHT - 80, this);
        strzalUI = new Strzal(80, C.FRAME_HEIGHT - 70, this);
        rakietaUI = new Rakieta(120, C.FRAME_HEIGHT - 80, 25, 25, this);
        bateriaUI = new PunktBateria(190, C.FRAME_HEIGHT - 80, 25, 25, this);
        monetaUI = new Punkt(C.FRAME_WIDTH - 225, C.FRAME_HEIGHT - 80, this);
        menuCursor = new MenuCursor(C.FRAME_WIDTH / 2 - - 180, 310, this);
        if(C.LEVEL!=10 ||C.LEVEL!=20 ||C.LEVEL!=30 ||C.LEVEL!=31 )playMenuBackground();
        C.MUSIC_PLAYING = true;

        //watek,usuwanie obiektów,sprawdzanie kolizji gracza i obiektow itd
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

///////////////////////////zmiania pozycji kursora w menu///////////////////////////////////////////////////////////////////////////////////
                    if (C.GAMESTATE == 0) {
                        menuCursor.setX(C.FRAME_WIDTH / 2 - 180);
                        if (menu != null && C.cursorPosition == 0) {
                            menuCursor.setY(310);
                        }
                        if (menu != null && C.cursorPosition == 1) {
                            menuCursor.setY(410);
                        }
                        if (menu != null && C.cursorPosition == 2) {
                            menuCursor.setY(510);
                        }
                        if (menu != null && C.cursorPosition == 3) {
                            menuCursor.setY(610);
                        }
                        if (menu != null && C.cursorPosition == 4) {
                            menuCursor.setY(710);
                        }
 ////////////////////////generowane wrogow w menu i usuwanie gdy wyjda poza okno//////////////////////////////
                        menudelay--;
                        if(menudelay==0){
                            menudelay=30000000;
                            Random r = new Random();
                            if(r.nextInt(5)==0)nowyWrogMenu();
                        }
                        if (listWrogMenu != null) {
                            for (int iw = 0; iw < listWrogMenu.size(); iw++) {
                                Wrog wrog = listWrogMenu.get(iw);
                                if (wrog.getX() > C.FRAME_WIDTH+60) {
                                    listWrogMenu.remove(wrog);
                                }
                            }
                        }
                    }
                    ///////////////////////////zmiania pozycji kursora w menu-opcje///////////////////////////////////////////////////////////////////////////////////
                    if (C.GAMESTATE ==4) {
                        if (menu != null && C.cursorPositionOpcje == 0) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(100);
                        }
                        if (menu != null && C.cursorPositionOpcje == 1) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(250);
                        }
                        if (menu != null && C.cursorPositionOpcje == 2) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(400);
                        }
                        if (menu != null && C.cursorPositionOpcje == 3) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 300);
                            menuCursor.setY(550);
                        }
                        if (menu != null && C.cursorPositionOpcje == 4) {
                            menuCursor.setX(C.FRAME_WIDTH / 2 - 150);
                            menuCursor.setY(700);
                        }

                    }
//////////////////////////////////////obsługa rozgrywki//////////////////////////////////////////////////////////////////////
                    if (C.GAMESTATE == 1) {
                        labelTEST.setText("Poziom: " + C.LEVEL);
                        updatePunktacja();
                    //otrzymanie dodatkowej rakiety
                        if(C.MONETY>=50){
                            C.RAKIETY++;
                            try {
                                Dzwiek.playDodatkowaRakieta();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            C.MONETY-=50;
                        }
////////////////////////////obsługa labelki z liczbą HP bossów////////////////////////////////////////////
                        if (listWrog != null) {
                            int temp = 0;
                            int bosscounter = 0;
                            for (int iw = 0; iw < listWrog.size(); iw++) {
                                Wrog wrog = listWrog.get(iw);
                                if (wrog.getisBoss() != 0) {
                                    bosscounter++;
                                    bossHPtotal = 0;
                                    isBoss = true;
                                    bossHPtotal = temp;
                                    bossHPtotal = bossHPtotal + wrog.getHP();
                                    temp = bossHPtotal;
                                    labelBossHp.setText("Boss HP: " + bossHPtotal);
                                }
                            }
                            if (bosscounter == 0) labelBossHp.setText("");
                        }
///////////////////////////////////////////// etykieta pauzy
                        if (C.PAUSE == true) labelPauza.setText("Aby odpauzować, wciśnij klawisz \"p\".");
                        else labelPauza.setText("");
///////////////////////////////////////////// poruszanie gracza//////////////////////////////////////////
                        if (RIGHT_PRESSED == true) {
                            if (C.FRAME_WIDTH - 60 >= gracz.getX()) {
                                gracz.moveX(3);
                            }
                            if (C.FRAME_WIDTH - 60 <= gracz.getX()) {
                                gracz.moveX(-5);
                            }
                        }
                        if (LEFT_PRESSED == true) {
                            if (0 <= gracz.getX()) {
                                gracz.moveX(-3);
                            }
                            if (0 >= gracz.getX()) {
                                gracz.moveX(5);
                            }
                        }
                        if (DOWN_PRESSED == true) {
                            if (C.FRAME_HEIGHT - 150 >= gracz.getY()) {
                                gracz.moveY(3);
                            }
                        }
                        if (UP_PRESSED == true) {
                            if (0 < gracz.getY()) {
                                gracz.moveY(-3);
                            }
                        }
                        if (SHOT_PRESSED == true) {
                            if (C.GODMODE != true) strzalisCooldown = true;
                            nowyStrzal();
                            SHOT_PRESSED = false;
                            try {
                                Dzwiek.playStrzal();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        if (RAKIETA_PRESSED == true) {
                            if (C.GODMODE != true) rakietaisCooldown = true;
                            nowyRakieta(gracz.getX(), gracz.getY());
                            RAKIETA_PRESSED = false;
                            try {
                                Dzwiek.playStrzal();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        //opóźnienie między wystrzałami gracza
                        if (strzalisCooldown == true) {
                            strzalCooldown--;
                        }
                        if (strzalCooldown <= 0) {
                            if (C.BATERIA == false) strzalCooldown = 60;
                            if (C.BATERIA == true) strzalCooldown = 30;
                            strzalisCooldown = false;
                        }
                        //opóźnienie między wystrzałami rakiet
                        if (rakietaisCooldown == true) {
                            rakietaCooldown--;
                        }
                        if (rakietaCooldown <= 0) {
                            rakietaCooldown = 60;
                            rakietaisCooldown = false;
                        }
                        //opóźnienie wygaszenia tarczy
                        if (C.tarczaActivated == true) {
                            tarczaCooldown--;
                        }
                        if (tarczaCooldown <= 0) {
                            tarczaCooldown = 100;
                            C.tarczaActivated = false;
                        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////kolizja punktow//////////////////////////////////////////////////////////////////////
                        if (listPunkt != null) {
                            for (int i = 0; i < listPunkt.size(); i++) {
                                Punkt punkt = listPunkt.get(i);
//kolizja gracza z Punktem, gdy lewy/prawy dolny róg Punktu dotknie gornego boku Gracza daje punkt i niszczy obiekt
                                if (isCollision(gracz.getX(), gracz.getY(), punkt.getX(), punkt.getY(),
                                        gracz.getW(), gracz.getH(), punkt.getW(), punkt.getH()) == true) {
                                    try {
                                        Dzwiek.playPunkt();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    C.PUNKTY++;
                                    C.MONETY++;
                                    updatePunktacja();
                                    listPunkt.remove(punkt);
                                } else if (punkt.getY() > C.FRAME_HEIGHT) {
                                    listPunkt.remove(punkt);
                                }
                            }
                        }
//kolizja wroga i gracza
                        if (listWrog != null) {
                            for (int iw = 0; iw < listWrog.size(); iw++) {
                                Wrog wrog = listWrog.get(iw);
                                if (wrog.getHP() <= 0) {
                                    listWrog.remove(wrog);
                                }
                                //kolizja wroga i gracza
                                if (isCollision(gracz.getX(), gracz.getY(), wrog.getX(), wrog.getY(),
                                        gracz.getW(), gracz.getH(), wrog.getW(), wrog.getH()) == true && C.tarczaActivated==false) {
                                    if (wrog.getisBoss() == 0) {
                                        listWrog.remove(wrog);
                                        playerHit();
                                    } else {
                                        wrog.setHP(wrog.getHP() - 1);
                                        playerHit();
                                    }
                                }
                            }
                        }

                        if (listWrogLaser != null) {
                            for (int iw = 0; iw < listWrogLaser.size(); iw++) {
                                WrogLaser wrog = listWrogLaser.get(iw);
                                //kolizja wroga lasera i gracza
                                if (isCollision(gracz.getX(), gracz.getY(), wrog.getX(), wrog.getY(),
                                        gracz.getW(), gracz.getH(), wrog.getW(), wrog.getH()) == true&& C.tarczaActivated==false) {
                                    listWrogLaser.remove(wrog);
                                    playerHit();
                                } else if (wrog.getY() > C.FRAME_HEIGHT) {
                                    listMeteor.remove(wrog);
                                }

                                if (listStrzal != null) {
                                    for (int i = 0; i < listStrzal.size(); i++) {
                                        Strzal strzal = listStrzal.get(i);
                                        if (isCollision(strzal.getX(), strzal.getY(), wrog.getX(), wrog.getY(),
                                                strzal.getW(), strzal.getH(), wrog.getW(), wrog.getH()) == true) {
                                            //losowe wypadanie punkt lub ulepszenie broni lub dodatkowe życie lub bateria lub tarcza
                                            if (wrog.getHP() == 1) {
                                                listWrogLaser.remove(wrog);
                                                try {
                                                    Dzwiek.playEnemyHit();
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                Random random = new Random();
                                                int roll = random.nextInt(100);
                                                if (roll >= 0 && roll <= 5) {
                                                    nowyBron(wrog.getX(), wrog.getY());
                                                } else if (roll >= 95 && roll <= 100) {
                                                    nowyPunktZycie(wrog.getX(), wrog.getY());
                                                }else if (roll >= 16 && roll <= 18) {
                                                    nowyPunktPANS(wrog.getX(), wrog.getY());
                                                } else if (roll >= 10 && roll <= 15) {
                                                    nowyBateria(wrog.getX(), wrog.getY());
                                                }else if (roll >= 6 && roll <= 9) {
                                                    nowyPunktTarcza(wrog.getX(), wrog.getY());
                                                } else nowyPunkt(wrog.getX(), wrog.getY());

                                                if (strzal != null)
                                                    listStrzal.remove(strzal);
                                                C.PUNKTY += 20;
                                                updatePunktacja();
                                            } else {
                                                if (strzal != null)
                                                    listStrzal.remove(strzal);
                                                wrog.setHP(wrog.getHP() - 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        /////////////////////////////////obsluga strzału z rakiety//////////////////////////
                        if (listRakieta != null) {
                            for (int i = 0; i < listRakieta.size(); i++) {
                                Rakieta rakieta = listRakieta.get(i);
//rakieta wybucha po chwili niszcząc wszystkich wrogow i ich pociski na ekranie
                                if (rakieta.getDelay() < 0) {
                                    rakieta.setIsDetonated(true);
                                    removeEnemyObjects();
                                    try {
                                        Dzwiek.playRakietaWybuch();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if (rakieta.getIsDetonated() == true) listRakieta.remove(rakieta);
                            }
                        }


                        if (listStrzal != null) {
                            for (int i = 0; i < listStrzal.size(); i++) {
                                Strzal strzal = listStrzal.get(i);

                                ///////////////////// usuwanie wroga i strzału gdy ten ich trafi i dodanie pkt za to ////////////////////////
                                if (listWrog != null) {
                                    for (int iw = 0; iw < listWrog.size(); iw++) {
                                        Wrog wrog = listWrog.get(iw);
                                        //kolizja wroga i strzału
                                        if (isCollision(strzal.getX(), strzal.getY(), wrog.getX(), wrog.getY(),
                                                strzal.getW(), strzal.getH(), wrog.getW(), wrog.getH()) == true) {
                                            //losowe wypadanie punkt lub ulepszenie broni lub dodatkowe życie
                                            if (wrog.getHP() == 1) {
                                                listWrog.remove(wrog);
                                                try {
                                                    Dzwiek.playEnemyHit();
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                Random random = new Random();
                                                int roll = random.nextInt(100);
                                                if (roll >= 0 && roll <= 5) {
                                                    nowyBron(wrog.getX(), wrog.getY());
                                                } else if (roll >= 95 && roll <= 100) {
                                                    nowyPunktZycie(wrog.getX(), wrog.getY());
                                                }else if (roll >= 16 && roll <= 18) {
                                                    nowyPunktPANS(wrog.getX(), wrog.getY());
                                                } else if (roll >= 10 && roll <= 15) {
                                                    nowyBateria(wrog.getX(), wrog.getY());
                                                }else if (roll >= 6 && roll <= 9) {
                                                    nowyPunktTarcza(wrog.getX(), wrog.getY());
                                                } else nowyPunkt(wrog.getX(), wrog.getY());

                                                if (strzal != null)
                                                    listStrzal.remove(strzal);
                                                C.PUNKTY += 20;
                                                updatePunktacja();
                                            } else {
                                                if (strzal != null)
                                                    listStrzal.remove(strzal);
                                                wrog.setHP(wrog.getHP() - 1);
                                            }
                                        }
                                    }
                                }

                                //////////////////////////////// kolizja z meteorami///////////////////////////
                                if (listMeteor != null) {
                                    for (int im = 0; im < listMeteor.size(); im++) {
                                        Meteor meteor = listMeteor.get(im);
                                        //kolizja meteoru i strzału
                                        if (isCollision(strzal.getX(), strzal.getY(), meteor.getX(), meteor.getY(),
                                                strzal.getW(), strzal.getH(), meteor.getW(), meteor.getH()) == true) {
                                            listMeteor.remove(meteor);
                                            listStrzal.remove(strzal);
                                            try {
                                                Dzwiek.playMeteorHit();
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            C.PUNKTY += 10;
                                        }

                                    }
                                }
                                ///////////////////usuwanie strzałów gdy wyjda poza okno
                                if (strzal.getY() < -10) {
                                    if (strzal != null)
                                        listStrzal.remove(strzal);
                                }
                            }
                        }

                        if (listStrzalWroga != null) {
                            for (int i = 0; i < listStrzalWroga.size(); i++) {
                                StrzalWroga strzalWroga = listStrzalWroga.get(i);
                                //kolizja strzalu wroga i gracza
                                if (isCollision(gracz.getX(), gracz.getY(), strzalWroga.getX(), strzalWroga.getY(),
                                        gracz.getW(), gracz.getH(), strzalWroga.getW(), strzalWroga.getH()) == true && C.tarczaActivated==false) {
                                    if (strzalWroga != null)
                                        listStrzalWroga.remove(strzalWroga);
                                    playerHit();
                                }
                                ///////////////////usuwanie strzałów gdy wyjda poza okno
                                if (strzalWroga.getY() > C.FRAME_HEIGHT ||strzalWroga.getY() < 0) {
                                    if (strzalWroga != null)
                                        listStrzalWroga.remove(strzalWroga);
                                }
                            }
                        }

                        if (listLaserWroga != null) {
                            for (int i = 0; i < listLaserWroga.size(); i++) {
                                LaserWroga strzalWroga = listLaserWroga.get(i);
                                //kolizja strzalu wroga i gracza
                                if (isCollision(gracz.getX(), gracz.getY(), strzalWroga.getX(), strzalWroga.getY(),
                                        gracz.getW(), gracz.getH(), strzalWroga.getW(), strzalWroga.getH()) == true && C.tarczaActivated==false) {
                                    if (strzalWroga != null)
                                        listLaserWroga.remove(strzalWroga);
                                    playerHit();
                                }
                                ///////////////////usuwanie laserow po uplywie czasu
                                if (strzalWroga.getTime() > 20 && strzalWroga != null) {
                                    listLaserWroga.remove(strzalWroga);
                                }
                            }
                        }

///////////////////////////// generowanie wystrzałów wrogów
                        if (listWrog != null && C.PAUSE != true) {
                            for (int i = 0; i < listWrog.size(); i++) {
                                Wrog wrog = listWrog.get(i);
                                if (wrog.getisBoss() == 0) {
                                    Random random = new Random();
                                    int roll = random.nextInt(300);
                                    if (roll == 0) {
                                        nowyStrzalWroga(wrog.getX() + (wrog.getW()) / 2, wrog.getY() + wrog.getH());
                                        try {
                                            Dzwiek.playWrogStrzal();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                        }
///////////////////////////// generowanie wystrzałów lasera wrogów
                        if (listWrogLaser != null && C.PAUSE != true) {
                            for (int i = 0; i < listWrogLaser.size(); i++) {
                                WrogLaser wrog = listWrogLaser.get(i);
                                Random random = new Random();
                                int roll = random.nextInt(300);
                                if (roll == 0) {
                                    if (wrog.getDirection() == 0)
                                        nowyLaserWroga(wrog.getX() + (wrog.getW()), wrog.getY() + wrog.getH() - 10);
                                    if (wrog.getDirection() == 1)
                                        nowyLaserWroga(wrog.getX() - C.FRAME_WIDTH + 50, wrog.getY() + wrog.getH() - 10);
                                    try {
                                        Dzwiek.playWrogStrzal();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
//generowanie wystrzalow potrojnych dla bossow 1 (isBoss==1)

                            if (listWrog != null && C.PAUSE != true) {
                                for (int i = 0; i < listWrog.size(); i++) {
                                    Wrog wrog = listWrog.get(i);
                                    if (wrog.getisBoss() == 1) {
                                        Random random = new Random();
                                        int roll = random.nextInt(200);
                                        if (roll == 0) {
                                            nowyStrzalWroga(wrog.getX() + (wrog.getW()) / 2, wrog.getY() + wrog.getH(), 20, 20, 0);
                                            nowyStrzalWroga(wrog.getX() + (wrog.getW()) / 2, wrog.getY() + wrog.getH(), 20, 20, 1);
                                            nowyStrzalWroga(wrog.getX() + (wrog.getW()) / 2, wrog.getY() + wrog.getH(), 20, 20, 2);
                                            try {
                                                Dzwiek.playWrogStrzal();
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                            }
//generowanie wystrzalow potrojnych dla bossow 2 (isBoss==2)

                        if (listWrog != null && C.PAUSE != true) {
                            for (int i = 0; i < listWrog.size(); i++) {
                                Wrog wrog = listWrog.get(i);
                                if (wrog.getisBoss() == 2) {
                                    Random random = new Random();
                                    int roll = random.nextInt(200);
                                    if (roll == 0 || (wrog.getHP()<=50 && roll<3)) {
                                        nowyStrzalWroga(wrog.getX() + (wrog.getW()) / 2, wrog.getY() + wrog.getH(), 20, 20, 0);//dol
                                        nowyStrzalWroga((int) (wrog.getX() + wrog.getW()*0.75), (int) (wrog.getY() + wrog.getH()*0.75), 20, 20, 1);//prawo dol
                                        nowyStrzalWroga((int) (wrog.getX() + wrog.getW()*0.25), (int) (wrog.getY() + wrog.getH()*0.75), 20, 20, 2);//lewo dol
                                        nowyStrzalWroga((int) (wrog.getX() + wrog.getW()*0.25), (int) (wrog.getY() + wrog.getH()*0.25), 20, 20, 3);//lewo gora
                                        nowyStrzalWroga((int) (wrog.getX() + wrog.getW()*0.75), (int) (wrog.getY() + wrog.getH()*0.25), 20, 20, 4);//prawo gora
                                        nowyStrzalWroga(wrog.getX() + (wrog.getW()), wrog.getY() + wrog.getH()/2, 20, 20, 5);//prawo
                                        nowyStrzalWroga(wrog.getX(), wrog.getY() + wrog.getH()/2, 20, 20, 6);//lewo
                                        nowyStrzalWroga(wrog.getX() + (wrog.getW()) / 2, wrog.getY(), 20, 20, 7);//gora
                                        try {
                                            Dzwiek.playWrogStrzal();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                        }


////////////////////////kolizja, ulepszenie broni////////////////////////////////////
                        if (listPunktBron != null) {
                            for (int i = 0; i < listPunktBron.size(); i++) {
                                PunktBron punktBron = listPunktBron.get(i);
//kolizja gracza z Punktem, gdy lewy/prawy dolny róg Punktu dotknie gornego boku Gracza daje punkt i niszczy obiekt
                                if (isCollision(gracz.getX(), gracz.getY(), punktBron.getX(), punktBron.getY(),
                                        gracz.getW(), gracz.getH(), punktBron.getW(), punktBron.getH()) == true) {
                                    C.PUNKTY++;
                                    if(C.UPGRADE<=2) C.UPGRADE++;
                                    try {
                                        Dzwiek.playPunktBonus();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    updatePunktacja();
                                    listPunktBron.remove(punktBron);
                                } else if (punktBron.getY() > C.FRAME_HEIGHT) {
                                    listPunktBron.remove(punktBron);
                                }
                            }
                        }
//kolizja gracza z Punktem Tarcza
                        if (listPunktTarcza != null) {
                            for (int i = 0; i < listPunktTarcza.size(); i++) {
                                PunktTarcza punktTarcza = listPunktTarcza.get(i);
                                if (isCollision(gracz.getX(), gracz.getY(), punktTarcza.getX(), punktTarcza.getY(),
                                        gracz.getW(), gracz.getH(), punktTarcza.getW(), punktTarcza.getH()) == true) {
                                    try {
                                        Dzwiek.playTarczaPickup();//
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    C.tarczaActivated=true;
                                    C.PUNKTY+=10;
                                    tarczaCooldown=1000;
                                    updatePunktacja();
                                    listPunktTarcza.remove(punktTarcza);
                                } else if (punktTarcza.getY() > C.FRAME_HEIGHT) {
                                    listPunktTarcza.remove(punktTarcza);
                                }
                            }
                        }
//kolizja gracza z Punktem PANS
                        if (listPunktPANS != null) {
                            for (int i = 0; i < listPunktPANS.size(); i++) {
                                PunktPANS punkt = listPunktPANS.get(i);

                                if (isCollision(gracz.getX(), gracz.getY(), punkt.getX(), punkt.getY(),
                                        gracz.getW(), gracz.getH(), punkt.getW(), punkt.getH()) == true) {
                                    try {
                                        Dzwiek.playPunkt();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    nowyStatekBonus(-100, 50, 200, 50);
                                    C.PUNKTY+=20;
                                    updatePunktacja();
                                    listPunktPANS.remove(punkt);
                                } else if (punkt.getY() > C.FRAME_HEIGHT) {
                                    listPunktPANS.remove(punkt);
                                }
                            }
                        }
                        //kolizja gracza z PunktemBateria
                        if (listPunktBateria != null) {
                            for (int i = 0; i < listPunktBateria.size(); i++) {
                                PunktBateria punkt = listPunktBateria.get(i);
                                if (isCollision(gracz.getX(), gracz.getY(), punkt.getX(), punkt.getY(),
                                        gracz.getW(), gracz.getH(), punkt.getW(), punkt.getH()) == true) {
                                    try {
                                        Dzwiek.playBateria();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    C.PUNKTY++;
                                    C.BATERIA = true;
                                    updatePunktacja();
                                    listPunktBateria.remove(punkt);
                                } else if (punkt.getY() > C.FRAME_HEIGHT) {
                                    listPunktBateria.remove(punkt);
                                }
                            }
                        }
////////////////////////////kolizja, dodatkowe życie ///////////////////////////////////////////////
                        if (listPunktZycie != null) {
                            for (int i = 0; i < listPunktZycie.size(); i++) {
                                PunktZycie punktZycie = listPunktZycie.get(i);
                                if (isCollision(gracz.getX(), gracz.getY(), punktZycie.getX(), punktZycie.getY(),
                                        gracz.getW(), gracz.getH(), punktZycie.getW(), punktZycie.getH()) == true) {
                                    C.ZYCIA++;
                                    try {
                                        Dzwiek.playPunktZycie();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    C.PUNKTY += 25;
                                    updatePunktacja();
                                    listPunktZycie.remove(punktZycie);
                                } else if (punktZycie.getY() > C.FRAME_HEIGHT) {
                                    listPunktZycie.remove(punktZycie);
                                }

                            }
                        }
///////////////////////////////////////detekcja kolizji gracza z meteorem//////////
                        if (listMeteor != null) {
                            for (int i = 0; i < listMeteor.size(); i++) {
                                Meteor meteor = listMeteor.get(i);
                                //kolizja meteoru z graczem

                                if (isCollision(gracz.getX(), gracz.getY(), meteor.getX(), meteor.getY(),
                                        gracz.getW(), gracz.getH(), meteor.getW(), meteor.getH()) == true && C.tarczaActivated==false) {
                                    playerHit();
                                    listMeteor.remove(meteor);
                                } else if (meteor.getY() > C.FRAME_HEIGHT) {
                                    listMeteor.remove(meteor);
                                }
                            }
                        }
////////////////////////////statek z bonusami///////////////
                        if (listStatekBonus != null) {
                            for (int i = 0; i < listStatekBonus.size(); i++) {
                                StatekBonus statekBonus = listStatekBonus.get(i);
                                if (statekBonus.getX() + statekBonus.getW() / 2 >= C.FRAME_WIDTH / 2 - 30&& statekBonus.itemDropped == false) {
                                    Random random = new Random();
                                    int roll = random.nextInt(3);
                                    if (roll == 0){
                                        if(C.LEVEL!=30) nowyPunktZycie(statekBonus.getX() + statekBonus.getW() / 2-25, statekBonus.getY() + statekBonus.getH() / 2);
                                        if(C.LEVEL==30) nowyBron(statekBonus.getX() + statekBonus.getW() / 2-25, statekBonus.getY() + statekBonus.getH() / 2);
                                    }
                                    if (roll == 1)
                                        nowyBron(statekBonus.getX() + statekBonus.getW() / 2-25, statekBonus.getY() + statekBonus.getH() / 2);
                                    if (roll == 2)
                                        nowyBateria(statekBonus.getX() + statekBonus.getW() / 2-25, statekBonus.getY() + statekBonus.getH() / 2);
                                    int roll2 = random.nextInt(3);
                                    if (roll2 == 0){
                                        if(C.LEVEL!=30)nowyPunktZycie(statekBonus.getX() + statekBonus.getW() / 2+25, statekBonus.getY() + statekBonus.getH() / 2);
                                        if(C.LEVEL==30)nowyBron(statekBonus.getX() + statekBonus.getW() / 2+25, statekBonus.getY() + statekBonus.getH() / 2);
                                    }
                                    if (roll2 == 1)
                                        nowyBron(statekBonus.getX() + statekBonus.getW() / 2+25, statekBonus.getY() + statekBonus.getH() / 2);
                                    if (roll2 == 2)
                                        nowyBateria(statekBonus.getX() + statekBonus.getW() / 2+25, statekBonus.getY() + statekBonus.getH() / 2);
                                    statekBonus.itemDropped = true;
                                }
                                if (statekBonus.getX() > C.FRAME_WIDTH + statekBonus.getW()) {
                                    listStatekBonus.remove(statekBonus);
                                }
                            }
                        }
//////////////////////////////////warunek końca gry//////////////////////////////////////////////
                        if (C.ZYCIA <= 0 && C.GODMODE == false) {
                            C.PAUSE = true;
                            try {
                                Dzwiek.playPrzegrana();
                                stopAllMusic();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            int enddialog = JOptionPane.showConfirmDialog
                                    (null, "Uzyskałeś " + C.PUNKTY + " punktów!\nPrzegrałeś! Chcesz zagrać ponownie?",
                                            "Koniec gry", 0);
                            //zresetowanie gry po wybraniu tak
                            if (enddialog == 0) {
                                removeObjects();
                                resetVariables();
                                updatePunktacja();
                                gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                                C.PAUSE = false;
                                playBackground();
                            }
                            //koniec gry po kliknieciu nie, przejscie do menu glownego
                            if (enddialog == 1) {
                                C.PAUSE = false;
                                removeObjects();
                                resetVariables();
                                updatePunktacja();
                                gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                                C.GAMESTATE=0;
                                try {
                                    Dzwiek.playMenuBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                resetLabels();
                            }
                        }

                        ////////////////////////////////////tu POZIOMY, rozmieszczanie przeciwnikow///////////////////////////////////////////////////
                        if (C.LEVEL == 0) {
                            if (tempY < 501) tempY++;
                            if (tempY > 500) {
                                if (C.LEVEL_CREATED == false) nowyWrog(0, 50, 50, 50);
                                C.LEVEL_CREATED = true;
                                if (listWrog.isEmpty()) {
                                    if (tempX == 0) nowyStatekBonus(-100, 50, 200, 50);
                                    tempX = 1;
                                }
                                if (listWrog.isEmpty() && listStatekBonus.isEmpty() && tempX == 1) {
                                    C.LEVEL++;
                                    System.out.println("LEVEL: " + C.LEVEL);
                                    C.LEVEL_CREATED = false;

                                    tempX = 0;
                                    tempY = 0;
                                }
                            }
                        }

                        if (C.LEVEL == 99) {//////////testowe/////////
                            if (tick > 100 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                nowyStatekBonus(-100, 50, 200, 50);
                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 1) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listStatekBonus.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempX = 0;

                            }
                        }

                        if (C.LEVEL == 1 || C.LEVEL == 11 || C.LEVEL == 21) {

                            if (playedMusic == false &&( C.LEVEL == 11 || C.LEVEL == 21)) {
                                if(Dzwiek.clipboss!=null)Dzwiek.stopBoss();
                                try {
                                    playBackground();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            playedMusic = true;
                            if(tempZ<1000)tempZ++;
                            if(tempZ>300)
                            if (tick > 100 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                if (C.LEVEL == 1) nowyWrog(0, 50, 50, 50);
                                if (C.LEVEL == 11) nowyWrog(0, 50, 2, 2, 1, 0, 0, 0, 1);
                                if (C.LEVEL == 21) nowyWrog(0, 50, 2, 2, 1, 0, 0, 0, 2);
                                tick = 0;
                                enemyCreated++;
                                if (C.LEVEL == 1 && enemyCreated == 5) C.LEVEL_CREATED = true;
                                if ((C.LEVEL == 11 || C.LEVEL == 21) && enemyCreated == 10) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                playedMusic = false;
                                tempZ=0;
                            }
                        }
                        if (C.LEVEL == 2 || C.LEVEL == 12 || C.LEVEL == 22) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>300)
                            if (tick > 50 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                if (C.LEVEL == 2)
                                    nowyWrog(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                if (C.LEVEL == 12) {
                                    nowyWrog(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 + 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                    nowyWrog(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 100, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                }
                                if (C.LEVEL == 22) {
                                    nowyWrog(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 + 200, C.FRAME_HEIGHT / 2 - 25 - 100, 100, 2);
                                    nowyWrog(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 - 200, C.FRAME_HEIGHT / 2 - 25 - 100, 100, 2);
                                    nowyWrog(-60, -60, 1, 1, 2, C.FRAME_WIDTH / 2 ,100, 100, 2);
                                }

                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 7) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if(tempZ>1000 && C.LEVEL==22){
                                for (int i = 0; i < listWrog.size(); i++) {
                                    listWrog.get(i).setMovingType(5);
                                }
                            }

                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempZ=0;
                            }
                        }

                        if (C.LEVEL == 3 || C.LEVEL == 13|| C.LEVEL == 23) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 100 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                if (C.LEVEL == 3 || C.LEVEL == 13)
                                    nowyWrog(-60, -60, 1, 1, 5, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                if(C.LEVEL == 23)
                                    nowyWrog(-60, -60, 1, 1, 5, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                if (C.LEVEL == 13)
                                    nowyWrog(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 1);
                                if (C.LEVEL == 23)nowyWrog(C.FRAME_WIDTH + 60, C.FRAME_HEIGHT / 2 - 25 - 100, 1, 1, 5, C.FRAME_WIDTH / 2 - 55, C.FRAME_HEIGHT / 2 - 25 - 100, 200, 2);
                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 10) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if(tempZ>1500 && C.LEVEL==23){
                                for (int i = 0; i < listWrog.size(); i++) {
                                    listWrog.get(i).setMovingType(0);
                                }
                            }
                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempZ=0;
                            }
                        }
                        if (C.LEVEL == 4 || C.LEVEL == 14|| C.LEVEL == 24) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 50 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                if (C.LEVEL == 4)
                                    nowyWrog(-60, tempY, 1, 1, 3, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 350, 1);
                                if (C.LEVEL == 14)
                                    nowyWrog(tempY, 20, 1, 1, 4, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 350, 1);
                                if (C.LEVEL == 24)
                                   nowyWrog(tempY, -20, 1, 1, 4, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 350, 2);
                                if(tempX!=1 && C.LEVEL==24)
                                for (int i = 0; i < 8; i++) {
                                    nowyWrog(20,i*50, 1, 1, 3, C.FRAME_WIDTH/ 2 - 25 - 100 , C.FRAME_HEIGHT / 2 - 25, 350, 1);
                                    tempX=1;
                                }

                                tick = 0;
                                enemyCreated++;
                                tempY += 80;
                                if (enemyCreated == 9) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempY = 0;
                                tempX = 0;
                                tempZ = 0;
                            }
                        }
                        if (C.LEVEL == 5 || C.LEVEL == 15|| C.LEVEL == 25) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 50 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                Random random = new Random();
                                tempX = random.nextInt(100) + 25;
                                if (C.LEVEL == 5) nowyMeteor(random.nextInt(C.FRAME_WIDTH - 25), -25, tempX, tempX);
                                if (C.LEVEL == 15)
                                    nowyMeteor(random.nextInt(C.FRAME_WIDTH + 250) + 200, -50, tempX, tempX, 2);
                                if (C.LEVEL == 25)
                                    nowyMeteor(random.nextInt( 1000)-500, -50, tempX, tempX, 1);
                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 30) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listMeteor.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempY = 0;
                                tempX = 0;
                                tempZ = 0;
                            }
                        }
                        if (C.LEVEL == 6 || C.LEVEL == 16|| C.LEVEL == 26) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 75 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                if (C.LEVEL == 6)
                                    nowyWrog(-60, -60, 1, 1, 8, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                if (C.LEVEL == 16)
                                    nowyWrog(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                if (C.LEVEL == 26){
                                    nowyWrog(-60, -60, 1, 1, 7, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    nowyWrog(-60, -60, 1, 1, 8, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 2);
                                }
                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 14) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempY = 0;
                                tempX = 0;
                                tempZ = 0;
                            }
                        }

                        if (C.LEVEL == 7 || C.LEVEL == 17 || C.LEVEL == 27) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 50 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                if (C.LEVEL == 7) {
                                    nowyWrog(0, 0, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    nowyWrog(C.FRAME_WIDTH - 50, 0, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                }
                                if (C.LEVEL == 17) {
                                    nowyWrog(0, C.FRAME_HEIGHT - 50, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                    nowyWrog(C.FRAME_WIDTH - 50, C.FRAME_HEIGHT - 50, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 1);
                                }
                                if (C.LEVEL == 27) {
                                    if (tempX == 0) {
                                        nowyWrogLaser(0, 0, 50, 50, 0);
                                        nowyWrogLaser(C.FRAME_WIDTH - 65, 0, 50, 50, 1);
                                        tempX=1;
                                    }
                                    if (tempX == 1) {
                                    nowyWrog(0, C.FRAME_HEIGHT - 50, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 2);
                                    nowyWrog(C.FRAME_WIDTH - 50, C.FRAME_HEIGHT - 50, 1, 1, 1, C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT / 2 - 25 - 100, 230, 2);
                                     }
                                }
                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 7) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;
                            if(tempZ>1000 && C.LEVEL==27){
                                for (int i = 0; i < listWrog.size(); i++) {
                                    listWrog.get(i).setMovingType(0);
                                }
                            }
                            if (listWrog.isEmpty() && listWrogLaser.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempY = 0;
                                tempX = 0;
                                tempZ = 0;
                            }
                        }
                        if (C.LEVEL == 8 || C.LEVEL == 18 || C.LEVEL == 28) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 100 && C.PAUSE != true && C.LEVEL_CREATED == false) {

                                if (C.LEVEL == 8) {
                                    if (tempX == 0) {
                                        nowyWrogLaser(0, 0, 50, 50, 0);
                                    }
                                    if (tempX == 1) {
                                        nowyWrogLaser(C.FRAME_WIDTH - 65, 0, 50, 50, 1);
                                    }
                                }
                                if (C.LEVEL == 18 && tempX == 0) {
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 + 130, 0, 50, 50, 0);
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 - 130, 0, 50, 50, 1);
                                }
                                if (C.LEVEL == 18 && tempX == 1) {
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 + 130, 0, 50, 50, 1);
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 - 130, 0, 50, 50, 0);
                                }
                                if (C.LEVEL == 28 && tempX == 0) {
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 + 130, 0, 1, 1, 0,1,0,0,0,2);
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 - 130, 0, 1, 1, 1,1,0,0,0,2);
                                }
                                if (C.LEVEL == 28 && tempX == 1) {
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 + 130, 0, 1, 1, 1,1,0,0,0,2);
                                    nowyWrogLaser(C.FRAME_WIDTH / 2 - 130, 0, 1, 1, 0,1,0,0,0,2);
                                }

                                if (tempX == 0) tempX++;
                                else tempX--;

                                tick = 0;
                                enemyCreated++;
                                if (C.LEVEL == 8) if (enemyCreated == 10) C.LEVEL_CREATED = true;
                                if (C.LEVEL == 18) if (enemyCreated == 6) C.LEVEL_CREATED = true;
                                if (C.LEVEL == 28) if (enemyCreated == 8) C.LEVEL_CREATED = true;
                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listWrogLaser.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempX = 0;
                                tempZ = 0;
                                tempY = 0;
                            }
                        }
                        if (C.LEVEL == 9 || C.LEVEL == 19 || C.LEVEL == 29) {
                            if(tempZ<2000)tempZ++;
                            if(tempZ>400)
                            if (tick > 100 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                nowyStatekBonus(-100, 50, 200, 50);
                                tick = 0;
                                enemyCreated++;
                                if (enemyCreated == 1) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listStatekBonus.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempX = 0;
                            }
                        }

                        if (C.LEVEL == 10) {
                            if(Dzwiek.clipback!=null)Dzwiek.stopBackground();
                            if (playedMusic == false) {
                                try {
                                    Dzwiek.playBoss();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            playedMusic = true;
                            if (tick > 500 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                nowyBoss1(-100, 50, 100, 100, 50);
                                enemyCreated++;
                                if (enemyCreated == 1) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                C.PUNKTY+=500;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempX = 0;
                                tempY = 0;
                                playedMusic = false;
                            }
                        }
                        if (C.LEVEL == 20) {
                            if(Dzwiek.clipback!=null)Dzwiek.stopBackground();
                            if (playedMusic == false) {
                                try {
                                    Dzwiek.playBoss();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            playedMusic = true;
                            if (tick > 500 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                nowyBoss1(-100, tempY, 100, 100, 50);
                                enemyCreated++;
                                tick = 0;
                                tempY+=75;
                                if (enemyCreated == 3) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;

                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                C.PUNKTY+=1500;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempX = 0;
                                tempY = 0;
                                playedMusic = false;
                            }
                        }
                        if (C.LEVEL == 30) {
                            if(Dzwiek.clipback!=null)Dzwiek.stopBackground();
                            if (playedMusic == false) {
                                try {
                                    Dzwiek.playBoss();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            playedMusic = true;
                            if (tick > 500 && C.PAUSE != true && C.LEVEL_CREATED == false) {
                                nowyBoss2(-100, 100, 200, 200, 200);
                                enemyCreated++;
                                tick = 0;
                                if (enemyCreated == 1) C.LEVEL_CREATED = true;

                            } else if (C.PAUSE != true && C.LEVEL_CREATED != true) tick++;
                            if(C.LEVEL_CREATED==true && !(listWrog.isEmpty())){
                                tempX++;
                                if (tempX==1500){
                                    tempX=0;
                                    nowyStatekBonus(-100, 50, 200, 50);
                                }
                           }
                            if (listWrog.isEmpty() && C.LEVEL_CREATED == true) {
                                C.LEVEL++;
                                C.PUNKTY+=2500;
                                System.out.println("LEVEL: " + C.LEVEL);
                                C.LEVEL_CREATED = false;
                                enemyCreated = 0;
                                tempX = 0;
                                tempY = 0;
                                playedMusic = false;
                            }
                        }
                        if (C.LEVEL == 31) { /// ostatni poziom koniec gry
                            removeEnemyObjects();
                            if(tick==1000){
                                //okno dialogowe konca gry
                                int eenddialog = JOptionPane.showConfirmDialog
                                        (null, "Uzyskałeś " + C.PUNKTY + " punktów!\nGratulacje! Czy chcesz zagrać ponownie?",
                                                "Koniec gry", 0);
                                //zresetowanie gry po wybraniu tak
                                if (eenddialog == 0) {
                                    removeObjects();
                                    resetVariables();
                                    updatePunktacja();
                                    gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                                    C.PAUSE = false;
                                    playBackground();
                                }
                                //koniec gry po kliknieciu nie, przejscie do menu glownego
                                if (eenddialog == 1) {
                                    C.PAUSE = false;
                                    removeObjects();
                                    resetVariables();
                                    updatePunktacja();
                                    gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                                    C.GAMESTATE=0;
                                    try {
                                        Dzwiek.playMenuBackground();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    resetLabels();
                                }
                            }
                            else {
                                stopAllMusic();
                                if(playedMusic==false){
                                    try {
                                        Dzwiek.playEnd();
                                        playedMusic=true;
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                            }
                                tick++;
                            }

                        }

////////////////////////////najlepszy wynik///////////////////////////////////////////////////////////////////////////
                        if(C.PUNKTY>C.highscorePunkty){
                            updateHighscore();
                            labelRekord.setText("Nowy rekord!");
                        }
                        updatePunktacja();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    repaint();
                }
            }
        }).start();
    }


    //rysowanie obiektów
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        Image imgBack = new ImageIcon(getClass().getClassLoader().getResource("background.gif")).getImage();
        g.drawImage(imgBack, 0, 0, C.FRAME_WIDTH, C.FRAME_HEIGHT, null);
        if (C.GAMESTATE == 1) {
            //narysowanie gracza
            gracz.draw(g2D);
            //rysowanie pukntow i strzalow
            if (listPunkt != null)
                for (int i = 0; i < listPunkt.size(); i++) {
                    listPunkt.get(i).draw(g2D);
                }

            if (listStrzal != null)
                for (int i = 0; i < listStrzal.size(); i++) {
                    listStrzal.get(i).draw(g2D);
                }
            //wystrzal rakiety
            if (listRakieta != null && listRakieta.isEmpty() == false)
                for (int i = 0; i < listRakieta.size(); i++) {
                    listRakieta.get(i).draw(g2D);
                }

            if (listStrzalWroga != null)
                for (int i = 0; i < listStrzalWroga.size(); i++) {
                    listStrzalWroga.get(i).draw(g2D);
                }
            if (listLaserWroga != null)
                for (int i = 0; i < listLaserWroga.size(); i++) {
                    listLaserWroga.get(i).draw(g2D);
                }
            if (listStatekBonus != null)
                for (int i = 0; i < listStatekBonus.size(); i++) {
                    listStatekBonus.get(i).draw(g2D);
                }
//rysowanie wrogow
            if (listWrog != null)
                for (int i = 0; i < listWrog.size(); i++) {
                    listWrog.get(i).draw(g2D);
                }

            if (listWrogLaser != null)
                for (int i = 0; i < listWrogLaser.size(); i++) {
                    listWrogLaser.get(i).draw(g2D);
                }

            if (listPunktBron != null)
                for (int i = 0; i < listPunktBron.size(); i++) {
                    listPunktBron.get(i).draw(g2D);
                }
            if (listPunktBateria != null)
                for (int i = 0; i < listPunktBateria.size(); i++) {
                    listPunktBateria.get(i).draw(g2D);
                }
            if (listPunktPANS != null)
                for (int i = 0; i < listPunktPANS.size(); i++) {
                    listPunktPANS.get(i).draw(g2D);
                }
            //rysowanie punktu dodatkowe zycie
            if (listPunktZycie != null)
                for (int i = 0; i < listPunktZycie.size(); i++) {
                    listPunktZycie.get(i).draw(g2D);
                }

            //rysowanie meteorow
            if (listMeteor != null)
                for (int i = 0; i < listMeteor.size(); i++) {
                    listMeteor.get(i).draw(g2D);
                }
            //rysowanie punktoiw tarcza
            if (listPunktTarcza != null)
                for (int i = 0; i < listPunktTarcza.size(); i++) {
                    listPunktTarcza.get(i).draw(g2D);
                }
            //rysowanie boss 1
            try {
                if (listBoss1 != null && listBoss1.isEmpty() == false)
                    for (int i = 0; i < listBoss1.size(); i++) {
                        listBoss1.get(i).draw(g2D);
                    }
            } catch (Exception r) {
                System.out.println("rsysowanie blad");
            }

            if(C.LEVEL==0){
                g.setColor(Color.white);
                if(tempX==0) {
                    g.drawString("Użyj strzałek aby się poruszać. Użyj spacji, aby strzelać.", 400, C.FRAME_HEIGHT - 400);
                    g.drawString("Trafiaj w przeciwników, unikając ich oraz ich strzałów.", 200, 200);
                }
                if(tempX==1) {
                    g.drawString("Podczas gry pojawiają się sojusznicze statki wspierające bonusami.", 400, C.FRAME_HEIGHT - 400);
                    g.drawString("Po zestrzeleniu wroga pojawiają się bonusy do zbierania.", 200, 200);
                    g.drawString("Użyj \"R\", aby wystrzelić rakietę rażącą wszystkich przeciwników.", 240, C.FRAME_HEIGHT - 80);
                    g.drawString("Nie otzymasz za to bonusów. Zbierz 50 monet by otrzymać rakietę.", 240, C.FRAME_HEIGHT - 50);
                }
                g.drawString("Gra skończy się gdy stracisz wszystkie życia!",20,C.FRAME_HEIGHT-100);
            }
            if(C.LEVEL==31){
                g.setColor(Color.white);
                g.setFont(new Font("Arial",Font.BOLD,50));
                    g.drawString("Gratulacje! Zakończyłeś grę!", C.FRAME_WIDTH/2 - 350, 200);
                    g.drawString("Wynik końcowy: "+C.PUNKTY, C.FRAME_WIDTH/2 - 220, C.FRAME_HEIGHT - 200);
            }
            //rysowane elementow w UI
            livesUI.draw(g2D);
            strzalUI.draw(g2D);
            rakietaUI.draw(g2D);
            monetaUI.draw(g2D);
            if (C.BATERIA == true) bateriaUI.draw(g2D);
        }
        if (C.GAMESTATE == 0) {//menu
            menu = new Menu();
            if (listWrogMenu != null)
                for (int i = 0; i < listWrogMenu.size(); i++) {
                    listWrogMenu.get(i).draw(g2D);
                }
            menu.draw(g2D);
            menuCursor.draw(g2D);
        }
        if (C.GAMESTATE == 2) {//jak grac
            menujakgrac = new MenuJakGrac();
            menujakgrac.draw(g2D);
        }
        if (C.GAMESTATE == 3) {//o programie
            menuAutor = new MenuAutor();
            menuAutor.draw(g2D);
        }
        if (C.GAMESTATE == 4) {//opcje
            menuOpcje = new MenuOpcje();
            menuOpcje.draw(g2D);
            menuCursor.draw(g2D);
        }
    }

    //metoda usuwająca wszystkie obiekty z planszy
    public void removeObjects() {
        listPunkt.clear();
        listWrog.clear();
        listWrogLaser.clear();
        listPunktZycie.clear();
        listPunktBron.clear();
        listStrzal.clear();
        listStrzalWroga.clear();
        listMeteor.clear();
        listBoss1.clear();
        listRakieta.clear();
        listStatekBonus.clear();
    }

    //usuwanie wrogich obiektow (nie bossow)
    public void removeEnemyObjects() {
        if (listWrog != null)
            for (int i = 0; i < listWrog.size(); i++) {
                Wrog wrog = listWrog.get(i);
                wrog.setHP(wrog.getHP()-10);
            }
        listWrogLaser.clear();
        listBoss1.clear();
        listStrzalWroga.clear();
        listMeteor.clear();
    }

    //metoda sprawdzajaca kolizje 2 obiektow
    public boolean isCollision(int o1x, int o1y, int o2x, int o2y,
                               int o1w, int o1h, int o2w, int o2h) {
        if (
                o1x < o2x + o2w
                        && o1x + o1w > o2x
                        && o1y < o2y + o2h
                        && o1y + o1h > o2y
        ) return true;
        else return false;

    }
    //funkcja wywolywana przy trafieniu gracza
    public void playerHit(){
        if(C.tarczaActivated==false && C.GODMODE==false){
        try {
            Dzwiek.playGraczHit();
        } catch(Exception e)
        {throw new RuntimeException(e);}

        C.ZYCIA--;
        if(C.UPGRADE>2)C.UPGRADE=2;
        if(C.UPGRADE!=0)C.UPGRADE--;
        C.BATERIA =false;
        updatePunktacja();
        C.tarczaActivated =true;
        }
    }
    public void nowyPunkt(){ //utworzenie spadajacego punktu losowo
        Random random=new Random();
        Punkt punkt= new Punkt(random.nextInt(0,C.FRAME_WIDTH-25), 0,this);
        punkt.start();
        listPunkt.add(punkt);

    }
    public void nowyPunktTarcza(int x,int y){ //utworzenie spadajacego punktu dajacego tarcze
        PunktTarcza punktTarcza= new PunktTarcza(x, y,this);
        punktTarcza.start();
        listPunktTarcza.add(punktTarcza);
    }
    public void nowyPunktPANS(int x,int y){ //utworzenie spadajacego punktu wzywajacego statek z bonusem
        PunktPANS punktPANS= new PunktPANS(x, y,this);
        punktPANS.start();
        listPunktPANS.add(punktPANS);
    }
    public void nowyPunkt(int x,int y){ //utworzenie spadajacego punktu
        Punkt punkt= new Punkt(x, y,this);
        punkt.start();
        listPunkt.add(punkt);
    }
    public void nowyPunktZycie(int x,int y){ //utworzenie spadajacego punktu zycie
        PunktZycie punktZycie= new PunktZycie(x, y,this);
        punktZycie.start();
        listPunktZycie.add(punktZycie);
    }
    public void nowyStrzal(){//utworzenie strzalu
        if(C.UPGRADE==0){
            Strzal strzal = new Strzal(gracz.getX()+20,gracz.getY(),this);
            strzal.start();
            listStrzal.add(strzal);
        }
        if(C.UPGRADE==1){
            Strzal strzal = new Strzal(gracz.getX()+40,gracz.getY(),this);
            strzal.start();
            listStrzal.add(strzal);
            Strzal strzal2 = new Strzal(gracz.getX(),gracz.getY(),this);
            strzal2.start();
            listStrzal.add(strzal2);
        }
        if(C.UPGRADE>=2){
            Strzal strzal = new Strzal(gracz.getX()+40,gracz.getY(),this);
            strzal.start();
            listStrzal.add(strzal);
            Strzal strzal2 = new Strzal(gracz.getX(),gracz.getY(),this);
            strzal2.start();
            listStrzal.add(strzal2);
            Strzal strzal3 = new Strzal(gracz.getX()+20,gracz.getY(),this);
            strzal3.start();
            listStrzal.add(strzal3);
        }
    }
    public void nowyStrzalWroga(int x,int y) {//utworzenie strzalu wroga
        StrzalWroga strzalWroga = new StrzalWroga(x, y, this);
        strzalWroga.start();
        listStrzalWroga.add(strzalWroga);
    }
    public void nowyStrzalWroga(int x,int y,int w,int h,int m) {//utworzenie strzalu wroga
        StrzalWroga strzalWroga = new StrzalWroga(x,y,w,h,m, this);
        strzalWroga.start();
        listStrzalWroga.add(strzalWroga);
    }
    public void nowyWrog(){//utworzenie obiektu wroga--testowe
        Random random=new Random();
        Wrog wrog = new Wrog(random.nextInt(0,C.FRAME_WIDTH-50), 0,50,50,2,2,0,this);
        //test hp
        if(C.PUNKTY>300) {
            wrog.setHP(2);
        }else if(C.PUNKTY>1000) {
            wrog.setHP(3);
        }
        wrog.start();
        listWrog.add(wrog);
    }
    public void nowyWrogMenu(){//utworzenie obiektu wroga w menu
        Random random=new Random();
        Wrog wrog = new Wrog(-60,random.nextInt(C.FRAME_HEIGHT-100),50,50,1,1,0,this);
        wrog.start();
        listWrogMenu.add(wrog);
    }
    public void DeleteWrogMenu(){//usuniecie wszystkich wrogow w menu
        if (listWrogMenu != null) {
            for (int iw = 0; iw < listWrogMenu.size(); iw++) {
                Wrog wrog = listWrogMenu.get(iw);
                listWrogMenu.remove(wrog);
            }
        }
    }
    public void nowyStatekBonus(int x,int y,int w,int h){//utworzenie obiektu statku z bonusem
        StatekBonus statekBonus = new StatekBonus(x,y,w,h,this);
        statekBonus.start();
        listStatekBonus.add(statekBonus);

    }
    public void nowyWrog(int x,int y,int w,int h){//utworzenie obiektu wroga
        Wrog wrog = new Wrog(x,y,w,h,this);
        wrog.start();
        listWrog.add(wrog);

    }
    //utworzenie obiektu wroga wraz z wszystkimi jego parametrami
    public void nowyWrog(int x,int y,int velX,int velY,int movingType,int centerX,int centerY,int radius,int hp){
        Wrog wrog = new Wrog(x, y,50,50,velX,velY,movingType,this);
        wrog.setRadius(radius);
        wrog.setCircleCenterX(centerX);
        wrog.setCircleCenterY(centerY);
        wrog.setHP(hp);
        wrog.start();
        listWrog.add(wrog);

    }
        //utworzenie obiektu wroga-lasera
    public void nowyWrogLaser(int x,int y,int w,int h,int direction){//utworzenie obiektu wroga
        WrogLaser wrog = new WrogLaser(x,y,w,h,direction,this);
        wrog.start();
        listWrogLaser.add(wrog);
    }
    //utworzenie obiektu wroga-lasera
    public void nowyWrogLaser(int x,int y,int velX,int velY,int direction,int movingType,int centerX,int centerY,int radius,int hp){//utworzenie obiektu wroga
        WrogLaser wrog = new WrogLaser(x,y,50,50,velX,velY,direction,movingType,centerX,centerY,radius,hp,this);
        wrog.setRadius(radius);
        wrog.setCircleCenterX(centerX);
        wrog.setCircleCenterY(centerY);
        wrog.setHP(hp);
        wrog.start();
        listWrogLaser.add(wrog);
    }
    public void nowyLaserWroga(int x,int y){//utworzenie obiektu lasera
        LaserWroga laserWroga = new LaserWroga(x,y,this);
        laserWroga.start();
        listLaserWroga.add(laserWroga);

    }
    public void nowyBoss1(int x,int y,int w,int h,int hp){//utworzenie obiektu bossa
        Wrog wrog = new Wrog(x,y,w,h,this);
        wrog.setHP(hp);
        wrog.setisBoss(1);
        wrog.start();
        listWrog.add(wrog);
    }
    public void nowyBoss2(int x,int y,int w,int h,int hp){//utworzenie obiektu bossa2
        Wrog wrog = new Wrog(x,y,w,h,this);
        wrog.setHP(hp);
        wrog.setisBoss(2);
        wrog.setMovingType(22);
        wrog.start();
        listWrog.add(wrog);
    }
    public void nowyMeteor(int x,int y,int w,int h){//utworzenie obiektu meteoru
        Meteor meteor = new Meteor(x,y,w,h,this);
        meteor.start();
        listMeteor.add(meteor);
    }
    public void nowyMeteor(int x,int y,int w,int h,int movingtype){//utworzenie obiektu meteoru
        Meteor meteor = new Meteor(x,y,w,h,this);
        meteor.setMovingType(movingtype);
        meteor.start();
        listMeteor.add(meteor);
    }
    public void nowyBron(int x,int y){//utworzenie obiektu ulepszenia broni
        PunktBron punktBron = new PunktBron(x, y,this);
        punktBron.start();
        listPunktBron.add(punktBron);
    }
    public void nowyBateria(int x,int y){//utworzenie obiektu ulepszenia szybkostrzelnosci
        PunktBateria punktBateria = new PunktBateria(x, y,this);
        punktBateria.start();
        listPunktBateria.add(punktBateria);
    }
    public void nowyRakieta(int x,int y){//utworzenie obiektu rakiety
        Rakieta rakieta = new Rakieta(x, y,this);
        if(gracz.getX()>C.FRAME_WIDTH/2-20) rakieta.setkier(1);
        else rakieta.setkier(0);
        rakieta.start();
        listRakieta.add(rakieta);
    }
    public void updatePunktacja(){//aktualizacja labelek
        if(C.GAMESTATE==1){
        labelPunktacja.setText("Punkty: "+C.PUNKTY);
        labelMonety.setText("x "+C.MONETY);
        if(C.GODMODE==false) labelZycia.setText("x "+C.ZYCIA);
        else labelZycia.setText("x ∞");

        if(C.UPGRADE<=2) labelUpgrade.setText("x "+(C.UPGRADE+1));

        labelRakiety.setText("x "+C.RAKIETY);
        }
    }
    public void resetVariables(){//reset zmiennych do stanu poczatkowego
        C.PUNKTY=0;
        C.ZYCIA=3;
        C.LEVEL=0;
        C.RAKIETY=1;
        C.MONETY=0;
        C.BATERIA=false;
        LEFT_PRESSED=false;RIGHT_PRESSED=false;UP_PRESSED=false;DOWN_PRESSED=false;SHOT_PRESSED=false;RAKIETA_PRESSED=false;
        C.UPGRADE=0;
        labelBossHp.setText("");
        tick=0;
        C.LEVEL_CREATED=false;
        enemyCreated=0;
        tempY=0;
        tempX=0;
        tempZ=0;
        C.tarczaActivated=false;
        strzalCooldown=100;
        tarczaCooldown=100;
    }
    public void resetLabels() {//reset labelek
        labelPauza.setText("");
        labelRakiety.setText("");
        labelTEST.setText("");
        labelUpgrade.setText("");
        labelZycia.setText("");
        labelMonety.setText("");
        labelPunktacja.setText("");
        labelBossHp.setText("");
        labelRekord.setText("");
    }
    public void playBackground(){
        try {
            Dzwiek.playBackground();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void playMenuBackground(){
        try {
            Dzwiek.playMenuBackground();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /////////zatrzymanie muzyki
    public void stopAllMusic(){
        try {
            if(Dzwiek.clipmenu!=null) Dzwiek.stopMenuBackground();
            if(Dzwiek.clipback!=null)Dzwiek.stopBackground();
            if(Dzwiek.clipboss!=null)Dzwiek.stopBoss();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    ////////akutualizacja pliku ustawien
    public void updateSettings(){
        try {
            //otwarcie pliku ustawień
            File config = new File("src/ustawienia.txt");
            FileWriter out = new FileWriter(config);
            //wpisanie aktualnych ustawień do pliku ustawień
            out.write(C.musicVolume + "\n" + C.soundVolume+"\n"+C.isMuted);
            out.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
    /////////aktualizacja pliku z najlepszym wynikiem
    public void updateHighscore(){
        try {
            C.highscorePunkty=C.PUNKTY;
            C.highscoreLevel=C.LEVEL;
            //otwarcie pliku highscore
            File config = new File("src/highscore.txt");
            FileWriter out = new FileWriter(config);
            //wpisanie aktualnego najlepszego wyniku
            out.write(C.highscorePunkty + "\n" + C.highscoreLevel);
            out.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
    public void resetHighscore(){
        try {
            C.highscorePunkty=0;
            C.highscoreLevel=0;
            //otwarcie pliku highscore
            File config = new File("src/highscore.txt");
            FileWriter out = new FileWriter(config);
            //wpisanie aktualnego najlepszego wyniku
            out.write(C.highscorePunkty + "\n" + C.highscoreLevel);
            out.close();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
/////////////////////////////////////////////////////////////////////////////
    //obsługa klawiszy


    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        // obsługa menu enterem i powrot z elementow menu do menu
        if (e.getKeyCode()==10) {
            switch (C.GAMESTATE) {
                case 0:
                    if (C.cursorPosition == 0) {
                        C.GAMESTATE = 1;
                        DeleteWrogMenu();
                        Dzwiek.stopMenuBackground();
                        playBackground();
                    }
                    if (C.cursorPosition == 1) {C.GAMESTATE = 2;DeleteWrogMenu();}
                    if (C.cursorPosition == 2) {C.GAMESTATE = 4;DeleteWrogMenu();}
                    if (C.cursorPosition == 3) {C.GAMESTATE = 3;DeleteWrogMenu();}
                    if (C.cursorPosition == 4) System.exit(0);
                    break;
                case 2:
                    C.GAMESTATE = 0;//wyjscie z podmenu
                    break;
                case 3:
                    C.GAMESTATE = 0;//wyjscie z podmenu
                    break;
                case 4: /// obsluga entera w podmenu OPCJE
                    if(C.cursorPositionOpcje==3) {
                        //reset najlepszego wyniku
                        int enddialog = JOptionPane.showConfirmDialog
                                (null, "Czy na pewno chcesz zresetować najlepszy wynik?",
                                        "?", 0);
                        //zresetowanie po wybraniu tak
                        if (enddialog == 0)  resetHighscore();
                    }
                    if(C.cursorPositionOpcje==4) C.GAMESTATE = 0;
                    if(C.cursorPositionOpcje==2){
                    switch (C.isMuted){
                        case 0:
                            C.isMuted=1;
                            stopAllMusic();
                            updateSettings();
                            break;
                        case 1:
                            C.isMuted=0;
                            try {
                                Dzwiek.playMenuBackground();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            updateSettings();
                            break;
                        }
                    }
                    break;
            }

        }
        //poruszanie strzalkami kursorem w menu
        if (e.getKeyCode()==40 && C.GAMESTATE==0 && menu!=null){//s w dol
            if(C.cursorPosition!=4) {
                C.cursorPosition++;
                try {
                    Dzwiek.playStrzal();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);

                }
            }
        }
        if (e.getKeyCode()==38&& C.GAMESTATE==0 && menu!=null){//s w gore
            if(C.cursorPosition!=0) {
                C.cursorPosition--;
                try {
                    Dzwiek.playStrzal();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
//////////////////////////poruszanie strzalkami kursorem w menu-OPCJE///////////////////////////////
        if (e.getKeyCode()==40 && C.GAMESTATE==4 && menuOpcje!=null){//s w dol
            if(C.cursorPositionOpcje!=4) {
                C.cursorPositionOpcje++;
                try {
                    Dzwiek.playStrzal();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);

                }
            }
        }
        if (e.getKeyCode()==38&& C.GAMESTATE==4 && menuOpcje!=null){//s w gore
            if(C.cursorPositionOpcje!=0) {
                C.cursorPositionOpcje--;
                try {
                    Dzwiek.playStrzal();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (e.getKeyCode()==39 && C.GAMESTATE==4 && menuOpcje!=null){//s w prawo
            if(C.cursorPositionOpcje==0){
                if(C.musicVolume!=9) {
                    C.musicVolume++;
                    updateSettings();
                    try {
                        Dzwiek.playStrzal();
                        if(Dzwiek.clipmenu!=null) Dzwiek.stopMenuBackground();
                        Dzwiek.playMenuBackground();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            if(C.cursorPositionOpcje==1){
                    if(C.soundVolume!=9) {
                        C.soundVolume++;
                        updateSettings();
                        try {
                            Dzwiek.playStrzal();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

            if(C.cursorPositionOpcje==2){
                if(C.isMuted==0) {
                    C.isMuted=1;
                    updateSettings();
                    try {
                        Dzwiek.stopMenuBackground();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        if (e.getKeyCode()==37&& C.GAMESTATE==4 && menuOpcje!=null){//s w lewo
            if(C.cursorPositionOpcje==0){
                if(C.musicVolume!=0) {
                    C.musicVolume--;
                    updateSettings();
                    try {
                        Dzwiek.playStrzal();
                        if(Dzwiek.clipmenu!=null) Dzwiek.stopMenuBackground();
                        Dzwiek.playMenuBackground();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            if(C.cursorPositionOpcje==1){
                    if(C.soundVolume!=0) {
                        C.soundVolume--;
                        updateSettings();
                        try {
                            Dzwiek.playStrzal();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            if(C.cursorPositionOpcje==2){
                if(C.isMuted==1) {
                    C.isMuted=0;
                    updateSettings();
                    try {
                        Dzwiek.playMenuBackground();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            }
//////////////////////////////////////////////////////////////////
        if (e.getKeyCode()==27 && C.GAMESTATE==1){  //esc
            C.PAUSE=true;
            LEFT_PRESSED=false;
            RIGHT_PRESSED=false;
            UP_PRESSED=false;
            DOWN_PRESSED=false;
            SHOT_PRESSED=false;
            int enddialog = JOptionPane.showConfirmDialog
                    (null, "Czy chcesz powrócić do menu?",
                            "Pauza", 0);

            //powrót do menu po wybraniu tak
            if (enddialog == 0) {
                C.GAMESTATE=0;
                removeObjects();
                resetVariables();
                updatePunktacja();
                resetLabels();
                gracz = new Gracz(C.FRAME_WIDTH / 2 - 25, C.FRAME_HEIGHT - 150);
                C.PAUSE = false;
                stopAllMusic();
                playMenuBackground();
            }
            //wznowienie po kliknieciu nie
            if (enddialog == 1) {
                C.PAUSE = false;
            }
        }
 ////   ruch gracza
        if (e.getKeyCode()==37 && (C.PAUSE!=true)){//s w lewo
            LEFT_PRESSED=true;
        }
        if (e.getKeyCode()==39 && (C.PAUSE!=true)){//s w prawo
            RIGHT_PRESSED=true;
        }
        if (e.getKeyCode()==40 && (C.PAUSE!=true)){//s w dol
            DOWN_PRESSED=true;
        }
        if (e.getKeyCode()==38 && (C.PAUSE!=true)){//s w gore
            UP_PRESSED=true;
        }
        if (e.getKeyCode()==32 && (C.PAUSE!=true) &&strzalisCooldown==false ){//spacja strzał
            SHOT_PRESSED=true;
        }
        if (e.getKeyCode()==82 && (C.PAUSE!=true) && rakietaisCooldown==false){// rakieta (zabiera 10 hp wszystkim wrogom lub ich usuwa) "r"
            if(C.RAKIETY>0 || C.GODMODE==true){
            RAKIETA_PRESSED=true;
            if(C.GODMODE==false) C.RAKIETY--;
            labelRakiety.setText("x "+C.RAKIETY);
            }
                else{
                    try {
                        Dzwiek.playCantShoot();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                }
            }
        }
        if (e.getKeyCode()==80) {//p przycisk pauza
            if (C.PAUSE == true) {
                C.PAUSE=false;
            }
            else{
                C.PAUSE = true;
                RIGHT_PRESSED=false;LEFT_PRESSED=false;DOWN_PRESSED=false;UP_PRESSED=false;SHOT_PRESSED=false;RAKIETA_PRESSED=false;
            }
        }
    }
//////////////////////////////////////testing////////////////////////////////////////////////////////////
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==87&& C.GODMODE==true){// nowy wrog klawisz "w"
            Wrog wrog = new Wrog(-50, -50,50,50,2,2,2,this);
           wrog.setRadius(100);
//            wrog.setCircleCenterX();
//            wrog.setCircleCenterY();
            wrog.start();
            listWrog.add(wrog);
        }

        if (e.getKeyCode()==66&& C.GODMODE==true){// nowy boss1 klawisz "b"
            nowyBoss1(0,25,100,100,50);
        }
        if (e.getKeyCode()==77 && C.GODMODE==true){//          "m"
                nowyPunktTarcza(100,100);
            nowyPunktPANS(400,100);
        }
        if (e.getKeyCode()==71 && C.GAMESTATE==1){// niesmiertelnosc klawisz "g"
            C.ZYCIA=3;
            if(C.GODMODE==false) C.GODMODE=true;
            else C.GODMODE= false;
            updatePunktacja();
        }
        if (e.getKeyCode()==37 && (C.PAUSE!=true)){//s w lewo
            LEFT_PRESSED=false;
        }
        if (e.getKeyCode()==39 && (C.PAUSE!=true)){//s w prawo
            RIGHT_PRESSED=false;
        }
        if (e.getKeyCode()==40 && (C.PAUSE!=true)){//s w dol
            DOWN_PRESSED=false;
        }
        if (e.getKeyCode()==38 && (C.PAUSE!=true)){//s w gore
            UP_PRESSED=false;
        }
        if (e.getKeyCode()==32 && (C.PAUSE!=true)){//spacja
            SHOT_PRESSED=false;
        }

    }

}
