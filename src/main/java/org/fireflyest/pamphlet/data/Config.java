package org.fireflyest.pamphlet.data;

public class Config {
    
    public static boolean DEBUG;

    public static String LANGUAGE;

    public static boolean SQL;

    public static String URL;
    public static String USER;
    public static String PASSWORD;

    public static int SEASON;

    public static void setSeason(int season) {
        SEASON = season;
    }

    private Config() {
        
    }

}
