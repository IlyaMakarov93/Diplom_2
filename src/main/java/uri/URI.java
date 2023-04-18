package uri;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class URI {
    protected static final String URI_BURGER_API = "https://stellarburgers.nomoreparties.site/api/";
    protected static final String CREATE_USER = "auth/register/";
    protected static final String USER_LOGIN = "auth/login/";
    protected static final String USER_ACTIONS = "auth/user/";
    protected static final String USER_LOGOUT = "auth/logout/";
    protected static final String USER_ORDER = "orders/";
    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI_BURGER_API)
                .build();
    }

}

