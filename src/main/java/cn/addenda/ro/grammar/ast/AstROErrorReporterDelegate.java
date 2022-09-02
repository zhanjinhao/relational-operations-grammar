package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.error.reporter.AbstractROErrorReporterDelegate;
import cn.addenda.ro.grammar.lexical.scan.TokenSequence;

/**
 * @author addenda
 * @datetime 2021/7/18 17:50
 */
public class AstROErrorReporterDelegate extends AbstractROErrorReporterDelegate {

    protected TokenSequence tokenSequence;

    public AstROErrorReporterDelegate(TokenSequence tokenSequence) {
        super();
        this.tokenSequence = tokenSequence;
    }

    @Override
    public String getErrorMsg(int errorCode) {
        return super.getErrorMsg(errorCode) + SEPARATOR + getSuffix(tokenSequence);
    }

    @Override
    public void error(int errorCode) {
        throw new AstException(errorCode, getErrorMsg(errorCode));
    }

    @Override
    public void fillErrorMsg() {

        addErrorMsg(EXPRESSION_logic_PARSE, EXPRESSION_logic_PARSE_MSG);
        addErrorMsg(EXPRESSION_condition_PARSE, EXPRESSION_condition_PARSE_MSG);
        addErrorMsg(EXPRESSION_comparison_PARSE, EXPRESSION_comparison_PARSE_MSG);
        addErrorMsg(EXPRESSION_isNot_PARSE, EXPRESSION_isNot_PARSE_MSG);
        addErrorMsg(EXPRESSION_binaryArithmetic_PARSE, EXPRESSION_binaryArithmetic_PARSE_MSG);
        addErrorMsg(EXPRESSION_unaryArithmetic_PARSE, EXPRESSION_unaryArithmetic_PARSE_MSG);
        addErrorMsg(EXPRESSION_primary_PARSE, EXPRESSION_primary_PARSE_MSG);
        addErrorMsg(EXPRESSION_grouping_PARSE, EXPRESSION_grouping_PARSE_MSG);
        addErrorMsg(EXPRESSION_function_PARSE, EXPRESSION_function_PARSE_MSG);
        addErrorMsg(EXPRESSION_functionParameter_PARSE, EXPRESSION_functionParameter_PARSE_MSG);
        addErrorMsg(EXPRESSION_timeInterval_PARSE, EXPRESSION_timeInterval_PARSE_MSG);
        addErrorMsg(EXPRESSION_timeUnit_PARSE, EXPRESSION_timeUnit_PARSE_MSG);
        addErrorMsg(EXPRESSION_whereSeg_PARSE, EXPRESSION_whereSeg_PARSE_MSG);
        addErrorMsg(EXPRESSION_assignmentList_PARSE, EXPRESSION_assignmentList_PARSE_MSG);
        addErrorMsg(EXPRESSION_columnList_PARSE, EXPRESSION_columnList_PARSE_MSG);

        addErrorMsg(EXPRESSION_logic_VALIDATION, EXPRESSION_logic_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_condition_VALIDATION, EXPRESSION_condition_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_comparison_VALIDATION, EXPRESSION_comparison_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_isNot_VALIDATION, EXPRESSION_isNot_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_binaryArithmetic_VALIDATION, EXPRESSION_binaryArithmetic_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_unaryArithmetic_VALIDATION, EXPRESSION_unaryArithmetic_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_primary_VALIDATION, EXPRESSION_primary_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_grouping_VALIDATION, EXPRESSION_grouping_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_function_VALIDATION, EXPRESSION_function_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_functionParameter_VALIDATION, EXPRESSION_functionParameter_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_timeInterval_VALIDATION, EXPRESSION_timeInterval_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_timeUnit_VALIDATION, EXPRESSION_timeUnit_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_whereSeg_VALIDATION, EXPRESSION_whereSeg_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_assignmentList_VALIDATION, EXPRESSION_assignmentList_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_columnList_VALIDATION, EXPRESSION_columnList_VALIDATION_MSG);
        addErrorMsg(EXPRESSION_identifierName_VALIDATION, EXPRESSION_identifierName_VALIDATION_MSG);

        addErrorMsg(INSERT_insert_PARSE, INSERT_insert_PARSE_MSG);
        addErrorMsg(INSERT_insertValuesRep_PARSE, INSERT_insertValuesRep_PARSE_MSG);
        addErrorMsg(INSERT_insertSetRep_PARSE, INSERT_insertSetRep_PARSE_MSG);
        addErrorMsg(INSERT_insertSelectRep_PARSE, INSERT_insertSelectRep_PARSE_MSG);
        addErrorMsg(INSERT_onDuplicateKey_PARSE, INSERT_onDuplicateKey_PARSE_MSG);

        addErrorMsg(INSERT_insert_VALIDATION, INSERT_insert_VALIDATION_MSG);
        addErrorMsg(INSERT_insertValuesRep_VALIDATION, INSERT_insertValuesRep_VALIDATION_MSG);
        addErrorMsg(INSERT_insertSetRep_VALIDATION, INSERT_insertSetRep_VALIDATION_MSG);
        addErrorMsg(INSERT_insertSelectRep_VALIDATION, INSERT_insertSelectRep_VALIDATION_MSG);
        addErrorMsg(INSERT_onDuplicateKey_VALIDATION, INSERT_onDuplicateKey_VALIDATION_MSG);

        addErrorMsg(UPDATE_update_PARSE, UPDATE_update_PARSE_MSG);
        addErrorMsg(UPDATE_update_VALIDATION, UPDATE_update_VALIDATION_MSG);

        addErrorMsg(DELETE_delete_PARSE, DELETE_delete_PARSE_MSG);
        addErrorMsg(DELETE_delete_VALIDATION, DELETE_delete_VALIDATION_MSG);


        addErrorMsg(SELECT_select_PARSE, SELECT_select_PARSE_MSG);
        addErrorMsg(SELECT_singleSelect_PARSE, SELECT_singleSelect_PARSE_MSG);
        addErrorMsg(SELECT_columnSeg_PARSE, SELECT_columnSeg_PARSE_MSG);
        addErrorMsg(SELECT_columnRep_PARSE, SELECT_columnRep_PARSE_MSG);
        addErrorMsg(SELECT_caseWhen_PARSE, SELECT_caseWhen_PARSE_MSG);
        addErrorMsg(SELECT_tableSeg_PARSE, SELECT_tableSeg_PARSE_MSG);
        addErrorMsg(SELECT_tableRep_PARSE, SELECT_tableRep_PARSE_MSG);
        addErrorMsg(SELECT_groupBySeg_PARSE, SELECT_groupBySeg_PARSE_MSG);
        addErrorMsg(SELECT_orderBySeg_PARSE, SELECT_orderBySeg_PARSE_MSG);
        addErrorMsg(SELECT_limitSeg_PARSE, SELECT_limitSeg_PARSE_MSG);
        addErrorMsg(SELECT_condition_PARSE, SELECT_condition_PARSE_MSG);
        addErrorMsg(SELECT_inCondition_PARSE, SELECT_inCondition_PARSE_MSG);
        addErrorMsg(SELECT_existsCondition_PARSE, SELECT_existsCondition_PARSE_MSG);
        addErrorMsg(SELECT_primary_PARSE, SELECT_primary_PARSE_MSG);
        addErrorMsg(SELECT_groupFunction_PARSE, SELECT_groupFunction_PARSE_MSG);
        addErrorMsg(SELECT_lockSeg_PARSE, SELECT_lockSeg_PARSE_MSG);
        addErrorMsg(SELECT_groupConcat_PARSE, SELECT_groupConcat_PARSE_MSG);
        addErrorMsg(SELECT_windowFunction_PARSE, SELECT_windowFunction_PARSE_MSG);
        addErrorMsg(SELECT_window_PARSE, SELECT_window_PARSE_MSG);
        addErrorMsg(SELECT_dynamicFrame_PARSE, SELECT_dynamicFrame_PARSE_MSG);
        addErrorMsg(SELECT_frameBetween_PARSE, SELECT_frameBetween_PARSE_MSG);
        addErrorMsg(SELECT_frameEdge_PARSE, SELECT_frameEdge_PARSE_MSG);

        addErrorMsg(SELECT_select_VALIDATION, SELECT_select_VALIDATION_MSG);
        addErrorMsg(SELECT_singleSelect_VALIDATION, SELECT_singleSelect_VALIDATION_MSG);
        addErrorMsg(SELECT_columnSeg_VALIDATION, SELECT_columnSeg_VALIDATION_MSG);
        addErrorMsg(SELECT_columnRep_VALIDATION, SELECT_columnRep_VALIDATION_MSG);
        addErrorMsg(SELECT_caseWhen_VALIDATION, SELECT_caseWhen_VALIDATION_MSG);
        addErrorMsg(SELECT_tableSeg_VALIDATION, SELECT_tableSeg_VALIDATION_MSG);
        addErrorMsg(SELECT_tableRep_VALIDATION, SELECT_tableRep_VALIDATION_MSG);
        addErrorMsg(SELECT_groupBySeg_VALIDATION, SELECT_groupBySeg_VALIDATION_MSG);
        addErrorMsg(SELECT_orderBySeg_VALIDATION, SELECT_orderBySeg_VALIDATION_MSG);
        addErrorMsg(SELECT_limitSeg_VALIDATION, SELECT_limitSeg_VALIDATION_MSG);
        addErrorMsg(SELECT_condition_VALIDATION, SELECT_condition_VALIDATION_MSG);
        addErrorMsg(SELECT_inCondition_VALIDATION, SELECT_inCondition_VALIDATION_MSG);
        addErrorMsg(SELECT_existsCondition_VALIDATION, SELECT_existsCondition_VALIDATION_MSG);
        addErrorMsg(SELECT_primary_VALIDATION, SELECT_primary_VALIDATION_MSG);
        addErrorMsg(SELECT_groupFunction_VALIDATION, SELECT_groupFunction_VALIDATION_MSG);
        addErrorMsg(SELECT_lockSeg_VALIDATION, SELECT_lockSeg_VALIDATION_MSG);
        addErrorMsg(SELECT_groupConcat_VALIDATION, SELECT_groupConcat_VALIDATION_MSG);
        addErrorMsg(SELECT_windowFunction_VALIDATION, SELECT_windowFunction_VALIDATION_MSG);
        addErrorMsg(SELECT_window_VALIDATION, SELECT_window_VALIDATION_MSG);
        addErrorMsg(SELECT_dynamicFrame_VALIDATION, SELECT_dynamicFrame_VALIDATION_MSG);
        addErrorMsg(SELECT_frameBetween_VALIDATION, SELECT_frameBetween_VALIDATION_MSG);
        addErrorMsg(SELECT_frameEdge_VALIDATION, SELECT_frameEdge_VALIDATION_MSG);

        addErrorMsg(CURD_unknow_syntax_PARSE, CURD_unknow_syntax_PARSE_MSG);
        addErrorMsg(CURD_token_undefined_PARSE, CURD_token_undefined_PARSE_MSG);
        addErrorMsg(CURD_not_end_PARSE, CURD_not_end_PARSE_MSG);

        addSuffixFunction(TokenSequence.class, (error) -> {
            TokenSequence tokenSequence = (TokenSequence) error;
            return "Current token is " + tokenSequence.takeCur() + ", and current index is " + tokenSequence.getCurrent() + ".";
        });
    }

