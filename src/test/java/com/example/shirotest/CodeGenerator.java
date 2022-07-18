package com.example.shirotest;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StrUtil.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        // 设置作者名
        gc.setAuthor("iinonglqh");
        gc.setOpen(false);  //当代码生成完成之后是否打开代码所在的文件夹
        //gc.setSwagger2(true); 实体属性 Swagger2 注解
        // 去掉service接口的前缀I
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/shiro-test?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        //dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // 模块名 例如：sys
        //pc.setModuleName(scanner("模块名"));
        pc.setParent("com.example.shirotest");
        //pc.setXml("mapper.xml");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //templateConfig.setEntity("templates/entity2.java");
        //templateConfig.setService();
        //templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 设置字段名和表名是否把下划线换成驼峰命名规则
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 这里如果不设置，会跟随上面实体类的命名设置
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 字段注解：这里我们把表字段的注解打开，比如@TableField("car_name")
        strategy.setEntityTableFieldAnnotationEnable(true);

        // 设置生成的实体类继承的父类
        //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");

        // lombok：是否开启Lombok的注解，比如@Data,@EqualsAndHashCode(callSuper = true)
        strategy.setEntityLombokModel(true);

        // 控制器的@RestController注解：是否开启，不开启则默认为@Controller
        strategy.setRestControllerStyle(true);

        // 公共父类
        //strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");

        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");

        //设置要生成哪些表  如果不设置就是生成所有的表
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        // 控制器的请求映射url风格：驼峰转连字符，比如"carList"-> "car-list"
        strategy.setControllerMappingHyphenStyle(true);

        // 生成实体时去掉表前缀
        //strategy.setTablePrefix(pc.getModuleName() + "_");
        // 如果不想输入模块名，就直接去掉表的前缀
        // 系统表（s_）：System; 字典表（d_）：Dictionary; 业务表（b_）：Business; 中间表（r_）：Relationship
        // 用户表（s_user）为主、角色（s_role）表为从，那中间表就命名为s_user_role
        //strategy.setTablePrefix("sys_");

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


}
