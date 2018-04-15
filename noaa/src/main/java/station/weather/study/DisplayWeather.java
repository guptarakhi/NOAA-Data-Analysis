package station.weather.study;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DisplayWeather extends Application {
	private Station station;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/* Load Stations data from the txt file configured in constants.properties file */
		LoadData.readStations();
		
		/* Load Weather data from the txt file configured in constants.properties file. This should be called only after loading the stations data */
		LoadData.readWeather();
		station = LoadData.stations.get(LoadData.weatherFilename.substring(1, 13));
		if (station == null) {
			System.out.println("Station does not exist in look-up");
			return;
		}
		
		/* Drawing a line chart showing the avg temp for a station grouped by Year and month. This will help understand the temperature trend in any station/city */ 
		primaryStage.setTitle("Temperature trend for station "
				+ station.getName() + " Country:" + station.getCtryId() + " Latitude:" + station.getLat() + " Longitude:" + station.getLon());
		/* plotData method call returns the Linechart to be plotted */
		final LineChart<String, Number> chart = plotData();
		chart.setLegendVisible(false);

		VBox.setVgrow(chart, Priority.ALWAYS);
		final VBox pane = new VBox(chart);

		Scene scene = new Scene(pane, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/* This method provides the chart with all the data to be plotted as LineChart */
	private LineChart<String, Number> plotData() {
		final XYChart.Series<String, Number> series = new Series<String, Number>();
		try {
			/* Determine the Avg Tmp for Month and Year and add it to chart data */
			TreeMap<Integer, ArrayList<Double>> avgMonthlyEveryYear = chartAvgTmpData(series);
			
			// Tried to predict the temperature for 2018 based on data loaded till 2017 
			predictTempForNextYear(series, avgMonthlyEveryYear);
			
		} catch (Exception ex) {
			System.out.println("DisplayWeather:LineChart:" + ex);
		}
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Month and Year");
		final DoubleSummaryStatistics summary = series
				.getData()
				.stream()
				.collect(
						Collectors.summarizingDouble(item -> item.getYValue()
								.doubleValue()));
		final NumberAxis yAxis = new NumberAxis("Temperature",summary.getMin(),
				summary.getMax(), 1);
		final LineChart<String, Number> sc = new LineChart<>(xAxis, yAxis);
		sc.getData().add(series);
		/*Adding tool-tip */
		sc.getData()
				.stream()
				.flatMap(el -> el.getData().stream())
				.forEach(
						d -> Tooltip.install(
								d.getNode(),
								new Tooltip(d.getXValue() + " @ "
										+ d.getYValue())));		

		return sc;

	}

	/* Returns the TreeMap with data to be charted and also to the chart series */
	private TreeMap<Integer, ArrayList<Double>> chartAvgTmpData(
			final XYChart.Series<String, Number> series) {
		TreeMap<Integer, ArrayList<Double>> avgMonthlyEveryYear = new TreeMap<Integer, ArrayList<Double>>();
		List<Weather> weathers = station.getWeathers();
		
		/* Populate the collection with the avg temp by Month and Year */ 
		for (Weather weather : weathers) {
			if (weather != null) {
				averageTempByYearAndMonth(avgMonthlyEveryYear, weather);
			}

		}
		/* Pass the collection to the chart to draw */
		for (Map.Entry<Integer, ArrayList<Double>> entry : avgMonthlyEveryYear
				.entrySet()) {
			Integer key = entry.getKey();
			double finalAvgTemp = entry.getValue().get(0);
			String date = (key % 100) + "/" + (key / 100);
			XYChart.Data<String, Number> data = new XYChart.Data<>(""
					+ date, finalAvgTemp);
			series.getData().add(data);
		}
		return avgMonthlyEveryYear;
	}

/* This method assumes that the weather data is loaded till 2017 and then tries to predict the average temperature for 2018. 
 * We already have the avg mothly temperature from the previous years and it takes the average from this to predict for the next year. 
 * We can compare it with real temperatures to see if this prediction is anyway close to it.
*/
	private void predictTempForNextYear(
			final XYChart.Series<String, Number> series,
			TreeMap<Integer, ArrayList<Double>> avgMonthlyEveryYear) {
		ArrayList<Double> prediction = new ArrayList<Double>();
		for (int month = 1; month < 13; month++) {
			int startYear = 1995;
			int endYear = 2017;
			double temperatureSum = 0;
			ArrayList<Integer> yearsFound = new ArrayList<Integer>();
			for (int year = startYear; year <= endYear; year++) {
				int pastKey = year * 100 + month;
				if (avgMonthlyEveryYear.containsKey(pastKey)){
					if (yearsFound.indexOf(year) == -1){
						yearsFound.add(year);
					}
					temperatureSum += avgMonthlyEveryYear.get(pastKey).get(0);
				}
			}
			double avgTemp = temperatureSum / yearsFound.size();
			prediction.add(avgTemp);
			System.out.println("Prediction for month " + month + " of 2018: "
					+ avgTemp + " degrees of Celsius in " + station.getName());
			String date = "2018/" + month;
			XYChart.Data<String, Number> data = new XYChart.Data<>("" + date,
					avgTemp);
			series.getData().add(data);
		}
		System.out.println("==============\n");
	}

	/* This method calculates the average of air temperature by Month and Year for a weather and add it to the Treemap */
	private void averageTempByYearAndMonth(
			TreeMap<Integer, ArrayList<Double>> avgMonthlyEveryYear,
			Weather weather) {
		// Ensures a unique key for every year-month combination. Also keeps
		// sorting in the TreeMap in order
		int key = weather.getYear() * 100 + weather.getMonth();
		
		/* Divide by 10 due to the scale factor as mentioned in the technical document*/
		double temperature = ((double) weather.getAirtemp()) / 10; 
		
		/* temperature of -9999 means should be ignored */
		
		if (weather.getAirtemp() != -9999) { 
			if (!avgMonthlyEveryYear.containsKey(key)) {
				/* element 0 in the arraylist is the average temperature for
				 this key, and element 1 in the arraylist is the number of
				 data points seen so far */
				ArrayList<Double> avgTemp = new ArrayList<Double>();
				avgTemp.add(temperature);
				avgTemp.add((double) 1);
				avgMonthlyEveryYear.put(key, avgTemp);
			} else {
				ArrayList<Double> value = avgMonthlyEveryYear.get(key);
				/* Calculate the new Avg */
				double newAvgTemp = (value.get(0) * value.get(1) + temperature)
						/ (value.get(1) + 1);
				value.set(0, newAvgTemp);
				value.set(1, value.get(1) + 1);
			}
		}
	}

}
