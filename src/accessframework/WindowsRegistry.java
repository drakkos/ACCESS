package accessframework;

import java.util.prefs.*;
import java.util.*;
import java.lang.reflect.*;

public class WindowsRegistry {

    private static final int KEY_READ = 0x20019;

    public String getKey(String reg, String key, String val) {
        //Retrieve a reference to the root of the user preferences tree
        Preferences root;
        Class clz;

        if (reg.equalsIgnoreCase ("HKEY_LOCAL_MACHINE")) {
            root = Preferences.systemRoot();
        }
        else {
            root = Preferences.userRoot();
        }

        clz = root.getClass();

        String vals = "No value for " + key;
        
        try {
    	    Class[] params1 = {byte[].class, int.class, int.class};
            final Method openKey = clz.getDeclaredMethod("openKey",
                params1);
            openKey.setAccessible(true);

            Class[] params2 = {int.class};
            final Method closeKey = clz.getDeclaredMethod("closeKey",
                params2);
            closeKey.setAccessible(true);

            final Method winRegQueryValue = clz.getDeclaredMethod(
                "WindowsRegQueryValueEx",int.class, byte[].class);
            winRegQueryValue.setAccessible(true);

            int hKey = (Integer) openKey.invoke(root,
                toByteEncodedString(key), KEY_READ, KEY_READ);
            
            byte[] valb = (byte[]) winRegQueryValue.invoke(
                root, hKey, toByteEncodedString(val));

            vals = (valb != null ? new String(valb).trim() : null);
            closeKey.invoke(root, hKey);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vals;
    }

    private static byte[]  toByteEncodedString(String str) {

        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
           result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
}