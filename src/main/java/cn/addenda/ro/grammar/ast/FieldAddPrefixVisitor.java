package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.util.ReflectUtils;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/2/1 13:10
 */
public class FieldAddPrefixVisitor implements CurdVisitor<Void> {

    private final String prefix;

    public FieldAddPrefixVisitor(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Void visitSelect(Select select) {
        select.getLeftCurd().accept(this);
        nullAccept(select.getRightCurd());
        return null;
    }

    @Override
    public Void visitSingleSelect(SingleSelect singleSelect) {
        singleSelect.getColumnSeg().accept(this);
        singleSelect.getTableSeg().accept(this);
        nullAccept(singleSelect.getWhereSeg());
        nullAccept(singleSelect.getGroupBySeg());
        nullAccept(singleSelect.getOrderBySeg());
        return null;
    }

    @Override
    public Void visitColumnSeg(ColumnSeg columnSeg) {
        nullAccept(columnSeg.getColumnRepList());
        return null;
    }

    @Override
    public Void visitColumnRep(ColumnRep columnRep) {
        // 别名不是字段
        columnRep.getCurd().accept(this);
        return null;
    }

    @Override
    public Void visitTableSeg(TableSeg tableSeg) {
        tableSeg.getLeftCurd().accept(this);
        nullAccept(tableSeg.getRightCurd());
        nullAccept(tableSeg.getCondition());
        return null;
    }

    @Override
    public Void visitTableRep(TableRep tableRep) {
        Curd curd = tableRep.getCurd();
        // 表名不是字段
        if (curd instanceof Select) {
            curd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitInCondition(InCondition inCondition) {
        Token identifier = inCondition.getIdentifier();
        addPrefix(identifier);
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
        List<Token> columnList = groupBySeg.getColumnList();
        columnList.forEach(this::addPrefix);
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
        orderItem.getColumn().accept(this);
        return null;
    }

    @Override
    public Void visitLimitSeg(LimitSeg limitSeg) {
        return null;
    }

    @Override
    public Void visitGroupFunction(GroupFunction groupFunction) {
        groupFunction.getCurd().accept(this);
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
        nullAccept(caseWhen.getResultList());
        nullAccept(caseWhen.getDefaultValue());
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
        frameBetween.getFrom().accept(this);
        frameBetween.getTo().accept(this);
        return null;
    }

    @Override
    public Void visitDynamicFrame(DynamicFrame dynamicFrame) {
        dynamicFrame.getFrameRange().accept(this);
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
        nullAccept(windowFunction.getParameterList());
        nullAccept(windowFunction.getWindow());
        return null;
    }

    @Override
    public Void visitInsert(Insert insert) {
        insert.getInsertRep().accept(this);
        nullAccept(insert.getOnDuplicateUpdate());
        return null;
    }

    @Override
    public Void visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        return null;
    }

    @Override
    public Void visitInsertSetRep(InsertSetRep insertSetRep) {
        insertSetRep.getAssignmentList().accept(this);
        return null;
    }

    @Override
    public Void visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        nullAccept(onDuplicateKey.getAssignmentList());
        return null;
    }

    @Override
    public Void visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        insertSelectRep.getSelect().accept(this);
        return null;
    }

    @Override
    public Void visitUpdate(Update update) {
        nullAccept(update.getAssignmentList());
        nullAccept(update.getWhereSeg());
        return null;
    }

    @Override
    public Void visitDelete(Delete delete) {
        nullAccept(delete.getWhereSeg());
        return null;
    }

    @Override
    public Void visitWhereSeg(WhereSeg whereSeg) {
        whereSeg.getLogic().accept(this);
        return null;
    }

    @Override
    public Void visitLogic(Logic logic) {
        visitBinary(logic);
        return null;
    }

    @Override
    public Void visitComparison(Comparison comparison) {
        visitBinary(comparison);
        return null;
    }

    @Override
    public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        visitBinary(binaryArithmetic);
        return null;
    }

    private void visitBinary(Binary binary) {
        binary.getLeftCurd().accept(this);
        nullAccept(binary.getRightCurd());
    }

    @Override
    public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        unaryArithmetic.getCurd().accept(this);
        return null;
    }

    @Override
    public Void visitLiteral(Literal literal) {
        return null;
    }

    @Override
    public Void visitGrouping(Grouping grouping) {
        grouping.getCurd().accept(this);
        return null;
    }

    @Override
    public Void visitIdentifier(Identifier identifier) {
        addPrefix(identifier.getName());
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
        for (AssignmentList.Entry entry : entryList) {
            entry.getValue().accept(this);

            addPrefix(entry.getColumn());
        }
        return null;
    }

    private void addPrefix(Token field) {
        String literal = (String) field.getLiteral();
        if (!"*".equals(literal)) {
            ReflectUtils.setFieldValue(field, "literal", prefix + "." + literal);
        }
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
