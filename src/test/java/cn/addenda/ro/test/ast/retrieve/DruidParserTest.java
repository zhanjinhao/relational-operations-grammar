package cn.addenda.ro.test.ast.retrieve;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;
import java.util.Map;

/**
 * @Author ISJINHAO
 * @Date 2022/1/16 14:03
 */
public class DruidParserTest {

    public static void main(String[] args) {

        String dbType = JdbcConstants.MYSQL.toString();
        String sql = "select * from ts_user " +
                "where (select c.a from (select 1 as a from dual) c) > ts_user.a and (select c.a from (select true as a from dual) c)";

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        SQLStatement sqlStatement = stmtList.get(0);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);
        Map<TableStat.Name, TableStat> getTables = visitor.getTables();
        List<TableStat.Condition> conditions = visitor.getConditions();
        conditions.forEach(item -> {
            TableStat.Column column = item.getColumn();
            System.out.println(column);
        });

    }

}
