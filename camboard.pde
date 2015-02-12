import processing.video.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

Capture camera;
PImage captured_img;
PImage mapped_img;
FFTMap fftmap = new FFTMap(this);
ColorMap colormaps[] = {
  new NullMap(),
  new GreyscaleMap(this),
  new GradientMap(this, color(255, 150, 50), color(50, 50, 255)),
  fftmap
};
final int DEFAULT_MAP = 3;
ColorMap colormap = colormaps[DEFAULT_MAP];

final int CAPTURE_WIDTH = 1280;
final int CAPTURE_HEIGHT = 720;
String camera_name;

final int WIDTH = CAPTURE_WIDTH;
final int HEIGHT = CAPTURE_HEIGHT;  

Keyboard keyboard;
Minim minim;
AudioOutput sound;
AudioInput input;

FFT fourier;

void setup() {
  size(WIDTH, HEIGHT);
  
  try {
    camera_name = Capture.list()[0];
    camera = new Capture(this);//, WIDTH, HEIGHT);//, camera_name);
    camera.start();
  }
  catch (Exception e) {
    System.out.println("Failed to read from " + camera_name);
    System.out.println(e.getMessage());
  }
  
  minim = new Minim(this);
  sound = minim.getLineOut(Minim.STEREO);
  input = minim.getLineIn();
  fourier = new FFT(input.bufferSize(), input.sampleRate());
  fourier.window(FFT.HAMMING);
  fftmap.setSource((AudioSource)input, fourier);
  keyboard = new Keyboard(sound);
}

void keyPressed() {
  if(key >= '1' && key <= colormaps.length + '1' && key <= '9') {
    colormap = colormaps[key - '1'];
  }
  
  keyboard.play(key);
}

void draw() {
  
  if(camera.available()) {
    camera.read();
    captured_img = camera.get();
    captured_img.resize(CAPTURE_WIDTH, CAPTURE_HEIGHT);
    //image(captured_img, 0, CAPTURE_HEIGHT);
  }  
  
  colormap.update();
  
  if(captured_img != null) {
    mapped_img = captured_img.get();
    mapped_img.loadPixels();
    for(int i = 0; i < captured_img.pixels.length; i++) {
       mapped_img.pixels[i] = colormap.map(mapped_img.pixels[i]);
    }
    mapped_img.updatePixels();
    
    image(mapped_img, 0, 0);
    
    /* Draw FFT Spectrum for debugging purposes
    rect(0, CAPTURE_HEIGHT, WIDTH, HEIGHT);
    for (int i = 0; i < fourier.specSize(); i++)
    {
      // draw the line for frequency band i, scaling it by 4 so we can see it a bit better
      line(i, height, i, height - fftmap.getBandPeak(i) * 4);
    }
    */
  }
}

