package com.liujie.pictureBackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liujie.pictureBackend.common.SnowFlake;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.exception.BusinessException;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.mapper.UserInfoMapper;
import com.liujie.pictureBackend.model.enums.UserRoleEnum;
import com.liujie.pictureBackend.service.UserInfoService;
import com.liujie.pictureBackend.utils.common.passWordUtils;
import com.liujie.pictureBackend.utils.common.uuidUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Administrator
 * @description 针对表【user_info(用户信息表)】的数据库操作Service实现
 * @createDate 2025-11-17 16:51:00
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return
     */
    @Override
    public String userRegister(String userAccount, String userPassword, String checkPassword) {

        //注册流程
        //1.参数规范校验
        if(StrUtil.isEmpty(userAccount)||StrUtil.isEmpty(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码为空");
        }

        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码确认错误");
        }

        //账号查重
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名重复");
        }

        //TODO 自定义密码规则检验

        //2.注册成功信息保存

        //密码加密
        String encryptPassword= passWordUtils.getEncryptPassword(userPassword);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserNo(String.valueOf(new SnowFlake().nextId()));
        userInfo.setUserAccount(userAccount);
        userInfo.setUserPassword(encryptPassword);
        userInfo.setCreateTime(new Date());
        userInfo.setUserRole(UserRoleEnum.USER.getValue());
        userInfo.setUserName(uuidUtils.getUuid(10));
        if(!this.save(userInfo)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户信息注册失败，数据库错误");
        }

        return userInfo.getUserNo();
    }
}




