package ru.stepup;

public @interface LogTransformation {
    String logFile() default "log.txt";
}
