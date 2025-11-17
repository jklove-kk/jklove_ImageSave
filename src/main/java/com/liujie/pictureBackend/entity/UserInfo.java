package com.liujie.pictureBackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
public class UserInfo {
    /**
     * 用户信息主键
     */
    @TableId
    private String user_no;

    /**
     * 账号
     */
    private String user_account;

    /**
     * 密码
     */
    private String user_password;

    /**
     * 用户昵称
     */
    private String user_name;

    /**
     * 用户头像
     */
    private String user_avatar;

    /**
     * 用户简介
     */
    private String user_profile;

    /**
     * 用户角色：user/admin
     */
    private String user_role;

    /**
     * 编辑时间
     */
    private Date edit_time;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_delete;

    /**
     * 用户信息主键
     */
    public String getUser_no() {
        return user_no;
    }

    /**
     * 用户信息主键
     */
    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    /**
     * 账号
     */
    public String getUser_account() {
        return user_account;
    }

    /**
     * 账号
     */
    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    /**
     * 密码
     */
    public String getUser_password() {
        return user_password;
    }

    /**
     * 密码
     */
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    /**
     * 用户昵称
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * 用户昵称
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * 用户头像
     */
    public String getUser_avatar() {
        return user_avatar;
    }

    /**
     * 用户头像
     */
    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    /**
     * 用户简介
     */
    public String getUser_profile() {
        return user_profile;
    }

    /**
     * 用户简介
     */
    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    /**
     * 用户角色：user/admin
     */
    public String getUser_role() {
        return user_role;
    }

    /**
     * 用户角色：user/admin
     */
    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    /**
     * 编辑时间
     */
    public Date getEdit_time() {
        return edit_time;
    }

    /**
     * 编辑时间
     */
    public void setEdit_time(Date edit_time) {
        this.edit_time = edit_time;
    }

    /**
     * 创建时间
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * 创建时间
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    /**
     * 更新时间
     */
    public Date getUpdate_time() {
        return update_time;
    }

    /**
     * 更新时间
     */
    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    /**
     * 是否删除
     */
    public Integer getIs_delete() {
        return is_delete;
    }

    /**
     * 是否删除
     */
    public void setIs_delete(Integer is_delete) {
        this.is_delete = is_delete;
    }
}