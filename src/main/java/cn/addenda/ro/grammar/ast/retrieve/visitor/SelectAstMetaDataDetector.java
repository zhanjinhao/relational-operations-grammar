package cn.addenda.ro.grammar.ast.retrieve.visitor;

import cn.addenda.ro.error.reporter.ROErrorReporter;
import cn.addenda.ro.grammar.ast.AstMetaData;
import cn.addenda.ro.grammar.ast.expression.Curd;
import cn.addenda.ro.grammar.ast.expression.Identifier;
import cn.addenda.ro.grammar.ast.expression.Literal;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.lexical.token.Token;

import java.util.List;

/**
 * @author addenda
 * @datetime 2022/1/5 20:17
 */
public class SelectAstMetaDataDetector extends SelectVisitorWithDelegate<AstMetaData> {

    public SelectAstMetaDataDetector(ROErrorReporter roErrorReporter) {
        super.init(new ExpressionAstMetaDataDetectorWrapperForRetrieve(this, roErrorReporter));
        setErrorReporter(roErrorReporter);
    }

    @Override
    public AstMetaData visitSelect(Select select) {
        AstMetaData astMetaDataCur = select.getAstMetaData();
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
        AstMetaData astMetaDataCur = singleSelect.getAstMetaData();

        Curd columnSeg = singleSelect.getColumnSeg();
        AstMetaData columnSegAmd = columnSeg.accept(this);
        astMetaDataCur.mergeResultColumnReference(columnSegAmd.getConditionColumnReference());
        astMetaDataCur.getResultColumnList().addAll(columnSegAmd.getResultColumnList());

        TableSeg tableSeg = (TableSeg) singleSelect.getTableSeg();
        AstMetaData tableSegAmd = tableSeg.accept(this);
        astMetaDataCur.getAliasTableMap().putAll(tableSegAmd.getAliasTableMap());
//        astMetaDataCur.mergeJoinColumnReference(tableSegAmd.getConditionColumnReference());
//        astMetaDataCur.createTable(tableSegAmd.getResultColumnReference());
        astMetaDataCur.mergeColumnReference(tableSegAmd);

        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            astMetaDataCur.mergeColumnReference(whereSeg.accept(this));
        }

        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            AstMetaData accept = groupBySeg.accept(this);
            astMetaDataCur.mergeGroupByColumnReference(accept.getConditionColumnReference());
            astMetaDataCur.createTable(accept.getConditionColumnReference());
        }

        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            AstMetaData accept = orderBySeg.accept(this);
            astMetaDataCur.mergeOrderByColumnReference(accept.getConditionColumnReference());
            astMetaDataCur.createTable(accept.getConditionColumnReference());
        }

        // limit 不存在字段

        astMetaDataCur.sortMetaData();

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitColumnSeg(ColumnSeg columnSeg) {
        AstMetaData astMetaDataCur = columnSeg.getAstMetaData();

        List<Curd> columnRepList = columnSeg.getColumnRepList();
        for (Curd curd : columnRepList) {
            AstMetaData accept = curd.accept(this);
            astMetaDataCur.mergeColumnReference(accept);
            astMetaDataCur.getResultColumnList().addAll(accept.getResultColumnList());
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitColumnRep(ColumnRep columnRep) {
        AstMetaData astMetaDataCur = columnRep.getAstMetaData();
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
        AstMetaData astMetaDataCur = tableSeg.getAstMetaData();

        // resultColumnReference 存table信息
        AstMetaData leftAmd = tableSeg.getLeftCurd().accept(this);
        astMetaDataCur.mergeResultColumnReference(leftAmd.getResultColumnReference());
        astMetaDataCur.createTable(leftAmd.getResultColumnReference());
        astMetaDataCur.mergeColumnReference(leftAmd);

        astMetaDataCur.getAliasTableMap().putAll(leftAmd.getAliasTableMap());

        Curd rightCurd = tableSeg.getRightCurd();
        if (rightCurd != null) {
            AstMetaData rightAmd = rightCurd.accept(this);
            astMetaDataCur.mergeResultColumnReference(rightAmd.getResultColumnReference());
            astMetaDataCur.createTable(rightAmd.getResultColumnReference());
            astMetaDataCur.mergeColumnReference(rightAmd);

            astMetaDataCur.getAliasTableMap().putAll(rightAmd.getAliasTableMap());
        }

        // conditionColumnReference 存on logic信息
        Curd condition = tableSeg.getCondition();
        if (condition != null) {
            AstMetaData accept = condition.accept(this);
            astMetaDataCur.mergeJoinColumnReference(accept.getConditionColumnReference());
            astMetaDataCur.createTable(accept.getConditionColumnReference());
        }

        return astMetaDataCur;
    }

    @Override
    public AstMetaData visitTableRep(TableRep tableRep) {
        AstMetaData astMetaDataCur = tableRep.getAstMetaData();

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
                // 此时 accept 中有用的是 conditionColumnReference
                // 有别名之后，不在需要真实的表名了
                // astMetaDataCur.mergeColumnReference(accept);
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

        Curd curd = inCondition.getCurd();
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

}
