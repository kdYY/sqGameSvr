package org.sq.gameDemo.svr.game.bag.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.sq.gameDemo.svr.game.bag.model.Bag;
import org.sq.gameDemo.svr.game.bag.model.BagExample;

public interface BagMapper {
    int countByExample(BagExample example);

    int deleteByExample(BagExample example);

    int deleteByPrimaryKey(Long playerId);

    int insert(Bag record);

    int insertSelective(Bag record);

    List<Bag> selectByExample(BagExample example);

    Bag selectByPrimaryKey(Long playerId);

    int updateByExampleSelective(@Param("record") Bag record, @Param("example") BagExample example);

    int updateByExample(@Param("record") Bag record, @Param("example") BagExample example);

    int updateByPrimaryKeySelective(Bag record);

    int updateByPrimaryKey(Bag record);
}