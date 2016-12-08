package gov.usgs.volcanoes.swarmPlotter;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.volcanoes.core.configfile.ConfigFile;
import gov.usgs.volcanoes.swarmPlotter.SwarmPlotterArgs;
import gov.usgs.volcanoes.swarmPlotter.Version;

public class SwarmPlotter {
  static {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  /** my logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(SwarmPlotter.class);


  /**
   * Class constructor.
   *
   * @param configFile my config file
   */
  public SwarmPlotter(final SwarmPlotterArgs config) {

    final long now = System.currentTimeMillis();
    LOGGER.info("Launching Swarm Plotter ({})", Version.VERSION_STRING);

  }

  /**
   * Where it all begins.
   *
   * @param args command line args
   * @throws Exception when things go wrong
   */
  public static void main(final String[] args) throws Exception {
    final SwarmPlotterArgs config = new SwarmPlotterArgs(args);

    if (!config.help) {
      final SwarmPlotter swarmPlotter = new SwarmPlotter(config);      
    }
  }
}
