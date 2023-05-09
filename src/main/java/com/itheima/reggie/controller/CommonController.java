package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * @author L1ao
 * @version 1.0
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(file.getOriginalFilename().lastIndexOf("."));

            String fileType = ".jpg|.png";
            if (!suffix.matches(".*(" + fileType + ")$")) {
                throw new Exception("文件格式不正确");
            }

            String fileName = UUID.randomUUID().toString() + suffix;

            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(dir, fileName));

            return R.success(fileName);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.error("上传失败");
        }
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {

            String fileType = ".jpg|.png";
            if (!name.matches(".*(" + fileType + ")$")) {
                throw new Exception("文件格式不正确");
            }


            FileInputStream fileInputStream = new FileInputStream(new File(uploadPath, name));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            log.error("下载文件失败", e);
//            e.printStackTrace();
        }
    }
}
