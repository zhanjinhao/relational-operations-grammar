package cn.addenda.ro.grammar.ast.retrieve;

/**
 * @author addenda
 * @datetime 2021/3/11 10:22
 */
public enum SingleSelectType {

    /**
     * 构成返回值的一部分
     */
    TOP,
    /**
     * 具体的值，参与逻辑运算
     */
    PRIMARY,
    /**
     * InsertSelectParser
     */
    INSERT,
    /**
     * inCondition
     */
    IN,
    /**
     * existsCondition
     */
    EXISTS,
    /**
     * tableRep
     */
    TABLE,

}
