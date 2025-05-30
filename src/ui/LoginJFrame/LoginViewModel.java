/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.LoginJFrame;

import data.ClientAPI;
import data.Working;
import java.util.Objects;
import model.UserModel;
import org.melato.dev.MutableLiveData.MutableLiveData;
import org.springframework.lang.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author ahmad
 */
public class LoginViewModel {

    public final MutableLiveData<Working> working = new MutableLiveData<>();
    public MutableLiveData<UserModel> userLiveData = new MutableLiveData<>();

    public LoginViewModel() {
        setProgressOK();
    }
    

    private void setProgressOK() {
        synchronized (working) {
            working.setValue(new Working(ClientAPI.OK,"جاهز"));
        }
    }

    private void setProgressFiled(String msg) {
        synchronized (working) {
            working.setValue(new Working(ClientAPI.Filed, msg));
        }
    }

    private void setProgressDeny(String msg) {
        synchronized (working) {
            working.setValue(new Working(ClientAPI.Deny, msg));
        }
    }

    private void setProgressRun() {
        synchronized (working) {
            working.setValue(new Working(ClientAPI.Run,"الرجاء الانتظار ..."));
        }
    }

    public void login(String user_name_p, String password_p) {
        setProgressRun();
        ClientAPI.getClientAPI().login(user_name_p, password_p).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.code() == ClientAPI.OK) {
                    assert response.body() != null;
                    setProgressOK();
                    UserModel userModel = response.body();
                    ClientAPI.setClientAPIToken(userModel.token);
                    userLiveData.setValue(userModel);
//                    saveData(context, userModel);
                } else {
                    setProgressDeny(Objects.requireNonNull(ClientAPI.parseError(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                setProgressFiled("فشلت العملية تاكد من الاتصال");
                System.err.println(t.getMessage());
            }
        });
    }

}
