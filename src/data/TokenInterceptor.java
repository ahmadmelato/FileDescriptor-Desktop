/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Response;

/**
 *
 * @author ahmad
 */
public class TokenInterceptor implements Interceptor {

    private String token;

    public TokenInterceptor() {
        token = "";
    }

    public TokenInterceptor(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder().addHeader("Accept","application/json")
                .addHeader("token_p", token)
                .addHeader("token_h", data.Utilities.generateHardwareHash())
                .build();
        return chain.proceed(request);
    }
}
