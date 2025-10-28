package com.javarush.led.lesson04.processor.bfpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Component
public class AliasNamingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Получаем имена всех бинов, помеченных аннотациями @Service, @Repository, @Component
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            Class<?> beanClass = beanFactory.getType(beanName);

            if (beanClass != null) {
                // Обрабатываем бины с аннотацией @Service
                if (beanClass.isAnnotationPresent(Service.class)) {
                    beanFactory.registerAlias(beanName, "service-" + beanName);
                    System.out.println("@Service bean: " + beanName + " -> service-" + beanName);
                }

                // Обрабатываем бины с аннотацией @Repository
                if (beanClass.isAnnotationPresent(Repository.class)) {
                    beanFactory.registerAlias(beanName, "repo-" + beanName);
                    System.out.println("@Repository bean: " + beanName + " -> repo-" + beanName);
                }

                // Обрабатываем бины с аннотацией @Component (не @Service и не @Repository)
                if (beanClass.isAnnotationPresent(Component.class) &&
                    !beanClass.isAnnotationPresent(Service.class) &&
                    !beanClass.isAnnotationPresent(Repository.class)) {
                    beanFactory.registerAlias(beanName, "component-" + beanName);
                    System.out.println("@Component bean: " + beanName + " -> component-" + beanName);
                }
            }
        }
    }
}