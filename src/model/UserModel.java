package model;

import java.util.Date;

public class UserModel {

    public int id;
    public String user_name;
    public Integer center_id;
    public String center_name;
    public Date last_seen;
    public String token;

    public long getTimeStamp() {
        if (last_seen != null) {
            return last_seen.getTime();
        }
        return 0;
    }

}
