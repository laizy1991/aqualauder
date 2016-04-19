package utils.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

/**
 * @author nemo
 * @date 2016.04.15
 */


public class ValidatorUtil {
    private static Validator validator = new Validator();

    private ValidatorUtil() {
    }

    public static ValidatorUtil.Result validate(Object obj) {
        ValidatorUtil.Result result = new ValidatorUtil.Result();
        if(obj == null) {
            result.error = true;
            result.addMessage("obj is null");
            return result;
        } else {
            List constraintViolations = validator.validate(obj);
            result.error = !constraintViolations.isEmpty();
            Iterator var3 = constraintViolations.iterator();

            while(var3.hasNext()) {
                ConstraintViolation c = (ConstraintViolation)var3.next();
                result.addMessage(c.getMessage());
            }

            return result;
        }
    }

    public static class Result {
        private boolean error;
        private List<String> messages = new ArrayList();

        public Result() {
        }

        public ValidatorUtil.Result addMessage(String message) {
            this.messages.add(message);
            return this;
        }

        public List message() {
            return message();
        }

        public boolean hasError() {
            return this.error;
        }
    }
}
