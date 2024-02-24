package com.ipmcpmjournal.journal.ipmcpm.service.impl;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.util.IOUtilities;
import com.ipmcpmjournal.journal.ipmcpm.service.FlickrService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FlickrServiceImpl implements FlickrService {



    private Flickr flickr;

    @Override
    @SneakyThrows
    public String savePhoto(InputStream photo, String title) {
        connect();
        UploadMetaData uploadMetaData = new UploadMetaData();
        uploadMetaData.setTitle(title);

        String photoId = flickr.getUploader().upload(photo, uploadMetaData);
        return flickr.getPhotosInterface().getPhoto(photoId).getMedium640Url();
    }

    private void connect() throws InterruptedException, ExecutionException, IOException, FlickrException {
        Properties properties;
        InputStream in = null;
        try {
            in = FlickrServiceImpl.class.getResourceAsStream("/application.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtilities.close(in);
        }

        flickr = new Flickr(properties.getProperty("flickr.apiKey"), properties.getProperty("flickr.apiSecret"), new REST());
        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(properties.getProperty("appKey"));
        auth.setTokenSecret(properties.getProperty("appSecret"));
        RequestContext requestContext = RequestContext.getRequestContext();
        requestContext.setAuth(auth);
        flickr.setAuth(auth);
    }
}
