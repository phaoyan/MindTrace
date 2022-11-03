package pers.juumii.MindTrace.model.service;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.exception.DataClearedException;
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
            Content.class, SqlSessionUtils.getMapper(ContentMapper.class),
            QuizCard.class, SqlSessionUtils.getMapper(QuizCardMapper.class),
            Knowledge.class, SqlSessionUtils.getMapper(KnowledgeMapper.class),
            LearningTask.class, SqlSessionUtils.getMapper(LearningTaskMapper.class),
            LearningRecord.class, SqlSessionUtils.getMapper(LearningRecordMapper.class),
            QuizTask.class, SqlSessionUtils.getMapper(QuizTaskMapper.class),
            QuizRecord.class, SqlSessionUtils.getMapper(QuizRecordMapper.class),
            Settings.class, SqlSessionUtils.getMapper(SettingsMapper.class));
    private List<Persistent> dataRepository;

    /**
      commit时，统一进行增删改：
      如果数据进行了clear()，说明该数据被删除了，则要删除数据库中对应的数据；
      如果数据没有clear()，那么尝试更新这个数据。如果更新成功，说明该数据在数据库中存在，并且我们将其更新了；
      如果更新修改的行数为0，要么程序运行中我们没有修改过这个数据，要么这个数据是新增的。
       如果是前者，则执行insert并不会改变数据库中的数据
       如果是后者，则执行insert正常地将新数据插入数据库
     */
    @SuppressWarnings("all")
    public void commit(){
        dataRepository.forEach(data ->{
            for(Class<? extends Persistent> type: mappers.keySet()){
                if(type.isInstance(data)){
                    DataMapper mapper = mappers.get(type);
                    if(data.isClear())
                        mapper.deleteById(data.getId());
                    else if(mapper.update(data) == 0)
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
    }

    //get方法。由于repository中可能存有已经clear但没有体现在数据库中的数据，所以get原则上都要除去clear的数据
    @SuppressWarnings("unchecked")
    public <T extends Persistent> T getById(int id, Class<T> prototype) throws DataClearedException {
        T res = (T) DataUtils.getIf(dataRepository, data -> data.getId() == id && prototype.isInstance(data));
        if(res != null && res.isClear())
            throw new DataClearedException("pers.juumii.MindTrace.model.service.Repository.getById: knowledge with id: "+id+" has been removed.");
        return res;
    }
    @SuppressWarnings("unchecked")
    public <T extends Persistent> List<T> getByType(Class<T> prototype){
        List<T> res = (List<T>) DataUtils.getAllIf(dataRepository, prototype::isInstance);
        res.removeIf(Persistent::isClear);
        return res;
    }
    @SuppressWarnings("unchecked")
    public <T extends Persistent> List<T> getByKeyword(String keyword, Class<T> prototype){
        List<T> res = (List<T>) DataUtils.getAllIf(dataRepository, data -> data.isLike(keyword) && prototype.isInstance(data));
        res.removeIf(Persistent::isClear);
        return res;
    }
    public List<Persistent> getAll(){
        return dataRepository;
    }


    // remove
    public void remove(Persistent data){
        data.clear();
    }
    public <T extends Persistent> void removeById(int id, Class<T> prototype) throws DataClearedException {
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
