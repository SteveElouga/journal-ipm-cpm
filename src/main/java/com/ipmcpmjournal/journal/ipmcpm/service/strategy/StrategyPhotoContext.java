package com.ipmcpmjournal.journal.ipmcpm.service.strategy;

import ch.qos.logback.core.spi.ErrorCodes;
import com.flickr4java.flickr.FlickrException;
import com.ipmcpmjournal.journal.ipmcpm.exception.InvalidOperationException;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class StrategyPhotoContext {

    private BeanFactory beanFactory;
    private Strategy strategy;

    @Setter
    private String context;

    @Autowired
    public StrategyPhotoContext(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object savePhoto(String context, String email, InputStream photo, String title) throws FlickrException {
        determinContext(context);
        return strategy.savePhoto(email, photo, title);
    }

    private void determinContext(String context) {
        final String beanName = context + "Strategy";
        switch (context) {
            case "user" :
                strategy = beanFactory.getBean(beanName, SaveUserPhoto.class);
                break;
            default: throw new InvalidOperationException("Contexte inconnue pour l'enregistrement de la photo");
        }
    }
}
