/**
 * I waive copyright and related rights in the this work worldwide through the CC0 1.0
 * Universal public domain dedication.
 * https://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

package gov.usgs.volcanoes.swarmPlotter;

import java.awt.Dimension;

import com.martiansoftware.jsap.ParseException;
import com.martiansoftware.jsap.StringParser;

import gov.usgs.volcanoes.swarm.data.DataSourceType;
import gov.usgs.volcanoes.swarm.data.SeismicDataSource;

/**
 * Parse a date from a command line argument.
 * 
 * @author Tom Parker
 */
public class SeismicDataSourceParser extends StringParser {

  @Override
  public Object parse(String arg) throws ParseException {
    SeismicDataSource dataSource = DataSourceType.parseConfig("unnamed;" + arg + ":15000:1");
    
    // TODO: parseConfig should throw an exception, not return null
    if (dataSource == null) {
      throw new ParseException("Cannot parse data soruce: " + arg);
    }
    
    return dataSource;
  }
}
