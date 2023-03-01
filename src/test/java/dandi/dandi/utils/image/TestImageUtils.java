package dandi.dandi.utils.image;

import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class TestImageUtils {

    private static final String TEST_IMAGE_RESOURCE_DIR = "/src/test/resources/static/images/";
    public static final String TEST_IMAGE_FILE_NAME = "test_img.jpg";

    public static MultipartFile generateTestImgMultipartFile() throws IOException {
        File file = new File(new File("")
                .getAbsolutePath() + TEST_IMAGE_RESOURCE_DIR + TEST_IMAGE_FILE_NAME);
        FileItem fileItem = new DiskFileItem("originFile", Files.probeContentType(file.toPath()),
                false, file.getName(), (int) file.length(), file.getParentFile());

        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);

        return new CommonsMultipartFile(fileItem);
    }

    public static File generatetestImgFile() {
        return new File(new File("")
                .getAbsolutePath() + TEST_IMAGE_RESOURCE_DIR + TEST_IMAGE_FILE_NAME);
    }
}
