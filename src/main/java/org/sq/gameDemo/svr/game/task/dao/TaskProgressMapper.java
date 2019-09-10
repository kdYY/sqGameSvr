package org.sq.gameDemo.svr.game.task.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.sq.gameDemo.svr.game.task.model.TaskProgress;
import org.sq.gameDemo.svr.game.task.model.TaskProgressExample;

import java.util.List;
@Mapper
@Repository
public interface TaskProgressMapper {
    int countByExample(TaskProgressExample example);

    int deleteByExample(TaskProgressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TaskProgress record);

    int insertSelective(TaskProgress record);

    List<TaskProgress> selectByExample(TaskProgressExample example);

    TaskProgress selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TaskProgress record, @Param("example") TaskProgressExample example);

    int updateByExample(@Param("record") TaskProgress record, @Param("example") TaskProgressExample example);

    int updateByPrimaryKeySelective(TaskProgress record);

    int updateByPrimaryKey(TaskProgress record);
}