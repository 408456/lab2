package goltsman.btbookingservice.exception;

public record HttpErrorResponse(
        int code,
        String type,
        String message
) {
}
