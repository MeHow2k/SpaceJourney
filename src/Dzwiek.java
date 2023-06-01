import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

public class Dzwiek {
    static public Clip clipmenu;
    static public Clip clipback;
    static public Clip clipboss;

    //głośno 0       -40.0       -80.0 max wyciszenie

    //metody ustawiajace głośność wg ustawienia
    static float checkSoundVolume() {
        if (C.isMuted==0) {
            if (C.soundVolume == 0) {
                return (float) -80.0;
            } else if (C.soundVolume == 1) {
                return (float) -45.0;
            } else if (C.soundVolume == 2) {
                return (float) -35.0;
            } else if (C.soundVolume == 3) {
                return (float) -25.0;
            } else if (C.soundVolume == 4) {
                return (float) -20.0;
            } else if (C.soundVolume == 5) {
                return (float) -16.5;
            } else if (C.soundVolume == 6) {
                return (float) -13.0;
            } else if (C.soundVolume == 7) {
                return (float) -10.0;
            } else if (C.soundVolume == 8) {
                return (float) -5.0;
            } else if (C.soundVolume == 9) {
                return (float) 0.0;
            } else return (float) -20.0;
        }else return (float) -80.0;
    }
    static float checkMusicVolume() {
        if (C.isMuted==0) {
            if (C.musicVolume == 0) {
                return (float) -80.0;
            } else if (C.musicVolume == 1) {
                return (float) -50.0;
            } else if (C.musicVolume == 2) {
                return (float) -45.0;
            } else if (C.musicVolume == 3) {
                return (float) -35.0;
            } else if (C.musicVolume == 4) {
                return (float) -30.0;
            } else if (C.musicVolume == 5) {
                return (float) -25.0;
            } else if (C.musicVolume == 6) {
                return (float) -20.0;
            } else if (C.musicVolume == 7) {
                return (float) -15.0;
            } else if (C.musicVolume == 8) {
                return (float) -10.0;
            } else if (C.musicVolume == 9) {
                return (float) -5.0;
            } else return (float) -40.0;
        }
        return (float) -80.0;
    }
    public static void playBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("MMbackground.wav")));
        clipback = AudioSystem.getClip();
        clipback.open(ais);
        FloatControl gainControl = (FloatControl)
        clipback.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkMusicVolume());
        clipback.loop(Clip.LOOP_CONTINUOUSLY);
        clipback.start();
    }
    public static void playMenuBackground() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("DTMenubackground.wav")));
        clipmenu = AudioSystem.getClip();
        clipmenu.open(ais);
        FloatControl gainControl = (FloatControl)
                clipmenu.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkMusicVolume());
        clipmenu.loop(Clip.LOOP_CONTINUOUSLY);
        clipmenu.start();
    }
    public static void playBoss() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("PKMNboss.wav")));
        clipboss = AudioSystem.getClip();
        clipboss.open(ais);
        FloatControl gainControl = (FloatControl)
        clipboss.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkMusicVolume());
        clipboss.loop(Clip.LOOP_CONTINUOUSLY);
        clipboss.start();
    }
    public static void stopBackground(){
        clipback.stop();
    }
    public static void stopMenuBackground(){
        clipmenu.stop();
    }
    public static void stopBoss(){
        clipboss.stop();
    }
    public static void playStrzal() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("strzal.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        if(C.soundVolume!=0 && C.isMuted==0) gainControl.setValue((float) (checkSoundVolume()-10.0));
        else gainControl.setValue((checkSoundVolume()));
        clip.start();
    }
    public static void playPunktBonus() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("punktBonus.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playPunktZycie() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("punktZycie.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playPrzegrana() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("Przegrana.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playGraczHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("graczHit.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playPunkt() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("punkt.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playMeteorHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("MeteorHit.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playEnemyHit() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("MeteorHit.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());
        clip.start();
    }
    public static void playWrogStrzal() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("wrogStrzal.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
        clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }
    public static void playTarczaPickup() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("tarcza.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }
    public static void playRakietaWybuch() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("rakietaWybuch.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }
    public static void playCantShoot() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("cantshoot.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }
    public static void playBateria() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("bateria.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }
    public static void playDodatkowaRakieta() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("dodatkowarakieta.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }
    public static void playEnd() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(Dzwiek.class.getResourceAsStream("end.wav")));
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        FloatControl gainControl = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(checkSoundVolume());//-40.0       -80.0 max wyciszenie
        clip.start();
    }

}
