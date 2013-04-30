package accessframework;

public class AccessContextFactory {
  public static OperatingSystemContext getOperatingContext (String platform) {
    // Need a way to tell distributions apart, for filesystem reasons.
    // This will do for now though.
    System.out.println ("Getting context for " + platform);
    
    if (platform.equalsIgnoreCase ("linux")) {
      System.out.println ("Getting Ubuntu context");
//      return new UbuntuContext();
    }
    else if (platform.equalsIgnoreCase ("mac os x")) { 
      System.out.println ("Getting OS X context");
//      return new MacOsXContext();
    }
    else if (platform.equalsIgnoreCase ("windows xp")) {
      System.out.println ("Getting Win XP Context");
      return new WindowsXpContext();
    }
    else if (platform.equalsIgnoreCase ("windows 7")) {
      System.out.println ("Getting Win 7 Context");
      return new WindowsXpContext();
    }

    return null;
  }
}