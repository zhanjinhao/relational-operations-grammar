package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author addenda
 * @datetime 2021/3/2 20:12
 */
public interface CurdVisitor<R> {

    // ----------------------------------------------------------
    // retrieve 的观察方法
    // ----------------------------------------------------------

    R visitSelect(Select select);

    R visitSingleSelect(SingleSelect singleSelect);

    R visitColumnSeg(ColumnSeg columnSeg);

    R visitColumnRep(ColumnRep columnRep);

    R visitTableSeg(TableSeg tableSeg);

    R visitTableRep(TableRep tableRep);

    R visitInCondition(InCondition inCondition);

    R visitExistsCondition(ExistsCondition existsCondition);

    R visitGroupBySeg(GroupBySeg groupBySeg);

    R visitOrderBySeg(OrderBySeg orderBySeg);

    R visitOrderItem(OrderItem orderItem);

    R visitLimitSeg(LimitSeg limitSeg);

    R visitGroupFunction(GroupFunction groupFunction);

    R visitGroupConcat(GroupConcat groupConcat);

    R visitCaseWhen(CaseWhen caseWhen);

    R visitSLock(SLock sLock);

    R visitXLock(XLock xLock);

    R visitFrameEdge(FrameEdge frameEdge);

    R visitFrameBetween(FrameBetween frameBetween);

    R visitDynamicFrame(DynamicFrame dynamicFrame);

    R visitWindow(Window window);

    R visitWindowFunction(WindowFunction windowFunction);

    // ----------------------------------------------------------
    // create 的观察方法
    // ----------------------------------------------------------

    R visitInsert(Insert insert);

    R visitInsertValuesRep(InsertValuesRep insertValuesRep);

    R visitInsertSetRep(InsertSetRep insertSetRep);

    R visitOnDuplicateKey(OnDuplicateKey onDuplicateKey);

    R visitInsertSelectRep(InsertSelectRep insertSelectRep);

    // ----------------------------------------------------------
    // update 的观察方法
    // ----------------------------------------------------------

    R visitUpdate(Update update);

    // ----------------------------------------------------------
    // delete 的观察方法
    // ----------------------------------------------------------

    R visitDelete(Delete delete);

    // ----------------------------------------------------------
    // expression 的观察方法
    // ----------------------------------------------------------

    R visitWhereSeg(WhereSeg whereSeg);

    R visitLogic(Logic logic);

    R visitComparison(Comparison comparison);

    R visitBinaryArithmetic(BinaryArithmetic binaryArithmetic);

    R visitUnaryArithmetic(UnaryArithmetic unaryArithmetic);

    R visitLiteral(Literal literal);

    R visitGrouping(Grouping grouping);

    R visitIdentifier(Identifier identifier);

    R visitFunction(Function function);

    R visitAssignmentList(AssignmentList assignmentList);

    R visitTimeInterval(TimeInterval timeInterval);

    R visitTimeUnit(TimeUnit timeUnit);

    R visitIsNot(IsNot isNot);

    default R nullAccept(Curd curd) {
        if (curd == null) {
            return null;
        }
        return curd.accept(this);
    }

    default void nullAccept(List<Curd> curdList) {
        if (curdList == null) {
            return;
        }

        for (Curd curd : curdList) {
            curd.accept(this);
        }
    }

    default R nullAccept(List<Curd> curdList, BinaryOperator<R> mergeFunction) {
        if (curdList == null) {
            return null;
        }

        R result = null;
        for (Curd curd : curdList) {
            R accept = curd.accept(this);
            if (result == null) {
                result = accept;
            } else {
                result = mergeFunction.apply(result, accept);
            }
        }
        return result;
    }

}