    /**
     * expression 语法的语法解析错误代码从21001开始
     */
    public static final int EXPRESSION_logic_PARSE = 21001;
    public static final String EXPRESSION_logic_PARSE_MSG = "The grammar of logic is : 'condition ((\"or\" | \"and\") condition)*'.";

    public static final int EXPRESSION_condition_PARSE = 21002;
    public static final String EXPRESSION_condition_PARSE_MSG = "The grammar of condition is : 'comparison'.";

    public static final int EXPRESSION_comparison_PARSE = 21003;
    public static final String EXPRESSION_comparison_PARSE_MSG = "The grammar of comparison is : 'binaryArithmetic ((\">\" | \"<\" | \">=\" | \"<=\" | \"!=\" | \"=\" | \"like\" | \"contains\" | isNot) binaryArithmetic)?'.";

    public static final int EXPRESSION_isNot_PARSE = 21004;
    public static final String EXPRESSION_isNot_PARSE_MSG = "The grammar of isNot is : '\"is\" (\"not\")?'.";

    public static final int EXPRESSION_binaryArithmetic_PARSE = 21005;
    public static final String EXPRESSION_binaryArithmetic_PARSE_MSG = "The grammar of binaryArithmetic is : 'unaryArithmetic ((\"+\" | \"-\" | \"*\" | \"/\") unaryArithmetic)*'.";

