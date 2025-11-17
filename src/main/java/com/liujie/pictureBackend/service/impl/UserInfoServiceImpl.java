package com.liujie.pictureBackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liujie.pictureBackend.service.UserInfoService;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_info(用户信息表)】的数据库操作Service实现
* @createDate 2025-11-17 16:51:00
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {

}




