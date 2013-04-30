package accessframework;

public class InputEntry {
  private long time;
  private String type;
  private int x, y;
  String misc;
  String key;

  public void setKey (String k) {
    key = k;
  }

  public String getKey() {
      return key;
  }

  public void setTime (long t) {
    time = t;
  }

  public long getTime() {
    return time;
  }

  public void setType (String str) {
    type = str;
  }

  public String getType() {
    return type;
  }

  public void setX (int nx) {
    x = nx;
  }

  public int getX() {
    return x;
  }

  public void setY (int ny) {
    y = ny;
  }

  public int getY() {
    return y;
  }
  public void setMisc (String str) {
    misc = str;
  }

  public String getMisc() {
    return misc;
  }

  public String toString() {
    return getTime() + "|" + getType() + "|" + getX() + "|" + getY() + "|" +
            getMisc();
  }
}