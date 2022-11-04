package pers.juumii.MindTrace.model.data;

import java.lang.reflect.Field;

/**
 * 可存入数据库，受Repository托管的数据
 */
public interface Persistent {
    void setId(int id);
    int getId();
    default void clear(){
        for (Field field: getClass().getDeclaredFields()){
            try {
                field.setAccessible(true);
                if(field.getName().equals("id"))
                    continue;
                else if (field.get(this) instanceof Integer)
                    field.set(this, 0);
                else if (field.get(this) instanceof Double)
                    field.set(this, 0);
                else if (field.get(this) instanceof Boolean)
                    field.set(this, false);
                else field.set(this, null);
            } catch (IllegalAccessException ignored) {}
        }
    }
    default boolean isClear(){
        for(Field field: getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                if(field.getName().equals("id"))
                    continue;
                else if(value instanceof Integer && (int)value != 0)
                    return false;
                else if(value instanceof Double && (double)value != 0)
                    return false;
                else if(value instanceof Boolean && (boolean) value)
                    return false;
                else if(!(value instanceof Integer) &&
                        !(value instanceof Double) &&
                        !(value instanceof Boolean) &&
                        value != null)
                    return false;
            } catch (IllegalAccessException ignored) {}
        }
        return true;
    }
    boolean isLike(String keyword);
}
