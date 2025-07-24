package com.diagantika.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.diagantika.Constant;

public class LoggerConfig {
    // Desktop: Folder user home/.namaproject/
    //
    //Android: Di folder data/data/namapackage/files
    private final FileHandle logFile = Gdx.files.local(Constant.LOG_PATH);

    private boolean writeToFile;

    private LogLevel level;

    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    public LoggerConfig configure(boolean toFile, LogLevel minLevel) {
       if (toFile){
           writeToFile =true;
       }else {
           writeToFile=false;
       }
        level = minLevel;

       return this;
    }

    public FileHandle getLogFile() {
        return logFile;
    }

    public boolean isWriteToFile() {
        return writeToFile;
    }

    public LogLevel getLevel() {
        return level;
    }
}
