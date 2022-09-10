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

01、增加窗口函数。 √

02、select 增加锁语法。 √

03、toString()支持分隔符为 ''。 √

04、支持group_concat()语法。 √

05、AstMetaData增加 ?计数 和 #{}计数。 √

06、limitSeg 仅支持 limit countNum offset skipNum 语法。 √

07、关键字和identifier转小写。 √

08、InCondition range模式，Literal vs Token。

09、被 `` 包裹的都是Identifier。 √

10、FunctionDescriptor 增加 isIndependent 方法

11、CurdUtils 按 FunctionEvaluator 进行缓存。 √

12、InCondition 作为 Expression 的语法。 √

13、增加ExpressionVisitor，不用于Delegate。 √