# 🌦️ Weather App

A simple Android Weather App that displays real-time weather information using the OpenWeather API. It supports user authentication with Firebase, modular UI with Fragments, and uses SQLite to store user preferences locally. API communication is handled using Retrofit.

---

## 📱 Features

- 🔐 Firebase Authentication (Email/Password & Google Sign-In)
- 🌍 Search weather by city name
- 📍 Current weather and forecast display
- 🔄 Retrofit-based network client
- 🧩 Fragment-based UI architecture
- 🌡️ User preference for temperature units (Celsius / Fahrenheit) stored via SQLite

---

## 🛠️ Tech Stack

| Technology           | Description                          |
|----------------------|--------------------------------------|
| **Kotlin**            | Primary programming language         |
| **Firebase Auth**     | User authentication                  |
| **OpenWeather API**   | Real-time weather data               |
| **Retrofit**          | HTTP client for API calls            |
| **SQLite**            | Local data storage for preferences   |
| **Fragments**         | UI modularity                        |
| **ViewModel + LiveData** | Lifecycle-aware data handling        |

---

## 🧩 App Modules

- **MainActivity** – Handles Firebase login/register and hosts all fragments after authentication.
- **CurrentWeatherFragment** – Shows real-time weather for a searched city.
- **ForecastFragment** – Displays upcoming weather forecast.
- **SettingsFragment** – Allows users to select unit preference (Celsius/Fahrenheit), saved via SQLite.

---

## 🔧 Setup Instructions

1. **Clone this repository**
    ```bash
   git clone https://github.com/yourusername/weather-app.git
   ```

2. **Get OpenWeather API Key**
   - Register at https://openweathermap.org/
   - Open `gradle.properties` and add your key like:
        ```bash
        OPEN_WEATHER_API_KEY="your_api_key_here"
        ```
   - In your OpenWeatherService module or RetrofitClient API or module, access it via:
     ```kotlin
     BuildConfig.OPEN_WEATHER_API_KEY
     ```
   - Make sure your `build.gradle` (app-level) includes:
     ```groovy
     buildConfigField "String", "OPEN_WEATHER_API_KEY", OPEN_WEATHER_API_KEY
     ```
3. **Set Up Firebase**
   - Add your Android project to Firebase Console 
   - Download google-services.json and place it in the app/ folder 
   - Enable Email/Password and Google Sign-In in the Firebase console
4.  **Build and Run**
    - Open the project in Android Studio 
    - Sync Gradle and run the app on a device or emulator

## 🚀 Future Improvements
- Weather alerts with push notifications 
- Voice-based city search 
- Dark mode and accessibility enhancements 
- Cloud syncing of preferences with Firebase Realtime DB or Firestore

## 📸 Screenshots
- You can view the Output from the Outputimages folder.
