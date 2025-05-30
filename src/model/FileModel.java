/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author ahmad
 */
public class FileModel {

    public long file_user_id;
    public long file_id;
    public String file_name;
    public long file_size;
    public String file_code;
    public String extension;
    public String scert_key;
    private Date create_timestamp;
    
    
    public String getFileAllName(){
        return file_name + "." + extension;
    }
    
    public String getCreateDate() {
        if (create_timestamp != null) {
            return new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss a",Locale.US).format(create_timestamp);
        }
        return "";
    }
}
