package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * @author addenda
 * @datetime 2022/1/27 13:55
 */
public class DeepCloneVisitor implements CurdVisitor<Curd> {

    @Override
    public Curd visitSelect(Select select) {
        Select result = new Select(select.getLeftCurd().accept(this), nullClone(select.getToken()),
                nullClone(select.getAllToken()), nullAccept(select.getRightCurd()));
        result.setSelectType(select.getSelectType());
        return result;
    }

    @Override
    public Curd visitSingleSelect(SingleSelect singleSelect) {
        Curd columnSeg = singleSelect.getColumnSeg();
        Curd tableSeg = singleSelect.getTableSeg();
        Curd whereSeg = singleSelect.getWhereSeg();
        Curd groupBySeg = singleSelect.getGroupBySeg();
        Curd orderBySeg = singleSelect.getOrderBySeg();
        Curd limitSeg = singleSelect.getLimitSeg();
        Curd lockSeg = singleSelect.getLockSeg();

        SingleSelect newSingleSelect = new SingleSelect(nullAccept(columnSeg), nullAccept(tableSeg),
            nullAccept(whereSeg), nullAccept(groupBySeg), nullAccept(orderBySeg), nullAccept(limitSeg), nullAccept(lockSeg));
        newSingleSelect.setSingleSelectType(singleSelect.getSingleSelectType());

        return newSingleSelect;
    }

    private Token nullClone(Token curd) {
        return curd == null ? null : curd.deepClone();
    }

    @Override
    public Curd visitColumnSeg(ColumnSeg columnSeg) {
        List<Curd> columnRepList = columnSeg.getColumnRepList();
        List<Curd> newColumnRepList = new ArrayList<>();
        for (Curd curd : columnRepList) {
            newColumnRepList.add(curd.accept(this));
        }
        return new ColumnSeg(nullClone(columnSeg.getRestriction()), newColumnRepList);
    }

    @Override
    public Curd visitColumnRep(ColumnRep columnRep) {
        return new ColumnRep(nullAccept(columnRep.getCurd()), nullClone(columnRep.getOperator()));
    }

    @Override
    public Curd visitTableSeg(TableSeg tableSeg) {
        return new TableSeg(nullClone(tableSeg.getQualifier()), nullAccept(tableSeg.getLeftCurd()),
            nullClone(tableSeg.getToken()), nullAccept(tableSeg.getRightCurd()), nullAccept(tableSeg.getCondition()));
    }

    @Override
    public Curd visitTableRep(TableRep tableRep) {
        Curd curd = tableRep.getCurd();
        Token alias = tableRep.getAlias();
        return new TableRep(curd.accept(this), nullClone(alias));
    }

    @Override
    public Curd visitInCondition(InCondition inCondition) {
        Token in = inCondition.getIn();
        Token identifier = inCondition.getIdentifier();
        List<Curd> range = inCondition.getRange();

        if (range != null) {
            List<Curd> newRange = new ArrayList<>();
            for (Curd curd : range) {
                newRange.add(curd.accept(this));
            }
            return new InCondition(in, identifier.deepClone(), newRange);
        } else {
            Curd curd = inCondition.getSelect();
            return new InCondition(in, identifier.deepClone(), curd.accept(this));
        }
    }

    @Override
    public Curd visitExistsCondition(ExistsCondition existsCondition) {
        Curd curd = existsCondition.getCurd();
        Token operator = existsCondition.getOperator();
        return new ExistsCondition(operator.deepClone(), curd.accept(this));
    }

    @Override
    public Curd visitGroupBySeg(GroupBySeg groupBySeg) {
        List<Token> columnList = groupBySeg.getColumnList();
        List<Token> newColumnList = new ArrayList<>();
        for (Token token : columnList) {
            newColumnList.add(token.deepClone());
        }

        return new GroupBySeg(newColumnList, nullAccept(groupBySeg.getHaving()));
    }

    @Override
    public Curd visitOrderBySeg(OrderBySeg orderBySeg) {
        List<Curd> columnList = orderBySeg.getColumnList();
        List<Curd> newColumnList = new ArrayList<>();
        for (Curd curd : columnList) {
            newColumnList.add(curd.accept(this));
        }
        return new OrderBySeg(newColumnList);
    }

    @Override
    public Curd visitOrderItem(OrderItem orderItem) {
        return new OrderItem(nullAccept(orderItem.getColumn()), nullClone(orderItem.getOrderType()));
    }

