package cn.addenda.ro.grammar.ast.expression;

import cn.addenda.ro.grammar.ast.CurdVisitor;

/**
 * @author addenda
 * @datetime 2021/8/14 15:34
 */
public class Attachment extends Curd {

    private Curd attachment;

    public Attachment(Curd attachment) {
        this.attachment = attachment;
    }

    public Curd getAttachment() {
        return attachment;
    }

    @Override
    public <R> R accept(CurdVisitor<R> curdVisitor) {
        return curdVisitor.visitAttachment(this);
    }

}
