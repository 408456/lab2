package goltsman.btreviewservice.exception;

public record HttpErrorResponse(
        int code,
        String type,
        String message
) {
}
