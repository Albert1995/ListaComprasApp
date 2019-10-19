package br.pucpr.appdev.listacomprasapp.webservices;

import java.util.List;

import br.pucpr.appdev.listacomprasapp.Model.APIResponse;
import br.pucpr.appdev.listacomprasapp.Model.SubCategoria;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SubCategoriaService {

    @GET("/subCategorias/{id}")
    Call<List<SubCategoria>> getAll(@Path("id") String id);

    @POST("/subCategorias")
    Call<APIResponse> create(@Body SubCategoria subCategoria);

    @DELETE("/subCategorias/{id}")
    Call<APIResponse> delete(@Path("id") String id);

}