    @Override
    public Curd visitLimitSeg(LimitSeg limitSeg) {
        return new LimitSeg(nullClone(limitSeg.getNum()), nullClone(limitSeg.getOffset()));
    }

    @Override
    public Curd visitGroupFunction(GroupFunction groupFunction) {
        return new GroupFunction(groupFunction.getMethod().deepClone(), groupFunction.getCurd().accept(this));
    }

    @Override
    public Curd visitGroupConcat(GroupConcat groupConcat) {
        final List<Curd> resultList = groupConcat.getResultList();
        List<Curd> newResultList = new ArrayList<>();
        for (Curd curd : resultList) {
            newResultList.add(curd.accept(this));
        }

        final List<Curd> orderItemList = groupConcat.getOrderItemList();
        List<Curd> newOrderItemList = null;
        if (orderItemList != null) {
            newOrderItemList = new ArrayList<>();
            for (Curd curd : orderItemList) {
                newOrderItemList.add(curd.accept(this));
            }
        }

        return new GroupConcat(nullClone(groupConcat.getModifier()), newResultList, newOrderItemList, groupConcat.getSeparator());
    }

    @Override
    public Curd visitCaseWhen(CaseWhen caseWhen) {
        List<Curd> conditionList = caseWhen.getConditionList();
        List<Curd> newConditionList = new ArrayList<>();
        for (Curd curd : conditionList) {
            newConditionList.add(curd.accept(this));
        }

        List<Curd> resultList = caseWhen.getResultList();
        List<Curd> newResultList = new ArrayList<>();
        for (Curd curd : resultList) {
            newResultList.add(curd.accept(this));
        }

        return new CaseWhen(caseWhen.getValue().accept(this),
            newConditionList, newResultList, caseWhen.getDefaultValue().accept(this));
    }

    @Override
    public Curd visitSLock(SLock sLock) {
        return new SLock();
    }

    @Override
    public Curd visitXLock(XLock xLock) {
        return new XLock();
    }

    @Override
    public Curd visitFrameEdge(FrameEdge frameEdge) {
        return new FrameEdge(nullClone(frameEdge.getEdge()), nullClone(frameEdge.getTowards()));
    }

    @Override
    public Curd visitFrameBetween(FrameBetween frameBetween) {
        return new FrameBetween(nullAccept(frameBetween.getFrom()), nullAccept(frameBetween.getTo()));
    }

    @Override
    public Curd visitDynamicFrame(DynamicFrame dynamicFrame) {
        return new DynamicFrame(nullClone(dynamicFrame.getType()), nullAccept(dynamicFrame.getFrameRange()));
    }

    @Override
    public Curd visitWindow(Window window) {
        List<Curd> partitionByList = window.getPartitionByList();
        if (partitionByList == null) {
            return new Window(null,
                nullAccept(window.getOrderBySeg()), nullAccept(window.getDynamicFrame()));
        }
        List<Curd> newPartitionByList = new ArrayList<>();
        for (Curd curd : partitionByList) {
            newPartitionByList.add(curd.accept(this));
        }
        return new Window(newPartitionByList,
            nullAccept(window.getOrderBySeg()), nullAccept(window.getDynamicFrame()));
    }

    @Override
    public Curd visitWindowFunction(WindowFunction windowFunction) {
        List<Curd> parameterList = windowFunction.getParameterList();
        if (parameterList != null) {
            List<Curd> newParameterList = new ArrayList<>();
            for (Curd curd : parameterList) {
                newParameterList.add(curd.accept(this));
            }
            return new WindowFunction(nullClone(windowFunction.getMethod()),
                newParameterList, nullAccept(windowFunction.getWindow()));
        }

        return new WindowFunction(nullClone(windowFunction.getMethod()), null, nullAccept(windowFunction.getWindow()));
    }

    @Override
    public Curd visitInsert(Insert insert) {
        Token constrict = insert.getConstrict();
        Token tableName = insert.getTableName();
        Curd curd = insert.getInsertRep();
        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();

        return new Insert(nullClone(constrict), nullClone(tableName),
            nullAccept(curd), nullAccept(onDuplicateUpdate), insert.getInsertType());
    }

    @Override
    public Curd visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        List<List<Curd>> newCurdListList = new ArrayList<>();
        for (List<Curd> curdList : curdListList) {
            List<Curd> newCurdList = new ArrayList<>();
            for (Curd curd : curdList) {
                newCurdList.add(nullAccept(curd));
            }
            newCurdListList.add(newCurdList);
        }

