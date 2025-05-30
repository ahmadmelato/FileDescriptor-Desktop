package data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import model.ResponeBody;
import model.UserModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

/**
 *
 * @author ahmad
 */
public class ClientAPI {

    public static int OK = 200;
    public static int Deny = 400;
    public static int Filed = 0;
    public static int Run = -1;

    public static final String BASE_URL = data.Utilities.getBaseUrl();
    private final InterfaceAPI interfaceAPI;
    public static ClientAPI clientAPI;
    public TokenInterceptor tokenInterceptor;
    private static Socket socket;

    public ClientAPI() {
        tokenInterceptor = new TokenInterceptor();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        interfaceAPI = retrofit.create(InterfaceAPI.class);
    }

    public ClientAPI(String token_p) {

        tokenInterceptor = new TokenInterceptor(token_p);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        interfaceAPI = retrofit.create(InterfaceAPI.class);
    }

    public static Socket getSocket() {
        if (socket == null) {
            try {
                socket = IO.socket(BASE_URL);
            } catch (URISyntaxException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return socket;
    }

    public static void socketConnect() {
        if (socket == null) {
            try {
                socket = IO.socket(BASE_URL);
            } catch (URISyntaxException ex) {
                System.err.println(ex.getMessage());
            }
        }
        if (socket != null && !socket.connected()) {
            socket.connect();
        }
    }

    public static void socketDisconnect() {
        if (socket != null && !socket.connected()) {
            socket.disconnect();
        }
    }

    public static String parseError(Response<?> response) {
        try {
            assert response.errorBody() != null;
            String json = response.errorBody().string();
            Type listType = new TypeToken<List<Map<String, String>>>() {
            }.getType();
            try {
                List<Map<String, String>> list = new Gson().fromJson(json, listType);
                Map<String, String> map = new HashMap<>();
                for (Map<String, String> item : list) {
                    map.putAll(item);
                }
                return map.get("ar");
            } catch (JsonSyntaxException ex) {
                return json;
            }
        } catch (IOException | NullPointerException | AssertionError e) {
            return e.getMessage();
        }
    }

    public static void setClientAPIToken(String token_p) {
        clientAPI = new ClientAPI(token_p);
    }

    public static ClientAPI getClientAPI() {
        if (clientAPI == null) {
            clientAPI = new ClientAPI();
        }
        return clientAPI;
    }

     public Call<ResponseBody> downloadFile(String fileUrl) {
        return interfaceAPI.downloadFile(fileUrl);
    }
    
    public Call<UserModel> login(String user_name_p, String password_p) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("user_name_p", user_name_p);
        queryMap.put("password_p", password_p);
        return interfaceAPI.login(queryMap);
    }

    public Call<ResponeBody> get_receive_files() {
        return interfaceAPI.get_receive_files();
    }
    
    public Call<ResponeBody> get_all_users() {
        return interfaceAPI.get_all_users();
    }

    public Call<ResponseBody> uploadFile(File file,String scert_key_p,List<Integer> usList) {
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", URLEncoder.encode(file.getName(), "UTF-8"), requestFile);
            RequestBody scertKeyP = RequestBody.create(MediaType.parse("text/plain"), scert_key_p);
            RequestBody usersid = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(usList));
            return interfaceAPI.uploadFile(body, scertKeyP,usersid);
        } catch (UnsupportedEncodingException ex) {
            System.getLogger(ClientAPI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
    
    public Call<ResponseBody> file_received(long file_id_p) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("file_id_p", file_id_p);
        return interfaceAPI.file_received(queryMap);
    }

}
