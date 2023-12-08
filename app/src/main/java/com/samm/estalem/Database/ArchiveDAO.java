package com.samm.estalem.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
@Dao
public interface ArchiveDAO {


    // We only load Cart by Restaurant Id
    // Because each restaurant id will have order receipt different
    // Because each restaurant have different link payment, so we can't make 1 cart for all
    @Query("SELECT * FROM Archive ")
    Flowable<List<Archiveitem>> getAllArchive();


    @Query("SELECT COUNT(*) FROM Archive WHERE id=:fbid")
    Single<Integer> countItemInCart(String fbid);

    @Query("SELECT * FROM Archive WHERE id=:prodect_id AND id=:fbid ")
    Single<Archiveitem> getItemInArchive(String prodect_id, String fbid);

    // If conflict foodId, we will update information
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(Archiveitem... cartItems);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Single<Integer> updateArchive(Archiveitem cart);

    @Delete
    Single<Integer> deleteArchive(Archiveitem cart);

    @Query("DELETE FROM Archive ")
    Single<Integer> cleanArchive();
}
