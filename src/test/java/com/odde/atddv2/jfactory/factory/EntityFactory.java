package com.odde.atddv2.jfactory.factory;

import com.github.leeonky.jfactory.DataRepository;
import com.github.leeonky.jfactory.JFactory;
import com.github.leeonky.jfactory.Spec;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class EntityFactory extends JFactory {
    private static Set<Class> beanSpecsClasses = new HashSet<>();

    static {
        Reflections reflections = new Reflections("com.odde.atddv2.jfactory.factory");
        beanSpecsClasses.addAll(reflections.getSubTypesOf(Spec.class));
        reflections = new Reflections("com.odde.atddv2.jfactory.mongo");
        beanSpecsClasses.addAll(reflections.getSubTypesOf(Spec.class));
    }

    public EntityFactory() {
        configFactory();
    }

    public EntityFactory(DataRepository dataRepository) {
        super(dataRepository);
        configFactory();
    }

    private void configFactory() {
        ignoreDefaultValue(p -> p.getName().equals("id"));

        beanSpecsClasses.forEach(this::register);
    }
}
