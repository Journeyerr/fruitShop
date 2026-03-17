package com.fruitshop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SFTP配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpConfig {
    
    /**
     * SFTP服务器地址
     */
    private String host;
    
    /**
     * SFTP服务器端口
     */
    private Integer port = 22;
    
    /**
     * SFTP用户名
     */
    private String username;
    
    /**
     * SFTP密码
     */
    private String password = "@yy5201314aliyun";
    
    /**
     * 上传文件的远程目录
     */
    private String remotePath;
    
    /**
     * 访问URL前缀
     */
    private String urlPrefix;
}
