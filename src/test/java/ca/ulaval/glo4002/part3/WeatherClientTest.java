package ca.ulaval.glo4002.part3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherClientTest {

    private static final String WEATHER_CLIENT_MESSAGE = "30;4;320";

    private WeatherClient weatherClient;

    @Test
    void givenWebsiteResponds_whenFetchingWeather_thenShouldReturnWeather() {
        weatherClient = new TestableWeatherClient(WEATHER_CLIENT_MESSAGE);

        Weather weather = weatherClient.fetchWeather();

        assertEquals(30, weather.temperature());
        assertEquals(4, weather.windSpeed());
        assertEquals(320, weather.windDirection());
    }

    @Test
    void givenWebsiteIsDown_whenFetchingWeather_thenShouldThrowException() {
        weatherClient = new TestableWeatherClient();

        Executable call = () -> weatherClient.fetchWeather();

        assertThrows(WeatherUnavailableException.class, call);
    }

}

class TestableWeatherClient extends WeatherClient {

    private final String returnWeatherString;

    public TestableWeatherClient() {
        this("");
    }

    public TestableWeatherClient(String returnWeatherString) {
        this.returnWeatherString = returnWeatherString;
    }

    @Override
    public String fetchWeatherString() {
        return returnWeatherString;
    }
}
