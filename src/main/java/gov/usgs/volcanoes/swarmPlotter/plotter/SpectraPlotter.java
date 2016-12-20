package gov.usgs.volcanoes.swarmPlotter.plotter;

import java.awt.Color;

import gov.usgs.plot.Plot;
import gov.usgs.plot.PlotException;
import gov.usgs.plot.data.SliceWave;
import gov.usgs.plot.render.wave.SpectraRenderer;
import gov.usgs.volcanoes.core.util.UtilException;
import gov.usgs.volcanoes.swarmPlotter.SwarmPlotterArgs;

public class SpectraPlotter extends WavePlotter {

  public SpectraPlotter(SwarmPlotterArgs config) {
    super(config);
  }

  @Override
  public void plot() throws UtilException {
    getData();
    Plot plot = new Plot();
    plot.setSize(config.dimension);
    plot.setBackgroundColor(new Color(0.97f, 0.97f, 0.97f));

    SpectraRenderer sr = new SpectraRenderer();
    sr.setWave(new SliceWave(waveData));
    sr.setLocation(0, 0, config.dimension.width, config.dimension.height);

    sr.setAutoScale(true);
    sr.setLogPower(true);
    sr.setLogFreq(true);
    sr.setMaxFreq(20);
    sr.setMinFreq(0);
    sr.setYUnitText("Power");
    sr.setTitle(config.channel);

    sr.update();
    
    plot.addRenderer(sr);
    try {
      plot.writePNG(config.outputFile);
    } catch (PlotException e) {
      throw new UtilException("Cannot write file. (" + e.getLocalizedMessage() + ")");
    }
  }
}
