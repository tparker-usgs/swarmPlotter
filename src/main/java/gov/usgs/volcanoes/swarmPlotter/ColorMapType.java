package gov.usgs.volcanoes.swarmPlotter;

import java.util.Arrays;

import gov.usgs.volcanoes.core.legacy.plot.color.Inferno;
import gov.usgs.volcanoes.core.legacy.plot.color.Jet;
import gov.usgs.volcanoes.core.legacy.plot.color.Spectrum;
import gov.usgs.volcanoes.swarmPlotter.plotter.Plotter;

/**
 * Enumerate known plot types.
 * 
 * @author Tom Parker
 */
public enum ColorMapType {
  JET(Jet.getInstance()), 
  INFERNO(Inferno.getInstance());

  private Spectrum colormap;

  ColorMapType(Spectrum colormap) {
    this.colormap = colormap;
  }

  public static String types() {
    StringBuffer sb = new StringBuffer();
    for (ColorMapType type : Arrays.asList(values())) {
      sb.append(type.toString().toLowerCase() + ", ");
    }
    return sb.substring(0, sb.length() - 2);
  }

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }

  public Spectrum getColormap() throws IllegalArgumentException {
    try {
      return colormap;
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}
