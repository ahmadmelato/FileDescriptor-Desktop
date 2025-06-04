/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author ahmad
 */
public class FileSendModel {

    public String id;
    public String file_name;
    public String file_size;
    public String extension;
    public String file_code;
    public String scert_key;
    public Date create_timestamp;
    public ArrayList<User> users;

    public String getFileAllName() {
        return file_name + "." + extension;
    }

    public String getCreateDate() {
        if (create_timestamp != null) {
            return new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss a", Locale.US).format(create_timestamp);
        }
        return "";
    }

    public class User {

        public int id;
        public String user_name;
        public int center_id;
        public String center_name;
        public Date create_timestamp;
        public Date recive_timestamp;

        public String getCreateDate() {
            if (create_timestamp != null) {
                return new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss a", Locale.US).format(create_timestamp);
            }
            return "";
        }

        public boolean isReciver() {
            return recive_timestamp !a= null;
        }
    }

}
