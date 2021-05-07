package com.dust.storage;

import com.dust.core.NodeConfig;
import com.dust.grpc.kademlia.StoreRequest;
import com.dust.grpc.kademlia.StoreResponse;
import com.dust.storage.btree.BTreeManager;
import com.dust.storage.btree.DataNode;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.Optional;

public class FileStorageLayout extends StorageLayout {

    private static final String MD_SUFFIX = ".md";

    /**
     * 文件目录
     */
    private BTreeManager catalog;

    public FileStorageLayout(NodeConfig config) {
        super(config);
        catalog = BTreeManager.create(config.getOrderNum());
    }

    @Override
    public void load() throws IOException {
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }

        File[] mdFiles = dir.listFiles((f, name) -> name.endsWith(MD_SUFFIX));
        if (Objects.isNull(mdFiles) || mdFiles.length == 0) {
            return;
        }
        File minFile = mdFiles[0];
        for (File mdFile : mdFiles) {
            minFile = minFile.length() > mdFile.length() ? mdFile : minFile;
            RandomAccessFile raf = new RandomAccessFile(mdFile, "rw");
            raf.seek(0);
            final FileChannel channel = raf.getChannel();
            try (raf; channel) {
                while (raf.getFilePointer() < raf.length()) {
                    DataNode dataNode = DataNode.byFile(raf);
                    if (Objects.nonNull(dataNode))
                        catalog.insert(dataNode);
                }
            }
        }

        snapshot = new RandomAccessFile(minFile, "rw");
    }

    @Override
    public Optional<ByteBuffer> find(String fileId) throws IOException {
        return Optional.empty();
    }

    @Override
    public StoreResponse store(StoreRequest storeRequest) throws IOException {
        return null;
    }

}
