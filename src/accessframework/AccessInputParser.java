package accessframework;

import java.util.*;

public class AccessInputParser {
  InputEntry parseEntry (String str) {
      System.out.println ("Bing: " + str);
    InputEntry tmp = new InputEntry();
    StringTokenizer myToken = new StringTokenizer (str, "|");
    String txt;
    int counter = 0;
    
    while (myToken.hasMoreTokens()) {
      txt = myToken.nextToken();
      txt = txt.trim();
      switch (counter) {
        case 0:
          tmp.setTime (Long.parseLong (txt));
        break;
        case 1:
          tmp.setType (txt);
        break;
        case 2:
          tmp.setX (Integer.parseInt (txt));
        break;
        case 3:
          tmp.setY (Integer.parseInt (txt));
        break;
        case 4:
          tmp.setMisc (txt);
        break;
      }
      counter += 1;
    }

//    System.out.println ("Parsed: " + tmp);
    return tmp;
  }

  ArrayList<InputEntry> parseLog (ArrayList<String> str) {
    InputEntry tmp = null;
    ArrayList<InputEntry> inputToProcess = new ArrayList<InputEntry>();
    for (String s : str) {
      tmp = parseEntry (s);
      inputToProcess.add (tmp);
    }

    return inputToProcess;
  }
}