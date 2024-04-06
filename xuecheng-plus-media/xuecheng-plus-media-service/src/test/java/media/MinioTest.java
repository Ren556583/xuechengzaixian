package media;

import com.alibaba.nacos.common.utils.IoUtils;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import io.minio.MinioClient;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioTest {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "ryh";

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void test_upload() throws Exception {

        ContentInfo mimeTypeMatch = ContentInfoUtil.findMimeTypeMatch(".jpg");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (mimeTypeMatch != null) {
            mimeType = mimeTypeMatch.getMimeType();
        }

        //上传文件参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")
                .filename("D:\\鬼刀\\WLOP作品大全\\1.jpg")
                .object("test/wlop.jpg")
                .build();
        minioClient.uploadObject(uploadObjectArgs);
    }

    @Test
    public void deleteTest() {
        try {
            minioClient.removeObject(RemoveObjectArgs
                    .builder()
                    .bucket("testbucket")
                    .object("wlop.jpg")
                    .build());
            System.out.println("删除成功");
        } catch (Exception e) {
            System.out.println("删除失败");
        }
    }

    @Test
    public void getFileTest() {
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket("testbucket")
                    .object("test/wlop.jpg")
                    .build());
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ryh\\Desktop\\壁纸\\tmp.png");
            IoUtils.copy(inputStream,fileOutputStream);
            //DigestUtils.md5DigestAsHex(inputStream);
            inputStream.close();
            fileOutputStream.close();
            System.out.println("下载成功");
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }
}
