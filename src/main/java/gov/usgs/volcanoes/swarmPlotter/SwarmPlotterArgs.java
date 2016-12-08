package gov.usgs.volcanoes.swarmPlotter;
/**
 * I waive copyright and related rights in the this work worldwide through the CC0 1.0 Universal
 * public domain dedication. https://creativecommons.org/publicdomain/zero/1.0/legalcode
 */


import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import gov.usgs.volcanoes.core.args.Args;
import gov.usgs.volcanoes.core.args.Arguments;
import gov.usgs.volcanoes.core.args.decorator.ConfigFileArg;
import gov.usgs.volcanoes.core.args.decorator.CreateConfigArg;
import gov.usgs.volcanoes.core.args.decorator.DateRangeArg;
import gov.usgs.volcanoes.core.args.decorator.VerboseArg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Argument processor for Pensive.
 *
 * @author Tom Parker
 */
public class SwarmPlotterArgs {
  private static final Logger LOGGER = LoggerFactory.getLogger(SwarmPlotterArgs.class);

  private static final String PROGRAM_NAME = "java -jar swarmplotter.jar";
  private static final String EXPLANATION = "I am the Swarm plotter\n";

  /** format of time on cmd line */
  public static final String INPUT_TIME_FORMAT = "yyyyMMddHHmm";

  private static final Parameter[] PARAMETERS =
      new Parameter[] {new FlaggedOption("plotType", new PlotTypeParser(), JSAP.NO_DEFAULT,
          JSAP.REQUIRED, 't', "plotType", String.format("One of:  %s\n", PlotType.types()))};

  /** If true, log more. */
  public final boolean verbose;

  /** Earliest sample to plot. */
  public final long startTime;

  /** Latest sample to plot. */
  public final long endTime;

  /** my config file. */
  public final PlotType plotType;

  /** help requested */
  public final boolean help;
  /**
   * Class constructor.
   * 
   * @param commandLineArgs the command line arguments
   * @throws Exception when things go wrong
   */
  public SwarmPlotterArgs(final String[] commandLineArgs) throws Exception {
    Arguments args = null;
    args = new Args(PROGRAM_NAME, EXPLANATION, PARAMETERS);
    args = new DateRangeArg(INPUT_TIME_FORMAT, args);
    args = new VerboseArg(args);

    JSAPResult jsapResult = null;
    jsapResult = args.parse(commandLineArgs);

    help = jsapResult.getBoolean("help");
    LOGGER.debug("Setting: help={}", help);
    
    verbose = jsapResult.getBoolean("verbose");
    LOGGER.debug("Setting: verbose={}", verbose);

    plotType = (PlotType) jsapResult.getObject("plotType");
    LOGGER.debug("Setting: plotType={}", plotType);
    
    final Date startDate = jsapResult.getDate("startTime");
    if (startDate == null) {
      startTime = Long.MIN_VALUE;
    } else {
      startTime = jsapResult.getDate("startTime").getTime();
    }
    LOGGER.debug("Setting: startTime={}", startTime);

    final Date endDate = jsapResult.getDate("endTime");
    if (endDate == null) {
      endTime = Long.MIN_VALUE;
    } else {
      endTime = jsapResult.getDate("endTime").getTime();
    }
    LOGGER.debug("Setting: endTime={}", endTime);

  }
}
