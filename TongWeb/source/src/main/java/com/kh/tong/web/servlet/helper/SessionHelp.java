package com.kh.tong.web.servlet.helper;

import java.io.File;
import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.kh.tong.web.bean.User;
import com.kh.tong.web.db.UserDBManger;
import com.kh.webutils.WebConstants;

public class SessionHelp {
    static Logger log = Logger.getLogger(SessionHelp.class);

    public static void loginSession(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(true);
        session.setAttribute(WebConstants.SessionAttributeUser, user);
        UserDBManger.updateAppUserLastLoginTime(user.getUuid());
    }

    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 1024; // 1G
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 1024; // 1G

    public static boolean upload(String fileAbsolutePath, HttpServletRequest request, String dataUUID, String userUUID,
            String dataPath) {
        boolean result = false;
        if (!ServletFileUpload.isMultipartContent(request)) {
            return result;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new java.io.File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() == 1) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileType = item.getName().substring(item.getName().lastIndexOf(".") + 1);
                        File storeFile = new File(fileAbsolutePath);
                        item.write(storeFile);

                        result = UserDBManger.addResource(dataUUID, userUUID, fileType, dataPath);
                        result = true;
                    }
                }
            } else {
                log.error("Multi file upload not supported.");
                result = false;
            }
        } catch (Exception ex) {
            log.error("error uploading file.", ex);
            result = false;
        }
        return result;
    }
}
