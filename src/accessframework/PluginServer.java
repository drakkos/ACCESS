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
public class PluginServer implements Runnable {

    ServerSocket mySocket;
    AccessPlugin plug;

    public PluginServer(AccessPlugin en, int port) {
        plug = en;

        System.out.println ("Plugin Server Starting for " + en.getName());

        try {
            mySocket = new ServerSocket (port);
        }
        catch (Exception ex) {
            System.out.println ("Access Server Error");
            return;
        }

         System.out.println ("Access Server Started");


    }

    public void run() {
        Socket c;
        PluginThread st;
        InetAddress add;

        while (true) {
            try {
                c = mySocket.accept();

                add = c.getInetAddress();

                st = new PluginThread(c, plug);
                st.start();

            } catch (Exception ex) {
                return;
            }
        }
    }
}
