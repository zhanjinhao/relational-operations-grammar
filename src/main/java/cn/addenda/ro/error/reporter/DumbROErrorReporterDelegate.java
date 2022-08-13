package cn.addenda.ro.error.reporter;

import cn.addenda.ro.ROException;

/**
 * @author addenda
 * @datetime 2021/7/29 14:33
 */
public class DumbROErrorReporterDelegate extends AbstractROErrorReporterDelegate {

    private static volatile ROErrorReporter roErrorReporter;

    public static ROErrorReporter getInstance() {

        if (roErrorReporter == null) {
            synchronized (DumbROErrorReporterDelegate.class) {
                if (roErrorReporter == null) {
                    roErrorReporter = new DumbROErrorReporterDelegate();
                }
            }
        }

        return roErrorReporter;
    }

    @Override
    public void error(int errorCode) {
        throw new ROException(0, "runtime error.");
    }

    @Override
    protected void fillErrorMsg() {
        // nop
    }

}
