package cn.addenda.ro.util;

import cn.addenda.ro.grammar.ast.AbstractCurdParser;
import cn.addenda.ro.grammar.ast.CurdUtils;
import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.create.visitor.InsertVisitor;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.delete.visitor.DeleteVisitor;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.retrieve.visitor.SelectVisitor;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.ast.update.visitor.UpdateVisitor;
import cn.addenda.ro.grammar.function.evaluator.DefaultFunctionEvaluator;
import cn.addenda.ro.grammar.function.evaluator.FunctionEvaluator;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;
import cn.addenda.ro.grammar.util.ReflectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @datetime 2022/11/12 8:14
 */
public class SqlAddConditionUtils {

    private SqlAddConditionUtils() {

    }

    public static String addTableCondition(String sql, String tableName, String condition, FunctionEvaluator<?> functionEvaluator) {
        Curd parse = CurdUtils.parse(sql, functionEvaluator, false);
        if (parse instanceof Select) {
            Select select = (Select) parse;
            select.accept(new SelectAddTableConditionVisitor(tableName, condition));
        } else if (parse instanceof Delete) {
            Delete delete = (Delete) parse;
            delete.accept(new DeleteAddTableConditionVisitor(tableName, condition));
        } else if (parse instanceof Update) {
            Update update = (Update) parse;
            update.accept(new UpdateAddTableConditionVisitor(tableName, condition));
        } else if (parse instanceof Insert) {
            Insert insert = (Insert) parse;
            Curd insertRep = insert.getInsertRep();
            if (insertRep instanceof InsertSelectRep) {
                insert.accept(new InsertAddTableConditionVisitor(tableName, condition));
            }
        }
        return parse.toString();
    }

    public static String addTableCondition(String sql, String tableName, String condition) {
        return addTableCondition(sql, tableName, condition, DefaultFunctionEvaluator.getInstance());
    }

    public static String addViewCondition(String sql, String tableName, String condition, FunctionEvaluator<?> functionEvaluator) {
        Curd parse = CurdUtils.parse(sql, functionEvaluator, false);
        if (parse instanceof Select) {
            Select select = (Select) parse;
            select.accept(new SelectAddViewConditionVisitor(tableName, condition));
        } else if (parse instanceof Delete) {
            Delete delete = (Delete) parse;
            delete.accept(new DeleteAddViewConditionVisitor(tableName, condition));
        } else if (parse instanceof Update) {
            Update update = (Update) parse;
            update.accept(new UpdateAddViewConditionVisitor(tableName, condition));
        } else if (parse instanceof Insert) {
            Insert insert = (Insert) parse;
            Curd insertRep = insert.getInsertRep();
            if (insertRep instanceof InsertSelectRep) {
                insert.accept(new InsertAddViewConditionVisitor(tableName, condition));
            }
        }
        return parse.toString();
    }

    public static String addViewCondition(String sql, String tableName, String condition) {
        return addViewCondition(sql, tableName, condition, DefaultFunctionEvaluator.getInstance());
    }

    public static class SelectAddTableConditionVisitor extends SelectVisitor<Void> {

        protected final String tableName;

        protected final String condition;

        public SelectAddTableConditionVisitor(String tableName, String condition) {
            this.tableName = tableName;
            this.condition = condition;
        }

        @Override
        public Void visitSelect(Select select) {
            nullAccept(select.getLeftCurd());
            nullAccept(select.getRightCurd());
            return null;
        }

        @Override
        public Void visitSingleSelect(SingleSelect singleSelect) {
            nullAccept(singleSelect.getColumnSeg());
            nullAccept(singleSelect.getTableSeg());
            nullAccept(singleSelect.getWhereSeg());
            nullAccept(singleSelect.getGroupBySeg());
            nullAccept(singleSelect.getOrderBySeg());
            nullAccept(singleSelect.getLimitSeg());
            nullAccept(singleSelect.getLockSeg());
            return null;
        }

        @Override
        public Void visitColumnSeg(ColumnSeg columnSeg) {
            nullAccept(columnSeg.getColumnRepList());
            return null;
        }

        @Override
        public Void visitColumnRep(ColumnRep columnRep) {
            nullAccept(columnRep.getCurd());
            return null;
        }

        @Override
        public Void visitTableSeg(TableSeg tableSeg) {
            nullAccept(tableSeg.getLeftCurd());
            nullAccept(tableSeg.getRightCurd());
            nullAccept(tableSeg.getCondition());
            return null;
        }

