
import ddf.minim.*;

public class Keyboard {
  private class Key {
    protected char code;
    protected int pitch;
    
    public Key(char code, int pitch) {
      this.code = code;
      this.pitch = pitch;
    } 
  }
  
  // key pitch settings from http://www.openprocessing.org/sketch/99584
  private Key[] keys = {
      new Key('z', 262),
      new Key('s', 277),
      new Key('x', 294),
      new Key('d', 311),
      new Key('c', 330),
      new Key('v', 349),
      new Key('g', 370),
      new Key('b', 392),
      new Key('h', 415),
      new Key('n', 440),
      new Key('j', 466),
      new Key('m', 494),
      new Key(',', 523),
      new Key('l', 554),
      new Key('.', 587),
      new Key(';', 622),
      new Key('/', 659)
  };
  
  private AudioOutput audio;
  
  public Keyboard(AudioOutput audio) {
    this.audio = audio;
  }
  
  public void play(char code) {
    for(Key k : keys) {
      if(k.code == code) {
       // play k.pitch
       audio.playNote((float)0.0, (float)1.0, (float)k.pitch);
      } 
    }
  }
  
}
