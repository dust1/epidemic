package com.dust.epidemic.fs.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 追加写入的时候写入到哪里全部交给fs模块决定，因此DataNode的写入操作只需要给fs对应的数据即可
 * fs在写入完成之后告知DataNode写入后的具体信息，包括：fileId、ObjectNo、ObjectFileLength等
 */
@Getter
@Setter
public class WriteResult {

    private long length;

    private String fileId;

    private long objNo;

    private long version;

}