    public static final int EXPRESSION_unaryArithmetic_PARSE = 21006;
    public static final String EXPRESSION_unaryArithmetic_PARSE_MSG = "The grammar of unaryArithmetic is : '(\"!\"|\"-\") unaryArithmetic | primary'.";

    public static final int EXPRESSION_primary_PARSE = 21007;
    public static final String EXPRESSION_primary_PARSE_MSG = "The grammar of primary is : '#{xxx} | ? | \"true\" | \"false\" | \"null\" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function'.";

    public static final int EXPRESSION_grouping_PARSE = 21008;
    public static final String EXPRESSION_grouping_PARSE_MSG = "The grammar of grouping is : '\"where\" logic'.";

    public static final int EXPRESSION_function_PARSE = 21009;
    public static final String EXPRESSION_function_PARSE_MSG = "The grammar of function is : 'functionName \"(\" functionParameter? (\",\" functionParameter)* \")\"'.";

    public static final int EXPRESSION_functionParameter_PARSE = 21010;
    public static final String EXPRESSION_functionParameter_PARSE_MSG = "The grammar of functionParameter is : 'logic | timeInterval | timeUnit | function'.";

    public static final int EXPRESSION_timeInterval_PARSE = 21011;
    public static final String EXPRESSION_timeInterval_PARSE_MSG = "The grammar of timeInterval is : '\"interval\" INTEGER IDENTIFIER'.";

