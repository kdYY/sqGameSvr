package org.sq.gameDemo.svr.game.guild.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.sq.gameDemo.svr.game.guild.model.Guild;
import org.sq.gameDemo.svr.game.guild.model.GuildExample;

import java.util.List;
@Mapper
@Repository
public interface GuildMapper {
    int countByExample(GuildExample example);

    int deleteByExample(GuildExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Guild record);

    int insertSelective(Guild record);

    List<Guild> selectByExample(GuildExample example);

    Guild selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Guild record, @Param("example") GuildExample example);

    int updateByExample(@Param("record") Guild record, @Param("example") GuildExample example);

    int updateByPrimaryKeySelective(Guild record);

    int updateByPrimaryKey(Guild record);
}