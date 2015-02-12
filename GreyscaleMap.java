import processing.core.*;

public class GreyscaleMap extends ColorMap {
  PApplet parent;
  public GreyscaleMap(PApplet parent) {
    this.parent = parent;  
  }
  
  public int map(int c) {
    int b = (int)parent.brightness(c);
    return parent.color(b);
  }
}
