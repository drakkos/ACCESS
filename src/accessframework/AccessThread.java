package accessframework;

import java.io.*;
import java.net.*;
import java.util.*;

public class AccessThread extends Thread {

    Socket socket;
    BufferedReader in;
    ObjectOutputStream out;
    AccessEngine engine;

    public AccessThread(Socket s, AccessEngine e) {
        InputStreamReader isr;

        engine = e;
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
//            System.out.println (line);
            engine.parseEntry (line);

        }
        catch (Exception ex) {
        }


        return line;
    }
}
