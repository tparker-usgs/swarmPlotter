package gov.usgs.volcanoes.swarmPlotter.plotter;

import gov.usgs.volcanoes.core.util.UtilException;
import gov.usgs.volcanoes.swarmPlotter.SwarmPlotterArgs;

public abstract class Plotter {
  protected final SwarmPlotterArgs config;

  public Plotter(SwarmPlotterArgs config) {
    this.config = config;
  }

  public abstract void plot() throws UtilException;

}
