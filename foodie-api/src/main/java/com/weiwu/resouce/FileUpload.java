package com.weiwu.resouce;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-${spring.profiles.active}.properties")
@Data
public class FileUpload {
    /**
     * 用户上传头像的位置
     */
    private String imageUserFaceLocation;

    /**
     * 图片服务地址
     */
    private String imageServerUrl;
}
