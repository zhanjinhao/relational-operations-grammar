package cn.addenda.ro.error.reporter;

import cn.addenda.ro.ROException;
import cn.addenda.ro.error.ROError;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author addenda
 * @datetime 2021/7/18 10:47
 */
public abstract class AbstractROErrorReporterDelegate implements ROErrorReporter {

    protected static final String SEPARATOR = " ";

    private final Map<Class<? extends ROError>, Function<? super ROError, String>> msgSuffix = new ROErrorMsg();

    private static final int ERROR_CODE_BOOTSTRAP = 90000;

    protected Map<Integer, String> digitizedErrorMsg = new HashMap<>();

    public AbstractROErrorReporterDelegate() {
        fillErrorMsg();
    }

    protected abstract void fillErrorMsg();

    protected String getErrorMsg(int errorCode) {
        return digitizedErrorMsg.get(errorCode);
    }

    protected void addErrorMsg(int errorCode, String errorMsg) {
        if (digitizedErrorMsg.containsKey(errorCode)) {
            throw new ROException(ERROR_CODE_BOOTSTRAP, "The code of error has existed, current msg is : " + getErrorMsg(errorCode) + ".");
        }
        digitizedErrorMsg.put(errorCode, errorMsg);
    }

    protected void addSuffixFunction(Class<? extends ROError> type, Function<? super ROError, String> function) {
        msgSuffix.put(type, function);
    }


    protected String getSuffix(ROError attachment) {
        if (attachment == null) {
            return "";
        }
        Function<? super ROError, String> stringFunction = msgSuffix.get(attachment);
        if (stringFunction == null) {
            return "";
        }
        return stringFunction.apply(attachment);
    }


    /**
     * 存储
     */
    private static class ROErrorMsg extends HashMap<Class<? extends ROError>, Function<? super ROError, String>> {

        @Override
        public Function<? super ROError, String> get(Object object) {
            Set<Entry<Class<? extends ROError>, Function<? super ROError, String>>> entries = this.entrySet();

            for (Entry<Class<? extends ROError>, Function<? super ROError, String>> entry : entries) {
                Class<? extends ROError> key = entry.getKey();
                if (key.isAssignableFrom(object.getClass())) {
                    return entry.getValue();
                }
            }
            return null;
        }
    }

}
