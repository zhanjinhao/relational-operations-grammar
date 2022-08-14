package cn.addenda.ro.grammar.ast;

import cn.addenda.ro.grammar.ast.create.*;
import cn.addenda.ro.grammar.ast.delete.Delete;
import cn.addenda.ro.grammar.ast.expression.*;
import cn.addenda.ro.grammar.ast.retrieve.*;
import cn.addenda.ro.grammar.ast.update.Update;
import cn.addenda.ro.grammar.lexical.token.Token;
import cn.addenda.ro.grammar.lexical.token.TokenType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author addenda
 * @datetime 2021/3/6 15:31
 */
public class CurdPrinter implements CurdVisitor<String> {

    private int deepth = -1;

    private static final String BLANK = " ";

    @Override
    public String visitSelect(Select select) {
        StringBuilder sb = new StringBuilder();

        Curd left = select.getLeftCurd();
        sb.append(left.accept(this)).append("\n");

        Token token = select.getToken();
        if (token != null) {
            sb.append(token.getLiteral()).append(BLANK);
            Token allToken = select.getAllToken();
            if (allToken != null) {
                sb.append(allToken.getLiteral());
            }
            sb.append("\n");
        }

        Curd right = select.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visitSingleSelect(SingleSelect singleSelect) {
        StringBuilder sb = new StringBuilder();
        SingleSelectType singleSelectType = singleSelect.getSingleSelectType();

        boolean notNeedParen = SingleSelectType.TOP.equals(singleSelectType) || SingleSelectType.INSERT.equals(singleSelectType);
        if (!notNeedParen) {
            sb.append(BLANK).append("(").append(BLANK);
        }

        deepth++;

        sb.append(blankStr()).append("select\t").append(singleSelect.getColumnSeg().accept(this)).append("\n");

        Curd tableSeg = singleSelect.getTableSeg();
        if (tableSeg != null) {
            sb.append(blankStr()).append("from\t").append(tableSeg.accept(this)).append("\n");
        }

        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            sb.append(blankStr()).append(whereSeg.accept(this)).append("\n");
        }

        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            sb.append(blankStr()).append(groupBySeg.accept(this)).append("\n");
        }

        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            sb.append(blankStr()).append(orderBySeg.accept(this)).append("\n");
        }

        Curd limitSeg = singleSelect.getLimitSeg();
        if (limitSeg != null) {
            sb.append(blankStr()).append(limitSeg.accept(this)).append("\n");
        }
        deepth--;
        if (!notNeedParen) {
            sb.append(BLANK).append(")").append(BLANK);
        }
        return sb.toString();
    }

    @Override
    public String visitColumnSeg(ColumnSeg columnSeg) {

        StringBuilder sb = new StringBuilder();
        Token restriction = columnSeg.getRestriction();
        if (restriction != null) {
            sb.append(restriction.getLiteral()).append(BLANK);
        }

        List<Curd> columnRepList = columnSeg.getColumnRepList();
        sb.append(columnRepList.stream().map(item -> item.accept(this))
                .collect(Collectors.joining(", ")));
        return sb.toString();
    }

    @Override
    public String visitColumnRep(ColumnRep columnRep) {
        StringBuilder sb = new StringBuilder();
        Curd curd = columnRep.getCurd();
        sb.append(curd.accept(this));

        Token operator = columnRep.getOperator();
        if (operator != null) {
            sb.append(BLANK).append("as").append(BLANK).append(operator.getLiteral());
        }
        return sb.toString();
    }

    @Override
    public String visitTableSeg(TableSeg tableSeg) {

        StringBuilder sb = new StringBuilder();

        Curd left = tableSeg.getLeftCurd();
        sb.append(left.accept(this)).append(BLANK);

        Token qualifier = tableSeg.getQualifier();
        if (qualifier != null) {
            sb.append(qualifier.getLiteral()).append(BLANK);
        }

        Token join = tableSeg.getToken();
        if (join != null) {
            sb.append(join.getLiteral()).append(BLANK);
        }

        Curd right = tableSeg.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append(BLANK);
        }

        Curd condition = tableSeg.getCondition();
        if (condition != null) {
            sb.append("on").append(BLANK).append(condition.accept(this)).append(BLANK);
        }

        return sb.toString();
    }

    @Override
    public String visitTableRep(TableRep tableRep) {
        StringBuilder sb = new StringBuilder();
        Curd curd = tableRep.getCurd();
        Token alias = tableRep.getAlias();
        String accept = curd.accept(this);
        if (curd instanceof Select) {
            sb.append(accept).append(BLANK);
            if (alias != null) {
                sb.append(alias.getLiteral());
            }
        } else if (alias == null && curd instanceof Identifier) {
            sb.append(accept);
        } else if (alias != null && curd instanceof Identifier) {
            sb.append(accept).append(BLANK).append(alias.getLiteral());
        } else {
            sb.setLength(0);
        }

        return sb.toString();
    }

    @Override
    public String visitWhereSeg(WhereSeg whereSeg) {
        return "where" + BLANK + whereSeg.getLogic().accept(this);
    }

    @Override
    public String visitInCondition(InCondition inCondition) {
        StringBuilder sb = new StringBuilder();
        Token identifier = inCondition.getIdentifier();
        sb.append(identifier.getLiteral());

        Token in = inCondition.getIn();
        if (in.getType().equals(TokenType.IN)) {
            sb.append(BLANK).append("in").append(BLANK);
        } else {
            sb.append(BLANK).append("not").append(BLANK).append("in").append(BLANK);
        }
        Curd curd = inCondition.getSelect();
        if (curd != null) {
            sb.append(curd.accept(this));
        } else {
            sb.append("(").append(BLANK);
            List<Curd> range = inCondition.getRange();
            if (range != null && !range.isEmpty()) {
                sb.append(range.stream()
                        .map(item -> item.accept(this))
                        .collect(Collectors.joining("," + BLANK)));
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String visitExistsCondition(ExistsCondition existsCondition) {
        StringBuilder sb = new StringBuilder();
        Token operator = existsCondition.getOperator();
        if (operator.getType().equals(TokenType.EXISTS)) {
            sb.append(BLANK).append("exists").append(BLANK);
        } else {
            sb.append(BLANK).append("not").append(BLANK).append("exists").append(BLANK);
        }
        Curd curd = existsCondition.getCurd();
        sb.append(curd.accept(this));
        return sb.toString();
    }

    @Override
    public String visitGroupBySeg(GroupBySeg groupBySeg) {
        StringBuilder sb = new StringBuilder();
        sb.append("group by\t");
        List<Token> columnList = groupBySeg.getColumnList();
        if (columnList != null && !columnList.isEmpty()) {
            sb.append(columnList.stream()
                    .map(item -> item.getLiteral().toString())
                    .collect(Collectors.joining(", ")));
        }
        Curd having = groupBySeg.getHaving();
        if (having != null) {
            sb.append(BLANK).append("having").append(BLANK).append(having.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitOrderBySeg(OrderBySeg orderBySeg) {
        StringBuilder sb = new StringBuilder();
        sb.append("order by\t");
        List<OrderBySeg.OrderItem> columnList = orderBySeg.getColumnList();
        if (columnList != null && !columnList.isEmpty()) {
            OrderBySeg.OrderItem orderItem = columnList.get(0);
            Token column = orderItem.getColumn();
            Token orderType = orderItem.getOrderType();
            sb.append(column.getLiteral()).append(BLANK).append(orderType == null ? "" : orderType.getLiteral());
            for (int i = 1; i < columnList.size(); i++) {
                orderItem = columnList.get(i);
                column = orderItem.getColumn();
                orderType = orderItem.getOrderType();
                sb.append(",").append(BLANK).append(column.getLiteral()).append(BLANK).append(orderType == null ? "" : orderType.getLiteral());
            }
        }
        return sb.toString();
    }

    @Override
    public String visitLimitSeg(LimitSeg limitSeg) {
        String s = "limit\t" + limitSeg.getNum().getLiteral();
        Token offset = limitSeg.getOffset();
        if (offset == null) {
            return s;
        }

        return s + "," + BLANK + limitSeg.getOffset().getLiteral();
    }

    @Override
    public String visitLogic(Logic logic) {
        return visitBinary(logic, BLANK).toString();
    }

    @Override
    public String visitComparison(Comparison comparison) {
        StringBuilder sb = new StringBuilder();

        Curd left = comparison.getLeftCurd();
        sb.append(left.accept(this)).append(BLANK);

        Token token = comparison.getToken();
        if (token != null) {
            sb.append(token.getLiteral()).append(BLANK);
        }

        sb.append(comparison.getComparisonSymbol().accept(this));

        Curd right = comparison.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append(BLANK);
        }
        return sb.toString();
    }

    @Override
    public String visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return visitBinary(binaryArithmetic, BLANK).toString();
    }

    @Override
    public String visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        return unaryArithmetic.getOperator().getLiteral() +
                unaryArithmetic.getCurd().accept(this);
    }

    @Override
    public String visitLiteral(Literal literal) {
        if (literal.getValue() == null) {
            return null;
        }
        Token token = literal.getValue();

        Object value = token.getLiteral();

        if (TokenType.STRING.equals(token.getType())) {
            return "'" + value + "'";
        }

        return value.toString();
    }

    @Override
    public String visitGroupFunction(GroupFunction groupFunction) {
        return groupFunction.getMethod().getLiteral() +
                "(" + BLANK +
                groupFunction.getCurd().accept(this) +
                BLANK + ")";
    }

    @Override
    public String visitCaseWhen(CaseWhen caseWhen) {
        StringBuilder sb = new StringBuilder();
        sb.append(BLANK).append("case").append(BLANK).append(caseWhen.getValue().accept(this));
        List<Curd> conditionList = caseWhen.getConditionList();
        List<Curd> resultList = caseWhen.getResultList();

        int size = conditionList.size();
        for (int i = 0; i < size; i++) {
            Curd condition = conditionList.get(i);
            Curd result = resultList.get(i);
            sb.append(BLANK).append("when").append(BLANK).append(condition.accept(this))
                    .append(BLANK).append("then").append(BLANK).append(result.accept(this));
        }

        Curd defaultValue = caseWhen.getDefaultValue();
        if (defaultValue != null) {
            sb.append(BLANK).append("else").append(BLANK).append(defaultValue.accept(this));
        }
        sb.append(BLANK).append("end");

        return sb.toString();
    }

    @Override
    public String visitGrouping(Grouping grouping) {
        return BLANK + "(" + BLANK + grouping.getCurd().accept(this) + BLANK + ")" + BLANK;
    }

    @Override
    public String visitIdentifier(Identifier identifier) {
        return identifier.getName().getLiteral().toString() + BLANK;
    }

    @Override
    public String visitFunction(Function function) {
        StringBuilder sb = new StringBuilder();
        Token method = function.getMethod();
        List<Curd> parameterList = function.getParameterList();
        sb.append(method.getLiteral());
        if (parameterList == null || parameterList.isEmpty()) {
            sb.append("()");
            return sb.toString();
        }
        sb.append(parameterList.stream().map(item -> item.accept(this)).collect(Collectors.joining(", ", "( ", " ) ")));
        return sb.toString();
    }

    @Override
    public String visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        StringBuilder sb = new StringBuilder();
        List<Token> columnList = insertValuesRep.getColumnList();
        if (columnList != null && !columnList.isEmpty()) {
            String collect = columnList.stream().map(item -> (String) item.getLiteral()).collect(Collectors.joining(", ", " ( ", " ) "));
            sb.append(collect);
        }

        sb.append("values");

        List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        String collect = curdListList.stream()
                .map(i -> i.stream().map(j -> j.accept(this)).collect(Collectors.joining(", ", " ( ", " ) ")))
                .collect(Collectors.joining(", "));

        sb.append(collect);

        return sb.toString();
    }

    @Override
    public String visitInsertSetRep(InsertSetRep insertSetRep) {
        StringBuilder sb = new StringBuilder();

        sb.append(BLANK).append("set").append(BLANK);

        Curd entryList = insertSetRep.getAssignmentList();
        String accept = entryList.accept(this);
        sb.append(accept);

        return sb.toString();
    }

    @Override
    public String visitInsert(Insert insert) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert").append(BLANK);
        Token constrict = insert.getConstrict();
        if (constrict != null) {
            sb.append(constrict.getLiteral());
        }
        sb.append(BLANK).append("into").append(BLANK);

        Token tableName = insert.getTableName();
        sb.append(tableName.getLiteral());

        sb.append(insert.getInsertRep().accept(this));

        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();
        if (onDuplicateUpdate != null) {
            sb.append(onDuplicateUpdate.accept(this));
        }

        return sb.toString();
    }

    @Override
    public String visitAssignmentList(AssignmentList assignmentList) {
        return assignmentList.getEntryList().stream()
                .map(item -> item.getColumn().getLiteral() + "=" + item.getValue().accept(this))
                .collect(Collectors.joining(", "));
    }

    @Override
    public String visitTimeInterval(TimeInterval timeInterval) {
        return BLANK + "interval" + BLANK + timeInterval.getInterval() + BLANK + timeInterval.getTimeType().getLiteral();
    }

    @Override
    public String visitTimeUnit(TimeUnit timeUnit) {
        return BLANK + timeUnit.getTimeType().getLiteral() + BLANK + "from" + BLANK + timeUnit.getCurd().accept(this);
    }

    @Override
    public String visitIsNot(IsNot isNot) {
        Token isToken = isNot.getIsToken();
        StringBuilder sb = new StringBuilder();
        sb.append(isToken.getLiteral()).append(BLANK);
        Token notToken = isNot.getNotToken();
        if (notToken != null) {
            sb.append(notToken.getLiteral()).append(BLANK);
        }
        return sb.toString();
    }

    @Override
    public String visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        return BLANK + "on" + BLANK + "duplicate" + BLANK + "key" + BLANK + "update" + BLANK + onDuplicateKey.getAssignmentList().accept(this);
    }

    @Override
    public String visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        List<Token> columnList = insertSelectRep.getColumnList();
        StringBuilder sb = new StringBuilder();
        if (columnList != null && !columnList.isEmpty()) {
            String collect = columnList.stream().map(item -> (String) item.getLiteral()).collect(Collectors.joining(", ", " ( ", " ) "));
            sb.append(collect);
        }

        Curd select = insertSelectRep.getSelect();
        String accept = select.accept(this);

        sb.append(BLANK).append(accept);

        return sb.toString();
    }


    @Override
    public String visitUpdate(Update update) {
        String str = "update" + BLANK +
                update.getTableName().getLiteral() +
                BLANK + "set" + BLANK + update.getAssignmentList().accept(this);
        Curd whereSeg = update.getWhereSeg();
        if (whereSeg == null) {
            return str;
        }
        return str + BLANK + whereSeg.accept(this);
    }

    @Override
    public String visitDelete(Delete delete) {
        Token tableName = delete.getTableName();
        Curd whereSeg = delete.getWhereSeg();
        String str = "delete" + BLANK + "from" + BLANK + tableName.getLiteral();
        if (whereSeg != null) {
            return str + BLANK + whereSeg.accept(this);
        }
        return str;
    }

    private String blankStr() {
        StringBuilder blackStr = new StringBuilder();
        for (int i = 0; i < deepth; i++) {
            blackStr.append("\t");
        }
        return blackStr.toString();
    }

    private StringBuilder visitBinary(Binary binary, String separator) {
        StringBuilder sb = new StringBuilder();

        Curd left = binary.getLeftCurd();
        sb.append(left.accept(this)).append(separator);

        Token token = binary.getToken();
        if (token != null) {
            sb.append(token.getLiteral()).append(separator);
        }

        Curd right = binary.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append(separator);
        }
        return sb;
    }

}
