package com.diagantika.Util;

public class Bean {
        // thread save, semua thread akan merujuk ke 1 instance yang sama
        private static volatile LoggerConfig config;

        public static LoggerConfig getLogConfigInstance(){

            if (config==null){
                config = new LoggerConfig().configure(false, LoggerConfig.LogLevel.DEBUG);
            }

            return config;
        }
}
