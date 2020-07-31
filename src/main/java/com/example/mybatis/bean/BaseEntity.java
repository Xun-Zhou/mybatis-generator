package com.example.mybatis.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author admin
 * @date 2020年07月31日 15时00分00秒
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 是否删除0:正常 1:删除
     */
    private Integer isDeleted;
}
