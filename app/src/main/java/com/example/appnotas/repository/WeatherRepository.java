package com.example.appnotas.repository;

import android.util.Log;

import com.example.appnotas.data.weather.RetrofitInstance;
import com.example.appnotas.data.weather.WeatherApiService;
import com.example.appnotas.data.weather.WeatherResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    public interface WeatherCallback {
        void onSuccess(WeatherResponse response);
        void onFailure(Throwable t);
    }

    public void getWeather(String city, String apiKey, WeatherCallback callback) {
        WeatherApiService apiService = RetrofitInstance.getApiService();
        Call<WeatherResponse> call = apiService.getCurrentWeather(city, apiKey, "metric");

        // Loguea la URL antes de encolar
        Log.d("Clima", "URL de petición: " + call.request().url());

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    // Extrae el cuerpo de error para ver el mensaje que devuelve OpenWeather
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Clima", "Código: " + response.code() + " / ErrorBody: " + errorBody);
                    callback.onFailure(new Exception("Respuesta fallida: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}