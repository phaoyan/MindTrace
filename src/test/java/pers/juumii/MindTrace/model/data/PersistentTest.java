package pers.juumii.MindTrace.model.data;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class PersistentTest {

    @Test
    void clear() {
        LearningRecord record = new LearningRecord();
        record.setCardId(1);
        record.setTime(LocalDateTime.now());
        record.setCompletion(0);
        record.setId(1);
        record.setDescription("test record");
        System.out.println(record);
        System.out.println(record.isClear());
        record.clear();
        System.out.println(record);
        System.out.println(record.isClear());
    }
}