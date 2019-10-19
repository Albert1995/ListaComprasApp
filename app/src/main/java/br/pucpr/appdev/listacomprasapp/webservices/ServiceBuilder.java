package br.pucpr.appdev.listacomprasapp.webservices;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceBuilder {

    public static CategoriaService getCategoriaService (final String tokenRef) {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("token", tokenRef)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit r = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api-lista-compras.herokuapp.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return r.create(CategoriaService.class);
    }

    public static SubCategoriaService getSubCategoriaService (final String tokenRef) {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("token", tokenRef)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit r = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api-lista-compras.herokuapp.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return r.create(SubCategoriaService.class);
    }

    public static LoginService getUsuarioService () {
        Retrofit r = new Retrofit.Builder()
                .baseUrl("https://api-lista-compras.herokuapp.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return r.create(LoginService.class);
    }

}
