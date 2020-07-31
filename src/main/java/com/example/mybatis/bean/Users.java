package com.example.mybatis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author admin
 * @date 2020年07月31日 16时51分06秒
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Users extends BaseEntity {
    /**
     * 名称
     */
    private String userName;

    /**
     * 性别 1:男 2:女 3:未知
     */
    private Integer userSex;

    /**
     * 年龄
     */
    private Integer userAge;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机
     */
    private String userPhone;
}