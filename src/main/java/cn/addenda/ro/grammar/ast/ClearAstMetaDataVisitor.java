package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/8/16 10:59
 */
public class ClearAstMetaDataVisitor implements CurdVisitor<Void> {

    @Override
    public Void visitSelect(Select select) {
        nullAccept(select.getLeftCurd());
        nullAccept(select.getRightCurd());
        nullReset(select);
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
        nullReset(singleSelect);
        return null;
    }

    @Override
    public Void visitColumnSeg(ColumnSeg columnSeg) {

        final List<Curd> columnRepList = columnSeg.getColumnRepList();
        if (columnRepList != null) {
            columnRepList.forEach(this::nullAccept);
        }

        nullReset(columnSeg);
        return null;
    }

    @Override
    public Void visitColumnRep(ColumnRep columnRep) {
        nullAccept(columnRep.getCurd());
        nullReset(columnRep);
        return null;
    }

    @Override
    public Void visitTableSeg(TableSeg tableSeg) {
        nullAccept(tableSeg.getLeftCurd());
        nullAccept(tableSeg.getRightCurd());
        nullReset(tableSeg);
        return null;
    }

    @Override
    public Void visitTableRep(TableRep tableRep) {
        nullAccept(tableRep.getCurd());
        nullReset(tableRep);
        return null;
    }

    @Override
    public Void visitInCondition(InCondition inCondition) {
        nullAccept(inCondition.getSelect());
        final List<Curd> range = inCondition.getRange();

        if (range != null) {
            range.forEach(this::nullAccept);
        }

        nullReset(inCondition);
        return null;
    }

    @Override
    public Void visitExistsCondition(ExistsCondition existsCondition) {
        nullAccept(existsCondition.getCurd());
        nullReset(existsCondition);
        return null;
    }

    @Override
    public Void visitGroupBySeg(GroupBySeg groupBySeg) {
        nullAccept(groupBySeg.getHaving());
        nullReset(groupBySeg);
        return null;
    }

    @Override
    public Void visitOrderBySeg(OrderBySeg orderBySeg) {
        nullReset(orderBySeg);
        return null;
    }

    @Override
    public Void visitLimitSeg(LimitSeg limitSeg) {
        nullReset(limitSeg);
        return null;
    }

    @Override
    public Void visitGroupFunction(GroupFunction groupFunction) {
        nullAccept(groupFunction.getCurd());
        nullReset(groupFunction);
        return null;
    }

    @Override
    public Void visitCaseWhen(CaseWhen caseWhen) {
        nullAccept(caseWhen.getValue());
        nullAccept(caseWhen.getDefaultValue());

        final List<Curd> conditionList = caseWhen.getConditionList();
        if (conditionList != null) {
            conditionList.forEach(this::nullAccept);
        }

        final List<Curd> resultList = caseWhen.getResultList();
        if (resultList != null) {
            resultList.forEach(this::nullAccept);
        }

        nullReset(caseWhen);
        return null;
    }

    @Override
    public Void visitInsert(Insert insert) {
        nullAccept(insert.getInsertRep());
        nullAccept(insert.getOnDuplicateUpdate());
        nullReset(insert);
        return null;
    }

    @Override
    public Void visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        final List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        if (curdListList != null) {
            for (List<Curd> curdList : curdListList) {
                curdList.forEach(this::nullAccept);
            }
        }
        nullReset(insertValuesRep);
        return null;
    }

    @Override
    public Void visitInsertSetRep(InsertSetRep insertSetRep) {
        nullAccept(insertSetRep.getAssignmentList());
        nullReset(insertSetRep);
        return null;
    }

    @Override
    public Void visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        nullAccept(onDuplicateKey.getAssignmentList());
        nullReset(onDuplicateKey);
        return null;
    }

    @Override
    public Void visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        nullAccept(insertSelectRep.getSelect());
        nullReset(insertSelectRep);
        return null;
    }

    @Override
    public Void visitUpdate(Update update) {
        nullAccept(update.getAssignmentList());
        nullAccept(update.getWhereSeg());
        nullReset(update);
        return null;
    }

    @Override
    public Void visitDelete(Delete delete) {
        nullAccept(delete.getWhereSeg());
        nullReset(delete);
        return null;
    }

    @Override
    public Void visitWhereSeg(WhereSeg whereSeg) {
        nullAccept(whereSeg.getLogic());
        nullReset(whereSeg);
        return null;
    }

    @Override
    public Void visitLogic(Logic logic) {
        nullAccept(logic.getLeftCurd());
        nullAccept(logic.getRightCurd());
        nullReset(logic);
        return null;
    }

    @Override
    public Void visitComparison(Comparison comparison) {
        nullAccept(comparison.getLeftCurd());
        nullAccept(comparison.getRightCurd());
        nullReset(comparison);
        return null;
    }

    @Override
    public Void visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        nullAccept(binaryArithmetic.getLeftCurd());
        nullAccept(binaryArithmetic.getRightCurd());
        nullReset(binaryArithmetic);
        return null;
    }

    @Override
    public Void visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        nullAccept(unaryArithmetic.getCurd());
        nullReset(unaryArithmetic);
        return null;
    }

    @Override
    public Void visitLiteral(Literal literal) {
        nullReset(literal);
        return null;
    }

    @Override
    public Void visitGrouping(Grouping grouping) {
        nullAccept(grouping.getCurd());
        nullReset(grouping);
        return null;
    }

    @Override
    public Void visitIdentifier(Identifier identifier) {
        nullReset(identifier);
        return null;
    }

    @Override
    public Void visitFunction(Function function) {
        final List<Curd> parameterList = function.getParameterList();
        if (parameterList != null) {
            parameterList.forEach(this::nullAccept);
        }
        nullReset(function);
        return null;
    }

    @Override
    public Void visitAssignmentList(AssignmentList assignmentList) {
        nullReset(assignmentList);
        return null;
    }

    @Override
    public Void visitTimeInterval(TimeInterval timeInterval) {
        nullReset(timeInterval);
        return null;
    }

    @Override
    public Void visitTimeUnit(TimeUnit timeUnit) {
        nullAccept(timeUnit.getCurd());
        nullReset(timeUnit);
        return null;
    }

    @Override
    public Void visitIsNot(IsNot isNot) {
        nullReset(isNot);
        return null;
    }

    private void nullReset(Curd curd) {
        if (curd == null) {
            return;
        }

        final AstMetaData astMetaData = curd.getAstMetaData();
        final AstMetaData newAstMetaData = astMetaData.getNewInstance();
        newAstMetaData.setCurd(curd);
        curd.setAstMetaData(newAstMetaData);
    }


}