    public static final int EXPRESSION_timeUnit_PARSE = 21012;
    public static final String EXPRESSION_timeUnit_PARSE_MSG = "The grammar of timeUnit is : 'unit \"from\" binaryArithmetic'.";

    public static final int EXPRESSION_whereSeg_PARSE = 21013;
    public static final String EXPRESSION_whereSeg_PARSE_MSG = "The grammar of whereSeg is : '\"where\" logic'.";

    public static final int EXPRESSION_assignmentList_PARSE = 21014;
    public static final String EXPRESSION_assignmentList_PARSE_MSG = "The grammar of assignmentList is : '(IDENTIFIER \"=\" binaryArithmetic) (\",\" IDENTIFIER \"=\" binaryArithmetic)*'.";

    public static final int EXPRESSION_columnList_PARSE = 21015;
    public static final String EXPRESSION_columnList_PARSE_MSG = "The grammar of columnList is : 'IDENTIFIER (\",\" IDENTIFIER)*'.";

    /**
     * expression 语法的语法验证错误代码从21501开
     */
    public static final int EXPRESSION_logic_VALIDATION = 21501;
    public static final String EXPRESSION_logic_VALIDATION_MSG = "logic grammar validation failed : 'condition ((\"or\" | \"and\") condition)*'.";

    public static final int EXPRESSION_condition_VALIDATION = 21502;
    public static final String EXPRESSION_condition_VALIDATION_MSG = "condition grammar validation failed : 'comparison'.";

    public static final int EXPRESSION_comparison_VALIDATION = 21503;
    public static final String EXPRESSION_comparison_VALIDATION_MSG = "comparison grammar validation failed : 'binaryArithmetic ((\">\" | \"<\" | \">=\" | \"<=\" | \"!=\" | \"=\" | \"like\" | \"contains\" | isNot) binaryArithmetic)?'.";

    public static final int EXPRESSION_isNot_VALIDATION = 21504;
    public static final String EXPRESSION_isNot_VALIDATION_MSG = "isNot grammar validation failed : '\"is\" (\"not\")?'.";

    public static final int EXPRESSION_binaryArithmetic_VALIDATION = 21505;
    public static final String EXPRESSION_binaryArithmetic_VALIDATION_MSG = "binaryArithmetic grammar validation failed  : 'unaryArithmetic ((\"+\" | \"-\" | \"*\" | \"/\") unaryArithmetic)*'.";

    public static final int EXPRESSION_unaryArithmetic_VALIDATION = 21506;
    public static final String EXPRESSION_unaryArithmetic_VALIDATION_MSG = "unaryArithmetic grammar validation failed  : '(\"!\"|\"-\") unaryArithmetic | primary'.";

    public static final int EXPRESSION_primary_VALIDATION = 21507;
    public static final String EXPRESSION_primary_VALIDATION_MSG = "primary grammar validation failed : '#{xxx} | ? | \"true\" | \"false\" | \"null\" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function'.";

    public static final int EXPRESSION_grouping_VALIDATION = 21508;
    public static final String EXPRESSION_grouping_VALIDATION_MSG = "grouping grammar validation failed : '\"where\" logic'.";

    public static final int EXPRESSION_function_VALIDATION = 21509;
    public static final String EXPRESSION_function_VALIDATION_MSG = "function grammar validation failed : 'functionName \"(\" functionParameter? (\",\" functionParameter)* \")\"'.";

    public static final int EXPRESSION_functionParameter_VALIDATION = 21510;
    public static final String EXPRESSION_functionParameter_VALIDATION_MSG = "functionParameter grammar validation failed : 'logic | timeInterval | timeUnit | function'.";

    public static final int EXPRESSION_timeInterval_VALIDATION = 21511;
    public static final String EXPRESSION_timeInterval_VALIDATION_MSG = "timeInterval grammar validation failed : '\"interval\" INTEGER IDENTIFIER'.";

    public static final int EXPRESSION_timeUnit_VALIDATION = 21512;
    public static final String EXPRESSION_timeUnit_VALIDATION_MSG = "timeUnit grammar validation failed : 'unit \"from\" binaryArithmetic'.";

    public static final int EXPRESSION_whereSeg_VALIDATION = 21513;
    public static final String EXPRESSION_whereSeg_VALIDATION_MSG = "whereSeg grammar validation failed : '\"where\" logic'.";

