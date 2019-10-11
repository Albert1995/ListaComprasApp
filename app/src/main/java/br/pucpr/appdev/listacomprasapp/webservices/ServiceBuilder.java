package br.pucpr.appdev.listacomprasapp.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceBuilder {

    public static CategoriaService getCategoriaService () {
        Retrofit r = new Retrofit.Builder()
                .baseUrl("https://api-lista-compras.herokuapp.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return r.create(CategoriaService.class);
    }

}
