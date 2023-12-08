package com.samm.estalem.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalArchiveDataSource implements ArchiveDataSource {

    private ArchiveDAO mCartDAO;

    public LocalArchiveDataSource(ArchiveDAO cartDAO) {
        mCartDAO = cartDAO;
    }

    @Override
    public Flowable<List<Archiveitem>> getAllCart() {
        return mCartDAO.getAllArchive();
    }



    @Override
    public Single<Archiveitem> getItemInCart(String foodId, String fbid) {
        return mCartDAO.getItemInArchive(foodId, fbid);
    }

    @Override
    public Completable insertOrReplaceAll(Archiveitem... cartItems) {
        return mCartDAO.insertOrReplaceAll(cartItems);
    }

    @Override
    public Single<Integer> updateCart(Archiveitem cart) {
        return mCartDAO.updateArchive(cart);
    }

    @Override
    public Single<Integer> deleteCart(Archiveitem cart) {
        return mCartDAO.deleteArchive(cart);
    }

    @Override
    public Single<Integer> cleanCart() {
        return mCartDAO.cleanArchive();
    }

    @Override
    public Single<Integer> countItemInCart(String fbid) {
        return mCartDAO.countItemInCart(fbid);
    }
    }
