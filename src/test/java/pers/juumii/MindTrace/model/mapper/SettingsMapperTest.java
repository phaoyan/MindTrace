package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.Settings;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import static org.junit.jupiter.api.Assertions.*;

class SettingsMapperTest {
    public static final SettingsMapper mapper = SqlSessionUtils.getMapper(SettingsMapper.class);
    @Test
    void insert() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void queryById() {
    }

    @Test
    void queryAll() {
        mapper.queryAll().forEach(setting -> System.out.println(setting.getK() + ": " + setting.getV()));
    }

    @Test
    void queryByLike() {
    }
}