package util.restUtil;

public class HelperMethods {

    BaseURL baseURL = new BaseURL();
    public String getProductsURL()
    {
        return baseURL.getBaseURL()+"/api/v2/storefront/products";
    }
    public String getauthorizationURL()
    {
        return baseURL.getBaseURL()+"/spree_oauth/token";
    }
    public String getaddItemURL()
    {
        return baseURL.getBaseURL()+"/api/v2/storefront/cart/add_item";
    }
    public String getCartURL()
    {
        return baseURL.getBaseURL()+"/api/v2/storefront/cart";
    }

    public String getdeleteItemURL()
    {
        return baseURL.getBaseURL()+"/api/v2/storefront/cart/remove_line_item/";
    }

}
