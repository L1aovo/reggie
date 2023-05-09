# 瑞吉外卖
照黑马学着码了一遍，自己实现了OrderController，UserController部分代码（课程中未给出的）


## 学习所得
了解了基本的JavaWeb开发，springboot的简单使用，mybatisplus的简单使用

发现了一堆bug，即员工和客户的权限没有区分，使得客户也可以使用员工的api接口

发现安全漏洞：课程原版中对文件下载没有做校验使得可以下载任意文件，我对此进行了修复

## 不足

等我后面有空或者学到了回来补一下，权限校验这部分的东西 -- shiro

## 吐槽

更多的是根据前端的需要来设计后端返回的数据 


## 项目结构
```text
D:.
└─com
    └─itheima
        └─reggie
            │  ReggieApplication.java
            │
            ├─common # 一些全局的东西，比如字段填充，线程处理，全局异常处理等等
            │      BaseContext.java
            │      CustomException.java
            │      GlobalExceptionHandler.java
            │      JacksonObjectMapper.java
            │      MyMetaObjectHandler.java
            │      R.java
            │
            ├─config # 一些配置，例如静态资源映射，mybatisPlus设置等等
            │      MybatisPlusConfig.java
            │      WebMvcConfig.java
            │
            ├─controller # 路由文件，用于处理请求
            │      AddressBookController.java
            │      CategoryController.java
            │      CommonController.java
            │      DishController.java
            │      EmployeeController.java
            │      OrderController.java
            │      OrderDetailController.java
            │      SetmealController.java
            │      ShoppingCartController.java
            │      UserController.java
            │
            ├─dto # 数据传输对象（DTO）
            │      DishDto.java
            │      SetmealDto.java
            │
            ├─entity # 路由文件，用于处理请求
            │      AddressBook.java
            │      Category.java
            │      Dish.java
            │      DishFlavor.java
            │      Employee.java
            │      OrderDetail.java
            │      Orders.java
            │      Setmeal.java
            │      SetmealDish.java
            │      ShoppingCart.java
            │      User.java
            │
            ├─filter # 拦截器
            │      LoginCheckFilter.java
            │
            ├─mapper # 包含Mybatis的映射器接口，用于将Java对象映射到数据库表中的行和列
            │      AddressBookMapper.java
            │      CategoryMapper.java
            │      DishFlavorMapper.java
            │      DishMapper.java
            │      EmployeeMapper.java
            │      OrderDetailMapper.java
            │      OrderMapper.java
            │      SetmealDishMapper.java
            │      SetmealMapper.java
            │      ShoppingCartMapper.java
            │      UserMapper.java
            │
            ├─service # 包含服务接口和服务实现类，服务接口定义了业务逻辑方法，服务实现类提供了服务接口中定义的方法的具体实现
            │  │  AddressBookService.java
            │  │  CategoryService.java
            │  │  DishFlavorService.java
            │  │  DishService.java
            │  │  EmployeeService.java
            │  │  OrderDetailService.java
            │  │  OrderService.java
            │  │  SetmealDishService.java
            │  │  SetmealService.java
            │  │  ShoppingCartService.java
            │  │  UserService.java
            │  │
            │  └─impl # mybatisPlus的东西，实现等等
            │          AddressBookServiceImpl.java
            │          CategoryServiceImpl.java
            │          DishFlavorServiceImpl.java
            │          DishServiceImpl.java
            │          EmployeeServiceImpl.java
            │          OrderDetailServiceImpl.java
            │          OrderServiceImpl.java
            │          SetmealDishServiceImpl.java
            │          SetmealServiceImpl.java
            │          ShoppingCartServiceImpl.java
            │          UserServiceImpl.java
            │
            └─utils # 工具类-短信 没怎么用上
                    SMSUtils.java
                    ValidateCodeUtils.java
```
