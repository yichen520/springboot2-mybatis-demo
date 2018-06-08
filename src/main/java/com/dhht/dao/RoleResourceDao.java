package com.dhht.dao;

import com.dhht.model.RoleResourceExample;
import com.dhht.model.RoleResourceKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleResourceDao {
    int countByExample(RoleResourceExample example);

    int deleteByExample(RoleResourceExample example);

    int deleteByPrimaryKey(RoleResourceKey key);
    int  deleteRole(@Param("roleId")String id);
    int insert(RoleResourceKey record);

    int insertSelective(RoleResourceKey record);

    List<RoleResourceKey> selectByExample(RoleResourceExample example);

    List<RoleResourceKey> selectByRoleID(@Param("roleId")String roleId);

    int updateByExampleSelective(@Param("record") RoleResourceKey record, @Param("example") RoleResourceExample example);

    int updateByExample(@Param("record") RoleResourceKey record, @Param("example") RoleResourceExample example);
}