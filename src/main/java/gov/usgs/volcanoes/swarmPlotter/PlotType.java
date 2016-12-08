package gov.usgs.volcanoes.swarmPlotter;

import java.util.Arrays;

/**
 * Enumerate known plot types.
 * 
 * @author Tom Parker
 */
public enum PlotType {
    HELI, 
    WAVE;

    public static String types() {
      StringBuffer sb = new StringBuffer();
      for (PlotType type : Arrays.asList(values())) {
        sb.append(type.toString().toLowerCase() + ", ");
      }
      return  sb.substring(0, sb.length()-2);
    }
}
