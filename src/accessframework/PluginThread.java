package accessframework;

import java.io.*;
import java.net.*;
import java.util.*;

public class PluginThread extends Thread {

    Socket socket;
    BufferedReader in;
    ObjectOutputStream out;
    AccessPlugin plugin;

    public PluginThread(Socket s, AccessPlugin plug) {
        InputStreamReader isr;

        plugin = plug;
        System.out.println("Creating new thread");

        socket = s;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Can't setup writer.");
            return;
        }

        try {
            isr = new InputStreamReader(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Can't setup readers.");
            return;
        }

        in = new BufferedReader(isr);

    }

    public void run() {
        String str;

        while (true) {
            str = readFromSocket();

            if (str == null) {
                continue;
            } else {
//                System.out.println(System.currentTimeMillis() + ":" + str);
            }
        }

    }

    public String readFromSocket() {
        String line = null;

        try {
            line = in.readLine();
            System.out.println (line);
            plugin.parseEntry (line);

        }
        catch (Exception ex) {
        }


        return line;
    }
}
