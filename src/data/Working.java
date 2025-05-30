package data;

import java.awt.Color;

/**
 *
 * @author ahmad
 */
public class Working {

    private int connectionState;
    private String smg;
    private Color color;
    

    public Working(int connectionState, String msg) {
        smg = msg;
        this.connectionState = connectionState;
        if (connectionState == ClientAPI.OK) {
            color = MyColor.BackColor;
        } else if (connectionState == ClientAPI.Deny || connectionState == ClientAPI.Run) {
            color = MyColor.Yellow;
        } else if (connectionState == ClientAPI.Filed) {
            color = MyColor.Red;
        }
    }

    
    public boolean isSuccessful() {
        return (connectionState == ClientAPI.OK);
    }

    public boolean isRunning() {
        return (connectionState == ClientAPI.Run);
    }

    public String getsSmg() {
        return smg;
    }

    public Color getColor() {
        return color;
    }

    public int getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(int connectionState) {
        this.connectionState = connectionState;
    }

}
