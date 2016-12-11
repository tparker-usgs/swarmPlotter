package gov.usgs.volcanoes.swarmPlotter.plotter;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import gov.usgs.plot.HelicorderSettings;
import gov.usgs.plot.Plot;
import gov.usgs.plot.PlotException;
import gov.usgs.plot.data.SliceWave;
import gov.usgs.plot.data.Wave;
import gov.usgs.plot.render.wave.SliceWaveRenderer;
import gov.usgs.volcanoes.core.time.J2kSec;
import gov.usgs.volcanoes.core.util.UtilException;
import gov.usgs.volcanoes.swarmPlotter.SwarmPlotterArgs;

public class WavePlotter extends Plotter {

  private static final Logger LOGGER = Logger.getLogger(WavePlotter.class);

  protected Wave waveData;
  protected double startTimeLocal;
  protected double endTimeLocal;
  
  public WavePlotter(SwarmPlotterArgs config) {
    super(config);
  }
  
  protected void getData() {
    final String channel = config.channel;
    final double startTime = J2kSec.fromEpoch(config.timeSpan.startTime);
    final double endTime = J2kSec.fromEpoch(config.timeSpan.endTime);

    LOGGER.debug("getting data");
    config.seismicDataSource.establish();
    waveData = config.seismicDataSource.getWave(channel, startTime, endTime);
    
    long st = J2kSec.asEpoch(startTime);
    st += config.timeZone.getOffset(st);
    startTimeLocal = J2kSec.fromEpoch(st);

    waveData.setStartTime(startTimeLocal);
    long et = J2kSec.asEpoch(endTime);
    et += config.timeZone.getOffset(et);
    endTimeLocal = J2kSec.fromEpoch(et);

    waveData.detrend();
    waveData.invalidateStatistics();
    LOGGER.debug("got data");
    config.seismicDataSource.close();
  }

  @Override
  public void plot() throws UtilException {
    getData();
    Plot plot = new Plot();
    plot.setSize(config.dimension);
    plot.setBackgroundColor(new Color(0.97f, 0.97f, 0.97f));
    SliceWaveRenderer wr = new SliceWaveRenderer();

    wr.setColor(Color.BLUE);

    wr.setMinY(waveData.min());
    wr.setMaxY(waveData.max());
    wr.setWave(new SliceWave(waveData));
    wr.setViewTimes(startTimeLocal, endTimeLocal, config.timeZone.getDisplayName());
    wr.setLocation(50, 20, config.dimension.width - 80, config.dimension.height - 50);
    wr.update();
    
    plot.addRenderer(wr);
    try {
      plot.writePNG("wave.png");
    } catch (PlotException e) {
      throw new UtilException("Cannot write file. (" + e.getLocalizedMessage() + ")");
    }
  }
}
