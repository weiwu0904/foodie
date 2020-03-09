package com.weiwu.controller.center;

import com.weiwu.controller.BaseController;
import com.weiwu.pojo.Users;
import com.weiwu.pojo.bo.center.CenterUserBO;
import com.weiwu.resouce.FileUpload;
import com.weiwu.service.center.CenterUserService;
import com.weiwu.utils.CookieUtils;
import com.weiwu.utils.DateUtil;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
@Api(value = "用户信息接口", tags = "用户信息接口")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @RequestMapping("/update")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    public IMOOCJSONResult update(@RequestParam String userId,
                                  @RequestBody @Valid CenterUserBO centerUserBO,
                                  BindingResult bindingResult,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 判断bindingResult是否有错误的信息，有则直接返回
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = getErrors(bindingResult);
            return IMOOCJSONResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response,
                "user",
                JsonUtils.objectToJson(userResult), true);

        //TODO 后续要改，增加token 会整合进redis 分布式会话

        return IMOOCJSONResult.ok(userResult);
    }

    @PostMapping("/uploadFace")
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    public IMOOCJSONResult uploadFace(@RequestParam String userId,
                                      @ApiParam(name = "file", value = "用户头像", required = true) MultipartFile file,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        // 判断bindingResult是否有错误的信息，有则直接返回
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        if (file == null || file.getOriginalFilename() == null) {
            return IMOOCJSONResult.errorMsg("文件或文件名不能为空");
        }

        //1. 定义头像保存的位置
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //2. 在路径上为每一个用户增加 一个用户id的文件夹，区分用户
        String uploadPathPrefix = File.separator + userId;
        //3. 开始文件上传
        if (file == null) {
            IMOOCJSONResult.errorMsg("文件不能为空");
        }
        //4. 获得文件名称
        String originalFilename = file.getOriginalFilename();

        OutputStream fileOutputStream = null;
        try {
            // face-{userId}.png
            // 文件重命名 xxx.png
            String[] fileNameArr = originalFilename.split("\\.");
            // 获取文件的后缀名
            String suffix = fileNameArr[fileNameArr.length - 1];
            if (!suffix.equalsIgnoreCase("jpg") &&
                    !suffix.equalsIgnoreCase("jpeg") &&
                    !suffix.equalsIgnoreCase("png")) {
                return IMOOCJSONResult.errorMsg("文件格式不正确");
            }
            // 文件名重组, 覆盖式上传
            String newFileName = "face-" + userId + "." + suffix;
            // 上传的头像最终保存的位置
            String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
            File outFile = new File(finalFacePath);
            if (!outFile.getParentFile().exists()) {
                // 创建父文件夹
                outFile.getParentFile().mkdir();
            }
            // 文件输出，保存到目录
            fileOutputStream = new FileOutputStream(outFile);
            InputStream fileInputStream = file.getInputStream();
            IOUtils.copy(fileInputStream, fileOutputStream);

            uploadPathPrefix += ("/" + newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg(e.getMessage());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String imageServerUrl = fileUpload.getImageServerUrl();

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        // 用户头像保存到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return IMOOCJSONResult.ok();
    }

    /// 解析错误信息
    private Map<String, String> getErrors(BindingResult bindingResult) {

        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError error : errorList) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }


    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setEmail(null);
        users.setMobile(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
        return users;
    }
}
