package com.dust.fundation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.function.Function;

/**
 * 节点系统中通用的类
 * 主要承担的功能就是在整个存储节点层次的部分简单的功能，过于复杂的功能需要使用新的模块
 */
public class EpidemicUtils {

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算SHA1的值
     */
    public static String getSHA1(byte[] data) {
        if (Objects.isNull(messageDigest)) {
            return null;
        }
        byte[] cipherBytes = messageDigest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte c : cipherBytes) {
            sb.append(String.format("%02x", c));
        }
        return sb.toString();
    }


    /**
     * 在指定目录检查给定版本文件名的版本信息并确定当前版本是否兼容。
     * @param  versionPath 版本文件保存的路径
     * @param versionName 版本文件名称
     * @param isCompatibleVersion 如果本地保存版本，则该函数用于判断本地的版本与程序版本是否兼容
     * @param systemVersion 系统的版本号，在校验后会将次版本号写入其中
     * @throws IOException
     *      如果读取文件失败
     */
    public static void checkAndwriteVersion(String versionPath, String versionName,
                                            Function<Integer, Boolean> isCompatibleVersion,
                                            String systemVersion) throws IOException {
        File dir = new File(versionPath);
        if (!dir.exists()) {
            boolean mkdirCheck = dir.mkdirs();
            if (!mkdirCheck) {
                throw new IOException("创建文件夹失败：" + dir.getPath());
            }
        }
        if (!dir.isDirectory()) {
            throw new IOException("版本文件路径不是一个文件夹目录:" + versionPath);
        }

        File versionFile = new File(versionPath, versionName);
        if (versionFile.exists()) {
            CharBuffer text = CharBuffer.allocate((int) versionFile.length());
            try (var in = new FileReader(versionFile)) {
                int readLen = in.read(text);
                if (readLen < 0) {
                    throw new IOException("读取版本文件失败" + versionFile.getPath());
                }
            }
            int versionOnDisk = Integer.valueOf(text.toString());
            if (!isCompatibleVersion.apply(versionOnDisk)) {
                throw new IOException("本地的版本文件与当前系统并不兼容！" + versionName);
            }
        }
        final File tmpFile = new File(versionPath, versionName + "_tmp");
        try (var out = new FileWriter(tmpFile)) {
            out.write(systemVersion);
        }
        boolean renameResult = tmpFile.renameTo(versionFile);
        if (!renameResult) {
            System.out.println("版本文件重命名失败");
        }
    }


}
