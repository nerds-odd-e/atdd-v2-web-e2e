package com.odde.atddv2.jfactory.factory;

import com.github.leeonky.jfactory.DataRepository;
import com.github.leeonky.jfactory.JFactory;
import com.github.leeonky.jfactory.repo.JPADataRepository;
import com.odde.atddv2.entity.mongo.Express;
import com.yaoruozhou.jfactory.MockServerDataRepository;
import com.yaoruozhou.jfactory.MockServerDataRepositoryImpl;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

@Component
public class FactoryConfig {

    private static ApplicationContext context;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public FactoryConfig(ApplicationContext ac) {
        context = ac;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public JFactory factorySet(MockServerClient mockServerClient) {
        return new EntityFactory(new MockServerDataRepository() {
            private final DataRepository jpaDataRepo = new JPADataRepository(entityManagerFactory.createEntityManager());
            private final MockServerDataRepository mockServerDataRepo = new MockServerDataRepositoryImpl(mockServerClient);

            @Override
            public void setUrlParams(String s) {
                mockServerDataRepo.setUrlParams(s);
            }

            @Override
            public void setRootClass(Class<?> aClass) {
                mockServerDataRepo.setRootClass(aClass);
            }

            @Override
            public void setPathVariables(String s) {
                mockServerDataRepo.setPathVariables(s);
            }

            @Override
            public void setSerializer(Function<Object, String> function) {
                mockServerDataRepo.setSerializer(function);
            }

            @Override
            public <T> Collection<T> queryAll(Class<T> type) {
                if (type.getPackageName().startsWith("com.odde.atddv2.jfactory.api")) {
                    return mockServerDataRepo.queryAll(type);
                } else if (type.getPackageName().startsWith("com.odde.atddv2.entity.mongo")) {
                    if (type.equals(Express.class))
                        return (Collection<T>) mongoTemplate.findAll(Express.class);
                    return Collections.emptyList();
                } else {
                    return jpaDataRepo.queryAll(type);
                }
            }

            @Override
            public void clear() {
                jpaDataRepo.clear();
                mockServerDataRepo.clear();
                mongoTemplate.getDb().drop();
            }

            @Override
            public void save(Object object) {
                if (object.getClass().getPackageName().startsWith("com.odde.atddv2.jfactory.api")) {
                    mockServerDataRepo.save(object);
                } else if (object.getClass().getPackageName().startsWith("com.odde.atddv2.entity.mongo")) {
                    if (object instanceof Express)
                        mongoTemplate.save(object);
                } else {
                    jpaDataRepo.save(object);
                }
            }
        });
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
