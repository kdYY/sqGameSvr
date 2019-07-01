package org.sq.gameDemo.svr.game.entity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.entity.dao.OrderRuleMapper;
import org.sq.gameDemo.svr.game.entity.model.OrderRule;
import org.sq.gameDemo.svr.game.entity.model.OrderRuleExample;

import java.util.List;

@Service
public class OrderRuleService {

    @Autowired
    private OrderRuleMapper orderRuleMapper;

    public List<OrderRule> getOrderRuleList() {
        return orderRuleMapper.selectByExample(new OrderRuleExample());
    }
}
