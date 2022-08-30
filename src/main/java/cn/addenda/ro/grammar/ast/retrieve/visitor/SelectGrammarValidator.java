package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.LogicGrammarValidationDelegate;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.Identifier;
import cn.addenda.ro.grammar.ast.expression.Literal;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionGrammarValidator;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.List;

/**
 * @author addenda
 * @datetime 2021/12/16 19:50
 */
public class SelectGrammarValidator extends SelectVisitorWithDelegate<Void> {

    public SelectGrammarValidator(ROErrorReporter roErrorReporter) {
        super.init(new ExpressionGrammarValidator(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public Void visitSelect(Select select) {
        Curd leftCurd = select.getLeftCurd();
        if (leftCurd == null) {
            error(AstROErrorReporterDelegate.SELECT_select_VALIDATION);
            return null;
        }
        leftCurd.accept(this);
        Curd rightCurd = select.getRightCurd();
        if (rightCurd != null) {
            rightCurd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitSingleSelect(SingleSelect singleSelect) {
        Curd columnSeg = singleSelect.getColumnSeg();
        if (columnSeg == null) {
            error(AstROErrorReporterDelegate.SELECT_singleSelect_VALIDATION);
        } else {
            columnSeg.accept(this);
        }
        Curd tableSeg = singleSelect.getTableSeg();
        if (tableSeg == null) {
            error(AstROErrorReporterDelegate.SELECT_singleSelect_VALIDATION);
        } else {
            tableSeg.accept(this);
        }
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
        Curd limitSeg = singleSelect.getLimitSeg();
        if (limitSeg != null) {
            limitSeg.accept(this);
        }
        Curd lockSeg = singleSelect.getLockSeg();
        if (lockSeg != null) {
            lockSeg.accept(this);
        }

        return null;
    }

    @Override
    public Void visitColumnSeg(ColumnSeg columnSeg) {
        List<Curd> columnRepList = columnSeg.getColumnRepList();
        for (Curd columnRep : columnRepList) {
            columnRep.accept(this);
        }
        return null;
    }

    @Override
    public Void visitColumnRep(ColumnRep columnRep) {
        Token alias = columnRep.getOperator();
        Curd curd = columnRep.getCurd();
        if (alias != null && curd instanceof Identifier) {
            // * 不能存在别名
            Token token = ((Identifier) curd).getName();
            if (TokenType.STAR.equals(token.getType())) {
                error(AstROErrorReporterDelegate.SELECT_columnRep_VALIDATION);
                return null;
            }
        }

        // 当值不是Literal或Identifier时，必须存在别名
        if (!(curd instanceof Literal || curd instanceof Identifier) && alias == null) {
            error(AstROErrorReporterDelegate.SELECT_columnRep_VALIDATION);
        }
        curd.accept(this);
        return null;
    }

    @Override
    public Void visitCaseWhen(CaseWhen caseWhen) {
        Curd value = caseWhen.getValue();
        if (value == null) {
            error(AstROErrorReporterDelegate.SELECT_caseWhen_VALIDATION);
        } else {
            value.accept(this);
        }
        List<Curd> conditionList = caseWhen.getConditionList();
        List<Curd> resultList = caseWhen.getResultList();
        if (conditionList.size() != resultList.size()) {
            error(AstROErrorReporterDelegate.SELECT_caseWhen_VALIDATION);
        } else {
            for (Curd curd : conditionList) {
                curd.accept(this);
            }
            for (Curd curd : resultList) {
                curd.accept(this);
            }
        }
        Curd defaultValue = caseWhen.getDefaultValue();
        if (defaultValue == null) {
            error(AstROErrorReporterDelegate.SELECT_caseWhen_VALIDATION);
        } else {
            defaultValue.accept(this);
        }
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
    public Void visitTableSeg(TableSeg tableSeg) {

        Curd leftCurd = tableSeg.getLeftCurd();
        if (leftCurd == null) {
            error(AstROErrorReporterDelegate.SELECT_tableSeg_VALIDATION);
        } else {
            leftCurd.accept(this);
        }

        Token qualifier = tableSeg.getQualifier();
        Token token = tableSeg.getToken();
        Curd condition = tableSeg.getCondition();
        Curd rightCurd = tableSeg.getRightCurd();
        //  , 做连接条件时，不能被修饰，不能加连接条件
        if ((condition != null || qualifier != null) && TokenType.COMMA.equals(token.getType())) {
            error(AstROErrorReporterDelegate.SELECT_tableSeg_VALIDATION);
        }

        // cross join 不能加连接条件
        if (condition != null && qualifier != null && TokenType.CROSS.equals(qualifier.getType())) {
            error(AstROErrorReporterDelegate.SELECT_tableSeg_VALIDATION);
        }

        // 不存在右表时不能存在连接条件
        if (rightCurd == null && condition != null) {
            error(AstROErrorReporterDelegate.SELECT_tableSeg_VALIDATION);
        }

        if (rightCurd != null) {
            rightCurd.accept(this);
        }

        if (condition != null) {
            // 连接条件需要是如下格式：A.a1 = B.b1 and A.a2 > B.b2 or B.b3 > C.c1
            if (!LogicGrammarValidationDelegate.checkComparisonResult(condition)) {
                error(AstROErrorReporterDelegate.SELECT_tableSeg_VALIDATION);
            } else {
                condition.accept(this);
            }
        }

        if (condition == null && qualifier != null &&
            (TokenType.LEFT.equals(qualifier.getType()) || TokenType.RIGHT.equals(qualifier.getType()))) {
            error(AstROErrorReporterDelegate.SELECT_tableSeg_VALIDATION);
        }

        return null;
    }


    @Override
    public Void visitTableRep(TableRep tableRep) {
        Curd curd = tableRep.getCurd();

        curd.accept(this);

        return null;
    }

    @Override
    public Void visitGroupBySeg(GroupBySeg groupBySeg) {
        Curd having = groupBySeg.getHaving();
        if (having != null) {
            if (!LogicGrammarValidationDelegate.checkBooleanResult(having)) {
                error(AstROErrorReporterDelegate.SELECT_groupBySeg_VALIDATION);
            } else {
                having.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitOrderBySeg(OrderBySeg orderBySeg) {
        List<Curd> columnList = orderBySeg.getColumnList();
        if (columnList.isEmpty()) {
            error(AstROErrorReporterDelegate.SELECT_orderBySeg_VALIDATION);
        }
        return null;
    }

    @Override
    public Void visitOrderItem(OrderItem orderItem) {
        return null;
    }


    @Override
    public Void visitInCondition(InCondition inCondition) {

        Curd curd = inCondition.getSelect();
        List<Curd> range = inCondition.getRange();

        if (curd == null && range == null) {
            error(AstROErrorReporterDelegate.SELECT_inCondition_VALIDATION);
        }
        if (curd != null && range != null) {
            error(AstROErrorReporterDelegate.SELECT_inCondition_VALIDATION);
        }
        if (curd != null) {
            curd.accept(this);
        }
        if (range != null) {
            for (Curd item : range) {
                if (!(item instanceof Literal)) {
                    error(AstROErrorReporterDelegate.SELECT_inCondition_VALIDATION);
                }
            }
        }

        return null;
    }

    @Override
    public Void visitExistsCondition(ExistsCondition existsCondition) {
        Curd curd = existsCondition.getCurd();
        if (curd != null) {
            curd.accept(this);
        } else {
            error(AstROErrorReporterDelegate.SELECT_existsCondition_VALIDATION);
        }
        return null;
    }

    @Override
    public Void visitLimitSeg(LimitSeg limitSeg) {
        Token num = limitSeg.getNum();
        if (num == null) {
            error(AstROErrorReporterDelegate.SELECT_limitSeg_VALIDATION);
            return null;
        }
        if (!(TokenType.INTEGER.equals(num.getType()) || TokenType.PARAMETER.equals(num.getType()) || TokenType.HASH_MARK_PLACEHOLDER.equals(num.getType()))) {
            error(AstROErrorReporterDelegate.SELECT_limitSeg_VALIDATION);
            return null;
        }
        Token offset = limitSeg.getOffset();
        if (offset != null
            && !(TokenType.INTEGER.equals(offset.getType()) || TokenType.PARAMETER.equals(offset.getType()) || TokenType.HASH_MARK_PLACEHOLDER.equals(offset.getType()))) {
            error(AstROErrorReporterDelegate.SELECT_limitSeg_VALIDATION);
        }
        return null;
    }

    @Override
    public Void visitGroupFunction(GroupFunction groupFunction) {
        Curd curd = groupFunction.getCurd();
        if (curd == null) {
            error(AstROErrorReporterDelegate.SELECT_groupFunction_VALIDATION);
        } else {
            curd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitGroupConcat(GroupConcat groupConcat) {
        final List<Curd> resultList = groupConcat.getResultList();
        if (resultList == null) {
            error(AstROErrorReporterDelegate.SELECT_groupConcat_VALIDATION);
        }
        return null;
    }

}
