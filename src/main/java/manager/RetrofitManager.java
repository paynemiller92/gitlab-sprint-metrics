package manager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Manager for {@link Retrofit}, the HTTP Networking tool that executes
 * the GitLab requests.
 */

public class RetrofitManager {
    private Retrofit retrofit;
    private Properties properties;
    private static RetrofitManager instance;

    /**
     * Retrieves the instance of the singleton manager using the configuration
     * found within the config.properties {@link Properties} file.
     *
     * @param properties the {@link Properties} object.
     * @return the singleton instance of the manager.
     */

    public static RetrofitManager getInstance(Properties properties) {
        if (instance == null) {
            instance = new RetrofitManager(properties);
        }
        return instance;
    }

    private RetrofitManager(Properties properties) {
        this.properties = properties;
        constructRetrofitFromProperties();
    }

    /**
     * Retrieves the {@link Retrofit} instance.
     *
     * @return the instance of Retrofit.
     */

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private void constructRetrofitFromProperties() {
        String baseUrl = properties.getProperty("baseUrl");
        String token = properties.getProperty("token");

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(constructHttpClient(token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient constructHttpClient(String token) {
        return new OkHttpClient.Builder()
                .addInterceptor(constructInterceptor(token))
                .build();
    }

    private Interceptor constructInterceptor(final String token) {
        return new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .addHeader("PRIVATE-TOKEN", token)
                        .build();
                return chain.proceed(newRequest);
            }
        };
    }
}
