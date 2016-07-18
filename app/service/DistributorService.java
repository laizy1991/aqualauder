package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.AuditInfo;
import models.CashInfo;
import models.Distributor;
import models.DistributorSuperior;
import models.Order;
import models.User;
import models.UserMonthBlotter;
import models.UserWallet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.wx.WXPay;
import service.wx.dto.qrcode.CreateQrCodeRspDto;
import service.wx.dto.qrcode.limit.CreateLimitQrCodeReqDto;
import utils.DateUtil;
import common.constants.AuditStatus;
import common.constants.AuditType;
import common.constants.BillType;
import common.constants.CashStatus;
import common.constants.CommonDictType;
import common.constants.DistributorStatus;
import common.constants.DistributorType;
import common.constants.OrderStatus;
import dao.AuditInfoDao;
import dao.CashInfoDao;
import dao.DistributorDao;
import dao.DistributorSuperiorDao;
import dao.OrderDao;
import dao.UserDao;
import dao.UserMonthBlotterDao;
import dao.UserWalletDao;
import dto.DistributorDetail;
import exception.BusinessException;

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

    public static boolean add(Distributor distributor) {
        distributor.setCreateTime(System.currentTimeMillis());
        distributor.setUpdateTime(System.currentTimeMillis());
        return DistributorDao.insert(distributor);
    }

    public static void delete(Distributor distributor) {
        DistributorDao.delete(distributor);
    }

    public static boolean update(Distributor distributor) {
        distributor.setUpdateTime(System.currentTimeMillis());
        return DistributorDao.update(distributor);
    }
    
    public static void blotterProduce(Integer userId, int blotterAmount, String outTradeNo) {
        if(userId == null || userId.intValue() < 0) {
            return;
        }
        
        int currMonth = DateUtil.getThisMonth();
        
        
        UserMonthBlotter monthBlotter = UserMonthBlotterDao.get(currMonth, userId);
        if(monthBlotter == null) {
            monthBlotter = new UserMonthBlotter();
            monthBlotter.setBlotterMonth(currMonth);
            monthBlotter.setCreateTime(System.currentTimeMillis());
            monthBlotter.setUpdateTime(System.currentTimeMillis());
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
        
        int addMoney = getAddMoney(depth, blotterAmount);
        if(addMoney <= 0) {
            return;
        }
        String userName = "未命名";
        User user = UserService.get(consumerId);
        if(user != null && StringUtils.isNotBlank(user.getNickname())) {
            userName = user.getNickname();
        }
        BillType billType = BillType.SPREAD;
        if(depth == 1) {
            billType = BillType.CONSUMPTION;
        }
        String desc = String.format(billType.getTemplate(), userName);
        billType.setDesc(desc);
        UserWalletService.income(billType, original.getUserId(), consumerId, currMonth, addMoney, outTradeNo);
        
        
        DistributorSuperior superior = DistributorSuperiorDao.get(original.getUserId());
        if (superior == null || superior.getSuperior() == null
                || superior.getSuperior().intValue() <= 0
                || superior.getSuperior().intValue() == original.getUserId()) {
            return;
        }

        Distributor distributor = DistributorDao.get(superior.getSuperior());
        if(distributor == null) {
            return;
        }
        
        // 递归访问上上线, 给上上线增加下下线流水
        depth++;
        flushBlotterToSuperiors(distributor, blotterAmount, depth, currMonth, consumerId, outTradeNo);
    }
    
    public static int getAddMoney(int level, long blotterAmount) {
        String levelPay = CommonDictService.getValue(CommonDictType.LEVEL_PAY, String.valueOf(level));
        if(StringUtils.isBlank(levelPay)) {
            return 0;
        }
        int per = 0;
        try {
            per = Integer.valueOf(levelPay);
        } catch(Exception e) {
            Logger.error("level pay invalid, level:%s, value:%s", level, levelPay);
            return 0;
        }
        
        //加50做四舍五入
        int addMoney = (int)((blotterAmount * per + 50) / 100);
        return addMoney;
    }
    
    public static DistributorDetail distributorDetail(Integer userId) {
        DistributorDetail detail = new DistributorDetail();
        Distributor distributor = DistributorDao.get(userId);
        if(distributor == null) {
            return null;
        }
        DistributorSuperior ds = DistributorSuperiorDao.get(userId);
        if(ds != null) {
            User user = UserDao.get(ds.getSuperior());
            if(user != null) {
                detail.setSuperior(user.getNickname());
            }
        }
        
        detail.setExtensionQrCode(distributor.getQrcodeLimitPath());
        detail.setExtensionUrl(distributor.getLink());
        detail.setType(distributor.getType());
        
        List<Integer> superiors = new ArrayList<Integer>();
        superiors.add(userId);
        List<Integer> undelingUids = new ArrayList<Integer>();
        Map<Integer, List<Integer>> underlingLevel = new HashMap<Integer, List<Integer>>();
        for(int level=1; level<=MAX_BLOTTER_DEPTH; level++) {
            List<DistributorSuperior> list = DistributorSuperiorDao.getBySuperiors(superiors);
            if(CollectionUtils.isEmpty(list)) {
                break;
            }
            
            superiors.clear();
            for(DistributorSuperior item : list) {
                superiors.add(item.getUserId());
                User user = UserService.get(item.getUserId());
                detail.addUnderling(level, user == null ? "" : user.getNickname());
                if(user != null) {
                    undelingUids.add(user.getUserId());
                    List<Integer> tmpList = underlingLevel.get(level);
                    if(tmpList == null) {
                        tmpList = new ArrayList<Integer>();
                        underlingLevel.put(level, tmpList);
                    }
                    tmpList.add(user.getUserId());
                }
            }
            
            List<UserMonthBlotter> blotters = UserMonthBlotterDao.getByUserIds(superiors);
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
        List<CashInfo> cashInfos = CashInfoDao.getByStatus(userId, types);
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
        
        List<Order> orders = OrderDao.getByUserIds(undelingUids);
        int orderSuccessCount = 0;
        long orderSuccessAmount = 0;
        int orderFailCount = 0;
        long orderFailAmount = 0;
        for(Order order : orders) {
            
            if(order.getState().intValue() == OrderStatus.COMPLETE.getState()) {
                orderSuccessCount ++;
                orderSuccessAmount += order.getTotalFee();
            } else {
                orderFailCount ++;
                orderFailAmount += order.getTotalFee();
            }
            int level = -1;
            for(Entry<Integer, List<Integer>> entry : underlingLevel.entrySet()) {
                if(entry.getValue().contains(order.getUserId())) {
                    level = entry.getKey();
                    break;
                }
            }
            
            if(level > 0) {
                int addMoney = getAddMoney(level, order.getTotalFee());
                if(order.getState().intValue() == OrderStatus.INIT.getState()) {
                    if(addMoney > 0) {
                        detail.addUnpayWealth(addMoney);
                    }
                }
                if (order.getState().intValue() == OrderStatus.PAYED.getState()
                        || order.getState().intValue() == OrderStatus.DELIVERING.getState()
                        || order.getState().intValue() == OrderStatus.DELIVERED.getState()) {
                    if(addMoney > 0) {
                        detail.addPayWealth(addMoney);
                    }
                }
                if(order.getState().intValue() == OrderStatus.RECE.getState()) {
                    if(addMoney > 0) {
                        detail.addReceWealth(addMoney);
                    }
                }
            }
        }
        detail.setOrderFailAmount(orderFailAmount);
        detail.setOrderFailCount(orderFailCount);
        detail.setOrderSuccessAmount(orderSuccessAmount);
        detail.setOrderSuccessCount(orderSuccessCount);
        return detail;
    }
    
    public static boolean checkAndBecomeDistributor(Integer userId) {
        User user = UserDao.get(userId);
        if(user == null) {
            return false;
        }
        Distributor distributor = DistributorDao.get(userId);
        if(distributor != null) {
            return true;
        }
        
        //生成推广链接
        String link = Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") +
        		"/front/Users/qrcodeShare?userId=" + userId;
        //获取二维码
    	CreateLimitQrCodeReqDto req = new CreateLimitQrCodeReqDto(user.getOpenId());
    	CreateQrCodeRspDto rsp = null;
		try {
			rsp = WXPay.CreateLimitQrCodeService(req);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
    	if(null == rsp || rsp.isSuccess() == false) {
    		Logger.error("获取用户二维码图片失败，userId: %d", userId);
    		return false;
    	}
    	
        return createDistributor(userId, "", DistributorType.PERSONAL, link, rsp);
        
    }    
    private static boolean createDistributor(Integer userId, String realName, DistributorType type, String link, CreateQrCodeRspDto rsp) {
        User user = UserDao.get(userId);
        if(user == null) {
            return false;
        }
        Distributor distributor = DistributorDao.get(userId);
        if(distributor != null) {
            return true;
        }
        
        distributor = new Distributor();
        distributor.setJoinTime(System.currentTimeMillis());
        distributor.setType(type.getCode());
        distributor.setLink(link);
        distributor.setQrcodeLimitWxUrl(rsp.getUrl());
        distributor.setQrcodeLimitPath(rsp.getPicRelPath());
        distributor.setQrcodeLimitTicket(rsp.getTicket());
        distributor.setRealName(realName);
        distributor.setStatus(DistributorStatus.PASS.getCode());
        distributor.setUserId(userId);
        return add(distributor);
    }

    public static boolean updateStatus(int userId, int status) {
        if(!DistributorStatus.isExist(status)) {
            return false;
        }
        
        Distributor distributor = DistributorDao.get(userId);
        if(distributor == null) {
            return false;
        }
        distributor.setStatus(status);
        return update(distributor);
    }
    
    public static boolean createDistributorRef(Integer userId, Integer superiors) {
        if(userId == null || superiors == null || superiors <= 0) {
            Logger.warn("create ref fail, userId:%d, superiors:%d.", userId, superiors);
            return false;
        }
        
        if(superiors.intValue() == userId.intValue()) {
            Logger.warn("user and superiors is same people");
            return false;
        }
        
        //检查是否有环
        if(isSuperiors(userId, superiors, CHECK_DEPTH)) {
            Logger.warn("(userId)%d is the superiors of (superiorsId)%d", userId, superiors);
            return false;
        }
        
        Distributor superDistributor = DistributorDao.get(superiors);
        if(superDistributor == null || superDistributor.getStatus().intValue() != DistributorStatus.PASS.getCode()) {
            Logger.warn("superiors not found userId:%d, superiors:%d", userId, superiors);
            return false;
        }
            
        if(DistributorSuperiorDao.get(userId) != null) {
            Logger.warn("superiors is exist, userId:%d", userId);
            return false;
        }
        
        return DistributorSuperiorDao.create(userId, superiors);
    }


    private static final int CHECK_DEPTH = 20;
    /**
     * 检查userId是否为superiors的直接上线或间接上线
     * @param userId
     * @param superiors
     * @param currDepth
     * @return
     */
    private static boolean isSuperiors(int userId, Integer superiors, int currDepth) {
        if(currDepth <= 0) {
            return false;
        }
        
        DistributorSuperior ref = DistributorSuperiorDao.get(superiors);
        if(ref == null || ref.getSuperior() == null || ref.getSuperior().intValue() <= 0) {
            return false;
        } else if(ref.getSuperior().intValue() == userId) {
            return true;
        } else {
            currDepth --;
            return isSuperiors(userId, ref.getSuperior(), currDepth);
        }
    }
    
    public static boolean applyAudit(int userId, String imgUrl, String realName) {
        if(StringUtils.isBlank(realName) || StringUtils.isBlank(imgUrl)) {
            return false;
        }
        
        Distributor distributor = DistributorDao.get(userId);
        if(distributor == null) {
            return false;
        }
        distributor.setRealName(realName);
        boolean isSucc =DistributorDao.update(distributor);
        if(!isSucc) {
            return false;
        }
        
        if(AuditInfoDao.isExist(userId, AuditType.DISTRIBUTOR.getCode())) {
            Logger.error("audit info is exist, userId:%s, type:%s", userId, AuditType.DISTRIBUTOR.getCode());
            return false;
        }
        
        AuditInfo info = new AuditInfo();
        info.setAuditType(AuditType.DISTRIBUTOR.getCode());
        info.setAuditStatus(AuditStatus.INIT.getStatus());
        info.setContent(realName);
        info.setImgUrls(imgUrl);
        info.setUserId(userId);
        return AuditInfoDao.insert(info);
    }
    
    public static List<Integer> getSuperiors(int userId) {
        List<Integer> list = new ArrayList<Integer>();
        int findId = userId;
        for(int i=0; i<MAX_BLOTTER_DEPTH; i++) {
            DistributorSuperior ds = DistributorSuperiorDao.get(findId);
            if(ds == null) {
                break;
            }
            findId = ds.getSuperior();
            list.add(ds.getSuperior());
        }
        
        return list;
    }
}
