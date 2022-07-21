package org.zhang.mybatisplus.mapper;

/**
 * @author: zhanghao
 * @date: 2022/7/18-10:10
 */


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.zhang.mybatisplus.bean.User;

/**
 * @Description 继承BaseMapper即可对数据库进行简单操作
 *              无需自己写方法及在xml映射文件中写SQL
 *     注:需要被@Mapper声明或者在启动类中用@MapperScan()配置扫描包来表明这是一个Mapper
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {

    User findUserById(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

}
