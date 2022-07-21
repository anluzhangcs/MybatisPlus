package org.zhang.mybatisplus.mapper;

/**
 * @author: zhanghao
 * @date: 2022/7/19-11:06
 */

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.zhang.mybatisplus.bean.Orders;

import java.util.List;

/**
 * @Description
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    Orders findOrdersById(@Param(Constants.WRAPPER) Wrapper<Orders> wrapper);

    IPage<Orders> selectCustomPage(IPage<?> page);

}
