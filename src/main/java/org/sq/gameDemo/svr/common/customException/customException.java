package org.sq.gameDemo.svr.common.customException;

public class customException{

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
}