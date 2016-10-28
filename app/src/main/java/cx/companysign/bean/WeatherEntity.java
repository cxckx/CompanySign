package cx.companysign.bean;

/**
 * Created by cxcxk on 2016/4/14.
 */
public class WeatherEntity {

    private String week;
    private String dayImage;
    private String nightImage;
    private String temp;
    private String wind;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDayImage() {
        return dayImage;
    }

    public void setDayImage(String dayImage) {
        this.dayImage = dayImage;
    }

    public String getNightImage() {
        return nightImage;
    }

    public void setNightImage(String nightImage) {
        this.nightImage = nightImage;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }


    public WeatherEntity() {
    }
}
