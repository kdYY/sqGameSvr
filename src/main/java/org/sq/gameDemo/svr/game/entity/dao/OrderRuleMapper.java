package org.sq.gameDemo.svr.game.entity.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.sq.gameDemo.svr.game.entity.model.OrderRule;
import org.sq.gameDemo.svr.game.entity.model.OrderRuleExample;

import java.util.List;
@Mapper
@Repository
public interface OrderRuleMapper {
    int countByExample(OrderRuleExample example);

    int deleteByExample(OrderRuleExample example);

    int deleteByPrimaryKey(String orderName);

    int insert(OrderRule record);

    int insertSelective(OrderRule record);

    List<OrderRule> selectByExample(OrderRuleExample example);

    OrderRule selectByPrimaryKey(String orderName);

    int updateByExampleSelective(@Param("record") OrderRule record, @Param("example") OrderRuleExample example);

    int updateByExample(@Param("record") OrderRule record, @Param("example") OrderRuleExample example);

    int updateByPrimaryKeySelective(OrderRule record);

    int updateByPrimaryKey(OrderRule record);
}