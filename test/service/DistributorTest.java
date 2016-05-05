package service;

import org.junit.Test;

import play.test.UnitTest;

import common.constants.AuditStatus;

public class DistributorTest extends UnitTest{

    @Test
    public void test() {
        become();
        applyAudit();
        audit();
        createRef();
        blotterProduce();
    }
    
    public void become() {
        DistributorService.checkAndBecomeDistributor(4);
    }
    
    public void applyAudit() {
        DistributorService.applyAudit(4, "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=880481751,522292637&fm=58", "呵呵");
    }
    
    public void audit() {
        AuditService.audit(5, AuditStatus.PASS.getStatus());
    }
    
    public void createRef() {
        DistributorService.createDistributorRef(5, 4);
    }
    
    public void blotterProduce() {
        DistributorService.blotterProduce(6, 100000, "201904262222");
    }
}
