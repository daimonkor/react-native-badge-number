package com.github.amarcruz.rnshortcutbadge;

public class ShortcutBadgeException extends Exception{
    public static int NO_SUPPORT_LAUNCHER = 10000;
    public static int STANDARD = 1;
    int code;

    public ShortcutBadgeException(int code, String message){
        super(message);
        this.code = code;
    }

    public ShortcutBadgeException(int code, String message, Exception e) {
        super(message, e);
        this.code = code;
    }


    public int getCode(){
        return this.code;
   }

   @Override
   public String toString() {
       return String.format("code = %s, message = %s, exception = %s", this.getCode(), this.getMessage(), this.getCause());
   }
}
