/**
 * I waive copyright and related rights in the this work worldwide through the CC0 1.0 Universal
 * public domain dedication. https://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

package gov.usgs.volcanoes.swarmPlotter;

import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.StringParser;

import gov.usgs.volcanoes.core.legacy.plot.color.Spectrum;

/**
 * Parse a date from a command line argument.
 * 
 * @author Tom Parker
 */
public class ColorMapParser extends StringParser {

  @Override
  public Object parse(String arg) throws ParseException {
    Spectrum result = null;
    try {
      result = ColorMapType.valueOf(arg.toUpperCase()).getColormap();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      throw new ParseException(
          "I don't know the '" + arg + "' colormap. Try one of: " + ColorMapType.types());
    }

    return result;
  }
}