        @Override
        public Void visitTableRep(TableRep tableRep) {
            Curd curd = tableRep.getCurd();
            if (curd != null && curd instanceof Select) {
                nullAccept(curd);
            } else {
                Token alias = tableRep.getAlias();
                Identifier identifier = (Identifier) curd;
                String tn = (String) identifier.getName().getLiteral();
                if (!tableName.equals(tn)) {
                    return null;
                }
                // 存在别名的时候，curd直接替换
                // A A1  -> "(select * from A where condition) A1"
                if (alias != null) {
                    Select select = (Select) CurdUtils.parse("select * from " + tn + " where " + condition, false);
                    AbstractCurdParser.saveSelectType(select, SingleSelectType.TABLE, SelectType.VIEW, true);
                    ReflectUtils.setFieldValue(tableRep, "curd", select);
                }
                // 不存在别名的时候，使用表名作为别名
                // A  ->  "(select * from A where condition) A"
                else {
                    Select select = (Select) CurdUtils.parse("select * from " + tn + " where " + condition, false);
                    AbstractCurdParser.saveSelectType(select, SingleSelectType.TABLE, SelectType.VIEW, true);
                    ReflectUtils.setFieldValue(tableRep, "curd", select);
                    ReflectUtils.setFieldValue(tableRep, "alias", identifier.getName().deepClone());
                }
            }
            return null;
        }

        @Override
        public Void visitInCondition(InCondition inCondition) {
            nullAccept(inCondition.getSelect());
            nullAccept(inCondition.getRange());
            return null;
        }

        @Override
        public Void visitExistsCondition(ExistsCondition existsCondition) {
            nullAccept(existsCondition.getCurd());
            return null;
        }

        @Override
        public Void visitGroupBySeg(GroupBySeg groupBySeg) {
            nullAccept(groupBySeg.getHaving());
            return null;
        }

        @Override
        public Void visitOrderBySeg(OrderBySeg orderBySeg) {
            nullAccept(orderBySeg.getColumnList());
            return null;
        }

        @Override
        public Void visitOrderItem(OrderItem orderItem) {
            nullAccept(orderItem.getColumn());
            return null;
        }

        @Override
        public Void visitLimitSeg(LimitSeg limitSeg) {
            return null;
        }

        @Override
        public Void visitGroupFunction(GroupFunction groupFunction) {
            nullAccept(groupFunction.getCurd());
            return null;
        }

        @Override
        public Void visitGroupConcat(GroupConcat groupConcat) {
            nullAccept(groupConcat.getResultList());
            nullAccept(groupConcat.getOrderItemList());
            return null;
        }

        @Override
        public Void visitCaseWhen(CaseWhen caseWhen) {
            nullAccept(caseWhen.getValue());
            nullAccept(caseWhen.getConditionList());
            nullAccept(caseWhen.getDefaultValue());
            nullAccept(caseWhen.getResultList());
            return null;
        }

        @Override
        public Void visitSLock(SLock sLock) {
            return null;
        }

        @Override
        public Void visitXLock(XLock xLock) {
            return null;
        }

        @Override
        public Void visitFrameEdge(FrameEdge frameEdge) {
            return null;
        }

        @Override
        public Void visitFrameBetween(FrameBetween frameBetween) {
            nullAccept(frameBetween.getFrom());
            nullAccept(frameBetween.getTo());
            return null;
        }

        @Override
        public Void visitDynamicFrame(DynamicFrame dynamicFrame) {
            nullAccept(dynamicFrame.getFrameRange());
            return null;
        }

        @Override
        public Void visitWindow(Window window) {
            nullAccept(window.getPartitionByList());
            nullAccept(window.getOrderBySeg());
            nullAccept(window.getDynamicFrame());
            return null;
        }

        @Override
        public Void visitWindowFunction(WindowFunction windowFunction) {
            nullAccept(windowFunction.getWindow());
            nullAccept(windowFunction.getParameterList());
            return null;
        }

        @Override
        public Void visitWhereSeg(WhereSeg whereSeg) {
            nullAccept(whereSeg.getLogic());
            return null;
        }

        @Override
        public Void visitLogic(Logic logic) {
            nullAccept(logic.getLeftCurd());
            nullAccept(logic.getRightCurd());
            return null;
        }

