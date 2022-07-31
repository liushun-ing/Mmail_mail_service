package com.example.smtpserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ls
 * @since 2022-04-30
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 1普通用户 0 管理员
     */
    private Integer type;

    /**
     * 权限
     */
    private Integer authority;

    /**
     * 头像图片的base64编码，可以直接在前端显示
     */
    private String avatar;

    /**
     * 是否禁用
     */
    private Integer isDisabled;

}