    public static final int EXPRESSION_assignmentList_VALIDATION = 21514;
    public static final String EXPRESSION_assignmentList_VALIDATION_MSG = "assignmentList grammar validation failed : '(IDENTIFIER \"=\" binaryArithmetic) (\",\" IDENTIFIER \"=\" binaryArithmetic)*'.";

    public static final int EXPRESSION_columnList_VALIDATION = 21515;
    public static final String EXPRESSION_columnList_VALIDATION_MSG = "columnList grammar validation failed : 'IDENTIFIER (\",\" IDENTIFIER)*'.";

    public static final int EXPRESSION_identifierName_VALIDATION = 21516;
    public static final String EXPRESSION_identifierName_VALIDATION_MSG = "illegal identifier name.";

    /**
     * insert 语法的语法检测错误代码从22001开始
     */
    public static final int INSERT_insert_PARSE = 22001;
    public static final String INSERT_insert_PARSE_MSG = "The grammar of function is : '\"insert\" (constrict)? \"into\" IDENTIFIER (((valuesRep | setRep) onDuplicateKey?) | (selectRep))'.";

    public static final int INSERT_insertValuesRep_PARSE = 22002;
    public static final String INSERT_insertValuesRep_PARSE_MSG = "The grammar of valuesRep is : '(\"(\" IDENTIFIER (, IDENTIFIER)* \")\")? \"values\" (\"(\" binaryArithmetic (, binaryArithmetic)* \")\") (\",\" \"(\" binaryArithmetic (, binaryArithmetic)* \")\")*'.";

    public static final int INSERT_insertSetRep_PARSE = 22003;
    public static final String INSERT_insertSetRep_PARSE_MSG = "The grammar of setRep is : '\"set\" (IDENTIFIER \"=\" binaryArithmetic) (\",\" IDENTIFIER \"=\" binaryArithmetic)*'.";

    public static final int INSERT_insertSelectRep_PARSE = 22004;
    public static final String INSERT_insertSelectRep_PARSE_MSG = "The grammar of setRep is : '\"set\" (IDENTIFIER \"=\" binaryArithmetic) (\",\" IDENTIFIER \"=\" binaryArithmetic)*'.";

    public static final int INSERT_onDuplicateKey_PARSE = 22005;
    public static final String INSERT_onDuplicateKey_PARSE_MSG = "The grammar of onDuplicateKey is : '\"on\" \"duplicate\" \"key\" \"update\" assignmentList'.";


    /**
     * insert 语法的语法检测错误代码从22501开始
     */
    public static final int INSERT_insert_VALIDATION = 22501;
    public static final String INSERT_insert_VALIDATION_MSG = "insert grammar validation failed : '\"insert\" (constrict)? \"into\" IDENTIFIER (((valuesRep | setRep) onDuplicateKey?) | (selectRep))'.";

    public static final int INSERT_insertValuesRep_VALIDATION = 22502;
    public static final String INSERT_insertValuesRep_VALIDATION_MSG = "insertValuesRep grammar validation failed : '(\"(\" IDENTIFIER (, IDENTIFIER)* \")\")? \"values\" (\"(\" binaryArithmetic (, binaryArithmetic)* \")\") (\",\" \"(\" binaryArithmetic (, binaryArithmetic)* \")\")*'.";

    public static final int INSERT_insertSetRep_VALIDATION = 22503;
    public static final String INSERT_insertSetRep_VALIDATION_MSG = "insertSetRep grammar validation failed : '\"set\" (IDENTIFIER \"=\" binaryArithmetic) (\",\" IDENTIFIER \"=\" binaryArithmetic)*'.";

    public static final int INSERT_insertSelectRep_VALIDATION = 22504;
    public static final String INSERT_insertSelectRep_VALIDATION_MSG = "insertSelectRep grammar validation failed : '\"set\" (IDENTIFIER \"=\" binaryArithmetic) (\",\" IDENTIFIER \"=\" binaryArithmetic)*'.";

    public static final int INSERT_onDuplicateKey_VALIDATION = 22505;
    public static final String INSERT_onDuplicateKey_VALIDATION_MSG = "onDuplicateKey grammar validation failed : '\"on\" \"duplicate\" \"key\" \"update\" assignmentList'.";


    /**
     * update 语法的语法解析错误代码从23001开始
     */
    public static final int UPDATE_update_PARSE = 23001;
    public static final String UPDATE_update_PARSE_MSG = "The grammar of function is : '\"update\" IDENTIFIER \"set\" assignmentList whereSeg'.";

