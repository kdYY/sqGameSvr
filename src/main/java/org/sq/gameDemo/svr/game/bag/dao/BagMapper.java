package org.sq.gameDemo.svr.game.bag.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.BagExample;

import java.util.List;
@Mapper
@Repository
public interface BagMapper {
    int countByExample(BagExample example);

    int deleteByExample(BagExample example);

    int deleteByPrimaryKey(Integer unId);

    int insert(Bag record);

    int insertSelective(Bag record);

    List<Bag> selectByExample(BagExample example);

    Bag selectByPrimaryKey(Integer unId);

    int updateByExampleSelective(@Param("record") Bag record, @Param("example") BagExample example);

    int updateByExample(@Param("record") Bag record, @Param("example") BagExample example);

    int updateByPrimaryKeySelective(Bag record);

    int updateByPrimaryKey(Bag record);
}