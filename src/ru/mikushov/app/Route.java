package ru.mikushov.app;

public @interface Route {
    String url() default "";
}
