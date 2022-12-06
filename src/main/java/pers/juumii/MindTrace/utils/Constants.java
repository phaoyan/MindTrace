package pers.juumii.MindTrace.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class Constants {
    public static final LocalDateTime timeAnchor = LocalDateTime.of(2000,1,1,0,0);
    public static final Duration ignoreTimeInterval = Duration.ofHours(1);
    public static final LocalDateTime today = LocalDateTime.now().toLocalDate().atTime(0,0,0);
}
