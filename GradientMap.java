import processing.core.*;

public class GradientMap extends ColorMap {
  PApplet parent;
  int r1, r2, g1, g2, b1, b2;
  public GradientMap(PApplet parent, int c1, int c2) {
    this.parent = parent;
    r1 = (c1 >> 16) & 0xff;
    g1 = (c1 >> 8 ) & 0xff;
    b1 = (c1      ) & 0xff;
    r2 = (c2 >> 16) & 0xff;
    g2 = (c2 >> 8 ) & 0xff;
    b2 = (c2      ) & 0xff;
  }
  
  public int map(int c) {
    float bright = (float)(parent.brightness(c) / 255.0);
    int r = (int)(r1*bright + r2*(1-bright));
    int g = (int)(g1*bright + g2*(1-bright));
    int b = (int)(b1*bright + b2*(1-bright));
    return parent.color(r,g,b);
  }
}
