package org.sq.gameDemo.svr.common.customException;

public class CustomException {

    public static class RemoveFailedException extends RuntimeException{
        public RemoveFailedException(String message) {
            super(message);
        }
    }

    public static class NoSuchSenceException extends RuntimeException{
        public NoSuchSenceException(String message) {
            super(message);
        }
    }
    public static class BindRoleInSenceException extends RuntimeException  {
        public BindRoleInSenceException() {
        }

        public BindRoleInSenceException(String message) {
            super(message);
        }
    }

    public static class ParamNoMatchException extends RuntimeException  {
        public ParamNoMatchException() {
        }

        public ParamNoMatchException(String message) {
            super(message);
        }
    }
    public static class PlayerAlreadyDeadException extends Throwable   {
        public PlayerAlreadyDeadException() {
            super();
        }

        public PlayerAlreadyDeadException(String message) {
            super(message);
        }
    }

    public static class SystemSendMailErrException extends Throwable {
        public SystemSendMailErrException(String message) {
            super(message);
        }
    }
}
