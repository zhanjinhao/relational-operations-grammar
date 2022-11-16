package cn.addenda.ro.test.util;

import cn.addenda.ro.util.SqlAddConditionUtils;

/**
 * @author addenda
 * @datetime 2022/11/13 20:44
 */
public class SqlAddConditionUtilsTest {

    public static void main(String[] args) {

        String[] sqls = new String[]{

                "insert  into table_listnames ( name, address, tele )  " +
                        "select *  from  (  select 'rupert', 'somewhere', '022' from dual   )  tmp  " +
                        "where  not exists  (  select name  from t_week_flight   where name  = 'rupert'  )  limit 1",

                "select a, b"
                        + "  from tab2 t cross join tab3 left join tab3 on tab4.e = tab2.e, (select * from tab5) t5\n"
                        + " where t.m = ?\n"
                        + "   and exists (select 1\n"
                        + "                 from tab4 t4\n"
                        + "                where t1.n = t4.n)\n"
                        + "   and t.tm >= '2016-11-11'",

                "select * from t_week_flight t1 left join t_flight_plan t2 on t1.sqc = t2.week_flight_sqc",
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
