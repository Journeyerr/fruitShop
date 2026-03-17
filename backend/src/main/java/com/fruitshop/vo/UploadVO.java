package com.fruitshop.vo;

import lombok.Data;

/**
 * 文件上传结果
 */
@Data
public class UploadVO {
    
    /**
     * 文件访问URL
     */
    private String url;
    
    /**
     * 原始文件名
     */
    private String name;
    
    /**
     * 新文件名
     */
    private String filename;
    
    /**
     * 文件大小（字节）
     */
    private Long size;
    
    /**
     * 文件类型
     */
    private String contentType;
    
    public static UploadVO of(String url, String name, String filename, Long size, String contentType) {
        UploadVO vo = new UploadVO();
        vo.setUrl(url);
        vo.setName(name);
        vo.setFilename(filename);
        vo.setSize(size);
        vo.setContentType(contentType);
        return vo;
    }
}
