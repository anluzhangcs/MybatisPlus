package org.zhang.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zhang.mybatisplus.bean.Orders;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author: zhanghao
 * @date: 2022/7/20-10:28
 */
@SpringBootTest
class OrdersServiceTest {

    @Autowired
    private OrdersService ordersService;

    @Test
    void testInsertFill() {
        Orders orders = new Orders();
        orders.setId(51L);
        ordersService.save(orders);
    }

    @Test
    void testUpdateFill() {
        Orders orders = new Orders();
//        orders.setId(12L);
//        orders.setPrice(99);
        ordersService.update(orders,null);
    }

    @Test
    void testLogicDelete() {
        ordersService.removeById(2L);
    }

    @Test
    void testLogicDelete02() {
        ordersService.list();
    }

    @Test
    void lock() {
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", 2L);
        ordersService.update(updateWrapper);

    }
}