    /**
     * update 语法的语法验证错误代码从23501开始
     */
    public static final int UPDATE_update_VALIDATION = 23501;
    public static final String UPDATE_update_VALIDATION_MSG = "update grammar validation failed : '\"update\" IDENTIFIER \"set\" assignmentList whereSeg'.";


    /**
     * delete 语法的语法解析错误代码从24001开始
     */
    public static final int DELETE_delete_PARSE = 24001;
    public static final String DELETE_delete_PARSE_MSG = "The grammar of delete is : '\"delete\" \"from\" IDENTIFIER whereSeg'.";

    /**
     * delete 语法的语法验证错误代码从24501开始
     */
    public static final int DELETE_delete_VALIDATION = 24501;
    public static final String DELETE_delete_VALIDATION_MSG = "delete grammar validation failed : '\"delete\" \"from\" IDENTIFIER whereSeg'.";


    /**
     * select 语法的语法解析错误代码从25001开始
     */
    public static final int SELECT_select_PARSE = 25001;
    public static final String SELECT_select_PARSE_MSG = "The grammar of select is : 'singleSelect ((\"union\" | \"minus\" | \"intersect\" | \"except\") (\"all\")? singleSelect)*'.";

    public static final int SELECT_singleSelect_PARSE = 25002;
    public static final String SELECT_singleSelect_PARSE_MSG = "The grammar of select is : 'columnSeg tableSeg (whereSeg)? (groupBySeg)? (orderBySeg)? (limitSeg)?'.";

    public static final int SELECT_columnSeg_PARSE = 25003;
    public static final String SELECT_columnSeg_PARSE_MSG = "The grammar of columnSeg is : '\"select\" (\"distinct\")? columnRep (\",\" columnRep)*'.";

    public static final int SELECT_columnRep_PARSE = 25004;
    public static final String SELECT_columnRep_PARSE_MSG = "The grammar of columnRep is : '(* | binaryArithmetic | caseWhen) (\"as\" IDENTIFIER)?'.";

    public static final int SELECT_caseWhen_PARSE = 25005;
    public static final String SELECT_caseWhen_PARSE_MSG = "The grammar of caseWhen is : '\"case\" binaryArithmetic \"when\" binaryArithmetic \"then\" binaryArithmetic ... (\"else\" binaryArithmetic)? \"end\"'.";

    public static final int SELECT_tableSeg_PARSE = 25006;
    public static final String SELECT_tableSeg_PARSE_MSG = "The grammar of tableSeg is : '\"from\" tableRep ((\"left\" | \"right\" | \"full\" | \"cross\")? (\"inner\" | \"outer\")? (\"join\" | \",\") tableRep (\"on\" logic)?)*'.";

    public static final int SELECT_tableRep_PARSE = 25007;
    public static final String SELECT_tableRep_PARSE_MSG = "The grammar of tableRep is : '(\"(\" singleSelect \")\" | IDENTIFIER)? (\"as\")? IDENTIFIER'.";

    public static final int SELECT_groupBySeg_PARSE = 25008;
    public static final String SELECT_groupBySeg_PARSE_MSG = "The grammar of groupBySeg is : '\"group\" \"by\" IDENTIFIER (\",\" IDENTIFIER)* (\"having\" logic)?'.";

    public static final int SELECT_orderBySeg_PARSE = 25009;
    public static final String SELECT_orderBySeg_PARSE_MSG = "The grammar of orderBySeg is : '\"order\" \"by\" IDENTIFIER (\"desc\" | \"asc\") (\",\" IDENTIFIER (\"desc\" | \"asc\"))*'.";

    public static final int SELECT_limitSeg_PARSE = 25010;
    public static final String SELECT_limitSeg_PARSE_MSG = "The grammar of limitSeg is : '\"limit\" INTEGER (\"offset\" INTEGER)?'.";

    public static final int SELECT_condition_PARSE = 25011;
    public static final String SELECT_condition_PARSE_MSG = "The grammar of condition is : 'inCondition | existsCondition | comparison'.";

    public static final int SELECT_inCondition_PARSE = 25012;
    public static final String SELECT_inCondition_PARSE_MSG = "The grammar of inCondition is : 'IDENTIFIER (\"not\")? \"in\" \"(\" (single | (primary (, primary)*) \")\"'.";

    public static final int SELECT_existsCondition_PARSE = 25013;
    public static final String SELECT_existsCondition_PARSE_MSG = "The grammar of existsCondition is : '(\"not\")? \"exists\" \"(\" singleSelect \")\"'.";

    public static final int SELECT_primary_PARSE = 25014;
    public static final String SELECT_primary_PARSE_MSG = "The grammar of primary is : '#{xxx} | ? | \"true\" | \"false\" | \"null\" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function | \"(\" singleSelect \")\" | groupFunction'.";

