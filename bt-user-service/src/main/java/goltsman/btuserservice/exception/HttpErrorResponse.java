package goltsman.btuserservice.exception;

public record HttpErrorResponse(
        int code,
        String type,
        String message
) {
}
