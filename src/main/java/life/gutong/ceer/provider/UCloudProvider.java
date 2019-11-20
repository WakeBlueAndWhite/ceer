package life.gutong.ceer.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import life.gutong.ceer.exception.CustomizeException;
import life.gutong.ceer.exception.CustomizeStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.provider
 * @ClassName: UCloudProvider
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/18 21:40
 */
@Component
@Slf4j
public class UCloudProvider {

    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    @Value("${ucloud.ufile.expires}")
    private Integer expires;

    public String upload(InputStream fileStream, String mimeType, String fileName) {
        String generatedFileName;
        //.在正则中表示任何字符  所以需要加\\转译
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            //存储的文件名为随机的UUID+上传文件的格式后缀
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            //图片上传校验
            throw new CustomizeException(CustomizeStatusMessage.FILE_UPLOAD_FAIL);
        }
        try {
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
            ObjectConfig config = new ObjectConfig(region, suffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    })
                    .execute();
            //response.getRetCode() == 0 表示图片上传成功
            if (response != null && response.getRetCode() == 0) {
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expires)
                        .createUrl();
                return url;
            } else {
                //记录文件上传失败日志
                log.error("upload error,{}", response);
                throw new CustomizeException(CustomizeStatusMessage.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException e) {
            log.error("upload error,{}", fileName, e);
            throw new CustomizeException(CustomizeStatusMessage.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            log.error("upload error,{}", fileName, e);
            throw new CustomizeException(CustomizeStatusMessage.FILE_UPLOAD_FAIL);
        }
    }
}
