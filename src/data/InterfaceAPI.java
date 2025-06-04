/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data;


import java.util.Map;
import model.UserModel;
import model.ResponeBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 *
 * @author oem
 */
public interface InterfaceAPI {

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @POST("/login")
    Call<UserModel> login(@Body Map<String, Object> queryMap);

    
    @GET("/get_receive_files")
    Call<ResponeBody> get_receive_files();
    
    @GET("/get_send_files")
    Call<ResponeBody> get_send_files();
    
    @GET("/get_all_users")
    Call<ResponeBody> get_all_users();
    
    @Multipart
    @POST("upload_file")
    Call<ResponseBody> uploadFile(
        @Part MultipartBody.Part file,
        @Part("scert_key_p") RequestBody scertKeyP,
        @Part("user_ids") RequestBody user_ids
    );
    
    @PUT("/file_received")
    Call<ResponseBody> file_received(@Body Map<String, Object> queryMap);

}
