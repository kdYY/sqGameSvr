package org.sq.gameDemo.svr.game.transaction.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.sq.gameDemo.svr.game.transaction.model.DealTrade;
import org.sq.gameDemo.svr.game.transaction.model.Trade;
import org.sq.gameDemo.svr.game.transaction.model.TradeExample;

import java.util.List;
@Mapper
@Repository
public interface TradeMapper {
    int countByExample(TradeExample example);

    int deleteByExample(TradeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Trade record);

    int insertSelective(Trade record);

    List<Trade> selectByExample(TradeExample example);

    Trade selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Trade record, @Param("example") TradeExample example);

    int updateByExample(@Param("record") Trade record, @Param("example") TradeExample example);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);

    List<Trade> selectDealHistory(Integer unId);

    List<Trade> selectOnlineTradeHistory(Integer unId);
}