package com.fruitshop.util;

import com.fruitshop.config.SftpConfig;
import com.fruitshop.vo.UploadVO;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * SFTP工具类
 */
@Slf4j
@Component
public class SftpUtil {

    @Autowired
    private SftpConfig sftpConfig;

    // 允许的图片类型
    private static final List<String> IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"
    );

    // 图片最大大小：5MB
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    // 文件最大大小：10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 获取SFTP连接
     */
    private ChannelSftp connect() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
        session.setPassword(sftpConfig.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    /**
     * 关闭SFTP连接
     */
    private void disconnect(ChannelSftp sftp) {
        if (sftp != null) {
            try {
                sftp.disconnect();
                if (sftp.getSession() != null) {
                    sftp.getSession().disconnect();
                }
            } catch (Exception e) {
                log.error("关闭SFTP连接失败", e);
            }
        }
    }

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 上传结果
     */
    public UploadVO uploadImage(MultipartFile file) {
        // 验证文件是否为空
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("请选择要上传的文件");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !IMAGE_TYPES.contains(contentType)) {
            throw new RuntimeException("只能上传图片文件（jpeg、png、gif、webp、bmp）");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new RuntimeException("图片大小不能超过5MB");
        }
        
        return doUpload(file);
    }

    /**
     * 上传文件（通用）
     *
     * @param file 文件
     * @return 上传结果
     */
    public UploadVO uploadFile(MultipartFile file) {
        // 验证文件是否为空
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("请选择要上传的文件");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小不能超过10MB");
        }
        
        return doUpload(file);
    }

    /**
     * 执行上传
     */
    private UploadVO doUpload(MultipartFile file) {
        ChannelSftp sftp = null;
        try {
            sftp = connect();
            
            // 按日期创建子目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String remoteDir = sftpConfig.getRemotePath() + "/" + datePath;
            
            // 创建目录
            createDir(sftp, remoteDir);
            
            // 生成新文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;
            String remoteFilePath = remoteDir + "/" + newFilename;
            
            // 上传文件
            try (InputStream inputStream = file.getInputStream()) {
                sftp.put(inputStream, remoteFilePath);
            }
            log.info("文件上传成功: {}", remoteFilePath);
            
            // 返回上传结果
            String url = sftpConfig.getUrlPrefix() + "/" + datePath + "/" + newFilename;
            return UploadVO.of(url, originalFilename, newFilename, file.getSize(), file.getContentType());
        } catch (Exception e) {
            log.error("SFTP上传文件失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        } finally {
            disconnect(sftp);
        }
    }

    /**
     * 通过输入流上传文件
     *
     * @param inputStream 文件输入流
     * @param originalFilename 原始文件名
     * @param size 文件大小
     * @param contentType 文件类型
     * @return 上传结果
     */
    public UploadVO upload(InputStream inputStream, String originalFilename, Long size, String contentType) {
        ChannelSftp sftp = null;
        try {
            sftp = connect();
            
            // 按日期创建子目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String remoteDir = sftpConfig.getRemotePath() + "/" + datePath;
            
            // 创建目录
            createDir(sftp, remoteDir);
            
            // 生成新文件名
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;
            String remoteFilePath = remoteDir + "/" + newFilename;
            
            // 上传文件
            sftp.put(inputStream, remoteFilePath);
            log.info("文件上传成功: {}", remoteFilePath);
            
            // 返回上传结果
            String url = sftpConfig.getUrlPrefix() + "/" + datePath + "/" + newFilename;
            return UploadVO.of(url, originalFilename, newFilename, size, contentType);
        } catch (Exception e) {
            log.error("SFTP上传文件失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        } finally {
            disconnect(sftp);
        }
    }

    /**
     * 创建目录（支持多级目录）
     */
    private void createDir(ChannelSftp sftp, String path) throws SftpException {
        String[] dirs = path.split("/");
        String currentPath = "";
        for (String dir : dirs) {
            if (dir.isEmpty()) continue;
            currentPath += "/" + dir;
            try {
                sftp.cd(currentPath);
            } catch (SftpException e) {
                sftp.mkdir(currentPath);
                sftp.cd(currentPath);
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径（相对于remotePath）
     */
    public boolean delete(String filePath) {
        ChannelSftp sftp = null;
        try {
            sftp = connect();
            String fullPath = sftpConfig.getRemotePath() + "/" + filePath;
            sftp.rm(fullPath);
            log.info("文件删除成功: {}", fullPath);
            return true;
        } catch (Exception e) {
            log.error("SFTP删除文件失败", e);
            return false;
        } finally {
            disconnect(sftp);
        }
    }
}
