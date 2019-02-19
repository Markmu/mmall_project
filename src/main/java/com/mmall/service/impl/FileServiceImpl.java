package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

//    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = new StringBuilder().append(UUID.randomUUID()).append(".").append(fileExtensionName).toString();
        log.info("开始上传文件, 文件名: {}, 路径: {}, 新文件名: {}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);

            // todo upload file to ftp
            FTPUtil.uploadFiles(Lists.newArrayList(targetFile));

            // todo remove file from local
            targetFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return targetFile.getName();
    }

}
