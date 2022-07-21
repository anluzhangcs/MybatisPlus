package org.zhang.mybatisplus.service.impl;

/**
 * @author: zhanghao
 * @date: 2022/7/19-17:21
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zhang.mybatisplus.bean.User;
import org.zhang.mybatisplus.mapper.UserMapper;
import org.zhang.mybatisplus.service.UserService;

/**
 * @Description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
