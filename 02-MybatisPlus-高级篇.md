# MybatisPlus从入门到精通-高级篇

## 1.自动填充 

​	在实际项目中的表会和我们的orders表一样，有**更新时间**，**创建时间**，**创建人，更新人**等字段。

​	我们可以使用 `@TableField` 的 `fill`  属性来设置字段的自动填充。让我们能更方便的更新相关字段。

注意事项：
* 填充原理是直接给entity的属性设置值!!!
* 注解则是指定该属性在对应情况下必有值,如果无值则入库会是null
* MetaObjectHandler提供的默认方法的策略均为:如果属性有值则不覆盖,如果填充值为null则不填充
* 字段必须声明TableField注解,属性fill选择对应策略,该声明告知Mybatis-Plus需要预留注入SQL字段
* 填充处理器MyMetaObjectHandler在 Spring Boot 中需要声明@Component或@Bean注入
* 要想根据注解FieldFill.xxx和字段名以及字段类型来区分必须使用父类的strictInsertFill或者strictUpdateFill方法
* 不需要根据任何来区分可以使用父类的fillStrategy方法
* update(T t,Wrapper updateWrapper)时t不能为空,否则自动填充失效


### 示例

①在对应字段上增加注解

​	使用TableField注解的fill属性来标注此字段在哪种情况下需要自动填充，且会在对应的SQL中为我们预留注入字段

~~~~java
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
~~~~





②自定义填充处理器实现MetaObjectHandler 并放到容器中

​   重写insertFill和updateFill方法,通过元对象向entity属性注入值

~~~~java
@Component
public class MetaObjectHanderUtil implements MetaObjectHandler {

    //insert操作时被调用
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
    }

    //update操作时被调用
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
    }
}

~~~~

​	

## 2.逻辑删除

​	MP也支持逻辑删除的处理。我们只需要配置好逻辑删除的实体字段名，代表删除的字段值和代表未删除的字段值后即可。

此时删除就相当于更新逻辑删除字段设置为已删除

查询和更新也会过滤掉逻辑删除字段为已删除

​	注意：如果3.3.0版本之前还需要在对应的字段上加上`@TableLogic`注解

~~~~yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: delFlag  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
~~~~









## 3.乐观锁

​	并发操作时,我们需要保证对数据的操作不发生冲突。乐观锁就是其中一种方式。乐观锁就是先加上不存在并发冲突问题，在进行实际数据操作的时候再检查是否冲突。

​	我们在使用乐观锁时一般在表中增加一个version列。用来记录我们对每天记录操作的版本。每次对某条记录进行过操作是，对应的版本也需要+1。

​	然后我们在每次要进行更新操作时，先查询对应数据的version值。在执行更新时， set version = 老版本+1 where version = 老版本 

​	如果在查询老版本号到更新操作的中间时刻有其他人更新了这条数据，这样这次更新语句就会更新失败。

​	这里在更新时对version的操作如果有我们自己做就会显的有点麻烦。所以MP提供了乐观锁插件。

​	使用后我们就可以非常方便的实现对version的操作。



### 3.1 使用

①配置对应插件

~~~~java
@Configuration
public class MybatisPlusConfig {
    /**
     * 新版
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
~~~~



②在实体类的字段上加上`@Version`注解

~~~~java
@Version
private Integer version;
~~~~

③更新

**注意：在更新前我们一定要先查询到version设置到实体类上再进行更新才能生效**

~~~~java
    @Test
    public void testVersion(){
        //查询id为3的数据
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",3);
        Orders orders = ordersMapper.selectOne(queryWrapper);


        //对id为3的数据进行更新  把price修改为88
        orders.setPrice(88);
        ordersMapper.updateById(orders);
    }
~~~~

这种情况下我们可以看到执行的sql已经发生了变化。

~~~~
==>  Preparing: UPDATE orders SET price=?, update_time=?, version=? WHERE id=? AND version=? AND del_flag=0
==> Parameters: 8888(Integer), null, 2(Integer), 2(Long), 1(Integer)
~~~~





### 3.2 多插件配置问题

​	我们在使用3.4.0版本以后的MP时，如果需要用到多个插件的话要注意。在配置的时候只需要注入一个MybatisPlusInterceptor 对象，把插件对象添加到MybatisPlusInterceptor 对象中即可。

​	例如：

~~~~java
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
~~~~



