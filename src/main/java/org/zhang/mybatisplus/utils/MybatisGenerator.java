package org.zhang.mybatisplus.utils;

/**
 * @author: zhanghao
 * @date: 2022/7/19-17:52
 */

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * @Description
 */
public class MybatisGenerator {

    public static void main(String[] args) {
        //1、配置数据源
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/test?serverTimezone=UTC", "root", "root")
                //2、全局配置
                .globalConfig(builder -> {
                    builder.author("Zhanghao") // 设置作者名
                            .outputDir(System.getProperty("user.dir") + "/src/main/java")   //设置输出路径：项目的 java 目录下
                            .commentDate("yyyy/MM/dd-hh:mm")   //注释日期
                            .fileOverride()   //覆盖之前的文件
                            .disableOpenDir();   // 生成结束后是否打开文件夹
                })
                /*3、包配置
                 * 一般只需要设置父包名和实体类包名(默认为entity),其他的默认就行
                 **/
                .packageConfig(builder -> {
                    builder.parent("org.zhang.mp") // 设置父包名
                            .entity("bean")   //pojo 实体类包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "/src/main/resources/mapper"));    //配置 mapper.xml 路径信息：项目的 resources 目录下
                })
                //4、策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("user", "orders") // 设置需要生成的数据表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀

                            //4.1、Mapper策略配置
                            .mapperBuilder().superClass(BaseMapper.class)   //设置父类
                            .formatMapperFileName("%sMapper")   //格式化 mapper 文件名称
                            .formatXmlFileName("%sMapper")//格式化 Xml 文件名称

                            //4.2、service 策略配置
                            .serviceBuilder().formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                            .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                            .superServiceClass(IService.class) //	设置 service 接口父类
                            .superServiceImplClass(ServiceImpl.class) //设置 service 实现类父类


                            //4.3、实体类策略配置
                            .entityBuilder().enableLombok() //开启 Lombok
                            .disableSerialVersionUID()  //不实现 Serializable 接口，不生产 SerialVersionUID
                            .logicDeleteColumnName("deleted")   //逻辑删除字段名
                            .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                            .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命
                            .addTableFills(new Column("create_time", FieldFill.INSERT), new Column("modify_time", FieldFill.INSERT_UPDATE))   //添加表字段填充，"create_time"字段自动填充为插入时间，"modify_time"字段自动填充为插入修改时间
                            .enableTableFieldAnnotation()       // 开启生成实体时生成字段注解

                            //4.4、Controller策略配置
                            .controllerBuilder().formatFileName("%sController"); //格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
                })
                //5、模板
                .templateEngine(new FreemarkerTemplateEngine())
                //6、执行
                .execute();
    }

}
