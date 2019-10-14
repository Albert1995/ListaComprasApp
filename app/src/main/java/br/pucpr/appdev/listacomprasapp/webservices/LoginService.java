package br.pucpr.appdev.listacomprasapp.webservices;

import br.pucpr.appdev.listacomprasapp.Model.APIResponse;
import br.pucpr.appdev.listacomprasapp.Model.Auth;
import br.pucpr.appdev.listacomprasapp.Model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/user")
    Call<APIResponse> create(@Body Usuario user);

    @POST("/login")
    Call<Auth> login(@Body Usuario user);
}
