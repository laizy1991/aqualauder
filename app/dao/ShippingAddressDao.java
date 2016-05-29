package dao;

import javax.persistence.Query;

import models.ShippingAddress;
import play.db.jpa.Model;

public class ShippingAddressDao {
    public static ShippingAddress get(long id) {
        return ShippingAddress.findById(id);
    }

    public static void save(ShippingAddress sa) {
        sa.save();
    }

    public static void insert(ShippingAddress sa) {
        sa.create();
    }
    
    public static ShippingAddress getByUserId(int userId) {
        String sql = "SELECT * FROM shipping_address where user_id = " + userId;  
        Query query = Model.em().createNativeQuery(sql,ShippingAddress.class);  
        if(query.getResultList().size() > 0) {
            return (ShippingAddress)query.getResultList().get(0);
        }
        
        return null;
    }
}