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
public class IdentifierFillTNVisitor implements CurdVisitor<Void> {

    private final String tableName;

    public IdentifierFillTNVisitor(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Void visitSelect(Select select) {
        select.getLeftCurd().accept(this);
        Curd rightCurd = select.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitSingleSelect(SingleSelect singleSelect) {
        singleSelect.getColumnSeg().accept(this);
        singleSelect.getTableSeg().accept(this);
        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }
        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            groupBySeg.accept(this);
        }
        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            orderBySeg.accept(this);
        }
        return null;
    }

    @Override
    public Void visitColumnSeg(ColumnSeg columnSeg) {
        List<Curd> columnRepList = columnSeg.getColumnRepList();
        if (columnRepList != null) {
            columnRepList.forEach(item -> item.accept(this));
        }
        return null;
    }

    @Override
    public Void visitColumnRep(ColumnRep columnRep) {
        // 不处理别名
        columnRep.getCurd().accept(this);
        return null;
    }

    @Override
    public Void visitTableSeg(TableSeg tableSeg) {
        tableSeg.getLeftCurd().accept(this);
        Curd rightCurd = tableSeg.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        Curd condition = tableSeg.getCondition();
        if (condition != null) {
            condition.accept(this);
        }
        return null;
    }

    @Override
    public Void visitTableRep(TableRep tableRep) {
        tableRep.getCurd().accept(this);
        return null;
    }

    @Override
    public Void visitInCondition(InCondition inCondition) {
        Token identifier = inCondition.getIdentifier();
        addTableName(identifier);

        Curd curd = inCondition.getSelect();
        if (curd != null) {
            curd.accept(this);
        }

        List<Curd> range = inCondition.getRange();
        if (range != null) {
            range.forEach(item -> item.accept(this));
        }
        return null;
    }

    @Override
    public Void visitExistsCondition(ExistsCondition existsCondition) {
        Curd curd = existsCondition.getCurd();
        if (curd != null) {
            curd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitGroupBySeg(GroupBySeg groupBySeg) {
        List<Token> columnList = groupBySeg.getColumnList();
        columnList.forEach(this::addTableName);
        Curd having = groupBySeg.getHaving();
        if (having != null) {
            having.accept(this);
        }
        return null;
    }

    @Override
    public Void visitOrderBySeg(OrderBySeg orderBySeg) {
        List<Curd> columnList = orderBySeg.getColumnList();
        for (Curd curd : columnList) {
            OrderItem orderItem = (OrderItem) curd;
            orderItem.getColumn().accept(this);
        }
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
        Curd curd = groupFunction.getCurd();
        curd.accept(this);
        return null;
    }

    @Override
    public Void visitGroupConcat(GroupConcat groupConcat) {
        final List<Curd> resultList = groupConcat.getResultList();
        for (Curd curd : resultList) {
            curd.accept(this);
        }

        final List<Curd> orderItemList = groupConcat.getOrderItemList();
        if (orderItemList != null) {
            for (Curd curd : orderItemList) {
                curd.accept(this);
            }
        }

        return null;
    }

    @Override
    public Void visitCaseWhen(CaseWhen caseWhen) {
        Curd value = caseWhen.getValue();
        if (value != null) {
            value.accept(this);
        }
        List<Curd> conditionList = caseWhen.getConditionList();
        conditionList.forEach(item -> item.accept(this));
        List<Curd> resultList = caseWhen.getResultList();
        resultList.forEach(item -> item.accept(this));
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
        List<Curd> partitionByList = window.getPartitionByList();
        if (partitionByList != null && !partitionByList.isEmpty()) {
            for (Curd curd : partitionByList) {
                curd.accept(this);
            }
        }
        nullAccept(window.getOrderBySeg());
        nullAccept(window.getDynamicFrame());
        return null;
    }

    @Override
    public Void visitWindowFunction(WindowFunction windowFunction) {
        List<Curd> parameterList = windowFunction.getParameterList();
        if (parameterList != null && !parameterList.isEmpty()) {
            for (Curd curd : parameterList) {
                nullAccept(curd);
            }
        }
        nullAccept(windowFunction.getWindow());
        return null;
    }

    @Override
    public Void visitInsert(Insert insert) {
        insert.getInsertRep().accept(this);
        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();
        if (onDuplicateUpdate != null) {
            onDuplicateUpdate.accept(this);
        }
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
        Curd curd = onDuplicateKey.getAssignmentList();
        if (curd != null) {
            curd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        insertSelectRep.getSelect().accept(this);
        return null;
    }

    @Override
    public Void visitUpdate(Update update) {
        Curd assignmentList = update.getAssignmentList();
        if (assignmentList != null) {
            assignmentList.accept(this);
        }
        Curd whereSeg = update.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }
        return null;
    }

    @Override
    public Void visitDelete(Delete delete) {
        Curd whereSeg = delete.getWhereSeg();
        if (whereSeg != null) {
            whereSeg.accept(this);
        }
        return null;
    }

    @Override
    public Void visitWhereSeg(WhereSeg whereSeg) {
        whereSeg.getLogic().accept(this);
        return null;
    }

    @Override
    public Void visitLogic(Logic logic) {
        logic.getLeftCurd().accept(this);
        Curd rightCurd = logic.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitComparison(Comparison comparison) {
        comparison.getLeftCurd().accept(this);
        Curd rightCurd = comparison.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        binaryArithmetic.getLeftCurd().accept(this);
        Curd rightCurd = binaryArithmetic.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
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
        Token name = identifier.getName();
        addTableName(name);
        return null;
    }

    @Override
    public Void visitFunction(Function function) {
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null) {
            parameterList.forEach(item -> item.accept(this));
        }
        return null;
    }

    @Override
    public Void visitAssignmentList(AssignmentList assignmentList) {
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        for (AssignmentList.Entry entry : entryList) {
            entry.getValue().accept(this);
            Token columnName = entry.getColumn();
            addTableName(columnName);
        }
        return null;
    }

    private void addTableName(Token columnName) {
        String literal = (String) columnName.getLiteral();
        if (!"*".equals(literal)) {
            ReflectUtils.setFieldValue(columnName, "literal", tableName + "." + literal);
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
