package com.example.demo.config;


import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class FlickrConfiguration {

    public static void auth() throws IOException, FlickrException, ExecutionException, InterruptedException {

        Properties properties;
        InputStream in = null;
        try {
            in = FlickrConfiguration.class.getResourceAsStream("/application.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }

        Flickr flickr = new Flickr(properties.getProperty("flickr.apiKey"), properties.getProperty("flickr.apiSecret"), new REST());
        Flickr.debugStream = false;
        AuthInterface authInterface = flickr.getAuthInterface();

        Scanner scanner = new Scanner(System.in);

        OAuth1RequestToken requestToken = authInterface.getRequestToken();
        System.out.println("token: " + requestToken);

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.DELETE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        String tokenKey = scanner.nextLine();
        scanner.close();

        OAuth1Token accessToken = authInterface.getAccessToken(requestToken, tokenKey);
        System.out.println("Authentication success");

        Auth auth = authInterface.checkToken(accessToken);

        // This token can be used until the user revokes it.
        System.out.println("Token: " + accessToken.getToken());
        System.out.println("Secret: " + accessToken.getTokenSecret());
        System.out.println("nsid: " + auth.getUser().getId());
        System.out.println("Realname: " + auth.getUser().getRealName());
        System.out.println("Username: " + auth.getUser().getUsername());
        System.out.println("Permission: " + auth.getPermission().getType());
    }

}
