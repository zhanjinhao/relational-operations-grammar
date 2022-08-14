package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.error.reporter.DumbROErrorReporterDelegate;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.AstMetaDataHelper;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.expression.visitor.ExpressionAstMetaDataDetector;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/1/5 20:17
 */
public class SelectAstMetaDataDetector extends SelectVisitorWithDelegate<AstMetaData> {

    public SelectAstMetaDataDetector() {
        super.init(new ExpressionAstMetaDataDetector(this));
        setErrorReporter(DumbROErrorReporterDelegate.getInstance());
    }

    @Override
    public AstMetaData visitSelect(Select select) {
        SelectAstMetaData astMetaDataCur = (SelectAstMetaData) select.getAstMetaData();
        Curd leftCurd = select.getLeftCurd();
        if (leftCurd instanceof Select) {
            AstMetaData accept = leftCurd.accept(this);
            accept.setParent(astMetaDataCur);
            astMetaDataCur.getSubSegments().add(accept);
        } else if (leftCurd instanceof SingleSelect) {
            AstMetaData accept = leftCurd.accept(this);
            astMetaDataCur.getSubSegments().add(accept);
        }

        Curd rightCurd = select.getRightCurd();
        // rightCurd如果不为空，就是SingleSelect
        if (rightCurd != null) {
            astMetaDataCur.getSubSegments().add(rightCurd.accept(this));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitSingleSelect(SingleSelect singleSelect) {
        SingleSelectAstMetaData astMetaDataCur = (SingleSelectAstMetaData) singleSelect.getAstMetaData();

        Curd columnSeg = singleSelect.getColumnSeg();
        SingleSelectAstMetaData columnSegAmd = (SingleSelectAstMetaData) columnSeg.accept(this);
        AstMetaDataHelper.mergeColumnReference(columnSegAmd.getConditionColumnReference(), astMetaDataCur.getResultColumnReference());
        astMetaDataCur.getResultColumnList().addAll(columnSegAmd.getResultColumnList());

        TableSeg tableSeg = (TableSeg) singleSelect.getTableSeg();
        SingleSelectAstMetaData tableSegAmd = (SingleSelectAstMetaData) tableSeg.accept(this);
        astMetaDataCur.getAliasTableMap().putAll(tableSegAmd.getAliasTableMap());
        astMetaDataCur.mergeColumnReference(tableSegAmd);

        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            astMetaDataCur.mergeColumnReference(whereSeg.accept(this));
        }

        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            AstMetaData accept = groupBySeg.accept(this);
            AstMetaDataHelper.mergeColumnReference(accept.getConditionColumnReference(), astMetaDataCur.getGroupByColumnReference());
            astMetaDataCur.createTable(accept.getConditionColumnReference());
        }

        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            AstMetaData accept = orderBySeg.accept(this);
            AstMetaDataHelper.mergeColumnReference(accept.getConditionColumnReference(), astMetaDataCur.getOrderByColumnReference());
            astMetaDataCur.createTable(accept.getConditionColumnReference());
        }

        // limit 不存在字段

