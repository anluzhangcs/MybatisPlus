package org.zhang.mybatisplus.bean;

/**
 * @author: zhanghao
 * @date: 2022/7/18-10:07
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.zhang.mybatisplus.mapper.UserMapper;

/**
 * @Description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private Integer age;
    private String address;
}
