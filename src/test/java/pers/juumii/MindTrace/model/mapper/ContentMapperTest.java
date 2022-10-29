package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.Content;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

class ContentMapperTest {
    public static final ContentMapper mapper = SqlSessionUtils.getSqlSession().getMapper(ContentMapper.class);
    @Test
    void insertContent() {
        Content content = new Content(1,1, "www.bilibili.com", "test insert content");
        int count = mapper.insert(content);
        System.out.println(count);
    }

    @Test
    void queryContents() {
        System.out.println(mapper.queryAll());
    }

    @Test
    void queryContentsByTaskId() {
    }

    @Test
    void update(){
        Content content = mapper.queryById(19);
        content.setSrc("www.bing.com");
        System.out.println(mapper.update(content));
    }
}