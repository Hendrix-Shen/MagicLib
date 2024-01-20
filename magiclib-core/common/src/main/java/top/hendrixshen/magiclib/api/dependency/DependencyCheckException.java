package top.hendrixshen.magiclib.api.dependency;

public class DependencyCheckException extends RuntimeException {
    public DependencyCheckException() {
        super();
    }

    public DependencyCheckException(String message) {
        super(message);
    }

    public DependencyCheckException(Throwable cause) {
        super(cause);
    }
}
