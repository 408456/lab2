package goltsman.btrestaurantservice.exception;

public record HttpErrorResponse(
        int code,
        String type,
        String message
) {
}
