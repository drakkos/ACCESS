/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package accessframework;

import java.io.*;
import java.net.*;

/**
 *
 * @author michael
 */
public class AccessServer implements Runnable {

    ServerSocket mySocket;
    AccessEngine engine;

    public AccessServer(AccessEngine en) {
        engine = en;

        System.out.println ("Access Server Starting");
        
        try {
            mySocket = new ServerSocket (10001);
        }
        catch (Exception ex) {
            System.out.println ("Access Server Error");
            return;
        }

         System.out.println ("Access Server Started");

//         en.setupPlugins();
    }

    public void run() {
        Socket c;
        AccessThread st;
        InetAddress add;

        while (true) {
            try {
                c = mySocket.accept();

                add = c.getInetAddress();

                st = new AccessThread(c, engine);
                st.start();

            } catch (Exception ex) {
                System.out.println ("Exception: " + ex.getMessage());
            }
        }
    }
}
