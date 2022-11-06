package pers.juumii.MindTrace.model.service;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.*;
import pers.juumii.MindTrace.model.mapper.*;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;
import pers.juumii.MindTrace.utils.DataUtils;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 缓存从数据库中拿到的数据。所有数据需求都通过Repository服务得到。
 * 如果数据不在Repository中，则Repository向数据库索要数据。
 * 如果数据在Repository中，则Repository将数据直接给出。
 * 外部对索要的数据进行修改，不会立即反映到数据库中，而是在Repository将改变提交到数据库后才能生效。
 */
@Service
@ToString
@NoArgsConstructor
public class Repository {

    // data type到mapper的映射
    @SuppressWarnings("all")
    private final Map<Class<? extends Persistent>, DataMapper> mappers = Map.of(
            Knowledge.class, SqlSessionUtils.getMapper(KnowledgeMapper.class),
            LearningCard.class, SqlSessionUtils.getMapper(LearningCardMapper.class),
            LearningRecord.class, SqlSessionUtils.getMapper(LearningRecordMapper.class),
            QuizCard.class, SqlSessionUtils.getMapper(QuizCardMapper.class),
            QuizRecord.class, SqlSessionUtils.getMapper(QuizRecordMapper.class),
            Settings.class, SqlSessionUtils.getMapper(SettingsMapper.class));
    private List<Persistent> dataRepository;

    //commit 统一进行增删改。将数据库数据全部清除，并将repository中的数据克隆回数据库
    @SuppressWarnings("all")
    public void commit(){
        mappers.forEach((aClass, dataMapper) -> dataMapper.deleteAll());
        dataRepository.forEach(data ->{
            for(Class<? extends Persistent> type: mappers.keySet()){
                if(type.isInstance(data)){
                    DataMapper mapper = mappers.get(type);
                    if(mapper.update(data) == 0)
                        mapper.insert(data);
                }
            }
        });
    }

    @SuppressWarnings("all")
    @PostConstruct
    public void load(){
        dataRepository = new ArrayList<>();
        for(DataMapper mapper: mappers.values())
            dataRepository.addAll(mapper.queryAll());
        DataUtils.forAllIf(dataRepository, data->data instanceof Linkable, data->((Linkable) data).link(this));
    }

    public void clear(){
        dataRepository = new ArrayList<>();
    }

    public <T extends Persistent> void put(T data) {
        //查重
        if(getById(data.getId(), data.getClass()) != null)
            return;
        //如果data为Linkable，则其成员对象也要录入repository中
        if(data instanceof Linkable)
            ((Linkable) data).queryLinked().forEach(this::put);
        dataRepository.add(data);
    }

    //get方法。由于repository中可能存有已经clear但没有体现在数据库中的数据，所以get原则上都要除去clear的数据
    @SuppressWarnings("unchecked")
    public <T extends Persistent> T getById(int id, Class<T> prototype){
        return (T) DataUtils.getIf(dataRepository, data -> data.getId() == id && prototype.isInstance(data));
    }
    @SuppressWarnings("unchecked")
    public <T extends Persistent> List<T> getByType(Class<T> prototype){
        return  (List<T>) DataUtils.getAllIf(dataRepository, prototype::isInstance);

    }
    @SuppressWarnings("unchecked")
    public <T extends Persistent> List<T> getByKeyword(String keyword, Class<T> prototype){
        return  (List<T>) DataUtils.getAllIf(dataRepository, data -> data.isLike(keyword) && prototype.isInstance(data));
    }
    public List<Persistent> getAll(){
        return dataRepository;
    }


    // remove
    public void remove(Persistent data){
        dataRepository.remove(data);
    }
    public <T extends Persistent> void removeById(int id, Class<T> prototype){
        remove(getById(id, prototype));
    }
    public <T extends Persistent> void removeByKeyword(String keyword, Class<T> prototype){
        getByKeyword(keyword, prototype).forEach(this::remove);
    }

    //add
    public void add(Persistent data){
        dataRepository.add(data);
    }
    public void addAll(List<? extends Persistent> dataList){
        dataList.forEach(this::add);
    }

    public <T extends Persistent> T create(Class<T> dataType){
        T data = null;
        try {
            data = dataType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        dataRepository.add(data);
        return data;
    }
}
