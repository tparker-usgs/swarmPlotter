package gov.usgs.volcanoes.swarmPlotter;

import java.util.Arrays;

import gov.usgs.volcanoes.swarmPlotter.plotter.HeliPlotter;
import gov.usgs.volcanoes.swarmPlotter.plotter.Plotter;
import gov.usgs.volcanoes.swarmPlotter.plotter.SpectrogramPlotter;
import gov.usgs.volcanoes.swarmPlotter.plotter.WavePlotter;

/**
 * Enumerate known plot types.
 * 
 * @author Tom Parker
 */
public enum PlotType {
  HELI(HeliPlotter.class), WAVE(WavePlotter.class), SPECTROGRAM(SpectrogramPlotter.class);

  private Class<? extends Plotter> plotter;

  PlotType(Class<? extends Plotter> plotter) {
    this.plotter = plotter;
  }

  public static String types() {
    StringBuffer sb = new StringBuffer();
    for (PlotType type : Arrays.asList(values())) {
      sb.append(type.toString().toLowerCase() + ", ");
    }
    return sb.substring(0, sb.length() - 2);
  }

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }

  public Plotter getPlotter(SwarmPlotterArgs config) throws IllegalArgumentException {
    try {
      return plotter.getConstructor(SwarmPlotterArgs.class).newInstance(config);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      throw new IllegalArgumentException(e);
    }
  }
}
