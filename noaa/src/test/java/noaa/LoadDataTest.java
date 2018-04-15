package noaa;

import java.io.IOException;

import org.junit.Test;

import station.weather.study.LoadData;


public class LoadDataTest {

    @Test
    public void testLoadStation() throws IOException{
		LoadData.readStations();
		assert LoadData.stations != null || LoadData.stations.size() > 0;
    }

}
