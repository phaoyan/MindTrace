package pers.juumii.MindTrace.model.mapper.utils;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListTypeHandler implements TypeHandler<List<Integer>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<Integer> ts, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, stringify(ts));
    }

    private String stringify(List<Integer> ts) {
        StringBuilder res = new StringBuilder();
        if(ts == null){
            return null;
        }
        else if(ts.size() == 1)
            res.append(ts.get(0).toString());
        else if(ts.size() > 1){
            res.append(ts.get(0).toString());
            for(Integer item: ts.subList(1, ts.size()-1))
                res.append(",").append(item.toString());
        }
        return res.toString();
    }

    @Override
    public List<Integer> getResult(ResultSet resultSet, String s) throws SQLException {
        List<Integer> res = new ArrayList<>();
        String strArray = resultSet.getString(s);
        if(strArray == null || strArray.equals(""))
            return res;
        for(String str: strArray.split(","))
            res.add(Integer.parseInt(str));
        return res;
    }

    @Override
    public List<Integer> getResult(ResultSet resultSet, int i) throws SQLException {
        List<Integer> res = new ArrayList<>();
        for(String str: resultSet.getString(i).split(","))
            res.add(Integer.parseInt(str));
        return res;
    }

    @Override
    public List<Integer> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
