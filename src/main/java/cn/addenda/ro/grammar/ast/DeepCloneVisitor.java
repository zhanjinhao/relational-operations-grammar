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
        return new Select(select.getLeftCurd().accept(this), nullClone(select.getToken()),
                nullClone(select.getAllToken()), nullClone(select.getRightCurd()));
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

        SingleSelect newSingleSelect = new SingleSelect(columnSeg.accept(this), tableSeg.accept(this),
                nullClone(whereSeg), nullClone(groupBySeg), nullClone(orderBySeg), nullClone(limitSeg), nullClone(lockSeg));
        newSingleSelect.setSingleSelectType(singleSelect.getSingleSelectType());

        return newSingleSelect;
    }

    private Curd nullClone(Curd curd) {
        return curd == null ? null : curd.accept(this);
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
        return new ColumnRep(nullClone(columnRep.getCurd()), nullClone(columnRep.getOperator()));
    }

    @Override
    public Curd visitTableSeg(TableSeg tableSeg) {
        return new TableSeg(nullClone(tableSeg.getQualifier()), nullClone(tableSeg.getLeftCurd()),
                nullClone(tableSeg.getToken()), nullClone(tableSeg.getRightCurd()), nullClone(tableSeg.getCondition()));
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

        return new GroupBySeg(newColumnList, nullClone(groupBySeg.getHaving()));
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
        return new OrderItem(orderItem.getColumn().deepClone(), nullClone(orderItem.getOrderType()));
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
        return new FrameBetween(nullClone(frameBetween.getFrom()), nullClone(frameBetween.getTo()));
    }

    @Override
    public Curd visitDynamicFrame(DynamicFrame dynamicFrame) {
        return new DynamicFrame(nullClone(dynamicFrame.getType()), nullClone(dynamicFrame.getFrameRange()));
    }

    @Override
    public Curd visitWindow(Window window) {
        List<Curd> partitionByList = window.getPartitionByList();
        if (partitionByList == null) {
            return new Window(null,
                    nullClone(window.getOrderBySeg()), nullClone(window.getDynamicFrame()));
        }
        List<Curd> newPartitionByList = new ArrayList<>();
        for (Curd curd : partitionByList) {
            newPartitionByList.add(curd.accept(this));
        }
        return new Window(newPartitionByList,
                nullClone(window.getOrderBySeg()), nullClone(window.getDynamicFrame()));
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
                    newParameterList, nullClone(windowFunction.getWindow()));
        }

        return new WindowFunction(nullClone(windowFunction.getMethod()), null, nullClone(windowFunction.getWindow()));
    }

    @Override
    public Curd visitInsert(Insert insert) {
        Token constrict = insert.getConstrict();
        Token tableName = insert.getTableName();
        Curd curd = insert.getInsertRep();
        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();

        return new Insert(nullClone(constrict), tableName.deepClone(),
                curd.accept(this), nullClone(onDuplicateUpdate), insert.getInsertType());
    }

    @Override
    public Curd visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        List<List<Curd>> newCurdListList = new ArrayList<>();
        for (List<Curd> curdList : curdListList) {
            List<Curd> newCurdList = new ArrayList<>();
            for (Curd curd : curdList) {
                newCurdList.add(curd.accept(this));
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
                update.getAssignmentList().accept(this), nullClone(update.getWhereSeg()));
    }

    @Override
    public Curd visitDelete(Delete delete) {
        return new Delete(delete.getTableName().deepClone(), nullClone(delete.getWhereSeg()));
    }

    @Override
    public Curd visitWhereSeg(WhereSeg whereSeg) {
        return new WhereSeg(whereSeg.getLogic().accept(this));
    }

    @Override
    public Curd visitLogic(Logic logic) {
        return new Logic(logic.getLeftCurd().accept(this),
                nullClone(logic.getToken().deepClone()), nullClone(logic.getRightCurd()));
    }

    @Override
    public Curd visitComparison(Comparison comparison) {
        return new Comparison(comparison.getLeftCurd().accept(this),
                nullClone(comparison.getComparisonSymbol()), nullClone(comparison.getRightCurd()));
    }

    @Override
    public Curd visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return new BinaryArithmetic(binaryArithmetic.getLeftCurd().accept(this),
                nullClone(binaryArithmetic.getToken()), nullClone(binaryArithmetic.getRightCurd()));
    }

    @Override
    public Curd visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        return new UnaryArithmetic(unaryArithmetic.getOperator().deepClone(), unaryArithmetic.getCurd().accept(this));
    }

    @Override
    public Curd visitLiteral(Literal literal) {
        return new Literal(literal.getValue().deepClone());
    }

    @Override
    public Curd visitGrouping(Grouping grouping) {
        return new Grouping(grouping.getCurd().accept(this));
    }

    @Override
    public Curd visitIdentifier(Identifier identifier) {
        return new Identifier(identifier.getName().deepClone());
    }

    @Override
    public Curd visitFunction(Function function) {
        Token method = function.getMethod();
        List<Curd> parameterList = function.getParameterList();
        if (parameterList != null) {
            List<Curd> newParameterList = new ArrayList<>();
            for (Curd curd : parameterList) {
                newParameterList.add(curd.accept(this));
            }
            return new Function(method.deepClone(), newParameterList);
        }
        return new Function(method.deepClone(), null);
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
        return new TimeUnit(timeType.deepClone(), curd.accept(this));
    }

    @Override
    public Curd visitIsNot(IsNot isNot) {
        Token notToken = isNot.getNotToken();
        Token isToken = isNot.getIsToken();

        return new IsNot(isToken.deepClone(), nullClone(notToken));
    }
}
