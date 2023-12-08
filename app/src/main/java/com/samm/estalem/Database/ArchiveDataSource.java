package com.samm.estalem.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ArchiveDataSource {

    Flowable<List<Archiveitem>> getAllCart();


    Single<Integer> countItemInCart(String fbid);
    Single<Archiveitem> getItemInCart(String foodId, String fbid);
    Completable insertOrReplaceAll(Archiveitem... cartItems);
    Single<Integer> updateCart(Archiveitem cart);

    Single<Integer> deleteCart(Archiveitem cart);

    Single<Integer> cleanCart();

}