    public static final int SELECT_groupFunction_PARSE = 25015;
    public static final String SELECT_groupFunction_PARSE_MSG = "The grammar of groupFunction is : '(\"avg\" | \"max\" | \"min\" | \"count\" | \"sum\" | \"flat\") \"(\" binaryArithmetic \")\"'.";

    public static final int SELECT_lockSeg_PARSE = 25016;
    public static final String SELECT_lockSeg_PARSE_MSG = "The grammar of lockSeg is : 'sLock | xLock'.";

    public static final int SELECT_groupConcat_PARSE = 25017;
    public static final String SELECT_groupConcat_PARSE_MSG = "The grammar of groupConcat is : '\"group_concat\" \"(\" (\"distinct\")? binaryArithmetic (\",\" binaryArithmetic)* \"order\" \"by\" orderItem (\",\" orderItem)* (\"separator\" primary)? \")\"'.";

    public static final int SELECT_windowFunction_PARSE = 25018;
    public static final String SELECT_windowFunction_PARSE_MSG = "The grammar of windowFunction is : 'IDENTIFIER \"(\" binaryArithmetic? \")\" window'.";

    public static final int SELECT_window_PARSE = 25019;
    public static final String SELECT_window_PARSE_MSG = "The grammar of window is : '\"over\" \"(\" \"partition\" \"by\" binaryArithmetic (\",\" binaryArithmetic)* orderBySeg? dynamic_frame? \")\"'.";

    public static final int SELECT_dynamicFrame_PARSE = 25020;
    public static final String SELECT_dynamicFrame_PARSE_MSG = "The grammar of dynamicFrame is : '(\"rows\" | \"range\") (frame_between | frame_edge)'.";

    public static final int SELECT_frameBetween_PARSE = 25021;
    public static final String SELECT_frameBetween_PARSE_MSG = "The grammar of frameBetween is : '\"between\" frameEdge \"and\" frameEdge'.";

    public static final int SELECT_frameEdge_PARSE = 25022;
    public static final String SELECT_frameEdge_PARSE_MSG = "The grammar of frameEdge is : '(INTEGER | \"unbounded\" | \"current\") (\"preceding\" | \"following\" | \"row\")'.";


    /**
     * select 语法的语法验证错误代码从25501开始
     */
    public static final int SELECT_select_VALIDATION = 25501;
    public static final String SELECT_select_VALIDATION_MSG = "logic select validation failed : 'singleSelect ((\"union\" | \"minus\" | \"intersect\" | \"except\") (\"all\")? singleSelect)*'.";

    public static final int SELECT_singleSelect_VALIDATION = 25502;
    public static final String SELECT_singleSelect_VALIDATION_MSG = "singleSelect select validation failed : 'columnSeg tableSeg (whereSeg)? (groupBySeg)? (orderBySeg)? (limitSeg)?'.";

    public static final int SELECT_columnSeg_VALIDATION = 25503;
    public static final String SELECT_columnSeg_VALIDATION_MSG = "columnSeg grammar validation failed : '\"select\" (\"distinct\")? columnRep (\",\" columnRep)*'.";

    public static final int SELECT_columnRep_VALIDATION = 25504;
    public static final String SELECT_columnRep_VALIDATION_MSG = "columnRep grammar validation failed : '(* | binaryArithmetic | caseWhen) (\"as\" IDENTIFIER)?'.";

    public static final int SELECT_caseWhen_VALIDATION = 25505;
    public static final String SELECT_caseWhen_VALIDATION_MSG = "caseWhen grammar validation failed : '\"case\" binaryArithmetic \"when\" binaryArithmetic \"then\" binaryArithmetic ... (\"else\" binaryArithmetic)? \"end\"'.";

    public static final int SELECT_tableSeg_VALIDATION = 25506;
    public static final String SELECT_tableSeg_VALIDATION_MSG = "tableSeg grammar validation failed : '\"from\" tableRep ((\"left\" | \"right\" | \"full\" | \"cross\")? (\"inner\" | \"outer\")? (\"join\" | \",\") tableRep (\"on\" logic)?)*'.";

    public static final int SELECT_tableRep_VALIDATION = 25507;
    public static final String SELECT_tableRep_VALIDATION_MSG = "tableRep grammar validation failed : '(\"(\" singleSelect \")\" | IDENTIFIER)? (\"as\")? IDENTIFIER'.";

    public static final int SELECT_groupBySeg_VALIDATION = 25508;
    public static final String SELECT_groupBySeg_VALIDATION_MSG = "groupBySeg grammar validation failed : '\"group\" \"by\" IDENTIFIER (\",\" IDENTIFIER)* (\"having\" logic)?'.";

