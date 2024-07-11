package com.example.dataplat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// 定义一个包含文件名和URL的类
public class FileItem {
    private String name;
    private String url;

}