package com.fruitshop.controller.admin;

import com.fruitshop.common.Result;
import com.fruitshop.util.SftpUtil;
import com.fruitshop.vo.UploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台-文件上传
 */
@RestController
@RequestMapping("/admin/upload")
public class FileUploadController {

    @Autowired
    private SftpUtil sftpUtil;

    /**
     * 上传图片
     */
    @PostMapping("/image")
    public Result<UploadVO> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            UploadVO result = sftpUtil.uploadImage(file);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 上传文件（通用）
     */
    @PostMapping("/file")
    public Result<UploadVO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            UploadVO result = sftpUtil.uploadFile(file);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
