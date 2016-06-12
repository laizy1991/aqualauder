package service;

import models.ShippingAddress;
import dao.ShippingAddressDao;

public class ShippingAddressService {

    public static void create(int userId, String address, String name, String mobile, String weixin,  int postal) {
        ShippingAddress sa = ShippingAddressDao.getByUserId(userId);
        boolean insert = false;
        if(sa == null) {
            sa = new ShippingAddress();
            sa.setCreateTime(System.currentTimeMillis());
            insert = true;
        }
        
        sa.setAddress(address);
        sa.setName(name);
        sa.setDistrictId(0);
        sa.setMobile(mobile);
        sa.setPostal(postal);
        sa.setUserId(userId);
        sa.setWeixin(weixin);
        sa.setUpdateTime(System.currentTimeMillis());
        if(insert) {
            ShippingAddressDao.insert(sa);
        } else {
            ShippingAddressDao.save(sa);
        }
    }
    
}
