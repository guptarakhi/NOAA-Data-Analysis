package station.weather.study;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReadStation {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /* This method parses the fixed length Station file line and returns a station else returns null if not able to parse */ 
    public static Station parse(String line) {
    	
        if (line.length() != 99)
        {
        	return null;
        }
        

        try {
        	/* array indicating the ending positions of the different fields */
			int columnEndingPosition[] = {6, 12, 42, 50, 56, 64, 73, 81, 90, 99};
			
			final String uSAF = line.substring(0, columnEndingPosition[0]);
			final String wBAN = line.substring(columnEndingPosition[0]+1, columnEndingPosition[1]);
			final String name = line.substring(columnEndingPosition[1] +1, columnEndingPosition[2]);
			final String ctryId = line.substring(columnEndingPosition[2] + 1, columnEndingPosition[3]);
			final String icaoId = line.substring(columnEndingPosition[3] + 1, columnEndingPosition[4]);
			final String lat = line.substring(columnEndingPosition[4] + 1, columnEndingPosition[5]);
			final String lon = line.substring(columnEndingPosition[5] + 1, columnEndingPosition[6]);
			final String elev = line.substring(columnEndingPosition[6] + 1, columnEndingPosition[7]);       
			final LocalDate begindate = LocalDate.parse(line.substring(columnEndingPosition[7] + 1, columnEndingPosition[8]), FORMATTER);
			final LocalDate enddate = LocalDate.parse(line.substring(columnEndingPosition[8] + 1, columnEndingPosition[9]), FORMATTER);
			final Station station = new Station(uSAF, wBAN, ctryId, name, icaoId, lat, lon, elev, begindate, enddate );
			
      
			return station;
		} catch (Exception e) {
			return null;
		}
    }	
}
