package com.advertisement_sys.controller;

import com.advertisement_sys.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    /**
     * 上传文件基础路径
     */
    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    /**
     * 允许的图片类型
     */
    private static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp"
    ));

    /**
     * 允许的视频类型
     */
    private static final Set<String> ALLOWED_VIDEO_TYPES = new HashSet<>(Arrays.asList(
            "video/mp4", "video/avi", "video/mov", "video/wmv", "video/flv", "video/webm"
    ));

    /**
     * 最大文件大小（10MB）
     */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 最大视频文件大小（100MB）
     */
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024;

    /**
     * 初始化上传目录
     */
    @PostConstruct
    public void init() {
        try {
            // 创建图片上传目录
            Path imagePath = Paths.get(uploadPath, "images");
            Files.createDirectories(imagePath);
            
            // 创建视频上传目录
            Path videoPath = Paths.get(uploadPath, "videos");
            Files.createDirectories(videoPath);
        } catch (IOException e) {
            throw new RuntimeException("创建上传目录失败", e);
        }
    }

    /**
     * 统一文件上传接口（自动识别图片或视频）
     */
    @PostMapping("/file")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            // 获取文件类型
            String contentType = file.getContentType();
            if (contentType == null) {
                return Result.error("无法识别文件类型");
            }

            contentType = contentType.toLowerCase();
            boolean isImage = ALLOWED_IMAGE_TYPES.contains(contentType);
            boolean isVideo = ALLOWED_VIDEO_TYPES.contains(contentType);

            // 判断文件类型
            if (!isImage && !isVideo) {
                return Result.error("不支持的文件类型，仅支持图片（jpg、png、gif、bmp、webp）或视频（mp4、avi、mov、wmv、flv、webm）");
            }

            // 验证文件大小
            long maxSize = isImage ? MAX_FILE_SIZE : MAX_VIDEO_SIZE;
            if (file.getSize() > maxSize) {
                String sizeLimit = isImage ? "10MB" : "100MB";
                return Result.error("文件大小不能超过 " + sizeLimit);
            }

            // 确定文件类型和保存目录
            String fileType = isImage ? "image" : "video";
            String subDir = isImage ? "images" : "videos";

            // 生成文件名
            String fileName = generateFileName(file.getOriginalFilename(), fileType);
            
            // 保存文件
            String filePath = saveFile(file, subDir, fileName);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", filePath);
            result.put("fileName", fileName);
            result.put("type", fileType);
            
            String successMessage = isImage ? "图片上传成功" : "视频上传成功";
            return Result.success(successMessage, result);
        } catch (Exception e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }


    /**
     * 生成文件名
     */
    private String generateFileName(String originalFilename, String type) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 使用时间戳和UUID生成唯一文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateDir = sdf.format(new Date());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        
        return dateDir + "/" + type + "_" + uuid + extension;
    }

    /**
     * 保存文件
     */
    private String saveFile(MultipartFile file, String subDir, String fileName) throws IOException {
        // 创建日期目录
        String dateDir = fileName.substring(0, 8);
        Path targetDir = Paths.get(uploadPath, subDir, dateDir);
        Files.createDirectories(targetDir);
        
        // 获取文件名（去掉日期目录部分）
        String actualFileName = fileName.substring(fileName.indexOf("/") + 1);
        
        // 保存文件
        Path targetPath = targetDir.resolve(actualFileName);
        Files.copy(file.getInputStream(), targetPath);
        
        // 返回访问路径（用于前端访问）
        return "/uploads/" + subDir + "/" + fileName;
    }
}


