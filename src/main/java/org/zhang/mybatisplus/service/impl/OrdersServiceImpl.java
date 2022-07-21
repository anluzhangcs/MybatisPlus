package org.zhang.mybatisplus.service.impl;

/**
 * @author: zhanghao
 * @date: 2022/7/20-10:26
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zhang.mybatisplus.bean.Orders;
import org.zhang.mybatisplus.mapper.OrdersMapper;
import org.zhang.mybatisplus.service.OrdersService;

/**
 * @Description
 */

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {


}
