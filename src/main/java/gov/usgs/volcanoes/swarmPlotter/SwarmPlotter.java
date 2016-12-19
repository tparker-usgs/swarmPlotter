package gov.usgs.volcanoes.swarmPlotter;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.volcanoes.core.args.ArgumentException;
import gov.usgs.volcanoes.core.util.UtilException;
import gov.usgs.volcanoes.swarmPlotter.plotter.Plotter;

public class SwarmPlotter {
  static {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  /** my logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(SwarmPlotter.class);

  private Plotter plotter;

  /**
   * Class constructor.
   *
   * @param configFile my config file
   */
  public SwarmPlotter(final SwarmPlotterArgs config) {
    LOGGER.info("Launching Swarm Plotter ({})", Version.VERSION_STRING);
    
    plotter = config.plotType.getPlotter(config);
  }

  public void plot() throws UtilException {
    plotter.plot();
  }

  /**
   * Where it all begins.
   *
   * @param args command line args
   * @throws UtilException 
   * @throws ArgumentException when command line cannot be parsed
   * @throws Exception when things go wrong
   */
  public static void main(final String[] args) throws UtilException, ArgumentException {
    SwarmPlotterArgs config = null;
      config = new SwarmPlotterArgs(args);
      if (!config.help) {
        final SwarmPlotter swarmPlotter = new SwarmPlotter(config);
        swarmPlotter.plot();

        // TODO: Trace down lingering threads and get rid of exit call.
        System.exit(0);
      }
  }
}