    public static final int SELECT_orderBySeg_VALIDATION = 25509;
    public static final String SELECT_orderBySeg_VALIDATION_MSG = "orderBySeg grammar validation failed : '\"order\" \"by\" IDENTIFIER (\"desc\" | \"asc\") (\",\" IDENTIFIER (\"desc\" | \"asc\"))*'.";

    public static final int SELECT_limitSeg_VALIDATION = 25510;
    public static final String SELECT_limitSeg_VALIDATION_MSG = "limitSeg grammar validation failed : '\"limit\" INTEGER (\",\" INTEGER)?'.";

    public static final int SELECT_condition_VALIDATION = 25511;
    public static final String SELECT_condition_VALIDATION_MSG = "condition grammar validation failed : 'inCondition | existsCondition | comparison'.";

    public static final int SELECT_inCondition_VALIDATION = 25512;
    public static final String SELECT_inCondition_VALIDATION_MSG = "inCondition grammar validation failed : 'IDENTIFIER (\"not\")? \"in\" \"(\" (single | (primary (, primary)*) \")\"'.";

    public static final int SELECT_existsCondition_VALIDATION = 25513;
    public static final String SELECT_existsCondition_VALIDATION_MSG = "existsCondition grammar validation failed : '(\"not\")? \"exists\" \"(\" singleSelect \")\"'.";

    public static final int SELECT_primary_VALIDATION = 25514;
    public static final String SELECT_primary_VALIDATION_MSG = "primary grammar validation failed : '#{xxx} | ? | \"true\" | \"false\" | \"null\" | INTEGER | DECIMAL | STRING | IDENTIFIER | grouping | function | \"(\" singleSelect \")\" | groupFunction'.";

    public static final int SELECT_groupFunction_VALIDATION = 25515;
    public static final String SELECT_groupFunction_VALIDATION_MSG = "groupFunction grammar validation failed : '(\"avg\" | \"max\" | \"min\" | \"count\" | \"sum\" | \"flat\") \"(\" binaryArithmetic \")\"'.";

    public static final int SELECT_lockSeg_VALIDATION = 25516;
    public static final String SELECT_lockSeg_VALIDATION_MSG = "lockSeg grammar validation failed is : 'sLock | xLock'.";

    public static final int SELECT_groupConcat_VALIDATION = 25517;
    public static final String SELECT_groupConcat_VALIDATION_MSG = "groupConcat grammar validation failed : '\"group_concat\" \"(\" (\"distinct\")? binaryArithmetic (\",\" binaryArithmetic)* \"order\" \"by\" orderItem (\",\" orderItem)* (\"separator\" primary)? \")\"'.";

    public static final int SELECT_windowFunction_VALIDATION = 25518;
    public static final String SELECT_windowFunction_VALIDATION_MSG = "windowFunction grammar validation failed : 'IDENTIFIER \"(\" binaryArithmetic? \")\" window'.";

    public static final int SELECT_window_VALIDATION = 25519;
    public static final String SELECT_window_VALIDATION_MSG = "window grammar validation failed : '\"over\" \"(\" \"partition\" \"by\" binaryArithmetic (\",\" binaryArithmetic)* orderBySeg? dynamic_frame? \")\"'.";

    public static final int SELECT_dynamicFrame_VALIDATION = 25520;
    public static final String SELECT_dynamicFrame_VALIDATION_MSG = "dynamicFrame grammar validation failed : '(\"rows\" | \"range\") (frame_between | frame_edge)'.";

    public static final int SELECT_frameBetween_VALIDATION = 25521;
    public static final String SELECT_frameBetween_VALIDATION_MSG = "frameBetween grammar validation failed : '\"between\" frameEdge \"and\" frameEdge'.";

    public static final int SELECT_frameEdge_VALIDATION = 25522;
    public static final String SELECT_frameEdge_VALIDATION_MSG = "frameEdge grammar validation failed : '(INTEGER | \"unbounded\" | \"current\") (\"preceding\" | \"following\" | \"row\")'.";


    /**
     * curd 语法的语法解析错误代码从26001开始
     */
    public static final int CURD_token_undefined_PARSE = 26001;
    public static final String CURD_token_undefined_PARSE_MSG = "Unable to parse the current token.";

    public static final int CURD_not_end_PARSE = 27002;
    public static final String CURD_not_end_PARSE_MSG = "Curd did not end correctly.";

    public static final int CURD_unknow_syntax_PARSE = 26003;
    public static final String CURD_unknow_syntax_PARSE_MSG = "Unknown SQL syntax.";
}
