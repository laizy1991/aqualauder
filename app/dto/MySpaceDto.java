package dto;

import models.User;

public class MySpaceDto {

    private User user;
    private DistributorDetail detail;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DistributorDetail getDetail() {
        return detail;
    }

    public void setDetail(DistributorDetail detail) {
        this.detail = detail;
    }
    
}
