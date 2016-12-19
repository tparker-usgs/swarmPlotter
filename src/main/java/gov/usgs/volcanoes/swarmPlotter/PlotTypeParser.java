/**
 * I waive copyright and related rights in the this work worldwide through the CC0 1.0 Universal
 * public domain dedication. https://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

package gov.usgs.volcanoes.swarmPlotter;

import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.StringParser;

/**
 * Parse a date from a command line argument.
 * 
 * @author Tom Parker
 */
public class PlotTypeParser extends StringParser {

  @Override
  public Object parse(String arg) throws ParseException {
    PlotType result = null;
    try {
      result = PlotType.valueOf(arg.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ParseException(
          "I don't know how to create a '" + arg + "' plot. Try one of: " + PlotType.types());
    }

    return result;
  }
}