        @Override
        public Void visitComparison(Comparison comparison) {
            nullAccept(comparison.getLeftCurd());
            nullAccept(comparison.getComparisonSymbol());
            nullAccept(comparison.getRightCurd());
            return null;
        }

        @Override
        public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
            nullAccept(binaryArithmetic.getLeftCurd());
            nullAccept(binaryArithmetic.getRightCurd());
            return null;
        }

        @Override
        public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
            nullAccept(unaryArithmetic.getCurd());
            return null;
        }

        @Override
        public Void visitLiteral(Literal literal) {
            return null;
        }

        @Override
        public Void visitGrouping(Grouping grouping) {
            nullAccept(grouping.getCurd());
            return null;
        }

        @Override
        public Void visitIdentifier(Identifier identifier) {
            return null;
        }

        @Override
        public Void visitFunction(Function function) {
            nullAccept(function.getParameterList());
            return null;
        }

        @Override
        public Void visitTimeInterval(TimeInterval timeInterval) {
            return null;
        }

        @Override
        public Void visitTimeUnit(TimeUnit timeUnit) {
            nullAccept(timeUnit.getCurd());
            return null;
        }

        @Override
        public Void visitIsNot(IsNot isNot) {
            return null;
        }
    }

    public static class DeleteAddTableConditionVisitor extends DeleteVisitor<Void> {

        protected final String tableName;

        protected final String condition;


        public DeleteAddTableConditionVisitor(String tableName, String condition) {
            this.tableName = tableName;
            this.condition = condition;
        }

        @Override
        public Void visitInCondition(InCondition inCondition) {
            nullAccept(inCondition.getSelect());
            nullAccept(inCondition.getRange());
            return null;
        }

        @Override
        public Void visitDelete(Delete delete) {
            WhereSeg whereSeg = (WhereSeg) delete.getWhereSeg();
            nullAccept(whereSeg);
            String tn = (String) delete.getTableName().getLiteral();
            if (tableName.equals(tn)) {
                Delete conditionCurd = (Delete) CurdUtils.parse("delete from dual where " + condition, false);
                if (whereSeg == null) {
                    whereSeg = (WhereSeg) conditionCurd.getWhereSeg();
                    ReflectUtils.setFieldValue(delete, "whereSeg", whereSeg);
                } else {
                    Curd logic = whereSeg.getLogic();
                    logic = new Logic(logic, new Token(TokenType.AND, "and"), ((WhereSeg) conditionCurd.getWhereSeg()).getLogic());
                    ReflectUtils.setFieldValue(whereSeg, "logic", logic);
                }
            }
            return null;
        }

        @Override
        public Void visitWhereSeg(WhereSeg whereSeg) {
            nullAccept(whereSeg.getLogic());
            return null;
        }

        @Override
        public Void visitLogic(Logic logic) {
            nullAccept(logic.getLeftCurd());
            nullAccept(logic.getRightCurd());
            return null;
        }

        @Override
        public Void visitComparison(Comparison comparison) {
            nullAccept(comparison.getLeftCurd());
            nullAccept(comparison.getRightCurd());
            return null;
        }

        @Override
        public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
            nullAccept(binaryArithmetic.getLeftCurd());
            nullAccept(binaryArithmetic.getRightCurd());
            return null;
        }

        @Override
        public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
            nullAccept(unaryArithmetic.getCurd());
            return null;
        }

        @Override
        public Void visitLiteral(Literal literal) {
            return null;
        }

        @Override
        public Void visitGrouping(Grouping grouping) {
            nullAccept(grouping.getCurd());
            return null;
        }

        @Override
        public Void visitIdentifier(Identifier identifier) {
            return null;
        }

        @Override
        public Void visitFunction(Function function) {
            nullAccept(function.getParameterList());
            return null;
        }

        @Override
        public Void visitAssignmentList(AssignmentList assignmentList) {
            List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
            if (entryList != null) {
                List<Curd> collect = entryList
                        .stream()
                        .map(AssignmentList.Entry::getValue)
                        .collect(Collectors.toList());
                nullAccept(collect);
            }
            return null;
        }

        @Override
        public Void visitTimeInterval(TimeInterval timeInterval) {
            return null;
        }

        @Override
        public Void visitTimeUnit(TimeUnit timeUnit) {
            nullAccept(timeUnit.getCurd());
            return null;
        }

        @Override
        public Void visitIsNot(IsNot isNot) {
            return null;
        }
    }

    public static class UpdateAddTableConditionVisitor extends UpdateVisitor<Void> {

        protected final String tableName;

        protected final String condition;

        public UpdateAddTableConditionVisitor(String tableName, String condition) {
            this.tableName = tableName;
            this.condition = condition;
        }

        @Override
        public Void visitInCondition(InCondition inCondition) {
            nullAccept(inCondition.getSelect());
            nullAccept(inCondition.getRange());
            return null;
        }

        @Override
        public Void visitUpdate(Update update) {
            nullAccept(update.getAssignmentList());

            WhereSeg whereSeg = (WhereSeg) update.getWhereSeg();
            nullAccept(whereSeg);
            String tn = (String) update.getTableName().getLiteral();
            if (tableName.equals(tn)) {
                Delete conditionCurd = (Delete) CurdUtils.parse("delete from dual where " + condition, false);
                if (whereSeg == null) {
                    whereSeg = (WhereSeg) conditionCurd.getWhereSeg();
                    ReflectUtils.setFieldValue(update, "whereSeg", whereSeg);
                } else {
                    Curd logic = whereSeg.getLogic();
                    logic = new Logic(logic, new Token(TokenType.AND, "and"), ((WhereSeg) conditionCurd.getWhereSeg()).getLogic());
                    ReflectUtils.setFieldValue(whereSeg, "logic", logic);
                }
            }
            return null;
        }

        @Override
        public Void visitWhereSeg(WhereSeg whereSeg) {
            nullAccept(whereSeg.getLogic());
            return null;
        }

        @Override
        public Void visitLogic(Logic logic) {
            nullAccept(logic.getLeftCurd());
            nullAccept(logic.getRightCurd());
            return null;
        }

        @Override
        public Void visitComparison(Comparison comparison) {
            nullAccept(comparison.getLeftCurd());
            nullAccept(comparison.getRightCurd());
            return null;
        }

        @Override
        public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
            nullAccept(binaryArithmetic.getLeftCurd());
            nullAccept(binaryArithmetic.getRightCurd());
            return null;
        }

        @Override
        public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
            nullAccept(unaryArithmetic.getCurd());
            return null;
        }

        @Override
        public Void visitLiteral(Literal literal) {
            return null;
        }

        @Override
        public Void visitGrouping(Grouping grouping) {
            nullAccept(grouping.getCurd());
            return null;
        }

        @Override
        public Void visitIdentifier(Identifier identifier) {
            return null;
        }

        @Override
        public Void visitFunction(Function function) {
            nullAccept(function.getParameterList());
            return null;
        }

        @Override
        public Void visitAssignmentList(AssignmentList assignmentList) {
            List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
            if (entryList != null) {
                List<Curd> collect = entryList
                        .stream()
                        .map(AssignmentList.Entry::getValue)
                        .collect(Collectors.toList());
                nullAccept(collect);
            }
            return null;
        }

        @Override
        public Void visitTimeInterval(TimeInterval timeInterval) {
            return null;
        }

        @Override
        public Void visitTimeUnit(TimeUnit timeUnit) {
            nullAccept(timeUnit.getCurd());
            return null;
        }

        @Override
        public Void visitIsNot(IsNot isNot) {
            return null;
        }
    }

    public static class InsertAddTableConditionVisitor extends InsertVisitor<Void> {

        protected final String tableName;

        protected final String condition;

        public InsertAddTableConditionVisitor(String tableName, String condition) {
            this.tableName = tableName;
            this.condition = condition;
        }

        @Override
        public Void visitInsert(Insert insert) {
            nullAccept(insert.getInsertRep());
            return null;
        }

        @Override
        public Void visitInsertValuesRep(InsertValuesRep insertValuesRep) {
            return null;
        }

        @Override
        public Void visitInsertSetRep(InsertSetRep insertSetRep) {
            return null;
        }

        @Override
        public Void visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
            return null;
        }

        @Override
        public Void visitInsertSelectRep(InsertSelectRep insertSelectRep) {
            Curd select = insertSelectRep.getSelect();
            SelectAddTableConditionVisitor visitor = new SelectAddTableConditionVisitor(tableName, condition);
            select.accept(visitor);
            return null;
        }

        @Override
        public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
            return null;
        }

        @Override
        public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
            return null;
        }

        @Override
        public Void visitLiteral(Literal literal) {
            return null;
        }

        @Override
        public Void visitGrouping(Grouping grouping) {
            return null;
        }

        @Override
        public Void visitIdentifier(Identifier identifier) {
            return null;
        }

        @Override
        public Void visitFunction(Function function) {
            return null;
        }

        @Override
        public Void visitAssignmentList(AssignmentList assignmentList) {
            return null;
        }

        @Override
        public Void visitTimeInterval(TimeInterval timeInterval) {
            return null;
        }

        @Override
        public Void visitTimeUnit(TimeUnit timeUnit) {
            return null;
        }

        @Override
        public Void visitIsNot(IsNot isNot) {
            return null;
        }
    }

    public static class SelectAddViewConditionVisitor extends SelectAddTableConditionVisitor {

        public SelectAddViewConditionVisitor(String tableName, String condition) {
            super(tableName, condition);
        }

        @Override
        public Void visitTableRep(TableRep tableRep) {
            Curd curd = tableRep.getCurd();
            if (curd != null && curd instanceof Select) {
                nullAccept(curd);
            } else {
                Token alias = tableRep.getAlias();
                Identifier identifier = (Identifier) curd;
                // 如果不存在别名，表名就是view名
                String tn = (String) identifier.getName().getLiteral();
                if (alias == null) {
                    if (!tableName.equals(tn)) {
                        return null;
                    } else {
                        // 不存在别名的时候，使用表名作为别名
                        // A  ->  "(select * from A where condition) A"
                        Select select = (Select) CurdUtils.parse("select * from " + tn + " where " + condition, false);
                        AbstractCurdParser.saveSelectType(select, SingleSelectType.TABLE, SelectType.VIEW, true);
                        ReflectUtils.setFieldValue(tableRep, "curd", select);
                        ReflectUtils.setFieldValue(tableRep, "alias", identifier.getName().deepClone());
                    }
                } else {
                    String view = (String) alias.getLiteral();
                    if (!tableName.equals(view)) {
                        return null;
                    } else {
                        // 存在别名的时候，curd直接替换
                        // A A1  -> "(select * from A where condition) A1"
                        Select select = (Select) CurdUtils.parse("select * from " + tn + " where " + condition, false);
                        AbstractCurdParser.saveSelectType(select, SingleSelectType.TABLE, SelectType.VIEW, true);
                        ReflectUtils.setFieldValue(tableRep, "curd", select);
                    }
                }

            }
            return null;
        }

    }

    public static class DeleteAddViewConditionVisitor extends DeleteAddTableConditionVisitor {

        public DeleteAddViewConditionVisitor(String tableName, String condition) {
            super(tableName, condition);
        }
    }

    public static class UpdateAddViewConditionVisitor extends UpdateAddTableConditionVisitor {

        public UpdateAddViewConditionVisitor(String tableName, String condition) {
            super(tableName, condition);
        }
    }

    public static class InsertAddViewConditionVisitor extends InsertAddTableConditionVisitor {

        public InsertAddViewConditionVisitor(String tableName, String condition) {
            super(tableName, condition);
        }
    }

    public static SelectVisitor<Void> getSelectAddTableConditionVisitor(String tableName, String condition) {
        return new SelectAddTableConditionVisitor(tableName, condition);
    }

    public static DeleteVisitor<Void> getDeleteAddTableConditionVisitor(String tableName, String condition) {
        return new DeleteAddTableConditionVisitor(tableName, condition);
    }

    public static UpdateVisitor<Void> getUpdateAddTableConditionVisitor(String tableName, String condition) {
        return new UpdateAddTableConditionVisitor(tableName, condition);
    }

    public static InsertVisitor<Void> getInsertAddTableConditionVisitor(String tableName, String condition) {
        return new InsertAddTableConditionVisitor(tableName, condition);
    }

    public static SelectVisitor<Void> getSelectAddViewConditionVisitor(String tableName, String condition) {
        return new SelectAddViewConditionVisitor(tableName, condition);
    }

    public static DeleteVisitor<Void> getDeleteAddViewConditionVisitor(String tableName, String condition) {
        return new DeleteAddViewConditionVisitor(tableName, condition);
    }

    public static UpdateVisitor<Void> getUpdateAddViewConditionVisitor(String tableName, String condition) {
        return new UpdateAddViewConditionVisitor(tableName, condition);
    }

    public static InsertVisitor<Void> getInsertAddViewConditionVisitor(String tableName, String condition) {
        return new InsertAddViewConditionVisitor(tableName, condition);
    }

}
