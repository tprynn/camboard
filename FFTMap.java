import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

public class FFTMap extends ColorMap {
  PApplet parent;
  AudioSource source;
  FFT fourier;
  float peaks[];
  int ages[];
  float max_peaks[];
  int max_ages[];
  
  private static int AGE_DELAY = 10;
  private static double AGE_DECAY_RATE = 1.0;
  private static double FFT_LEVEL_THRESHOLD = 0.5;
  
  public FFTMap(PApplet parent) {
    this.parent = parent;
  }
  
  public void setSource(AudioSource source, FFT fourier) {
    this.source = source;
    this.fourier = fourier;  
    peaks = new float[fourier.specSize()];
    ages = new int[peaks.length];
    max_peaks = new float[peaks.length];
    max_ages = new int[peaks.length];
  }
  
  public int map(int c) {
    if(source == null) {
      return c;
    }
    
    int cr = (c >> 16) & 0xff;
    int cg = (c >> 8 ) & 0xff;
    int cb = (c      ) & 0xff;
    
    float bin_ratio = fourier.specSize()/((float)(3*255));
    
    int r_bin = (int)(bin_ratio * cr);
    float r_ratio = peaks[r_bin]/max_peaks[r_bin];
    float r_mapped = cr*r_ratio;
    
    int g_bin = (int)(bin_ratio * cg);
    float g_ratio = peaks[g_bin]/max_peaks[g_bin];
    float g_mapped = cr*g_ratio;

    int b_bin = (int)(bin_ratio * cb);
    float b_ratio = peaks[b_bin]/max_peaks[b_bin];
    float b_mapped = cr*b_ratio;
    
    float mag_orig = parent.sqrt(cr*cr + cg*cg + cb*cb);
    float mag_mapped = parent.sqrt(r_mapped*r_mapped + g_mapped*g_mapped + b_mapped*b_mapped);
    float mag_ratio = mag_orig/mag_mapped;
    
    int r = (int)(r_mapped*mag_ratio);
    int g = (int)(g_mapped*mag_ratio);
    int b = (int)(b_mapped*mag_ratio);

    return parent.color(r, g, b);
  }
  
  public float getBandPeak(int i) {
    return peaks[i];  
  }

  public void update() {
    if(source == null) {
      return;
    }
    
    fourier.forward(source.mix);
    
    for(int i = 0; i < peaks.length; i++) {
      float level = fourier.getBand(i);
      if(peaks[i] < level && level > FFT_LEVEL_THRESHOLD) {
        peaks[i] = level;
        ages[i] = 0;
      }
      
      if(peaks[i] > max_peaks[i]) {
        max_peaks[i] = peaks[i];
        max_ages[i] = 0;
      }
      
      ages[i] += 1;
      
      if(ages[i] > AGE_DELAY && peaks[i] > 0) {
        peaks[i] -= AGE_DECAY_RATE;
        peaks[i] = parent.max(peaks[i], (float)0);
      }
      
      max_ages[i] += 1;
      
      if(max_ages[i] > AGE_DELAY && peaks[i] > 1.0) {
        max_peaks[i] -= AGE_DECAY_RATE;
        max_peaks[i] = parent.max(peaks[i], (float)1);
      }
    }
  }
  
}
