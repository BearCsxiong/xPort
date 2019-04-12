package me.csxiong.library.di.module;


import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import me.csxiong.library.BuildConfig;
import me.csxiong.library.integration.http.HttpLogger;
import me.csxiong.library.integration.http.JsonConverterFactory;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : dagger2 + retrofit
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/21 created by csxiong
 * |--------------------------------------------------------------------------------
 */
@Module
public class ClientModule {

    /**
     * provide {@link Retrofit} base on OkHttpClient
     *
     * @param application
     * @param configuration
     * @param builder
     * @param client
     * @param httpUrl
     * @return {@link Retrofit}
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application,
                             @Nullable RetrofitConfiguration configuration,
                             Retrofit.Builder builder,
                             GsonBuilder gsonBuilder,
                             OkHttpClient client,
                             HttpUrl httpUrl,
                             ExecutorService executorService) {

        builder.baseUrl(httpUrl)
                .client(client);
        builder.callbackExecutor(executorService)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JsonConverterFactory.create(gsonBuilder.create()));
        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }
        return builder.build();
    }

    /**
     * provide {@link OkHttpClient}
     *
     * @param application
     * @param cacheFile
     * @param builder
     * @param configuration
     * @return {@link OkHttpClient}
     */
    @Singleton
    @Provides
    OkHttpClient provideAPIClient(Application application,
                                  File cacheFile,
                                  OkHttpClient.Builder builder,
                                  ConnectionPool pool,
                                  @Nullable OkhttpConfiguration configuration) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger("HttpJson"));
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(httpLoggingInterceptor);
        }
        builder.connectionPool(pool);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        if (configuration != null) {
            configuration.configOkhttp(application, builder);
        }
        return builder.build();
    }

    /**
     * Default threadpool
     * <p>
     * cachethreadpool {https://www.cnblogs.com/zhujiabin/p/5404771.html}
     *
     * @return
     */
    @Singleton
    @Provides
    ExecutorService provideExecutor() {
        return Executors.newCachedThreadPool();
    }

    /**
     * RxJava2 bask Scheduler
     *
     * @param service
     * @return
     */
    @Singleton
    @Provides
    Scheduler provideScheduler(ExecutorService service) {
        return Schedulers.from(service);
    }

    /**
     * for multi okHttpClient
     *
     * @return
     */
    @Singleton
    @Provides
    ConnectionPool provideConnectionPool4OkHttpClient() {
        return new ConnectionPool(10, 10, TimeUnit.SECONDS);
    }

    /**
     * gson builder
     * TODO to prepare
     *
     * @param application
     * @param configuration
     * @param builder
     * @return
     */
    @Singleton
    @Provides
    PrettyFormatStrategy provideFormatStrategy(Application application,
                                               @Nullable LoggerConfiguration configuration,
                                               PrettyFormatStrategy.Builder builder) {
        if (configuration != null) {
            configuration.configLogger(application, builder);
        } else {
            builder.tag("Logger");
        }
        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    PrettyFormatStrategy.Builder provideFormatStrategyBuilder() {
        return PrettyFormatStrategy.newBuilder();
    }

    @Singleton
    @Provides
    GsonBuilder provideGsonBuilder(Application application,
                                   @Nullable GsonConfiguration gsonConfiguration) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (gsonConfiguration != null) {
            gsonConfiguration.configGson(application, gsonBuilder);
        }
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        // Register an adapter to manage the date types as long values
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        gsonBuilder.registerTypeAdapter(boolean.class, new TypeAdapter<Boolean>() {
            @Override
            public void write(JsonWriter out, Boolean value) throws IOException {

            }

            @Override
            public Boolean read(JsonReader in) throws IOException {
                JsonToken peek = in.peek();
                switch (peek) {
                    case BOOLEAN:
                        return in.nextBoolean();
                    case NULL:
                        in.nextNull();
                        return null;
                    case NUMBER:
                        return in.nextInt() != 0;
                    case STRING:
                        return Boolean.parseBoolean(in.nextString());
                    default:
                        throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
                }
            }
        });

        gsonBuilder.registerTypeAdapter(Integer.class, new TypeAdapter<Integer>() {
            @Override
            public void write(JsonWriter out, Integer value) throws IOException {
                out.value(String.valueOf(value));
            }

            @Override
            public Integer read(JsonReader in) throws IOException {
                try {
                    int s = in.nextInt();
                    return s;
                } catch (Exception e) {
                    in.skipValue();
                    return 0;
                }
            }
        });

        gsonBuilder.registerTypeAdapter(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                String string;
                try {
                    string = json.getAsString();
                } catch (Exception e) {
                    return "";
                }
                return string;
            }
        });
        return gsonBuilder;
    }

    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkhttpConfiguration {
        void configOkhttp(Context context, OkHttpClient.Builder builder);
    }

    public interface LoggerConfiguration {
        void configLogger(Context context, PrettyFormatStrategy.Builder builder);
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }

    public static SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory url our all-trusting manager
        return sslContext
                .getSocketFactory();
    }

}
