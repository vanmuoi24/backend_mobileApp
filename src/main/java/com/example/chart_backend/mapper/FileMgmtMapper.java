package com.example.chart_backend.mapper;

import org.springframework.stereotype.Component;

import com.example.chart_backend.dto.FileInfo;
import com.example.chart_backend.entity.FileMgmt;

@Component
public class FileMgmtMapper {

    public FileMgmt toFileMgmt(FileInfo fileInfo) {
        FileMgmt file = new FileMgmt();

        // id dùng luôn tên file (uuid + extension)
        file.setId(fileInfo.getName());
        file.setContentType(fileInfo.getContentType());
        file.setSize(fileInfo.getSize());
        file.setMd5Checksum(fileInfo.getMd5Checksum());
        file.setPath(fileInfo.getPath());

        // ownerId set ở service (sau khi biết user hiện tại)
        return file;
    }
}
