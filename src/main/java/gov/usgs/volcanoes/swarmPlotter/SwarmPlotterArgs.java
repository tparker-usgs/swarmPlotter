package gov.usgs.volcanoes.swarmPlotter;
/**
 * I waive copyright and related rights in the this work worldwide through the CC0 1.0 Universal
 * public domain dedication. https://creativecommons.org/publicdomain/zero/1.0/legalcode
 */


import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import gov.usgs.volcanoes.core.args.Args;
import gov.usgs.volcanoes.core.args.ArgumentException;
import gov.usgs.volcanoes.core.args.Arguments;
import gov.usgs.volcanoes.core.args.decorator.DimensionArg;
import gov.usgs.volcanoes.core.args.decorator.TimeSpanArg;
import gov.usgs.volcanoes.core.args.decorator.TimeZoneArg;
import gov.usgs.volcanoes.core.args.decorator.VerboseArg;
import gov.usgs.volcanoes.core.legacy.plot.color.Spectrum;
import gov.usgs.volcanoes.core.time.TimeSpan;
import gov.usgs.volcanoes.swarm.data.SeismicDataSource;

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

  public static final String DEFAULT_HELI_ROW_SPAN = "30";
  public static final Map<PlotType, Dimension> DEFAULT_DIMENSION;
  public static final String DEFAULT_COLOR_MAP = "jet";
  private static final String dimensionDefaults;

  static {
    DEFAULT_DIMENSION = new HashMap<PlotType, Dimension>();
    DEFAULT_DIMENSION.put(PlotType.HELI, new Dimension(800, 800));
    DEFAULT_DIMENSION.put(PlotType.WAVE, new Dimension(750, 280));
    DEFAULT_DIMENSION.put(PlotType.SPECTRA, new Dimension(750, 280));
    DEFAULT_DIMENSION.put(PlotType.SPECTROGRAM, new Dimension(925, 280));

    StringBuffer sb = new StringBuffer();
    // This is intentional. All plot types must have a default dimension!
    for (PlotType type : PlotType.values()) {
      Dimension d = DEFAULT_DIMENSION.get(type);
      sb.append(type).append(": ").append(d.height).append(" x ").append(d.width).append("\n");
    }

    dimensionDefaults = sb.toString();
  }


  private static final Parameter[] PARAMETERS = new Parameter[] {};

  /** If true, log more. */
  public final boolean verbose;

  /** Time span of plot. */
  public final TimeSpan timeSpan;

  /** my config file. */
  public final PlotType plotType;

  /** help requested */
  public final boolean help;

  public final String channel;

  public final double heliRowSpan;

  public final boolean heliSuppressClip;

  public final boolean heliForceCenter;

  public final boolean plotLabel;

  public final Dimension dimension;

  public final TimeZone timeZone;

  public final SeismicDataSource seismicDataSource;

  public final Spectrum colorMap;

  public final String outputFile;

  /**
   * Class constructor.
   * 
   * @param commandLineArgs the command line arguments
   * @throws Exception when things go wrong
   */
  public SwarmPlotterArgs(final String[] commandLineArgs) throws ArgumentException {
    Arguments args = null;
    args = new Args(PROGRAM_NAME, EXPLANATION, PARAMETERS);
    args = new TimeSpanArg(INPUT_TIME_FORMAT, true, args);
    args = new VerboseArg(args);
    args = new TimeZoneArg(args);
    args = new DimensionArg(args);
    args.registerParameter(new FlaggedOption("plotType", new PlotTypeParser(), JSAP.NO_DEFAULT,
        JSAP.REQUIRED, 'p', "plotType", String.format("One of:  %s\n", PlotType.types())));
    args.registerParameter(new FlaggedOption("channel", JSAP.STRING_PARSER, JSAP.NO_DEFAULT,
        JSAP.REQUIRED, 'c', "channel", "channel as S_C_N_L\n"));
    args.registerParameter(new FlaggedOption("dataSource", new SeismicDataSourceParser(),
        JSAP.NO_DEFAULT, JSAP.REQUIRED, 's', "dataSource", "Seismic data source.\n"));
    args.registerParameter(new Switch("heliSuppressClip", JSAP.NO_SHORTFLAG, "heliSuppressClip",
        "Do not highlight clipping on helicorder."));
    args.registerParameter(new Switch("heliForceCenter", JSAP.NO_SHORTFLAG, "heliForceCenter",
        "Force center helicorder rows."));
    args.registerParameter(
        new FlaggedOption("heliRowSpan", JSAP.DOUBLE_PARSER, DEFAULT_HELI_ROW_SPAN,
            JSAP.NOT_REQUIRED, 'r', "heliRowSpan", "Length of heli row in minutes\n"));
    args.registerParameter(new Switch("plotLabel", 'l', "plotLabel", "Label helicorder plot."));
    args.registerParameter(new Switch("spectrogramWave", JSAP.NO_SHORTFLAG, "spectrogramWave",
        "Plot waveform above spectrogram."));
    args.registerParameter(new FlaggedOption("colormap", new ColorMapParser(), DEFAULT_COLOR_MAP,
        JSAP.NOT_REQUIRED, JSAP.NO_SHORTFLAG, "colormap", String.format("One of:  %s\n", ColorMapType.types())));
    args.registerParameter(
        new UnflaggedOption("outputFile", JSAP.STRING_PARSER, JSAP.REQUIRED, "Output file name\n"));

    JSAPResult jsapResult = null;

    jsapResult = args.parse(commandLineArgs);

    if (!jsapResult.success()) {
      LOGGER.error("Cannot parse command line. Try --help argument");
      throw new ArgumentException("Unable to parse command line.");
    }

    help = jsapResult.getBoolean("help");
    LOGGER.debug("Setting: help={}", help);

    verbose = jsapResult.getBoolean("verbose");
    LOGGER.debug("Setting: verbose={}", verbose);

    plotType = (PlotType) jsapResult.getObject("plotType");
    LOGGER.debug("Setting: plotType={}", plotType);

    timeSpan = (TimeSpan) jsapResult.getObject("timeSpan");
    LOGGER.debug("Setting: timeSpan={}", timeSpan);

    timeZone = (TimeZone) jsapResult.getObject("timeZone");
    LOGGER.debug("Setting: timeZone={}", timeZone);

    channel = jsapResult.getString("channel").replace('_', '$');

    heliRowSpan = jsapResult.getDouble("heliRowSpan") * 60;
    LOGGER.debug("Setting: heliRowSpan={}", heliRowSpan);

    heliSuppressClip = jsapResult.contains("heliSuppressClip");

    heliForceCenter = jsapResult.contains("heliForceCenter");
    plotLabel = jsapResult.contains("plotLabel");

    seismicDataSource = (SeismicDataSource) jsapResult.getObject("dataSource");
    LOGGER.debug("Setting: seismicDataSource={}", seismicDataSource);

    colorMap = (Spectrum) jsapResult.getObject("colormap");
    LOGGER.debug("Setting: colormap={}", colorMap);
    outputFile = jsapResult.getString("outputFile");
    LOGGER.debug("Setting: outputFile={}", outputFile);

    if (jsapResult.contains("dimension")) {
      dimension = (Dimension) jsapResult.getObject("dimension");
    } else {
      dimension = DEFAULT_DIMENSION.get(plotType);
    }
  }
}
