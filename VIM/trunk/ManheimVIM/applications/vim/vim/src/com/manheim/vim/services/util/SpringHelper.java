package com.manheim.vim.services.util;

import java.io.Serializable;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

import com.manheim.vim.services.vehicleimage.VehicleImageFacade;

/**
 * SpringHelper.java - A helper class used after injection occurs to retrieve a bean
 * @version 1.0 
 * @author Enterpulse
 */
public class SpringHelper implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Used to retrieve spring beans
     */
    public static Object getSpringBean(String beanName)
    {
        BeanFactoryLocator bfLocator = SingletonBeanFactoryLocator.getInstance("classpath:beanRefFactory.xml");
        BeanFactoryReference bfReference = bfLocator.useBeanFactory("beanFactory");
        BeanFactory factory = bfReference.getFactory();
        
        return factory.getBean(beanName);
    }

    /**
     * Used to retrieve VehicleImageFacade
     */
    public static VehicleImageFacade getVehicleImageFacade()
    {
        return (VehicleImageFacade)getSpringBean("vehicleImageFacade");
    }
}
