package com.example.weatheringo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatheringo.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchdata("kolkata")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    fetchdata(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return true
            }
        })
    }

    private fun fetchdata(cityname:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData("$cityname","da4c6a9012b7f9df8aadad4146883969","metric")
        response.enqueue(object :Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    val temprature = responseBody.main.temp.toString()
                    val overall = responseBody.weather[0].main
                    var current_time = LocalTime.now().hour
                    changebackground(overall.toString(),current_time.toInt())
                    binding.temprature.text = "$temprature °C"
                    binding.location.text = "${cityname.uppercase()}"
                }
            }

            private fun changebackground(weatherCondition:String,currenttime:Int) {
                if(currenttime>=5 && currenttime<=15){
                    binding.root.setBackgroundResource(R.drawable.dayy)
                }
                else if(currenttime>=16 && currenttime<=20){
                    binding.root.setBackgroundResource(R.drawable.sunset)
                    binding.temprature.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else if(currenttime>=21||currenttime<=4){
                    binding.root.setBackgroundResource(R.drawable.midnight)
                    binding.temprature.setTextColor(Color.parseColor("#FFFFFF"))
                }
             when(weatherCondition){
                 "Haze"-> {
                     binding.lottieAnimationView.setAnimation(R.raw.haze)
                     binding.lottieAnimationView.playAnimation()
                 }
                 "Mist"->{
                     binding.lottieAnimationView.setAnimation(R.raw.mist)
                     binding.lottieAnimationView.playAnimation()
                 }
                 "Clear"->{
                     if(currenttime>5 && currenttime<18) {
                         binding.lottieAnimationView.setAnimation(R.raw.sun)
                         binding.lottieAnimationView.playAnimation()
                     }
                     else{
                         binding.lottieAnimationView.setAnimation(R.raw.clearnight)
                         binding.lottieAnimationView.playAnimation()
                     }
                 }
                 "Drizzle"->{
                     binding.lottieAnimationView.setAnimation(R.raw.drizzle)
                     binding.lottieAnimationView.playAnimation()
                 }
                 "Rain"->{
                     binding.lottieAnimationView.setAnimation(R.raw.rain)
                     binding.lottieAnimationView.playAnimation()
                 }
                 "Thunderstorm"->{
                     binding.lottieAnimationView.setAnimation(R.raw.thunderstrom)
                     binding.lottieAnimationView.playAnimation()
                 }
                 "Snow"->{
                     binding.lottieAnimationView.setAnimation(R.raw.snow)
                     binding.lottieAnimationView.playAnimation()
                 }
                 "Clouds"->{
                     if(currenttime>5 && currenttime<18) {
                         binding.lottieAnimationView.setAnimation(R.raw.suncloud)
                         binding.lottieAnimationView.playAnimation()
                     }
                     else{
                         binding.lottieAnimationView.setAnimation(R.raw.cloudynight)
                         binding.lottieAnimationView.playAnimation()

                     }
                 }

                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}