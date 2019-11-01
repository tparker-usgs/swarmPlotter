package gov.usgs.volcanoes.swarmPlotter.plotter;

import java.awt.Color;

import gov.usgs.volcanoes.core.data.SliceWave;
import gov.usgs.volcanoes.core.legacy.plot.Plot;
import gov.usgs.volcanoes.core.legacy.plot.PlotException;
import gov.usgs.volcanoes.core.legacy.plot.render.wave.SliceWaveRenderer;
import gov.usgs.volcanoes.core.legacy.plot.render.wave.SpectrogramRenderer;
import gov.usgs.volcanoes.core.util.UtilException;
import gov.usgs.volcanoes.swarmPlotter.SwarmPlotterArgs;

public class SpectrogramPlotter extends WavePlotter {

  private static final double WAVE_HEIGHT_RATIO = .2;

  public SpectrogramPlotter(SwarmPlotterArgs config) {
    super(config);
  }

  @Override
  public void plot() throws UtilException {
    getData();
    Plot plot = new Plot();
    plot.setSize(config.dimension);
    plot.setBackgroundColor(new Color(0.97f, 0.97f, 0.97f));


    int waveHeight = (int) (config.dimension.height * WAVE_HEIGHT_RATIO);
    int spectrogramHeight = config.dimension.height - waveHeight;

    SliceWaveRenderer wr = new SliceWaveRenderer();

    wr.setColor(Color.BLUE);
    wr.xTickMarks = false;
    wr.xTickValues = false;
    wr.xUnits = false;
    wr.xLabel = false;

    wr.setMinY(waveData.min());
    wr.setMaxY(waveData.max());
    wr.setWave(new SliceWave(waveData));
    wr.setViewTimes(startTimeLocal, endTimeLocal, config.timeZone.getDisplayName());
    wr.setLocation(50, 20, config.dimension.width - 80, waveHeight);
    wr.update();
    plot.addRenderer(wr);

    SpectrogramRenderer sr = new SpectrogramRenderer(new SliceWave(waveData));

    sr.setLogPower(true);
    sr.setMinFreq(0);
    sr.setMaxFreq(20);
    sr.setNfft(0);
    sr.setBinSize(256);
    sr.setOverlap(0.859375);
    sr.setMaxPower(120);
    sr.setMinPower(20);
    sr.setSpectrum(config.colorMap);

    sr.setViewTimes();
    sr.setTimeZone(config.timeZone.getDisplayName());
    sr.setLocation(50, waveHeight + 20, config.dimension.width - 80, spectrogramHeight - 50);
    sr.update();

    plot.addRenderer(sr);
    try {
      plot.writePNG(config.outputFile);
    } catch (PlotException e) {
      throw new UtilException("Cannot write file. (" + e.getLocalizedMessage() + ")");
    }
  }
}
