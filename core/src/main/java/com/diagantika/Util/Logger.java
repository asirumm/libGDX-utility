package com.diagantika.Util;

import com.badlogic.gdx.Gdx;

import static java.lang.String.format;

/**
 * how to used :
 log = new Logger<>(Main.class, Bean.getLogConfigInstance());
 configure at Bean for output file or console both wheres to store file

 Note : file output belum sempurna, kita masih harus clear manual

 */
public class Logger<Clazz> {

    private String prefix;
    private LoggerConfig config;


    public Logger(Class<Clazz> clazz,LoggerConfig config) {
        prefix = clazz.getSimpleName();
        this.config = config;
    }

    public void error(String message, Throwable e,Object... args){
        log(LoggerConfig.LogLevel.ERROR,format(message,args),e);
    }

    public void info(String message,Object... args){
        log(LoggerConfig.LogLevel.INFO,format(message,args),null);
    }

    public void debug(String message,Object... args){
        log(LoggerConfig.LogLevel.DEBUG,format(message,args),null);
    }

    public void warn(String message,Object... args){
        log(LoggerConfig.LogLevel.WARN,format(message,args),null);
    }

    private void log(LoggerConfig.LogLevel logLevel,String message,Throwable e){

        // ordinal urutan posisi enum
        //   public enum LogLevel {
        //        DEBUG, => 1
        //        INFO, =>2
        //        WARN, => etc
        //        ERROR
        //    }
        if (logLevel.ordinal() < config.getLevel().ordinal()) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(logLevel);
        stringBuilder.append(" | [").append(prefix).append("] | - ")
            .append(message);

        if (e != null) {
            while (e != null) {
                stringBuilder.append(e.toString()).append("\n");
                for (StackTraceElement element : e.getStackTrace()) {
                    stringBuilder.append("\tat ").append(element).append("\n");
                }
                e = e.getCause();
                if (e != null) {
                    stringBuilder.append("Caused by: ");
                }
            }
        }

        // menulis file sesuai konfigurasi console / file
        if (config.isWriteToFile()){
            try {
                config.getLogFile().writeString(stringBuilder+ "\n", true); // Format newline disamakan
            } catch (Exception fileEx) {
                Gdx.app.error("ERROR", "Gagal menulis ke file log", fileEx);
            }

        }else {
            Gdx.app.log(logLevel.toString(), stringBuilder.toString());
        }
    }
}
