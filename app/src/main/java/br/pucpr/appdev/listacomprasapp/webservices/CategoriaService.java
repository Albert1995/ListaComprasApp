package br.pucpr.appdev.listacomprasapp.webservices;

import java.util.List;

import br.pucpr.appdev.listacomprasapp.Model.APIResponse;
import br.pucpr.appdev.listacomprasapp.Model.Categoria;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoriaService {

    @GET("/categorias")
    Call<List<Categoria>> getAll();

    @POST("/novaCategoria")
    Call<APIResponse> create(@Body Categoria categoria);

    @DELETE("/excluirCategoria/{id}")
    Call<APIResponse> delete(@Path("id") String id);


}
