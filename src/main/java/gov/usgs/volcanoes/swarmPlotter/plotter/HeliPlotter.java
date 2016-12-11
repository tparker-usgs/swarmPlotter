package gov.usgs.volcanoes.swarmPlotter.plotter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import gov.usgs.plot.HelicorderSettings;
import gov.usgs.plot.PlotException;
import gov.usgs.plot.data.HelicorderData;
import gov.usgs.volcanoes.core.time.J2kSec;
import gov.usgs.volcanoes.core.util.UtilException;
import gov.usgs.volcanoes.swarmPlotter.SwarmPlotterArgs;

public class HeliPlotter extends Plotter {

  private static final Logger LOGGER = Logger.getLogger(HeliPlotter.class);
  private final HelicorderSettings settings;

  public HeliPlotter(SwarmPlotterArgs config) {
    super(config);
    settings = configureHeli(config);
  }

  @Override
  public void plot() throws UtilException {
    final String channel = settings.channel;
    final double startTime = settings.startTime;
    final double endTime = settings.endTime;

    LOGGER.debug("getting data");
    HelicorderData heliData =
        config.seismicDataSource.getHelicorder(channel, startTime, endTime, null);
    LOGGER.debug("got data");
    config.seismicDataSource.close();

    try {
      byte[] bytes = settings.createPlot(heliData).getPNGBytes();
      Path path = Paths.get(config.outputFile);
      Files.write(path, bytes);
    } catch (PlotException e) {
      throw new UtilException(e.getLocalizedMessage());
    } catch (IOException e) {
      throw new UtilException(e.getLocalizedMessage());
    }
  }

  private HelicorderSettings configureHeli(SwarmPlotterArgs config) {
    HelicorderSettings settings = new HelicorderSettings();
    settings.channel = config.channel;

    settings.endTime = J2kSec.fromEpoch(config.timeSpan.endTime);
    settings.startTime = J2kSec.fromEpoch(config.timeSpan.startTime);

    settings.timeChunk = config.heliRowSpan;
    settings.setSizeFromPlotSize(config.dimension.width, config.dimension.height);

    settings.showClip = !config.heliSuppressClip;
    settings.forceCenter = !config.heliForceCenter;

    settings.barRange = -1;
    settings.clipValue = -1;

    settings.timeZone = config.timeZone;

    settings.largeChannelDisplay = config.plotLabel;

    return settings;
  }

}
