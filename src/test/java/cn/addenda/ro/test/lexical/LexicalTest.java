package cn.addenda.ro.test.lexical;

import cn.addenda.ro.grammar.lexical.scan.DefaultScanner;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;

/**
 * @Author ISJINHAO
 * @Date 2021/12/7 19:16
 */
public class LexicalTest {

    static String[] sqlList = new String[]{
            "select * from SCORE where C < '12'",
//
//            "select * from STUDENT order by CLASS desc",
//
//            "select * from SCORE order by CNO asc, DEGREE desc",
//
//            "select a.SNO, a.CNO from SCORE a, (select max(DEGREE) as MAX from SCORE) b where b.MAX = a.DEGREE",
//
//            "select SNO, CNO from SCORE where DEGREE in (select max(DEGREE) from SCORE)",
//
//            "select avg(DEGREE) from SCORE where CNO = '3-105'",
//
//            "select avg(DEGREE) from SCORE where CNO like '3%' group by CNO having count(CNO) > 5",
//
//            "select SNO from SCORE group by SNO having min(DEGREE) > 70 and max(DEGREE) < 90",
//
//            "select STUDENT.SNAME, SCORE.CNO, SCORE.DEGREE from STUDENT, SCORE where STUDENT.SNO = SCORE.SNO",
//
//            "select SCORE.SNO, COURSE.CNAME, SCORE.DEGREE from SCORE, COURSE where SCORE.CNO = COURSE.CNO",
//
//            "select STUDENT.SNAME, COURSE.CNAME, SCORE.DEGREE \n" +
//                    "from SCORE, COURSE, STUDENT where STUDENT.SNO = SCORE.SNO and SCORE.CNO = COURSE.CNO",
//
//            "select SCORE.SNO, SCORE.CNO, GRADE.RANK \n" +
//                    "from SCORE, GRADE where SCORE.DEGREE >= GRADE.LOW and SCORE.DEGREE <= GRADE.UPP",
//
//            "select * from SCORE \n" +
//                    "where \n" +
//                    "\tSCORE.CNO = '3-105' \n" +
//                    "\tand \n" +
//                    "\t(select DEGREE from SCORE where SNO = '109' and CNO = '3-105') < SCORE.DEGREE",
//
//            "select * \n" +
//                    "from SCORE a, (select SNO, max(DEGREE) as MAXDEGREE from SCORE group by SNO having count(*) > 1) b \n" +
//                    "where b.SNO = a.SNO and b.MAXDEGREE > a.DEGREE",
//
//            "select * from SCORE a \n" +
//                    "where \n" +
//                    "\t((select max(b.DEGREE) from SCORE b where a.SNO = b.SNO) > a.DEGREE) \n" +
//                    "\tand \n" +
//                    "\t(a.SNO in (select c.SNO from SCORE c group by c.SNO having count(*) > 1))",
//
//            "select * from SCORE where (select DEGREE from SCORE where SNO = '109' and CNO = '3-105') < DEGREE",
//
//            "select SNO, SNAME, SBIRTHDAY from STUDENT \n" +
//                    "where date_to_str(SBIRTHDAY, 'yyyy') = (select date_to_str(SBIRTHDAY, 'yyyy') from STUDENT where SNO = '108')",
//
//            "select * from SCORE \n" +
//                    "where \n" +
//                    "\tCNO in \n" +
//                    "\t(select CNO from COURSE where (select TNO from TEACHER where TNAME = '张旭') = COURSE.TNO)",
//
//            "select TNAME from TEACHER \n" +
//                    "where \n" +
//                    "\tTNO in \n" +
//                    "\t(select TNO from COURSE where CNO in (select CNO from SCORE group by CNO having count(*) > 5))",
//
//            "select * from SCORE \n" +
//                    "where CNO = '3-105' and ('3-245' = select min(DEGREE) from SCORE where CNO) <= DEGREE \n" +
//                    "order by DEGREE desc",
//
//            "select SNO, a.CNO, DEGREE from SCORE a, (select avg(DEGREE) AVGG, CNO from SCORE group by CNO) b \n" +
//                    "where a.CNO = b.CNO and a.DEGREE < b.AVGG",
//
//            "select * from SCORE a where (select avg(b.DEGREE) from SCORE  b where a.CNO = b.CNO) > a.DEGREE",
//
//            "select TNAME, DEPART from TEACHER where TEACHER.TNO not in (select distinct TNO from COURSE)",
//
//            "select * from STUDENT \n" +
//                    "where \n" +
//                    "\tSSEX = (select SSEX from STUDENT where SNAME = '李军') \n" +
//                    "\tand \n" +
//                    "\tCLASS = (select CLASS from STUDENT where SNAME = '李军') \n" +
//                    "\tand \n" +
//                    "\tSNAME != '李军'",
//
//            "select * from SCORE a \n" +
//                    "where (select count(*) from SCORE b where a.CNO = b.CNO and a.DEGREE < b.DEGREE) < 2",
//
//            "select * from SCORE minus \n" +
//                    "select * \n" +
//                    "from SCORE a where (select count(*) from SCORE b where a.CNO = b.CNO and a.DEGREE < b.DEGREE) < 1",

//            "select now() from dual",
//            "select extract(year from date_add(now(), interval 2 day)) as plus2Day from dual",
            "select extract(year from date_add(now(), interval 2 day)) as plus2Day from dual where #{aaa} = b",

    };

    public static void main(String[] args) {
        for (int i = 0; i < sqlList.length; i++) {
            assertLexicalSuccess(sqlList[i], i);
        }
    }

    private static void assertLexicalSuccess(String sql, int index) {
        String rawSql = sql.replaceAll("\\s+", "");
        DefaultScanner defaultScanner = new DefaultScanner(sql);
        TokenSequence tokenSequence = defaultScanner.scanTokens();

        String afterLexicalAnalysis = tokenSequence.toString().replaceAll("\\s+", "");
        if (!rawSql.equals(afterLexicalAnalysis)) {
            System.out.println("rawSql: " + rawSql);
            System.out.println("newSql: " + afterLexicalAnalysis);
            System.out.println("tokenSequence: " + tokenSequence);
            throw new RuntimeException("词法分析得到的结果与原始结果不一致。");
        } else {
            System.out.println(index + " : 词法分析通过:  " + sql.replaceAll("\\s+", " "));
        }
    }


}
