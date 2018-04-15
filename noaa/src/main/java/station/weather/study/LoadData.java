package station.weather.study;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Properties;

public class LoadData {

	public static Properties prop = new Properties();
	public static InputStream input = null;
	public static String stationFilename;
	public static String weatherFilename;
	public static HashMap<String, Station> stations = new HashMap<String, Station>();

	/*This method reads the stations information from the text file and create the stations collection */
	public static void readStations() throws IOException {
		try {
			// read teh properties file to fetch the txt file name having the stations metadata
			input = ReadStation.class
					.getResourceAsStream("constants.properties");

			// load a properties file
			prop.load(input);

			stationFilename = prop.getProperty("stationFileName");
			weatherFilename = prop.getProperty("weatherFileName");

			final InputStream in = ReadStation.class
					.getResourceAsStream(stationFilename);
			final Reader readerIn = new InputStreamReader(in);
			final LineNumberReader reader = new LineNumberReader(readerIn);
			while (reader.ready()) {
				final String line = reader.readLine();
				final Station station = ReadStation.parse(line);
				if (station != null) {
					stations.put(station.getUSAF() + "-" + station.getWBAN(),
							station);
				} else {
					/* Printing out the lines with line number in the file which were not able to parse to help debugging */
					System.out.println("Unable to parse line "
							+ reader.getLineNumber() + ":" + line);
				}
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*This method reads the weather information pertaining to a station from the text file and create the waethers collection for that station.
	 * As one file is supposed to have the data from a single station and the file name will have the Station USAF number 716230 (London Station) and WBAN number 99999 */	
	public static void readWeather() throws IOException {

		try {
			final InputStream in = ReadWeather.class
					.getResourceAsStream(weatherFilename);
			final Reader readerIn = new InputStreamReader(in);
			final LineNumberReader reader = new LineNumberReader(readerIn);
			while (reader.ready()) {
				final String line = reader.readLine();
				final Weather weather = ReadWeather.parse(line);
				/* This gives the station for which we read the weather data */
				Station fileStation = stations.get(weatherFilename.substring(1, 13));
				if (fileStation != null) {
					fileStation.addWeather(weather);
				} else {
					/* Printing out the lines with line number in the file which were not able to parse to help debugging */
					System.out.println("Unable to parse line "
							+ reader.getLineNumber() + ":" + line);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
