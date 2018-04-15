package station.weather.study;

import java.util.Arrays;

/*A class to hold the Weather data.*/
public class Weather {

	private int year;
	private int month = 0;
	private int day;
	private int hour;
	private int airTemp = 0;
	private int dewTemp;
	private int seaPressure;

	private Weather[] weatherData = null;

	public Weather(int year, int month, int day, int hour, int airtemp,
			int dewtemp, int seapressure) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.airTemp = airtemp;
		this.dewTemp = dewtemp;
		this.seaPressure = seapressure;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public Weather[] getWeatherdata() {
		return weatherData;
	}

	public void setWeatherdata(Weather[] weatherdata) {
		this.weatherData = weatherdata;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getAirtemp() {
		return airTemp;
	}

	public void setAirtemp(int airtemp) {
		this.airTemp = airtemp;
	}

	public int getDewtemp() {
		return dewTemp;
	}

	public void setDewtemp(int dewtemp) {
		this.dewTemp = dewtemp;
	}

	public int getSeapressure() {
		return seaPressure;
	}

	public void setSeapressure(int seapressure) {
		this.seaPressure = seapressure;
	}

	@Override
	public String toString() {
		return "Weather [year=" + year + ", month=" + month + ", day=" + day
				+ ", hour=" + hour + ", airTemp=" + airTemp + ", dewTemp="
				+ dewTemp + ", seaPressure=" + seaPressure + ", weatherData="
				+ Arrays.toString(weatherData) + "]";
	}
	

}
