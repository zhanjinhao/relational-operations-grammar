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

    private final String separator;

    private final boolean storable;

    public CurdPrinter(String separator) {
        this.separator = separator;
        storable = false;
    }

    public CurdPrinter() {
        this.separator = " ";
        storable = false;
    }

    public CurdPrinter(String separator, boolean storable) {
        this.separator = separator;
        this.storable = storable;
    }

    @Override
    public String visitSelect(Select select) {
        StringBuilder sb = new StringBuilder();

        Curd left = select.getLeftCurd();
        if (left != null) {
            sb.append(left.accept(this)).append("\n");
        }

        Token token = select.getToken();
        if (token != null) {
            sb.append(token.printLiteral()).append(separator);
            Token allToken = select.getAllToken();
            if (allToken != null) {
                sb.append(allToken.printLiteral());
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

        boolean notNeedParen = SingleSelectType.TOP.equals(singleSelectType)
                || SingleSelectType.INSERT.equals(singleSelectType);
        if (!notNeedParen) {
            sb.append(separator).append("(").append(separator);
        }

        deepth++;

        Curd columnSeg = singleSelect.getColumnSeg();
        if (columnSeg != null) {
            sb.append(blankStr()).append(separator).append("select").append(separator).append(columnSeg.accept(this)).append("\n");
        }

        Curd tableSeg = singleSelect.getTableSeg();
        if (tableSeg != null) {
            sb.append(blankStr()).append(separator).append("from").append(separator).append(tableSeg.accept(this)).append("\n");
        }

        Curd whereSeg = singleSelect.getWhereSeg();
        if (whereSeg != null) {
            sb.append(blankStr()).append(separator).append(whereSeg.accept(this)).append("\n");
        }

        Curd groupBySeg = singleSelect.getGroupBySeg();
        if (groupBySeg != null) {
            sb.append(blankStr()).append(separator).append(groupBySeg.accept(this)).append("\n");
        }

        Curd orderBySeg = singleSelect.getOrderBySeg();
        if (orderBySeg != null) {
            sb.append(blankStr()).append(separator).append(orderBySeg.accept(this)).append("\n");
        }

        Curd limitSeg = singleSelect.getLimitSeg();
        if (limitSeg != null) {
            sb.append(blankStr()).append(separator).append(limitSeg.accept(this)).append("\n");
        }

        Curd lockSeg = singleSelect.getLockSeg();
        if (lockSeg != null) {
            sb.append(blankStr()).append(separator).append(lockSeg.accept(this)).append("\n");
        }
        deepth--;
        if (!notNeedParen) {
            sb.append(separator).append(")").append(separator);
        }
        return sb.toString();
    }

    @Override
    public String visitColumnSeg(ColumnSeg columnSeg) {

        StringBuilder sb = new StringBuilder();
        Token restriction = columnSeg.getRestriction();
        if (restriction != null) {
            sb.append(restriction.printLiteral()).append(separator);
        }

        List<Curd> columnRepList = columnSeg.getColumnRepList();
        if (columnRepList != null) {
            sb.append(columnRepList.stream().map(item -> {
                        if (item != null) {
                            return item.accept(this);
                        }
                        return "";
                    })
                    .collect(Collectors.joining("," + separator)));
        }
        return sb.toString();
    }

    @Override
    public String visitColumnRep(ColumnRep columnRep) {
        StringBuilder sb = new StringBuilder();
        Curd curd = columnRep.getCurd();
        if (curd != null) {
            sb.append(curd.accept(this));
        }

        Token operator = columnRep.getOperator();
        if (operator != null) {
            sb.append(separator).append("as")
                    .append(separator).append(operator.printLiteral());
        }
        return sb.toString();
    }

    @Override
    public String visitTableSeg(TableSeg tableSeg) {

        StringBuilder sb = new StringBuilder();

        Curd left = tableSeg.getLeftCurd();
        if (left != null) {
            sb.append(left.accept(this)).append(separator);
        }

        Token qualifier = tableSeg.getQualifier();
        if (qualifier != null) {
            sb.append(qualifier.printLiteral()).append(separator);
        }

        Token join = tableSeg.getToken();
        if (join != null) {
            sb.append(join.printLiteral()).append(separator);
        }

        Curd right = tableSeg.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append(separator);
        }

        Curd condition = tableSeg.getCondition();
        if (condition != null) {
            sb.append("on").append(separator).append(condition.accept(this)).append(separator);
        }

        return sb.toString();
    }

    @Override
    public String visitTableRep(TableRep tableRep) {
        StringBuilder sb = new StringBuilder();
        Curd curd = tableRep.getCurd();
        Token alias = tableRep.getAlias();
        String accept = "";
        if (curd != null) {
            accept = curd.accept(this);
        }
        if (curd instanceof Select) {
            sb.append(accept).append(separator);
            if (alias != null) {
                sb.append(alias.printLiteral());
            }
        } else if (alias == null && curd instanceof Identifier) {
            sb.append(accept);
        } else if (alias != null && curd instanceof Identifier) {
            sb.append(accept).append(separator).append(alias.printLiteral());
        } else {
            sb.setLength(0);
        }

        return sb.toString();
    }

    @Override
    public String visitWhereSeg(WhereSeg whereSeg) {
        String result = "where" + separator;
        Curd logic = whereSeg.getLogic();
        if (logic != null) {
            return result + whereSeg.getLogic().accept(this);
        }
        return result;
    }

    @Override
    public String visitInCondition(InCondition inCondition) {
        StringBuilder sb = new StringBuilder();
        Token identifier = inCondition.getIdentifier();
        sb.append(identifier.printLiteral());

        Token in = inCondition.getIn();
        if (in.getType().equals(TokenType.IN)) {
            sb.append(separator).append("in").append(separator);
        } else {
            sb.append(separator).append("not").append(separator).append("in").append(separator);
        }
        Curd curd = inCondition.getSelect();
        if (curd != null) {
            sb.append(curd.accept(this));
        } else {
            sb.append("(").append(separator);
            List<Curd> range = inCondition.getRange();
            if (range != null && !range.isEmpty()) {
                sb.append(range.stream()
                        .map(item -> {
                            if (item != null) {
                                return item.accept(this);
                            }
                            return "";
                        })
                        .collect(Collectors.joining("," + separator)));
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
            sb.append(separator).append("exists").append(separator);
        } else {
            sb.append(separator).append("not").append(separator).append("exists").append(separator);
        }
        Curd curd = existsCondition.getCurd();
        if (curd != null) {
            sb.append(curd.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitGroupBySeg(GroupBySeg groupBySeg) {
        StringBuilder sb = new StringBuilder();
        sb.append("group").append(separator).append("by").append(separator);
        List<Token> columnList = groupBySeg.getColumnList();
        if (columnList != null && !columnList.isEmpty()) {
            sb.append(columnList.stream()
                    .map(item -> item.printLiteral().toString())
                    .collect(Collectors.joining("," + separator)));
        }
        Curd having = groupBySeg.getHaving();
        if (having != null) {
            sb.append(separator).append("having").append(separator).append(having.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitOrderBySeg(OrderBySeg orderBySeg) {
        StringBuilder sb = new StringBuilder();
        sb.append("order").append(separator).append("by").append(separator);
        List<Curd> columnList = orderBySeg.getColumnList();
        // columnList不会为空
        OrderItem orderItem = (OrderItem) columnList.get(0);
        sb.append(orderItem.accept(this));
        for (int i = 1; i < columnList.size(); i++) {
            orderItem = (OrderItem) columnList.get(i);
            sb.append(",").append(separator).append(orderItem.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visitOrderItem(OrderItem orderItem) {
        StringBuilder sb = new StringBuilder();
        Curd column = orderItem.getColumn();
        Token orderType = orderItem.getOrderType();
        sb.append(column.accept(this))
                .append(separator)
                .append(orderType == null ? "" : orderType.printLiteral());
        return sb.toString();
    }

    @Override
    public String visitLimitSeg(LimitSeg limitSeg) {
        String s = "limit" + separator + limitSeg.getNum().printLiteral();
        Token offset = limitSeg.getOffset();
        if (offset == null) {
            return s;
        }

        return s + separator + "offset" + separator + limitSeg.getOffset().printLiteral();
    }

    @Override
    public String visitLogic(Logic logic) {
        return visitBinary(logic, separator).toString();
    }

    @Override
    public String visitComparison(Comparison comparison) {
        StringBuilder sb = new StringBuilder();

        Curd left = comparison.getLeftCurd();
        if (left != null) {
            sb.append(left.accept(this)).append(separator);
        }

        Token token = comparison.getToken();
        if (token != null) {
            sb.append(token.printLiteral()).append(separator);
        }

        Curd comparisonSymbol = comparison.getComparisonSymbol();
        if (comparison != null) {
            sb.append(comparisonSymbol.accept(this));
        }

        Curd right = comparison.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append(separator);
        }
        return sb.toString();
    }

    @Override
    public String visitBinaryArithmetic(BinaryArithmetic binaryArithmetic) {
        return visitBinary(binaryArithmetic, separator).toString();
    }

    @Override
    public String visitUnaryArithmetic(UnaryArithmetic unaryArithmetic) {
        String result = unaryArithmetic.getOperator().printLiteral().toString();
        Curd curd = unaryArithmetic.getCurd();
        if (curd != null) {
            return result + curd.accept(this);
        }
        return result;
    }

    @Override
    public String visitLiteral(Literal literal) {
        if (literal.getValue() == null) {
            return null;
        }
        Token token = literal.getValue();

        Object value = token.printLiteral();

        if (TokenType.STRING.equals(token.getType())) {
            String result = String.valueOf(value);
            StringBuilder sb = new StringBuilder();
            if (storable) {
                sb.append("\\'");
            } else {
                sb.append("'");
            }
            int length = result.length();
            for (int i = 0; i < length; i++) {
                char c = result.charAt(i);
                if (storable && (c == '\'')) {
                    sb.append("\\'");
                }
                sb.append(c);
            }
            if (storable) {
                sb.append("\\'");
            } else {
                sb.append("'");
            }
            return sb.toString();
        }

        return value.toString();
    }

    @Override
    public String visitGroupFunction(GroupFunction groupFunction) {
        String a = "";
        Curd curd = groupFunction.getCurd();
        if (curd != null) {
            a = curd.accept(this);
        }
        return groupFunction.getMethod().printLiteral() + "(" + separator + a + separator + ")";
    }

    @Override
    public String visitGroupConcat(GroupConcat groupConcat) {
        StringBuilder sb = new StringBuilder();

        sb.append(separator).append("group_concat");
        sb.append("(");

        final Token modifier = groupConcat.getModifier();
        sb.append(separator).append(modifier == null ? "" : modifier.printLiteral());

        final List<Curd> resultList = groupConcat.getResultList();
        Curd curd = resultList.get(0);
        sb.append(separator).append(curd.accept(this));
        for (int i = 1; i < resultList.size(); i++) {
            curd = resultList.get(i);
            sb.append(",").append(separator).append(separator).append(curd.accept(this));
        }

        final List<Curd> orderItemList = groupConcat.getOrderItemList();
        if (orderItemList != null) {
            sb.append(separator).append("order");
            sb.append(separator).append("by");
            curd = orderItemList.get(0);
            sb.append(separator).append(curd.accept(this));
            for (int i = 1; i < orderItemList.size(); i++) {
                curd = orderItemList.get(i);
                sb.append(",").append(separator).append(separator).append(curd.accept(this));
            }
        }

        final String gs = groupConcat.getSeparator();
        if (gs != null) {
            sb.append(this.separator).append("separator");
            sb.append(this.separator);
            if (storable) {
                sb.append("\\'");
            } else {
                sb.append("'");
            }
            int length = gs.length();
            for (int i = 0; i < length; i++) {
                char c = gs.charAt(i);
                if (storable && (c == '\'')) {
                    sb.append("\\'");
                }
                sb.append(c);
            }
            if (storable) {
                sb.append("\\'");
            } else {
                sb.append("'");
            }
        }

        sb.append(this.separator).append(")");
        return sb.toString();
    }

    @Override
    public String visitCaseWhen(CaseWhen caseWhen) {
        StringBuilder sb = new StringBuilder();

        String a = "";
        Curd value = caseWhen.getValue();
        if (value != null) {
            a = value.accept(this);
        }

        sb.append(separator).append("case").append(separator).append(a);
        List<Curd> conditionList = caseWhen.getConditionList();
        List<Curd> resultList = caseWhen.getResultList();

        int size = conditionList.size();
        for (int i = 0; i < size; i++) {
            Curd condition = conditionList.get(i);
            Curd result = resultList.get(i);
            String b = "";
            if (condition != null) {
                b = condition.accept(this);
            }

            String c = "";
            if (result != null) {
                c = result.accept(this);
            }

            sb.append(separator).append("when").append(separator).append(b)
                    .append(separator).append("then").append(separator).append(c);
        }

        Curd defaultValue = caseWhen.getDefaultValue();
        if (defaultValue != null) {
            sb.append(separator).append("else").append(separator).append(defaultValue.accept(this));
        }
        sb.append(separator).append("end");

        return sb.toString();
    }

    @Override
    public String visitSLock(SLock sLock) {
        return "lock" + separator + "in" + separator + "share" + separator + "mode" + separator;
    }

    @Override
    public String visitXLock(XLock xLock) {
        return "for" + separator + "update" + separator;
    }

    @Override
    public String visitFrameEdge(FrameEdge frameEdge) {
        return frameEdge.getEdge().printLiteral() + separator
                + frameEdge.getTowards().printLiteral() + separator;
    }

    @Override
    public String visitFrameBetween(FrameBetween frameBetween) {
        return "between" + separator
                + frameBetween.getFrom().accept(this) + separator
                + "and" + separator
                + frameBetween.getTo().accept(this) + separator;
    }

    @Override
    public String visitDynamicFrame(DynamicFrame dynamicFrame) {
        return dynamicFrame.getType().printLiteral() + separator
                + dynamicFrame.getFrameRange().accept(this) + separator;
    }

    @Override
    public String visitWindow(Window window) {
        StringBuilder sb = new StringBuilder(
                "over" + separator
                        + "(" + separator);
        List<Curd> partitionByList = window.getPartitionByList();
        if (partitionByList != null && !partitionByList.isEmpty()) {
            sb.append("partition").append(separator)
                    .append("by").append(separator);
            for (Curd curd : partitionByList) {
                sb.append(curd.accept(this)).append(separator);
            }
        }
        Curd orderBySeg = window.getOrderBySeg();
        if (orderBySeg != null) {
            sb.append(orderBySeg.accept(this)).append(separator);
        }
        Curd dynamicFrame = window.getDynamicFrame();
        if (dynamicFrame != null) {
            sb.append(dynamicFrame.accept(this)).append(separator);
        }
        return sb + ")" + separator;
    }

    @Override
    public String visitWindowFunction(WindowFunction windowFunction) {
        List<Curd> parameterList = windowFunction.getParameterList();
        StringBuilder s = new StringBuilder();
        if (parameterList != null && !parameterList.isEmpty()) {
            Curd curd = parameterList.get(0);
            s.append(curd.accept(this)).append(separator);
            for (int i = 1; i < parameterList.size(); i++) {
                s.append(",").append(separator).append(parameterList.get(i).accept(this)).append(separator);
            }
        }

        return windowFunction.getMethod().printLiteral() + separator
                + "(" + separator
                + s + separator
                + ")" + separator
                + windowFunction.getWindow().accept(this) + separator;
    }

    @Override
    public String visitGrouping(Grouping grouping) {
        String a = "";
        Curd curd = grouping.getCurd();
        if (curd != null) {
            a = curd.accept(this);
        }

        return separator + "(" + separator + a + separator + ")" + separator;
    }

    @Override
    public String visitIdentifier(Identifier identifier) {
        Token name = identifier.getName();
        return name.printLiteral() + separator;
    }

    @Override
    public String visitFunction(Function function) {
        StringBuilder sb = new StringBuilder();
        Token method = function.getMethod();
        List<Curd> parameterList = function.getParameterList();
        sb.append(method.printLiteral());
        if (parameterList == null || parameterList.isEmpty()) {
            sb.append("()");
            return sb.toString();
        }
        sb.append(parameterList.stream().map(item -> {
            if (item != null) {
                return item.accept(this);
            }
            return "";
        }).collect(Collectors.joining(",", "(", ")")));
        return sb.toString();
    }

    @Override
    public String visitInsertValuesRep(InsertValuesRep insertValuesRep) {
        StringBuilder sb = new StringBuilder();
        List<Token> columnList = insertValuesRep.getColumnList();
        if (columnList != null && !columnList.isEmpty()) {
            String collect = columnList.stream()
                    .map(item -> (String) item.printLiteral())
                    .collect(Collectors.joining("," + separator, separator + "(" + separator, separator + ")" + separator));
            sb.append(collect);
        }

        sb.append("values");

        List<List<Curd>> curdListList = insertValuesRep.getCurdListList();
        String collect = curdListList.stream()
                .map(i -> i.stream().map(j -> {
                    if (j != null) {
                        return j.accept(this);
                    }
                    return "";
                }).collect(Collectors.joining("," + separator, separator + "(" + separator, separator + ")" + separator)))
                .collect(Collectors.joining("," + separator));

        sb.append(collect);

        return sb.toString();
    }

    @Override
    public String visitInsertSetRep(InsertSetRep insertSetRep) {
        StringBuilder sb = new StringBuilder();

        sb.append(separator).append("set").append(separator);

        Curd entryList = insertSetRep.getAssignmentList();
        if (entryList != null) {
            sb.append(entryList.accept(this));
        }

        return sb.toString();
    }

    @Override
    public String visitInsert(Insert insert) {
        StringBuilder sb = new StringBuilder();

        sb.append("insert").append(separator);
        Token constrict = insert.getConstrict();
        if (constrict != null) {
            sb.append(constrict.printLiteral());
        }
        sb.append(separator).append("into").append(separator);

        Token tableName = insert.getTableName();
        sb.append(tableName.printLiteral());

        Curd insertRep = insert.getInsertRep();
        if (insertRep != null) {
            sb.append(insertRep.accept(this));
        }

        Curd onDuplicateUpdate = insert.getOnDuplicateUpdate();
        if (onDuplicateUpdate != null) {
            sb.append(onDuplicateUpdate.accept(this));
        }

        return sb.toString();
    }

    @Override
    public String visitAssignmentList(AssignmentList assignmentList) {
        return assignmentList.getEntryList().stream()
                .map(item -> {
                    String a = item.getColumn().printLiteral() + "=";
                    Curd value = item.getValue();
                    if (value != null) {
                        return a + value.accept(this);
                    }
                    return a;
                })
                .collect(Collectors.joining("," + separator));
    }

    @Override
    public String visitTimeInterval(TimeInterval timeInterval) {
        return separator + "interval" + separator + timeInterval.getInterval() +
                separator + timeInterval.getTimeType().printLiteral();
    }

    @Override
    public String visitTimeUnit(TimeUnit timeUnit) {
        Curd curd = timeUnit.getCurd();
        String a = "";
        if (curd != null) {
            a = curd.accept(this);
        }
        return separator + timeUnit.getTimeType().printLiteral() + separator + "from" + separator + a;
    }

    @Override
    public String visitIsNot(IsNot isNot) {
        Token isToken = isNot.getIsToken();
        StringBuilder sb = new StringBuilder();
        sb.append(isToken.printLiteral()).append(separator);
        Token notToken = isNot.getNotToken();
        if (notToken != null) {
            sb.append(notToken.printLiteral()).append(separator);
        }
        return sb.toString();
    }

    @Override
    public String visitOnDuplicateKey(OnDuplicateKey onDuplicateKey) {
        String a = "";
        Curd assignmentList = onDuplicateKey.getAssignmentList();
        if (assignmentList != null) {
            a = assignmentList.accept(this);
        }
        return separator + "on" + separator + "duplicate" + separator + "key" + separator + "update" + separator + a;
    }

    @Override
    public String visitInsertSelectRep(InsertSelectRep insertSelectRep) {
        List<Token> columnList = insertSelectRep.getColumnList();
        StringBuilder sb = new StringBuilder();
        if (columnList != null && !columnList.isEmpty()) {
            String collect = columnList.stream()
                    .map(item -> (String) item.printLiteral())
                    .collect(Collectors.joining("," + separator, separator + "(" + separator, separator + ")" + separator));
            sb.append(collect);
        }

        Curd select = insertSelectRep.getSelect();
        if (select != null) {
            sb.append(separator).append(select.accept(this));
        }

        return sb.toString();
    }


    @Override
    public String visitUpdate(Update update) {

        Curd assignmentList = update.getAssignmentList();
        String a = "";
        if (assignmentList != null) {
            a = assignmentList.accept(this);
        }

        String str = "update" + separator +
                update.getTableName().printLiteral() +
                separator + "set" + separator + a;
        Curd whereSeg = update.getWhereSeg();
        if (whereSeg == null) {
            return str;
        }
        return str + separator + whereSeg.accept(this);
    }

    @Override
    public String visitDelete(Delete delete) {
        Token tableName = delete.getTableName();
        Curd whereSeg = delete.getWhereSeg();
        String str = "delete" + separator + "from" + separator + tableName.printLiteral();
        if (whereSeg != null) {
            return str + separator + whereSeg.accept(this);
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
        if (left != null) {
            sb.append(left.accept(this)).append(separator);
        }

        Token token = binary.getToken();
        if (token != null) {
            sb.append(token.printLiteral()).append(separator);
        }

        Curd right = binary.getRightCurd();
        if (right != null) {
            sb.append(right.accept(this)).append(separator);
        }
        return sb;
    }

}
