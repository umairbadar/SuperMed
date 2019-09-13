package managment.protege.supermed.Retrofit;

import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.Subcategory;
import managment.protege.supermed.Response.AddProductWishlistResponse;
import managment.protege.supermed.Response.AppointmentHistoryResponse;
import managment.protege.supermed.Response.AulterSearchResponse;
import managment.protege.supermed.Response.BannerResponse;
import managment.protege.supermed.Response.BookAppointmentResponse;
import managment.protege.supermed.Response.CartResponse;
import managment.protege.supermed.Response.CategoryResponse;
import managment.protege.supermed.Response.ChangePasswordResponse;
import managment.protege.supermed.Response.CityListResponse;
import managment.protege.supermed.Response.CodCheckoutResponse;
import managment.protege.supermed.Response.ContactUsResponse;
import managment.protege.supermed.Response.CountryListResponse;
import managment.protege.supermed.Response.CouponResponse;
import managment.protege.supermed.Response.DeleteWishlist;
import managment.protege.supermed.Response.DocResponse;
import managment.protege.supermed.Response.DoctorDetailsREsponse;
import managment.protege.supermed.Response.DoctorResponse;
import managment.protege.supermed.Response.EditProfileResponse;
import managment.protege.supermed.Response.EmergencyResponse;
import managment.protege.supermed.Response.FaqsResponse;
import managment.protege.supermed.Response.ForgotPassword;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Response.GetDoctorServiceResponse;
import managment.protege.supermed.Response.GetHospitalServiceResponse;
import managment.protege.supermed.Response.GetSpecializationServiceResponse;
import managment.protege.supermed.Response.GuestUserResponse;
import managment.protege.supermed.Response.HelpCenterResponse;
import managment.protege.supermed.Response.HelpCenterSearchResponse;
import managment.protege.supermed.Response.LoadLabsResponse;
import managment.protege.supermed.Response.LoadTestResponse;
import managment.protege.supermed.Response.LoginResponse;
import managment.protege.supermed.Response.Registration;
import managment.protege.supermed.Response.ResponseOrderHistroy;
import managment.protege.supermed.Response.SearchResponse;
import managment.protege.supermed.Response.SpecilizationResponse;
import managment.protege.supermed.Response.SubscribeNewsletterResponse;
import managment.protege.supermed.Response.TopicResponse;
import managment.protege.supermed.Response.UploadPrescResponse;
import managment.protege.supermed.Response.WishlistResponse;
import managment.protege.supermed.Response.WishlistToCart;
import managment.protege.supermed.Response.socialLoginResponse;
import managment.protege.supermed.Response.subcategoriesResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API {

    @FormUrlEncoded
    @POST("user-login")
    Call<LoginResponse> Login(@Field("email") String Email,
                              @Field("password") String Password);

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
    @POST("get_cart")
    Call<CartResponse> get_cart(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("searchMed")
    Call<SearchResponse> search(@Field("search") String search,
                                @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("cart_action")
    Call<CartActionResponse> cart_action(@Field("productid") String productid, @Field("userid") String userid,
                                         @Field("qty") String qty, @Field("action") String action);

    @FormUrlEncoded
    @POST("getProducts")
    Call<GetAllProductsResponse> getProducts(@Field("userid") String userid);

    @GET("list_lab")
    Call<LoadLabsResponse> LOAD_LABS_RESPONSE_CALL();

    @GET("getEmergency")
    Call<EmergencyResponse> getEmergency();

    @GET("getBanners")
    Call<BannerResponse> getBanners();

    @GET("getTopics")
    Call<HelpCenterResponse> getHelpCenter();

    @GET("getContactUS")
    Call<ContactUsResponse> contactUs();

    @GET("getTermStatus")
    Call<ContactUsResponse> getTermStatus();

    @GET("getGuestUserID")
    Call<LoginResponse> GUEST_USER_RESPONSE_CALL();

    @FormUrlEncoded
    @POST("getHelpCenterByTopic")
    Call<FaqsResponse> getHelpCenterByTopic(@Field("topic_id") String topic_id);

    @FormUrlEncoded
    @POST("SearchHelpCenterTopics")
    Call<HelpCenterSearchResponse> HELP_CENTER_SEARCH_RESPONSE_CALL(@Field("search") String search);

    @FormUrlEncoded
    @POST("get_wish")
    Call<WishlistResponse> WISHLIST_RESPONSE_CALL(@Field("userid") String userid);


    @FormUrlEncoded
    @POST("add_wish")
    Call<AddProductWishlistResponse> ADD_PRODUCT_WISHLIST_RESPONSE_CALL(@Field("productid") String productid,
                                                                        @Field("userid") String userid,
                                                                        @Field("qty") String qty);

    @FormUrlEncoded
    @POST("delete_wish")
    Call<DeleteWishlist> DELETE_WISHLIST_CALL(@Field("productid") String productid,
                                              @Field("userid") String userid);

    @FormUrlEncoded
    @POST("list_of_order")
    Call<ResponseOrderHistroy> list_of_order(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("list_test")
    Call<LoadTestResponse> LOAD_TEST_RESPONSE_CALL(@Field("lab_id") String lab_id);

    @FormUrlEncoded
    @POST("valCoupon")
    Call<CouponResponse> COUPON_RESPONSE_CALL(@Field("code") String code,
                                              @Field("userid") String userid);

    @FormUrlEncoded
    @POST("history_appointment")
    Call<AppointmentHistoryResponse> APPOINTMENT_HISTORY_RESPONSE_CALL(@Field("patient_id") String patient_id);

    @Multipart
    @POST("updateProfileUser")
    Call<LoginResponse> EDIT_PROFILE_RESPONSE_CALL(@Part("userid") String userid,
                                                   @Part("first_name") String first_name,
                                                   @Part("last_name") String last_name,
                                                   @Part("contact") String contact,
                                                   @Part("dob") String dob,
                                                   @Part("address") String address,
                                                   @Part("country") String country,
                                                   @Part("city") String city,
                                                   @Part MultipartBody.Part image
    );

    @GET("getCountries")
    Call<CountryListResponse> COUNTRY_LIST_RESPONSE_CALL();

    @FormUrlEncoded
    @POST("getCities")
    Call<CityListResponse> CITY_LIST_RESPONSE_CALL(@Field("country_id") String country_id);

    @FormUrlEncoded
    @POST("searchAlternateMed")
    Call<AulterSearchResponse> searchAlternateMed(@Field("search") String search,
                                                  @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("changePassword")
    Call<ChangePasswordResponse> CHANGE_PASSWORD_RESPONSE_CALL(@Field("userid") String userid,
                                                               @Field("old_pass") String old_pass,
                                                               @Field("new_pass") String new_pass,
                                                               @Field("con_pass") String con_pass);

    @Multipart
    @POST("uploadPrescription")
    Call<UploadPrescResponse> UPLOAD_PRESC_RESPONSE_CALL(@Part("userid") String userid,
                                                         @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("checkOut")
    Call<CodCheckoutResponse> COD_CHECKOUT_RESPONSE_CALL(@Field("userid") String userid,
                                                         @Field("contact") String contact,
                                                         @Field("address") String address,
                                                         @Field("ordernote") String ordernote,
                                                         @Field("paytype") String paytype,
                                                         @Field("coupanCode") String coupanCode,
                                                         @Field("first_name") String first_name,
                                                         @Field("last_name") String last_name,
                                                         @Field("country") String country,
                                                         @Field("city") String city,
                                                         @Field("email") String email);

    @FormUrlEncoded
    @POST("socialLogin")
    Call<LoginResponse> SOCIAL_LOGIN_RESPONSE_CALL(@Field("socialmediaid") String socialmediaid,
                                                   @Field("email") String email,
                                                   @Field("username") String username);
}

