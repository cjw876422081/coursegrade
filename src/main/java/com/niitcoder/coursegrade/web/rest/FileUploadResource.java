package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.config.FileUploadProperties;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.dto.FileInfo;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileUploadResource {
    @Autowired
    private FileUploadProperties fileUploadProperteis;

    @ApiOperation(value="单个文件上传")
    @ApiImplicitParam(name = "path",value="需要指定的虚拟目录")
    @PostMapping("/file/upload")
    public ResponseEntity uploadFile(@RequestParam(required = false,defaultValue = "") String path, MultipartFile file){
        //使用登录名作为虚拟目录的子目录，如果没有获取到当前登录用户，则使用时间戳命名
        String loginname=SecurityUtils.getCurrentUserLogin().get();
        if(StringUtils.isEmpty(loginname)){
            loginname=String.valueOf(System.currentTimeMillis());
        }
        if(path.length()==0){
            path+=loginname;
        }else{
            path+=File.separator+loginname;
        }

        FileInfo result=processFile(path,file);
        return ResponseEntity.ok(result);

    }
    @ApiOperation(value="批量文件上传")
    @ApiImplicitParam(name = "path",value="需要指定的虚拟目录")
    @PostMapping("/files/upload")
    public ResponseEntity uploadFiles(@RequestParam(required = false,defaultValue = "") String path,@RequestParam("files") MultipartFile[] files){
        //使用登录名作为虚拟目录的子目录，如果没有获取到当前登录用户，则使用时间戳命名
        String loginname=SecurityUtils.getCurrentUserLogin().get();
        if(StringUtils.isEmpty(loginname)){
            loginname=String.valueOf(System.currentTimeMillis());
        }
        if(path.length()==0){
            path+=loginname;
        }else{
            path+=File.separator+loginname;
        }
        List<FileInfo> result=new ArrayList<FileInfo>();
        if(files!=null && files.length>0){
            for (MultipartFile file : files) {
                result.add(processFile(path,file));
            }
        }
        return ResponseEntity.ok(result);
    }

    private FileInfo processFile(String path,MultipartFile file){
        String originalFilename = file.getOriginalFilename();

        /**
         * 文件名
         */
        String fileName = "";

        /**
         * 扩展名
         */
        String extName = "";
        //解析文件后缀名和文件名
        if (StringUtils.isNotEmpty(originalFilename)) {
            if (originalFilename.lastIndexOf(".") != -1) {
                fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
                extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }
        }
        //使用时间戳作为上传文件的文件名，防止同一个文件反复上传的重名影响
        String serverFilename= UUID.randomUUID()+"."+extName;
        //文件在服务器上的绝对路径
        String serverPath=fileUploadProperteis.getServerDisk()+(path!=null?File.separator+path+File.separator:"");
        //构建虚拟目录路径
        String vpath=fileUploadProperteis.getVpath()+(path!=null?(path.startsWith("/")?"":"/")+path+"/":"")+serverFilename;
        File serverDir= new File(serverPath);
        try {
            //如果目录不存在，自动创建
            if(!serverDir.exists()){
                serverDir.mkdirs();
            }
            String serverFilePath=serverPath+serverFilename;
            File serverFile = new File(serverFilePath);
            //构造返回结果
            FileInfo result=new FileInfo();
            //文件后追梦
            result.setFileExtName(extName);
            //文件名
            result.setFileName(fileName);
            //文件web访问路径
            result.setPath(vpath);
            //文件大小
            result.setSize(file.getSize());
            //服务器中文件绝对路径
            result.setFullPath(serverFilePath);

            //将上传的文件写入服务器中
            file.transferTo(serverFile);

            //返回上传结果
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(),"FileInfo","uploadError");
        }
    }
}
