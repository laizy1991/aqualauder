package service;

import java.util.ArrayList;
import java.util.List;

import models.CashInfo;
import models.Distributor;
import models.DistributorSuperior;
import models.User;
import models.UserMonthBlotter;
import models.UserWallet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import utils.DateUtil;
import common.constants.BillType;
import common.constants.CashStatus;
import common.constants.CommonDictType;
import dao.CashInfoDao;
import dao.DistributorDao;
import dao.DistributorSuperiorDao;
import dao.UserMonthBlotterDao;
import dao.UserWalletDao;
import dto.DistributorDetail;

/**
 * 分销商
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class DistributorService {
    private static final int MAX_BLOTTER_DEPTH = 3;
    
    public static Distributor get(int id) {
        return DistributorDao.get(id);
    }

    public static void add(Distributor distributor) {
        distributor.setCreateTime(System.currentTimeMillis());
        distributor.setUpdateTime(System.currentTimeMillis());
        DistributorDao.insert(distributor);
    }

    public static void delete(Distributor distributor) {
        DistributorDao.delete(distributor);
    }

    public static void update(Distributor distributor) {
        distributor.setUpdateTime(System.currentTimeMillis());
        DistributorDao.update(distributor);
    }
    
    public static void blotterProduce(Integer userId, Long blotterAmount, String outTradeNo) {
        if(userId == null || userId.intValue() < 0) {
            return;
        }
        
        int currMonth = DateUtil.getThisMonth();
        
        
        UserMonthBlotter monthBlotter = UserMonthBlotterDao.get(currMonth, userId);
        if(monthBlotter == null) {
            monthBlotter = new UserMonthBlotter();
            monthBlotter.setBlotterMonth(currMonth);
            monthBlotter.setCreateTime(System.currentTimeMillis());
            monthBlotter.setUserId(userId);
            monthBlotter.setMonthBlotters(0l);
        }
        monthBlotter.setMonthBlotters(monthBlotter.getMonthBlotters() + blotterAmount);
        UserMonthBlotterDao.save(monthBlotter);
        
        DistributorSuperior superior = DistributorSuperiorDao.get(userId);
        if (superior == null || superior.getSuperior() == null || superior.getSuperior().intValue() <= 0 || superior.getSuperior().intValue() == userId) {
            return;
        }
        
        //获取直接上线信息
        Distributor original = DistributorDao.get(superior.getSuperior());
        if (original == null) {
            Logger.error("original not found, userId:%d", superior.getSuperior());
            return;
        }

        flushBlotterToSuperiors(original, blotterAmount, 1, currMonth, userId, outTradeNo);
    }
    
    /**
     * 将充值流水记录扩散到上上级中
     * @param gameId
     * @param userId
     * @param blotterAmount
     * @param depth
     */
    private static void flushBlotterToSuperiors(Distributor original, long blotterAmount,
            int depth, int currMonth, int consumerId, String outTradeNo) {
        if (depth > MAX_BLOTTER_DEPTH) {
            return;
        }
        
        String levelPay = CommonDictService.getValue(CommonDictType.LEVEL_PAY, String.valueOf(depth));
        if(StringUtils.isBlank(levelPay)) {
            return;
        }
        int per = 0;
        try {
            per = Integer.valueOf(levelPay);
        } catch(Exception e) {
            Logger.error("level pay invalid, level:%s, value:%s", depth, levelPay);
            return;
        }
        
        //加50做四舍五入
        int addMoney = (int)((blotterAmount * per + 50) / 100);
        if(addMoney > 0) {
            String userName = "未命名";
            User user = UserService.get(consumerId);
            if(user != null && StringUtils.isNotBlank(user.getNickname())) {
                userName = user.getNickname();
            }
            BillType billType = BillType.CONSUMPTION;
            if(depth == 1) {
                billType = BillType.SPREAD;
            }
            String desc = String.format(billType.getTemplate(), userName);
            billType.setDesc(desc);
            UserWalletService.income(billType, original.getUserId(), consumerId, currMonth, addMoney, outTradeNo);
        }
        
        
        DistributorSuperior superior = DistributorSuperiorDao.get(original.getUserId());
        if (superior == null || superior.getSuperior() == null
                || superior.getSuperior().intValue() <= 0
                || superior.getSuperior().intValue() == original.getUserId()) {
            return;
        }

        Distributor user = DistributorDao.get(superior.getSuperior());
        if(user == null) {
            return;
        }
        
        // 递归访问上上线, 给上上线增加下下线流水
        depth++;
        flushBlotterToSuperiors(user, blotterAmount, depth, currMonth, consumerId, outTradeNo);
    }
    
    public DistributorDetail distributorDetail(Integer userId) {
        DistributorDetail detail = new DistributorDetail();
        Distributor distributor = DistributorDao.get(userId);
        if(distributor == null) {
            return null;
        }
        
        detail.setExtensionQrCode(distributor.getQrcodeUrl());
        detail.setExtensionUrl(distributor.getLink());
        detail.setType(distributor.getType());
        
        List<Integer> superiors = new ArrayList<Integer>();
        superiors.add(userId);
        for(int level=1; level<=MAX_BLOTTER_DEPTH; level++) {
            List<DistributorSuperior> list = DistributorSuperiorDao.getBySuperiors(superiors);
            if(CollectionUtils.isEmpty(list)) {
                break;
            }
            
            superiors.clear();
            for(DistributorSuperior item : list) {
                superiors.add(item.getUserId());
            }
            
            List<UserMonthBlotter> blotters = UserMonthBlotterDao.getByIds(superiors);
            for(UserMonthBlotter item : blotters) {
                detail.addBlotter(level, item.getUserId(), item.getMonthBlotters());
            }
            
        }
        
        UserWallet userWallet = UserWalletDao.getByUserId(userId);
        if(userWallet == null) {
            userWallet = UserWalletService.init(userId);
        }
        
        List<Integer> types = new ArrayList<Integer>();
        types.add(CashStatus.APPLY.getCode());
        types.add(CashStatus.ING.getCode());
        List<CashInfo> cashInfos = CashInfoDao.getByTypes(userId, types);
        int amount = 0;
        for(CashInfo info : cashInfos) {
            amount += info.getAmount();
        }
        
        if(userWallet == null) {
            detail.setTotalIncome(0);
            detail.setAllBalance(amount);
            detail.setUsefulBalance(0);
        } else {
            detail.setTotalIncome(userWallet.getIncome());
            detail.setAllBalance(amount + userWallet.getBalances());
            detail.setUsefulBalance(userWallet.getBalances());
        }
        
        return detail;
    }
}
