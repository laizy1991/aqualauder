package service;

import models.AuditInfo;
import play.Logger;

import common.constants.AuditStatus;
import common.constants.AuditType;
import common.constants.DistributorStatus;

import dao.AuditInfoDao;

public class AuditService {

    public static boolean audit(long id, int status) {
        AuditStatus auditStatus = AuditStatus.resolveType(status);
        if(auditStatus == null) {
            Logger.error("invalid status:", status);
            return false;
        }
        
        AuditInfo info = AuditInfoDao.get(id);
        if(info == null) {
            Logger.error("auditinfo not found, id:%s", id);
            return false;
        }

        info.setAuditStatus(status);
        boolean isSucc = AuditInfoDao.update(info);
        if(!isSucc) {
            return false;
        }
        if(info.getAuditType().intValue() == AuditType.DISTRIBUTOR.getCode()) {
            if(status == AuditStatus.PASS.getStatus()) {
                isSucc = DistributorService.updateStatus(info.getUserId(), DistributorStatus.PASS.getCode());
            } else if(status == AuditStatus.FAILED.getStatus()) {
                isSucc = DistributorService.updateStatus(info.getUserId(), DistributorStatus.FAILED.getCode());
            }
        }
        
        return isSucc;
    }
    
}
