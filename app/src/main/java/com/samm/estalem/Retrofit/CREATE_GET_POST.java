package com.samm.estalem.Retrofit;

import com.samm.estalem.Chat.ChatModel;
import com.samm.estalem.Classes.Model.District;
import com.samm.estalem.Classes.Model.App;
import com.samm.estalem.Classes.Model.Client;
import com.samm.estalem.Classes.Model.OfferPrice;
import com.samm.estalem.Classes.Model.Order;
import com.samm.estalem.Classes.Model.Proposals;
import com.samm.estalem.Classes.Model.Provider;
import com.samm.estalem.Classes.Model.ValuationProvider;
import com.samm.estalem.Classes.ResponseBody;
import com.samm.estalem.Classes.ViewModel.AddPriceToOrderViewModel;
import com.samm.estalem.Classes.ViewModel.CheckCodeViewModel;
import com.samm.estalem.Classes.ViewModel.ClientAcceptOfferPriceViewModel;
import com.samm.estalem.Classes.ViewModel.OfferPriceViewModel;
import com.samm.estalem.Classes.ViewModel.ProviderRecieveOrderDetilesViewModel;
import com.samm.estalem.Classes.ViewModel.TrackingOrderDataViewModel;
import com.samm.estalem.Classes.ViewModel.UpdateLocationViewModel;
import com.samm.estalem.Classes.ViewModel.UpdateTokenViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface CREATE_GET_POST {

    /**************************************************** Provider**Auth****************************************/
    @POST("ProviderAuth/CheckProviderNumberPhone")
    Call<ResponseBody> CheckProviderNumberPhone(@Body CheckCodeViewModel checkCodeViewModel);
    @POST("ProviderAuth/AddProvider")
    Call<ResponseBody> AddProvider(@Body Provider provider);

    @GET("ProviderAuth/CheckPhone")
    Call<ResponseBody> CheckPhoneProvider(@Query("phoneNumber") String phoneNumber);

    @Multipart
    @POST("ProviderAuth/UploadProviderImage")
    Call<ResponseBody> UploadProviderImage(@Query("providerPhone") String providerPhone, @Part MultipartBody.Part NationalCardImage, @Part MultipartBody.Part DrivinglicenseImage, @Part MultipartBody.Part FormImage, @Part MultipartBody.Part
            DrivinglicenseAuthoImage, @Part MultipartBody.Part BackCarImage, @Part MultipartBody.Part FrontCarImage);



    @GET("ProviderAuth/GetProfile")
    Call<Provider> GetProfileProvider(@Query("phoneNumber")String  phoneNumber);


    @POST("ProviderAuth/UpdateProvider")
    Call<ResponseBody> UpdateProvider(@Body Provider provider);



    @Multipart
    @POST("ProviderAuth/UploadProviderProfileImage")
    Call<ResponseBody>UploadProviderProfileImage(@Part MultipartBody.Part image, @Query("phone") String  phone);
    /******************************************Provider**************************************************************************/

    @GET("Provider/SetActive")
    Call<ResponseBody> SetActive(@Query("providerPhone") String providerPhone);

    @GET("Provider/SetNotActive")
    Call<ResponseBody> SetNotActive(@Query("providerPhone") String providerPhone);

    @GET("Provider/ProviderAccountAmount")
    Call<ResponseBody> ProviderAccountAmount(@Query("providerPhone") String providerPhone);


    @POST("Provider/UpdateLocation")
    Call<ResponseBody> UpdateLocation(@Body UpdateLocationViewModel updateLocationViewModel);

    @POST("Provider/UpdateProviderToken")
    Call<ResponseBody> UpdateProviderToken(@Body UpdateTokenViewModel updateTokenViewModel);

    @POST("Provider/ValuationProvider")
    Call<ResponseBody> ValuationProvider(@Body ValuationProvider valuationProvider);
    /******************************************Chat**************************************************************************/

    @POST("Chat/SendMessage")
    Call<ResponseBody> SendMessage(@Body ChatModel chatModel);

    @GET("Chat/GetMyChat")
    Call<List<ChatModel>> GetMyChat(@Query("userPhone") String userPhone,@Query("otherUserPhone") String otherUserPhone);

    @Multipart
    @POST("Chat/UploadImage")
    Call<ResponseBody>UploadImage(@Part MultipartBody.Part image);
    /******************************************************Client**Auth************************************************************************************/

    @POST("ClientAuth/CheckClientNumberPhone")
    Call<ResponseBody> CheckClientNumberPhone(@Body CheckCodeViewModel checkCodeViewModel);

    @GET("ClientAuth/CheckPhone")
    Call<ResponseBody> CheckPhone(@Query("phoneNumber")String  phoneNumber);

    @GET("ClientAuth/GetProfile")
    Call<Client> GetProfile(@Query("phoneNumber")String  phoneNumber);

    @POST("ClientAuth/AddClient")
    Call<ResponseBody> AddClient(@Body Client client);

    @POST("ClientAuth/UpdateClient")
    Call<ResponseBody> UpdateClient(@Body Client client);


    @Multipart
    @POST("ClientAuth/UploadClientImage")
    Call<ResponseBody>UploadImageClient(@Part MultipartBody.Part image, @Query("phone") String  phone);


    /********************************************Order*****************************************************/

    @GET("Order/GetDistrict")
    Call<List<District>> GetDistrict();

    @GET("order/GetDistrictByCity")
    Call<List<District>> getSuCity(@Query("city") String  city);

    @GET("Order/GetCity")
    Call<List<String>> getCiy();

    @POST("Order/AddOfferPrice")
    Call<ResponseBody> AddOfferPrice(@Body OfferPrice offerPrice);


    @GET("Order/GetNearProvider")
    Call<List<Provider>> GetNearProvider(@Query("clientLat") double clientLat , @Query("clientLong") double clientLong );

    @GET("Order/GetOfferPrice")
    Call<List<OfferPriceViewModel>> GetOfferPrice(@Query("orderId") int orderId);

    @POST("Order/AddOrder")
    Call<ResponseBody> AddOrder(@Body Order order);

    @GET("Order/ProviderRejectOrder")
    Call<ResponseBody> ProviderRejectOrder(@Query("providerPhone")String providerPhone);

    @POST("Order/ClientAcceptOfferPrice")
    Call<ResponseBody> ClientAcceptOfferPrice(@Body ClientAcceptOfferPriceViewModel clientAcceptOfferPriceViewModel);


    @GET("Order/ProviderGetOrderDetails")
    Call<ProviderRecieveOrderDetilesViewModel> ProviderGetOrderDetails(@Query("orderId")int orderId);

    @GET("Order/ClientCancelOrder")
    Call<ResponseBody> ClientCancelOrder(@Query("orderId")int orderId);

    @GET("Order/ProviderCancelOrder")
    Call<ResponseBody> ProviderCancelOrder(@Query("orderId")int orderId);


    @GET("Order/GetTrackingOrderData")
    Call<TrackingOrderDataViewModel> GetTrackingOrderData(@Query("providerPhone")String providerPhone , @Query("orderId") int orderId);

    @GET("Order/GetDataDirectionToClient")
    Call<TrackingOrderDataViewModel> GetDataDirectionToClient(@Query("providerPhone")String providerPhone , @Query("orderId") int orderId);


    @GET("Order/GetOrderDetails")
    Call<Order> GetOrderDetails(@Query("orderId") int orderId);

    @GET("Order/GetDelivery")
    Call<ResponseBody> GetDelivery(@Query("orderId") int orderId);

    @POST("Order/AddPriceToOrder")
    Call<ResponseBody> AddPriceToOrder(@Body AddPriceToOrderViewModel addPriceToOrderViewModel);
    /*******************************************Client*************************************/
    @POST("Client/UpdateClientToken")
    Call<ResponseBody> UpdateClientToken(@Body UpdateTokenViewModel updateTokenViewModel);

    @GET("Client/ClientAccountAmount")
    Call<ResponseBody> ClientAccountAmount(@Query("clientPhone") String clientPhone);


    @GET("Client/GetClientOrders")
    Call<List<Order>> GetClientOrders(@Query("clientPhone") String clientPhone);


    @GET("Order/ChangOrderToAttend")
    Call<ResponseBody> ChangOrderToAttend(@Query("orderId") int  orderId);

    @GET("Order/ChangOrderToInRoad")
    Call<ResponseBody> ChangOrderToInRoad(@Query("orderId") int  orderId);

    @GET("Order/ChangOrderEnded")
    Call<ResponseBody> ChangOrderEnded(@Query("orderId") int  orderId);


    /********************************************Genral*************************************/
    @GET("Genral/App")
    Call<App> App();

    @POST("Genral/AddSuggest")
    Call<ResponseBody> AddSuggest(@Body Proposals proposals);


    @GET("Provider/ProviderOrders")
    Call<List<Order>> GetProviderOrders(@Query("providerPhone") String providerPhone);

    /********************************************OrderStatus*************************************/


}
















