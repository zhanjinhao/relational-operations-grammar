package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;

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

    // ----------------------------------------------------------
    // 抽象类 的观察方法
    // ----------------------------------------------------------

    default R visitBinary(Binary binary) {
        return null;
    }

    default R visitUnary(Unary unary) {
        return null;
    }

    default R visitAttachment(Attachment attachment) {
        return attachment.getAttachment().accept(this);
    }

    default R nullAccept(Curd curd) {
        if (curd == null) {
            return null;
        }
        return curd.accept(this);
    }


}