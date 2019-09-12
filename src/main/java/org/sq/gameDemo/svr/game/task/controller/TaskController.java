package org.sq.gameDemo.svr.game.task.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.TaskPt;
import org.sq.gameDemo.common.proto.TradePt;
import org.sq.gameDemo.svr.common.Constant;
import org.sq.gameDemo.svr.common.OrderMapping;
import org.sq.gameDemo.svr.common.customException.CustomException;
import org.sq.gameDemo.svr.common.dispatch.ReqParseParam;
import org.sq.gameDemo.svr.common.dispatch.RespBuilderParam;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;
import org.sq.gameDemo.svr.game.characterEntity.service.EntityService;
import org.sq.gameDemo.svr.game.task.model.Task;
import org.sq.gameDemo.svr.game.task.model.TaskProgress;
import org.sq.gameDemo.svr.game.task.service.TaskService;
import org.sq.gameDemo.svr.game.transaction.model.DealTrade;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private EntityService entityService;
    /**
     * 查看当前可领取任务
     */
    @OrderMapping(OrderEnum.SHOW_TASK_CAN_ACCEPT)
    public MsgEntity showTaskCanAccpet(MsgEntity msgEntity,
                                  @RespBuilderParam TaskPt.TaskResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<TaskProgress> tasks = taskService.showTaskCanAccept(player);
        for (TaskProgress tp : tasks) {
            try {
                builder.addTask(taskService.TaskProTransformTaskPt(tp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }


    /**
     * 查看正在进行的任务
     */
    @OrderMapping(OrderEnum.SHOW_TASK)
    public MsgEntity showtask(MsgEntity msgEntity,
                                  @RespBuilderParam TaskPt.TaskResponseInfo.Builder builder) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        List<TaskProgress> tps = taskService.showTaskProgress(player);
        for (TaskProgress tp : tps) {
            try {
                builder.addTask(taskService.TaskProTransformTaskPt(tp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.setResult(Constant.SUCCESS);
        msgEntity.setData(builder.build().toByteArray());
        return msgEntity;
    }

    /**
     * 激活任务
     */
    @OrderMapping(OrderEnum.ACCEPT_TASK)
    public void accpetTask(MsgEntity msgEntity,
                            @ReqParseParam TaskPt.TaskRequestInfo requestInfo) {
        Player player = entityService.getPlayer(msgEntity.getChannel());
        taskService.acceptTask(player, requestInfo.getTaskId());
//        return msgEntity;
    }

}
