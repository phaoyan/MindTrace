package pers.juumii.MindTrace.model.data;

import pers.juumii.MindTrace.model.service.Repository;

//有子对象成员的Data实现该接口，暴露一个link()方法用于初始化这些子对象成员
public interface Linkable {

    void link(Repository repository);
}
