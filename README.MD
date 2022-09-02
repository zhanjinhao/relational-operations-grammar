# 项目描述

sql解析器。并可以对语法树进行分析，获取表、列、别名等元数据。

## 语法

参考：grammar.txt

## insert 测试

cn.addenda.ro.test.ast.create.InsertParserTest

## update 测试

cn.addenda.ro.test.ast.update.UpdateParserTest

## delete 测试

cn.addenda.ro.test.ast.delete.DeleteParserTest

## select 测试

cn.addenda.ro.test.ast.retrieve.SelectParserTest

# 依赖

无任何依赖。是一个非常单纯的工具项目。

# todo

1、增加窗口函数。 √

2、select 增加锁语法。 √

3、toString()支持分隔符为 ''。 √

4、支持group_concat()语法。 √

5、AstMetaData增加 ?计数 和 #{}计数。 √

6、limitSeg 仅支持 limit countNum offset skipNum 语法。 √

7、关键字和identifier转小写。 √

8、InCondition range模式，Literal vs Token。

9、被 `` 包裹的都是Identifier。 √