        List<Token> columnList = insertValuesRep.getColumnList();
        List<Token> newColumnList = new ArrayList<>();
        for (Token token : columnList) {
            newColumnList.add(token.deepClone());
        }

        return new InsertValuesRep(newColumnList, newCurdListList);
    }

    @Override
    public Curd visitInsertSetRep(InsertSetRep insertSetRep) {
        return new InsertSetRep(insertSetRep.getAssignmentList().accept(this));
    }

    @Override
    public Curd visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        return new OnDuplicateKey(onDuplicateKey.getAssignmentList().accept(this));
    }

    @Override
    public Curd visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        List<Token> columnList = insertSelectRep.getColumnList();
        List<Token> newColumnList = new ArrayList<>();
        for (Token token : columnList) {
            newColumnList.add(token.deepClone());
        }

        return new InsertSelectRep(newColumnList, insertSelectRep.getSelect().accept(this));
    }

    @Override
    public Curd visitUpdate(Update update) {
        return new Update(update.getTableName().deepClone(),
            nullAccept(update.getAssignmentList()), nullAccept(update.getWhereSeg()));
    }

    @Override
    public Curd visitDelete(Delete delete) {
        return new Delete(nullClone(delete.getTableName()), nullAccept(delete.getWhereSeg()));
    }

    @Override
    public Curd visitWhereSeg(WhereSeg whereSeg) {
        return new WhereSeg(nullAccept(whereSeg.getLogic()));
    }

    @Override
    public Curd visitLogic(Logic logic) {
        return new Logic(nullAccept(logic.getLeftCurd()),
            nullClone(logic.getToken()), nullAccept(logic.getRightCurd()));
    }

    @Override
    public Curd visitComparison(Comparison comparison) {
        return new Comparison(nullAccept(comparison.getLeftCurd()),
            nullAccept(comparison.getComparisonSymbol()), nullAccept(comparison.getRightCurd()));
    }

    @Override
    public Curd visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return new BinaryArithmetic(nullAccept(binaryArithmetic.getLeftCurd()),
            nullClone(binaryArithmetic.getToken()), nullAccept(binaryArithmetic.getRightCurd()));
    }

    @Override
    public Curd visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        return new UnaryArithmetic(nullClone(unaryArithmetic.getOperator()), nullAccept(unaryArithmetic.getCurd()));
    }

    @Override
    public Curd visitLiteral(Literal literal) {
        return new Literal(nullClone(literal.getValue()));
    }

    @Override
    public Curd visitGrouping(Grouping grouping) {
        return new Grouping(nullAccept(grouping.getCurd()));
    }

    @Override
    public Curd visitIdentifier(Identifier identifier) {
        return new Identifier(nullClone(identifier.getName()));
    }

    @Override
    public Curd visitFunction(Function function) {
        Token method = function.getMethod();
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null) {
            List<Curd> newParameterList = new ArrayList<>();
            for (Curd curd : parameterList) {
                newParameterList.add(nullAccept(curd));
            }
            return new Function(nullClone(method), newParameterList, function.isIndependent());
        }
        return new Function(nullClone(method), null, function.isIndependent());
    }

    @Override
    public Curd visitAssignmentList(AssignmentList assignmentList) {
        List<AssignmentList.Entry> entryList = assignmentList.getEntryList();
        List<AssignmentList.Entry> newEntryList = new ArrayList<>();

        for (AssignmentList.Entry entry : entryList) {
            Token columnName = entry.getColumn();
            Curd value = entry.getValue();
            newEntryList.add(new AssignmentList.Entry(columnName.deepClone(), value.accept(this)));
        }

        return new AssignmentList(newEntryList);
    }

    @Override
    public Curd visitTimeInterval(TimeInterval timeInterval) {
        return new TimeInterval(timeInterval.getTimeType().deepClone(), timeInterval.getInterval());
    }

    @Override
    public Curd visitTimeUnit(TimeUnit timeUnit) {
        Curd curd = timeUnit.getCurd();
        Token timeType = timeUnit.getTimeType();
        return new TimeUnit(nullClone(timeType), nullAccept(curd));
    }

    @Override
    public Curd visitAttachment(Attachment attachment) {
        return new Attachment(attachment.getAttachment().deepClone());
    }

    @Override
    public Curd visitIsNot(IsNot isNot) {
        Token notToken = isNot.getNotToken();
        Token isToken = isNot.getIsToken();

        return new IsNot(nullClone(isToken), nullClone(notToken));
    }
}
