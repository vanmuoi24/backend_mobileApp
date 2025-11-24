package com.example.chart_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_mgmt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMgmt {

    @Id
    String id; // ví dụ: "9ed9b8f0-6a54-4904-9154-95773602d9d3.jpg"
    String ownerId;
    String contentType;
    long size;
    String md5Checksum;
    String path;
}
