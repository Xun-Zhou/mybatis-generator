package com.example.mybatis.dao;

import com.example.mybatis.bean.Users;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @date 2020年07月31日 18时15分36秒
 */
@Mapper
public interface UsersDao {
    /**
     * 删除
     *
     * @param id 主键ID
     * @return 影响数据条数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
     *
     * @param record 数据实体
     * @return 影响数据条数
     */
    int insert(Users record);

    /**
     * 插入Selective
     *
     * @param record 数据实体
     * @return 影响数据条数
     */
    int insertSelective(Users record);

    /**
     * 根据主键查询
     *
     * @param id 主键ID
     * @return 数据实体
     */
    Optional<Users> selectByPrimaryKey(Long id);

    /**
     * 更新Selective
     *
     * @param record 数据实体
     * @return 影响数据条数
     */
    int updateByPrimaryKeySelective(Users record);

    /**
     * 更新
     *
     * @param record 数据实体
     * @return 影响数据条数
     */
    int updateByPrimaryKey(Users record);
}