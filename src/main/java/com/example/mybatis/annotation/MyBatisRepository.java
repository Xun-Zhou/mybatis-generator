package com.example.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author admin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MyBatisRepository {
}