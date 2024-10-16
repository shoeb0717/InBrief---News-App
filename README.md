# InBrief-News App

InBrief is a news application built with Kotlin that provides users with the latest news articles from various sources. The app utilizes Retrofit for network requests, Room for local database storage, and the NewsAPI for fetching news data.

## Features

- **Latest News**: Fetches and displays the latest news articles from various sources.
- **Offline Support**: Articles are stored in a local Room database for offline access.
- **User-Friendly Interface**: Simple and intuitive UI for seamless navigation.
- **Search Functionality**: Search for specific news articles by keywords.
- **Detail View**: View detailed information about each news article.

## Technologies Used

- **Kotlin**: The programming language used for building the app.
- **Retrofit**: A type-safe HTTP client for Android and Java to handle API requests.
- **Room Database**: A persistence library providing an abstraction layer over SQLite for local data storage.
- **NewsAPI**: The third-party API used to fetch news articles.
- 
## Getting Started

To get a copy of this project up and running on your local machine, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/shoeb0717/InBrief-News-App.git

2. **Open the Project**: Open the project in Android Studio.

3. **Create an API Key**:
   - Visit [NewsAPI.org](https://newsapi.org/).
   - Click on **Get API Key** or **Sign Up** if you don't have an account.
   - Fill out the registration form and submit.
   - After signing up, you will receive an API key in your account dashboard.

4. **Add Your API Key**:
   - Create a new file called `ApkiKey.kt` in the `app/src/main/java/com/learngram/mvvmnewsapp/utils` directory.
   - Add your NewsAPI key in the following format:
     ```
     class ApiKey {

      companion object{
        const val API_KEY=your_api_key
      }
      }
     ```

5. **Sync Gradle**: Make sure to sync your Gradle files.

6. **Run the App**: Connect your Android device or start an emulator and run the app.
