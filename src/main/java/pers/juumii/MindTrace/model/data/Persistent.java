package pers.juumii.MindTrace.model.data;


/**
 * 可存入数据库，受Repository托管的数据
 */
public interface Persistent {
    void setId(long id);
    long getId();
}
