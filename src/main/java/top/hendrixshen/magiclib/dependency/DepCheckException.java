package top.hendrixshen.magiclib.dependency;

public class DepCheckException extends RuntimeException {

    public DepCheckException() {
        super();
    }

    public DepCheckException(String message) {
        super(message);
    }

    public DepCheckException(Throwable cause) {
        super(cause);
    }
}