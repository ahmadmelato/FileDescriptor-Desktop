/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.MainJFrame;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import data.AESAlog;
import data.ClientAPI;
import data.FileDownloadProgressHelper;
import data.Working;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FileModel;
import model.ResponeBody;
import model.UserModel;
import okhttp3.ResponseBody;
import org.melato.dev.MutableLiveData.MutableLiveData;
import org.springframework.lang.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author ahmad
 */
public class MainViewModel {

    private static AESAlog aESAlog;
    public final MutableLiveData<Working> working = new MutableLiveData<>();
    public final static MutableLiveData<List<FileModel>> filesLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<UserModel>> usersLiveData = new MutableLiveData<>();
    public final MutableLiveData<Integer> progressValueLiveData = new MutableLiveData<>();

    public MainViewModel(UserModel model) {
        aESAlog = new AESAlog(model);
        setProgressOK();
        progressValueLiveData.setValue(0);
    }

    public MainViewModel() {
        setProgressOK();
        progressValueLiveData.setValue(0);
    }

    private void setProgressOK() {
        synchronized (working) {
            working.setValue(new Working(ClientAPI.OK, "جاهز"));
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
            working.setValue(new Working(ClientAPI.Run, "الرجاء الانتظار ..."));
        }
    }

    private void setProgressRun(String msg) {
        synchronized (working) {
            working.setValue(new Working(ClientAPI.Run, msg));
        }
    }

    private void sleeps() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainViewModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void get_receive_files() {
        setProgressRun();
        ClientAPI.getClientAPI().get_receive_files().enqueue(new Callback<ResponeBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponeBody> call, @NonNull Response<ResponeBody> response) {
                if (response.code() == ClientAPI.OK) {
                    assert response.body() != null;
                    if (response.body().toString() != null) {
                        try {
                            String decryptedJson = aESAlog.decrypt(response.body().toString());
                            Type listType = new TypeToken<List<FileModel>>() {
                            }.getType();
                            List<FileModel> fileList = new Gson().fromJson(decryptedJson, listType);
                            filesLiveData.setValue(fileList);
                        } catch (JsonSyntaxException e) {
                            filesLiveData.setValue(Collections.emptyList());
                        }
                    }
                    setProgressOK();
                } else {
                    setProgressDeny(Objects.requireNonNull(ClientAPI.parseError(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponeBody> call, @NonNull Throwable t) {
                setProgressFiled("فشلت العملية تاكد من الاتصال");
                System.err.println(t.getMessage());
            }
        });
    }

    public void file_received(long file_id_p) {
        setProgressRun();
        ClientAPI.getClientAPI().file_received(file_id_p).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == ClientAPI.OK) {
                    assert response.body() != null;
                    get_receive_files();
                } else {
                    setProgressDeny(Objects.requireNonNull(ClientAPI.parseError(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                setProgressFiled("فشلت العملية تاكد من الاتصال");
                System.err.println(t.getMessage());
            }
        });
    }

    public void get_all_users() {
        setProgressRun();
        ClientAPI.getClientAPI().get_all_users().enqueue(new Callback<ResponeBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponeBody> call, @NonNull Response<ResponeBody> response) {
                if (response.code() == ClientAPI.OK) {
                    assert response.body() != null;
                    if (response.body().toString() != null) {
                        try {
                            String decryptedJson = aESAlog.decrypt(response.body().toString());
                            Type listType = new TypeToken<List<UserModel>>() {
                            }.getType();
                            List<UserModel> userModels = new Gson().fromJson(decryptedJson, listType);
                            usersLiveData.setValue(userModels);
                        } catch (JsonSyntaxException e) {
                            usersLiveData.setValue(Collections.emptyList());
                        }
                    }
                    setProgressOK();
                } else {
                    setProgressDeny(Objects.requireNonNull(ClientAPI.parseError(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponeBody> call, @NonNull Throwable t) {
                setProgressFiled("فشلت العملية تاكد من الاتصال");
                System.err.println(t.getMessage());
            }
        });
    }

    public void uploadFile(File file, String scert_key_p, List<Integer> usList, javax.swing.JDialog jDialog) {
        setProgressRun();
        String decryptedScert_key_p = aESAlog.encrypt(scert_key_p);
        ClientAPI.getClientAPI().uploadFile(file, decryptedScert_key_p, usList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == ClientAPI.OK) {
                    assert response.body() != null;
                    setProgressOK();
                    jDialog.dispose();
                } else {
                    setProgressDeny(Objects.requireNonNull(ClientAPI.parseError(response)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                setProgressFiled("فشلت العملية تاكد من الاتصال");
                System.err.println(t.getMessage());
            }
        });
    }

    public void downloadFile(FileModel fileModel) {
        setProgressRun("0.00");
        progressValueLiveData.setValue(0);
        ClientAPI.getClientAPI().downloadFile("download/" + fileModel.file_code).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> rspns) {
                sleeps();
                if (rspns.isSuccessful()) {
                    ResponseBody responseBody = rspns.body();
                    if (responseBody != null) {
                        saveFile(responseBody, fileModel);
                    }
                } else {
                    System.err.println("حدث خطا اثناء تنزيل التحديث");
                    setProgressDeny("حدث خطا اثناء تنزيل التحديث");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable thrwbl) {
                setProgressFiled("فشلت العملية تاكد من الاتصال");
                System.err.println(thrwbl.getMessage());
            }
        });
    }

    private void saveFile(ResponseBody body, FileModel fileModel) {
        try {
//            System.out.println(targetPath);
            File file = new File(fileModel.getFileAllName());
            FileDownloadProgressHelper.ProgressListener progressListener = (bytesRead, contentLength, done) -> {
                // Update progress here
                float progress = (float) bytesRead / fileModel.file_size * 100;
                setProgressRun(String.format("%.1f", progress));
                progressValueLiveData.setValue((int) progress);
            };

            try (FileOutputStream outputStream = new FileOutputStream(file); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                FileDownloadProgressHelper.ProgressResponseBody progressResponseBody
                        = new FileDownloadProgressHelper.ProgressResponseBody(body, progressListener);
                bufferedOutputStream.write(progressResponseBody.bytes());
            }
            setProgressOK();
            file.deleteOnExit();
        } catch (IOException e) {
            setProgressDeny("حدث خطا اثناء تنزيل الملف");
            System.err.println(e.getMessage());
        } catch (Exception ex) {
            System.getLogger(MainViewModel.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            setProgressDeny("حدث خطا اثناء تنزيل الملف");
        }
    }

}
