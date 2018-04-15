package station.weather.study;

import java.io.IOException;
import java.util.DoubleSummaryStatistics;
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

public class DisplayStations extends Application {

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/* Load Stations data from the txt file configured in constants.properties file */
		LoadData.readStations();
		/* Load Weather data from the txt file configured in constants.properties file. This should be called only after loading the stations data */
		LoadData.readWeather();
		
		/* Drawing the Line Chart using javaFX. */ 
		primaryStage.setTitle("Visualize Number of Stations");
		
		/*plotData provides the Daata series to be charted */
		final LineChart<String, Number> chart = plotData();
		chart.setLegendVisible(false);
		VBox.setVgrow(chart, Priority.ALWAYS);
		final VBox pane = new VBox(chart);
		

		chart.getData()
				.stream()
				.flatMap(el -> el.getData().stream())
				.forEach(
						d -> Tooltip.install(
								d.getNode(),
								new Tooltip(d.getXValue() + " @ "
										+ d.getYValue())));

		Scene scene = new Scene(pane, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/* This method provides the data to be plotted on LineChart */
	private LineChart<String, Number> plotData() {
		final XYChart.Series<String, Number> series = new Series<String, Number>();
		try {
			/* Chart will show the data ranging from year 1910 to 2017. This can be added to constants properties file as well */
			int startyear = 1910;
			int endyear = 2017;
			
			TreeMap<Integer, Integer> totalNumberOfStations = chartStationCountData(
					startyear, endyear);

			// Pass the TreeMap data created above to the XYChart.Sata
			for (Map.Entry<Integer, Integer> entry : totalNumberOfStations
					.entrySet()) {
				Integer key = entry.getKey();
				Integer value = entry.getValue();

				XYChart.Data<String, Number> data = new XYChart.Data<>(
						"" + key, value);
				series.getData().add(data);

			}

		} catch (Exception ex) {
			System.out.println("DisplayStations:LineChart:" + ex);
			
		}

		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Year");
		final DoubleSummaryStatistics summary = series
				.getData()
				.stream()
				.collect(
						Collectors.summarizingDouble(item -> item.getYValue()
								.doubleValue()));
		final NumberAxis yAxis = new NumberAxis("Count of stations", summary.getMin(),
				summary.getMax(), 25);
		final LineChart<String, Number> sc = new LineChart<>(xAxis, yAxis);
		sc.getData().add(series);

		return sc;
	}

	/* This method returns the TreeMap with the count of stations each year to be charted */
	private TreeMap<Integer, Integer> chartStationCountData(int startyear,
			int endyear) {
		/* Saving the count of stations with Year in a Treemap to be sorted by Year
		 * Initializing the TreeMap */

		TreeMap<Integer, Integer> totalNumberOfStations = new TreeMap<Integer, Integer>();
		for (int i = startyear; i < endyear; i++) {
			totalNumberOfStations.put(i, 0);
		}
		
		/* Incrementing the count values in TreeMap for each year as we loop thru all the stations */
		for (Object value : LoadData.stations.values()) {
			Station station = (Station) value;
			if (station != null) {
				for (int i = startyear; i < endyear; i++) {
					if (i >= station.getBegindate().getYear()
							&& i < +station.getEnddate().getYear()) {
						totalNumberOfStations.put(i,
								totalNumberOfStations.get(i) + 1);
					}
				}
			}
		}
		return totalNumberOfStations;
	}

}
