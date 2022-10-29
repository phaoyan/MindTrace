package pers.juumii.MindTrace.model.mapper.utils;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.Persistent;

import java.util.List;

public interface DataMapper<T extends Persistent> {
    int insert(@Param("data") T data);
    int update(@Param("data") T data);
    void deleteById(@Param("id") int id);
    T queryById(@Param("id") int id);
    List<T> queryAll();
    List<T> queryByLike(@Param("keyword") String keyword);
}
