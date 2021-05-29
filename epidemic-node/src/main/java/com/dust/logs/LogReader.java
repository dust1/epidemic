package com.dust.logs;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Objects;

/**
 * 日志阅读器
 * 从日志的最后一行逐行往上读取日志
 */
public class LogReader implements Iterator<String>, Closeable {

    private final boolean exist;

    private final RandomAccessFile raf;

    private final long start;

    private long nextEnd;

    private String returnLine;


    public static LogReader create(String logPath) throws IOException {
        var file = new File(logPath);
        return new LogReader(logPath, file.exists());
    }

    private LogReader(String logPath, boolean exist) throws IOException {
        this.exist = exist;
        this.raf = new RandomAccessFile(logPath, "r");
        this.start = raf.getFilePointer();
        this.nextEnd = start + raf.length() - 1;
        this.returnLine = null;
        if (nextEnd >= 0) {
            raf.seek(nextEnd);
        }
    }

    @Override
    public boolean hasNext() {
        if (!exist || nextEnd < start) {
            return false;
        }
        try {
            if (raf.length() == 0) {
                return false;
            }
            int c = -1;
            while (nextEnd >= start) {
                c = raf.read();
                if (c == '\r' || c == '\n') {
                    returnLine = raf.readLine();
                    //越过换行符
                    nextEnd -= 1;
                    if (Objects.nonNull(returnLine))
                        break;
                }
                //往上读取
                nextEnd -= 1;
                if (nextEnd >= 0) {
                    raf.seek(nextEnd);
                    if (nextEnd == 0) {
                        returnLine = raf.readLine();
                    }
                }
            }
        } catch (IOException e) {
            Logger.systemLog.error(LogFormat.SYSTEM_ERROR_FORMAT, "读取日志文件失败", e.getMessage());
        }
        return Objects.nonNull(returnLine);
    }

    @Override
    public String next() {
        var result = returnLine;
        returnLine = null;
        return result;
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }
}
