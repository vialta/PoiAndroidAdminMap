package qualteh.com.androidadminmap.API;

import qualteh.com.androidadminmap.Model.AdminMapModel;
import qualteh.com.androidadminmap.Model.JsonModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Virgil Tanase on 29.04.2016.
 */
public interface IAdminMap {

    @GET("/services/geocoder/reverseWithID")
    public Call<AdminMapModel> getMapData ( @Query("lon") double lng, @Query("lat") double lat);

    @POST("/services/geocoder/upsert")
    public Call<JsonModel> createMarker( @Body JsonModel jsonModel);

    @POST("/services/geocoder/upsert")
    public Call<JsonModel> editMarker(@Body JsonModel jsonModel);

    @POST("/services/geocoder/upsert")
    public Call<JsonModel> deleteMarker(@Body JsonModel jsonModel);



}
