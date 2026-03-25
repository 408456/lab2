package goltsman.btrestaurantservice.common;

public class ApiConstant {
    public static final String ID = "/{id}";

    public static final String RESTAURANT_CONTROLLER_URL = "/api/v2/restaurants";
    public static final String CUISINE_CONTROLLER_URL = "/api/v2/cuisines";
    public static final String TABLE_CONTROLLER_URL = "/api/v2/tables";

    public static final String RESTAURANT_BY_CUISINE = "/cuisine/{cuisineId}";
    public static final String RESTAURANT_TABLES = "/{restaurantId}/tables";
    public static final String RESTAURANT_TABLES_AVAILABLE = "/{restaurantId}/tables/available";
}
