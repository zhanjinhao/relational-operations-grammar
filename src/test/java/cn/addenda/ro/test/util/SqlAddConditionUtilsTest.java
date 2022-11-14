package cn.addenda.ro.test.util;

import cn.addenda.ro.util.SqlAddConditionUtils;

/**
 * @author addenda
 * @datetime 2022/11/13 20:44
 */
public class SqlAddConditionUtilsTest {

    public static void main(String[] args) {

        String[] sqls = new String[]{
                "select t_week_flight.sqc from t_week_flight",
                "select t1.sqc from t_week_flight t1",
                "select t1.sqc from (select sqc, week_solution_sqc from t_week_flight) t1",
                "delete from t_week_flight",
                "delete from t_week_flight where sqc > 1",
                "update t_week_flight set sqc = 1 where sqc > 1",
                "update t_week_flight set sqc = 1 ",
                "insert into t_week_flight_bak ( sqc, week_solution_sqc) select sqc, week_solution_sqc from t_week_flight"
        };

        for (String sql : sqls) {
            System.out.println(SqlAddConditionUtils.addTableCondition(sql, "t_week_flight", "sqc > 1 and week_solution_sqc < 2"));
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("--------------------------------------------------------");
        System.out.println("--------------------------------------------------------");
        for (String sql : sqls) {
            System.out.println(SqlAddConditionUtils.addViewCondition(sql, "t_week_flight", "sqc > 1 and week_solution_sqc < 2"));
        }

    }

}
