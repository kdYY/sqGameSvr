package org.sq.gameDemo.aov;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.svr.game.characterEntity.model.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

@Component
public class PlayerModuleInitializer implements ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initModuleList();
    }
    // 玩家模块entity初始化操作集
    private List<IPlayerDataOperate> moduleInitEntityList;
    // 玩家模块data初始化操作集
    private List<IPlayerDataOperate> moduleInitDataList;
    
    private void initModuleList() {
        this.moduleInitEntityList = Lists.newArrayList(applicationContext.getBeansOfType(IPlayerDataOperate.class).values());
        this.moduleInitDataList = create();
    }
    

    private List<IPlayerDataOperate> create() {
        List<IPlayerDataOperate> moduleInitList = applicationContext.getBeansOfType(IPlayerDataOperate.class).values().stream().filter(operate -> !isCustomCall(operate.getClass())).collect(Collectors.toList());
        //注册有模块顺序关系的模块类型
        Map<Class<?>, ModuleInitDefinition> moduleInitDefinitionMap = createModule2InitDefineMap(moduleInitList);
        //生成aov网
        Map<Class<?>, VertexNode> module2VertexNodeMap = createModuleVertexNodeMap(moduleInitDefinitionMap);
        //拓扑排序
        return topoLogicSort(moduleInitDefinitionMap, module2VertexNodeMap);
    }

    private List<IPlayerDataOperate> topoLogicSort(Map<Class<?>, ModuleInitDefinition> moduleInitDefinitionMap, Map<Class<?>, VertexNode> module2VertexNodeMap) {
        List<ModuleInitDefinition> moduleInitDefinitions = Lists.newArrayList(moduleInitDefinitionMap.values());
        //将入度为0的模块入栈
        Stack<Class<?>> stack = new Stack<>();
        module2VertexNodeMap.values().stream().filter(vertexNode -> vertexNode.in == 0).forEach(vertexNode -> stack.add(vertexNode.vertex));
        
        List<IPlayerDataOperate> orderList = Lists.newArrayList();
        while(!stack.empty()) {
            //取入度为0的顶点 准备删边
            Class<?> beforClz = stack.pop();
            EdgeNode node = module2VertexNodeMap.get(beforClz).firstEdge;
            
            while(node != null) {
                Class<?> afterClaz = node.adjVex;
                VertexNode vertexNode = module2VertexNodeMap.get(afterClaz);
                vertexNode.in --;
                if(vertexNode.in == 0) {
                    stack.push(vertexNode.vertex);
                }
                node = node.next;
            }
            ModuleInitDefinition definition = moduleInitDefinitionMap.get(beforClz);
            orderList.add(definition.operate);
            moduleInitDataList.remove(definition);
        }
        if(moduleInitDefinitions.size() > 0) {
            throw new IllegalStateException("初始化错误");
        }
        return orderList;
    }

    private Map<Class<?>, VertexNode> createModuleVertexNodeMap( Map<Class<?>, ModuleInitDefinition> moduleInitDefinitionMap) {
        Map<Class<?>, VertexNode> moduleVertexNodeMap = Maps.newHashMap();
        for (Map.Entry<Class<?>, ModuleInitDefinition> entry : moduleInitDefinitionMap.entrySet()) {
            Class<?> moduleClz = entry.getKey();
            ModuleInitDefinition definition = entry.getValue();
            VertexNode afterVertexNode = moduleVertexNodeMap.computeIfAbsent(moduleClz, key -> new VertexNode(0, moduleClz, null));
            if(definition.afterModules != null) {
                for (Class<?> beforeMuduleClz : definition.afterModules) {
                    if(!moduleInitDefinitionMap.containsKey(beforeMuduleClz)) {
                        throw new IllegalStateException("beforeMuduleClz自定义调用了，moduleClz不能标记在它之后");
                    }
                    //增加模块关系的有向边
                    VertexNode beforeVertexNode = moduleVertexNodeMap.computeIfAbsent(beforeMuduleClz, key -> new VertexNode(0, beforeMuduleClz, null));
                    beforeVertexNode.addEdgeNode(new EdgeNode(moduleClz, null));
                    //入度+1
                    afterVertexNode.in++;
                }
            }
            
        }
        return moduleVertexNodeMap;
    }

    private Map<Class<?>, ModuleInitDefinition> createModule2InitDefineMap(List<IPlayerDataOperate> moduleInitList) {
        Map<Class<?>, ModuleInitDefinition> map = Maps.newHashMap();
        for (IPlayerDataOperate operate : moduleInitList) {
            Class<? extends IPlayerDataOperate> operateClass = operate.getClass();
            map.put(operateClass, createDefinition(operate));
        }
        return map;
    }

    private ModuleInitDefinition createDefinition(IPlayerDataOperate operate) {
        Class<? extends IPlayerDataOperate> operateClass = operate.getClass();
        Method method = getInitDataMethod(operateClass);
        assert method != null;
        PlayerInitModule annotation = method.getAnnotation(PlayerInitModule.class);
        Class<?>[] afterModules = null;
        if(annotation != null) {
            afterModules = annotation.after();
            for (Class<?> moduleclz : afterModules) {
                if(moduleclz == operateClass) {
                    throw new IllegalStateException("自环");
                }
            }
        }
        return new ModuleInitDefinition(afterModules, operate);
    }

    private boolean isCustomCall(Class<? extends IPlayerDataOperate> aClass) {
        Method initDataMethod = getInitDataMethod(aClass);
        if(initDataMethod == null) {
            return false;
        }
        PlayerInitModule annotation = initDataMethod.getAnnotation(PlayerInitModule.class);
        if(annotation == null) {
            return false;
        }
        return annotation.customCall();
    }
    
    private Method getInitDataMethod(Class<? extends IPlayerDataOperate> aClass) {
        try {
            return aClass.getDeclaredMethod("initData", Player.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class ModuleInitDefinition {
        private Class<?>[] afterModules;
        private IPlayerDataOperate operate;

        public ModuleInitDefinition(Class<?>[] afterModules, IPlayerDataOperate operate) {
            this.afterModules = afterModules;
            this.operate = operate;
        }
    }

    /**
     * 顶点表节点
     */
    private class VertexNode {
        /**
         * 顶点入度
         */
        private int in;

        /**
         * 该顶点
         */
        private Class<?> vertex;

        /**
         * 边表头指针
         */
        private EdgeNode firstEdge;

        public VertexNode(int in, Class<?> vertex, EdgeNode firstEdge) {
            this.in = in;
            this.vertex = vertex;
            this.firstEdge = firstEdge;
        }
        
        
        void addEdgeNode(EdgeNode edgeNode) {
            if(firstEdge == null) {
                firstEdge = edgeNode;
                return;
            }
            firstEdge.addNext(edgeNode);
        }
    }

    private class EdgeNode {
        /**
         * 该顶点
         */
        private Class<?> adjVex;

        /**
         * 指向下一节点
         */
        private EdgeNode next;


        public EdgeNode(Class<?> adjVex, EdgeNode next) {
            this.adjVex = adjVex;
            this.next = next;
        }
        
        void addNext(EdgeNode edgeNode) {
            if(next == null) {
                next = edgeNode;
                return;
            }
            addNext(next.next);
        }
    }
}
