package station.weather.study;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Station {

	private String USAF;
	private String WBAN;
	private String ctryId;
	private String name;
	private String icaoId;
	private String lat;
	private String lon;
	private String elev;
	private LocalDate beginDate;
	private LocalDate endDate;
	private List<Weather> weathers = new ArrayList<>();

	/*A class to hold the Station metadata.*/
	public Station(String uSAF, String wBAN, String ctryId, String name,
			String icaoId, String lat, String lon, String elev,
			LocalDate begindate, LocalDate enddate) {
		super();
		USAF = uSAF;
		WBAN = wBAN;
		this.ctryId = ctryId;
		this.name = name;
		this.icaoId = icaoId;
		this.lat = lat;
		this.lon = lon;
		this.elev = elev;
		this.beginDate = begindate;
		this.endDate = enddate;
	}

	public List<Weather> getWeathers() {
		return weathers;
	}

	/* This will add a new weather data belonging to a station*/
	public void addWeather(Weather weather) {
		this.weathers.add(weather);
	}

	public String getUSAF() {
		return USAF;
	}

	public String getWBAN() {
		return WBAN;
	}

	public String getCtryId() {
		return ctryId;
	}

	public String getName() {
		return name;
	}

	public String getIcaoId() {
		return icaoId;
	}

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}

	public String getElev() {
		return elev;
	}

	public LocalDate getBegindate() {
		return beginDate;
	}

	public LocalDate getEnddate() {
		return endDate;
	}

	@Override
	public String toString() {
		return "Station [USAF=" + USAF + ", WBAN=" + WBAN + ", ctryId="
				+ ctryId + ", name=" + name + ", icaoId=" + icaoId + ", lat="
				+ lat + ", lon=" + lon + ", elev=" + elev + ", begindate="
				+ beginDate + ", enddate=" + endDate + "]";
	}

}
