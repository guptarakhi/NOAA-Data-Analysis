package station.weather.study;

public class ReadWeather {

    /* This method parses the fixed length Weather file line and returns a weather object else returns null if not able to parse */
    public static Weather parse(String line) {
    	
        if (line.length() != 61)
        {
        	return null;
        }
        /* array indicating the ending positions of the different fields */
        int columnEndingPosition[] = {4, 7, 10, 13, 19, 25, 31};
        
        final int year = Integer.parseInt(line.substring(0, columnEndingPosition[0]));
        final int month = Integer.parseInt(line.substring(columnEndingPosition[0]+1, columnEndingPosition[1]));
        final int day = Integer.parseInt(line.substring(columnEndingPosition[1] +1, columnEndingPosition[2]));
        final int hour = Integer.parseInt(line.substring(columnEndingPosition[2] + 1, columnEndingPosition[3]));
        final int airtemp = Integer.parseInt(line.substring(columnEndingPosition[3] + 1, columnEndingPosition[4]).trim());
        final int dewpointtemp = Integer.parseInt(line.substring(columnEndingPosition[4] + 1, columnEndingPosition[5]).trim());
        final int sealevelpressure = Integer.parseInt(line.substring(columnEndingPosition[5] + 1, columnEndingPosition[6]).trim());
        
        final Weather weather = new Weather(year, month, day, hour, airtemp, dewpointtemp, sealevelpressure);
        
       
        return weather;
    }	
}