        astMetaDataCur.sortMetaData();

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitColumnSeg(ColumnSeg columnSeg) {
        SingleSelectAstMetaData astMetaDataCur = (SingleSelectAstMetaData) columnSeg.getAstMetaData();

        List<Curd> columnRepList = columnSeg.getColumnRepList();
        for (Curd curd : columnRepList) {
            SingleSelectAstMetaData accept = (SingleSelectAstMetaData) curd.accept(this);
            astMetaDataCur.mergeColumnReference(accept);
            astMetaDataCur.getResultColumnList().addAll(accept.getResultColumnList());
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitColumnRep(ColumnRep columnRep) {
        SingleSelectAstMetaData astMetaDataCur = (SingleSelectAstMetaData) columnRep.getAstMetaData();
        Token alias = columnRep.getOperator();
        Curd curd = columnRep.getCurd();

        // 当没有别名的时候，curd 只会是 Literal 或者 Identifier
        // 此时需要将 curd 加入到 resultColumnList
        if (alias == null) {
            if (curd instanceof Literal) {
                Literal literal = (Literal) curd;
                Token value = literal.getValue();
                astMetaDataCur.getResultColumnList().add(value);
            } else if (curd instanceof Identifier) {
                Identifier identifier = (Identifier) curd;
                Token value = identifier.getName();
                astMetaDataCur.getResultColumnList().add(value);
            } else {
                // error
            }
        }
        // 当存在别名的时候，需要将 别名 加入到 resultColumnList
        else {
            astMetaDataCur.getResultColumnList().add(alias);
        }
        AstMetaData accept = curd.accept(this);
        astMetaDataCur.mergeColumnReference(accept);

        return astMetaDataCur;
    }


    @Override
    public AstMetaData visitCaseWhen(CaseWhen caseWhen) {
        AstMetaData astMetaDataCur = caseWhen.getAstMetaData();

        Curd value = caseWhen.getValue();
        astMetaDataCur.mergeColumnReference(value.accept(this));

        List<Curd> conditionList = caseWhen.getConditionList();
        for (Curd curd : conditionList) {
            astMetaDataCur.mergeColumnReference(curd.accept(this));
        }

        List<Curd> resultList = caseWhen.getResultList();
        for (Curd curd : resultList) {
            astMetaDataCur.mergeColumnReference(curd.accept(this));
        }

        Curd defaultValue = caseWhen.getDefaultValue();
        astMetaDataCur.mergeColumnReference(defaultValue.accept(this));

        return astMetaDataCur;
    }


    @Override
    public AstMetaData visitTableSeg(TableSeg tableSeg) {
        SingleSelectAstMetaData astMetaDataCur = (SingleSelectAstMetaData) tableSeg.getAstMetaData();

        // resultColumnReference 存table信息
        SingleSelectAstMetaData leftAmd = (SingleSelectAstMetaData) tableSeg.getLeftCurd().accept(this);
        astMetaDataCur.createTable(leftAmd.getResultColumnReference());
        astMetaDataCur.mergeColumnReference(leftAmd);

        astMetaDataCur.getAliasTableMap().putAll(leftAmd.getAliasTableMap());

        Curd rightCurd = tableSeg.getRightCurd();
        if (rightCurd != null) {
            SingleSelectAstMetaData rightAmd = (SingleSelectAstMetaData) rightCurd.accept(this);
            astMetaDataCur.createTable(rightAmd.getResultColumnReference());
            astMetaDataCur.mergeColumnReference(rightAmd);

            astMetaDataCur.getAliasTableMap().putAll(rightAmd.getAliasTableMap());
        }

        // conditionColumnReference 存on logic信息
        Curd condition = tableSeg.getCondition();
        if (condition != null) {
            AstMetaData accept = condition.accept(this);
            AstMetaDataHelper.mergeColumnReference(accept.getConditionColumnReference(), astMetaDataCur.getJoinColumnReference());
            astMetaDataCur.createTable(accept.getConditionColumnReference());
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitTableRep(TableRep tableRep) {
        SingleSelectAstMetaData astMetaDataCur = (SingleSelectAstMetaData) tableRep.getAstMetaData();

        Curd curd = tableRep.getCurd();
        Token alias = tableRep.getAlias();

        // 有别名的场景下，表名就是别名。如果是 Select，设置父子引用。如果不是，合并即可
        if (alias != null) {
            String tableName = String.valueOf(alias.getLiteral());
            astMetaDataCur.createTable(tableName);
            AstMetaData accept = curd.accept(this);
            // 一旦有了别名，原始的表就不能再使用了，原始的表要存起来
            astMetaDataCur.getAliasTableMap().put(tableName, curd);
            if (curd instanceof Select) {
                accept.setParent(astMetaDataCur);
                astMetaDataCur.addChild(accept);
            } else {
                // 有别名之后，不在需要真实的表名了，所以这里不需要mergeColumnReference
            }
        }
        // 没有别名的场景下，curd只会是Identifier。此时需要在引用中新建一个表即可
        else {
            Identifier identifier = (Identifier) curd;
            String tableName = String.valueOf(identifier.getName().getLiteral());
            astMetaDataCur.createTable(tableName);
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitInCondition(InCondition inCondition) {

        AstMetaData astMetaDataCur = inCondition.getAstMetaData();

        // 条件字段需要加入
        Token identifier = inCondition.getIdentifier();
        astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(identifier.getLiteral()));

        Curd curd = inCondition.getSelect();
        // select 模式
        if (curd != null) {
            AstMetaData accept = curd.accept(this);
            accept.setParent(astMetaDataCur);
            astMetaDataCur.addChild(accept);
            return astMetaDataCur;
        }

        // range 模式下，range里的不会包含Identifier

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitExistsCondition(ExistsCondition existsCondition) {
        AstMetaData astMetaData = existsCondition.getAstMetaData();

        Curd curd = existsCondition.getCurd();
        AstMetaData accept = curd.accept(this);

        accept.setParent(astMetaData);
        astMetaData.addChild(accept);
        return astMetaData;
    }

    @Override
    public AstMetaData visitGroupBySeg(GroupBySeg groupBySeg) {
        AstMetaData astMetaData = groupBySeg.getAstMetaData();

        List<Token> columnList = groupBySeg.getColumnList();
        for (Token token : columnList) {
            astMetaData.putUndeterminedConditionColumn(String.valueOf(token.getLiteral()));
        }

        Curd having = groupBySeg.getHaving();
        if (having != null) {
            astMetaData.mergeColumnReference(having.accept(this));
        }

        return astMetaData;
    }

    @Override
    public AstMetaData visitOrderBySeg(OrderBySeg orderBySeg) {
        AstMetaData astMetaDataCur = orderBySeg.getAstMetaData();

        List<OrderBySeg.OrderItem> columnList = orderBySeg.getColumnList();
        for (OrderBySeg.OrderItem item : columnList) {
            Token column = item.getColumn();
            astMetaDataCur.putUndeterminedConditionColumn(String.valueOf(column.getLiteral()));
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitLimitSeg(LimitSeg limitSeg) {
        return limitSeg.getAstMetaData();
    }

    @Override
    public AstMetaData visitGroupFunction(GroupFunction groupFunction) {
        AstMetaData astMetaDataCur = groupFunction.getAstMetaData();
        Curd curd = groupFunction.getCurd();
        astMetaDataCur.mergeColumnReference(curd.accept(this));
        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitWhereSeg(WhereSeg whereSeg) {
        Curd curd = whereSeg.getLogic();
        if (curd instanceof Select) {
            AstMetaData astMetaDataCur = whereSeg.getAstMetaData();
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.addChild(accept);
            accept.setParent(astMetaDataCur);
            return astMetaDataCur;
        } else {
            return delegate.visitWhereSeg(whereSeg);
        }
    }

    @Override
    public AstMetaData visitLogic(Logic logic) {
        return visitBinary(logic);
    }

    @Override
    public AstMetaData visitComparison(Comparison comparison) {
        return visitBinary(comparison);
    }

    @Override
    public AstMetaData visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return visitBinary(binaryArithmetic);
    }

    @Override
    public AstMetaData visitBinary(Binary binary) {
        AstMetaData astMetaDataCur = binary.getAstMetaData();

        Curd leftCurd = binary.getLeftCurd();
        AstMetaData leftAccept = leftCurd.accept(this);
        if (leftCurd instanceof Select) {
            astMetaDataCur.addChild(leftAccept);
            leftAccept.setParent(astMetaDataCur);
        } else {
            astMetaDataCur.mergeColumnReference(leftAccept);
        }

        Curd rightCurd = binary.getRightCurd();
        if (rightCurd != null) {
            AstMetaData rightAccept = rightCurd.accept(this);
            if (rightCurd instanceof Select) {
                astMetaDataCur.addChild(rightAccept);
                rightAccept.setParent(astMetaDataCur);
            } else {
                astMetaDataCur.mergeColumnReference(rightAccept);
            }
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        Curd curd = unaryArithmetic.getCurd();
        if (curd instanceof Select) {
            AstMetaData astMetaDataCur = unaryArithmetic.getAstMetaData();
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.addChild(accept);
            return astMetaDataCur;
        } else {
            return delegate.visitUnaryArithmetic(unaryArithmetic);
        }
    }

}
