package com.project.server.database.annotation;

import com.project.server.database.repository.parser.ModelParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultParser {
    Class<? extends ModelParser<?>> value();
}
