package managment.protege.supermed.Retrofit;

import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Response.AppointmentHistoryResponse;
import managment.protege.supermed.Response.AulterSearchResponse;
import managment.protege.supermed.Response.BannerResponse;
import managment.protege.supermed.Response.CategoryResponse;
import managment.protege.supermed.Response.FaqsResponse;
import managment.protege.supermed.Response.ForgotPassword;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Response.HelpCenterResponse;
import managment.protege.supermed.Response.HelpCenterSearchResponse;
import managment.protege.supermed.Response.LoginResponse;
import managment.protege.supermed.Response.ResponseOrderHistroy;
import managment.protege.supermed.Response.SearchResponse;
import managment.protege.supermed.Response.subcategoriesResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded
    @POST("user-login")
    Call<LoginResponse> Login(@Field("email") String Email,
                              @Field("password") String Password);


    @GET("guest-user")
    Call<LoginResponse> LoginGuestUser();

    @FormUrlEncoded
    @POST("user-forget-password")
    Call<ForgotPassword> forgotPass(@Field("email") String Email);

    @GET("getCategories")
    Call<CategoryResponse> getCategories();

    @FormUrlEncoded
    @POST("getSubCategoriesById")
    Call<subcategoriesResponse> getSubCategoriesById(@Field("cat_id") String cat_id);

    @FormUrlEncoded
    @POST("getProductsBySubCatId")
    Call<GetAllProductsResponse> GetAllProducts(@Field("sub_cat_id") String sub_cat_id,
                                                @Field("userid") String userid);

    @FormUrlEncoded
    @POST("searchMed")
    Call<SearchResponse> search(@Field("keywords") String keywords,
                                @Field("category") String category);

    @FormUrlEncoded
    @POST("cart_action")
    Call<CartActionResponse> cart_action(@Field("productid") String productid, @Field("userid") String userid,
                                         @Field("qty") String qty, @Field("action") String action);

    @GET("getBanners")
    Call<BannerResponse> getBanners();

    @GET("getTopics")
    Call<HelpCenterResponse> getHelpCenter();

    @GET("getGuestUserID")
    Call<LoginResponse> GUEST_USER_RESPONSE_CALL();

    @FormUrlEncoded
    @POST("getHelpCenterByTopic")
    Call<FaqsResponse> getHelpCenterByTopic(@Field("topic_id") String topic_id);

    @FormUrlEncoded
    @POST("SearchHelpCenterTopics")
    Call<HelpCenterSearchResponse> HELP_CENTER_SEARCH_RESPONSE_CALL(@Field("search") String search);

    @FormUrlEncoded
    @POST("list_of_order")
    Call<ResponseOrderHistroy> list_of_order(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("history_appointment")
    Call<AppointmentHistoryResponse> APPOINTMENT_HISTORY_RESPONSE_CALL(@Field("patient_id") String patient_id);

    @FormUrlEncoded
    @POST("searchAlternateMed")
    Call<AulterSearchResponse> searchAlternateMed(@Field("search") String search,
                                                  @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("socialLogin")
    Call<LoginResponse> SOCIAL_LOGIN_RESPONSE_CALL(@Field("socialmediaid") String socialmediaid,
                                                   @Field("email") String email,
                                                   @Field("username") String username);
}

