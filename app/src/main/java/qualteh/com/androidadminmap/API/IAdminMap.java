package qualteh.com.androidadminmap.API;

import qualteh.com.androidadminmap.Model.AdminMapModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Virgil Tanase on 29.04.2016.
 */
public interface IAdminMap {

    @GET("/services/geocoder/reverse")
public Call<AdminMapModel> callVersion( @Query("lon") double lng, @Query("lat") double lat);

}
