package com.samm.estalem.Classes.ViewModel;

public class AddPriceToOrderViewModel {
    public int orderId ;
    public double price ;

    public AddPriceToOrderViewModel(int orderId, double price) {
        this.orderId = orderId;
        this.price = price;
    }
}
