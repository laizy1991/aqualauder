package common.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import play.mvc.Http;
import common.constants.GlobalConstants;
import utils.StringUtil;

/**
 * 用于分页的工具类
 * 
 * @author chenxx
 */
public class Pager<T> {

	/**
	 * 总记录数
	 */
	private int total = 0;
	
	/**
	 * 当前页
	 */
	private int page = GlobalConstants.DEFAULT_PAGE;
	
	/**
	 * 每页显示记录数
	 */
	private int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
	
	/**
	 * 总页数
	 */
	private int totalPage = 1;
	
	/**
	 * 对象记录结果集
	 */
	private List<T> list = new ArrayList<T>();

	/**
	 * 是否为第一页
	 */
	private boolean isFirstPage = false;
	
	/**
	 * 是否为最后一页
	 */
	private boolean isLastPage = false;
	
	/**
	 * 是否有前一页
	 */
	private boolean hasPreviousPage = false;
	
	/**
	 * 是否有下一页
	 */
	private boolean hasNextPage = false;
	
	/**
	 * 页码链接
	 */
	private String pageLink;
	
	public Pager() {
		init(0, GlobalConstants.DEFAULT_PAGE, GlobalConstants.DEFAULT_PAGE_SIZE);
	}

	public Pager(int total, int page) {
		init(total, page, pageSize);
	}

	public Pager(int total, int page, int pageSize) {
		init(total, page, pageSize);
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public List<T> getList() {
		return list;
	}

	public int getTotal() {
		return total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getPage() {
		return page;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public boolean hasPreviousPage() {
		return hasPreviousPage;
	}

	public boolean hasNextPage() {
		return hasNextPage;
	}
	
	/**
     * 返回上一前的页码
     */
    public int getPreviousPage() {
        return this.page > 1 ? this.page - 1 : this.page;
    }
    
    /**
     * 返回下一前的页码
     */
    public int getNextPage() {
        return this.page < this.totalPage ? this.page + 1 : this.page;
    }
	
	/**
     * 获取页码链接。在链接尾部增加“page=“参数
     */
    private String getPageLink() {
        if (pageLink == null) {
            String queryString = Http.Request.current().querystring;
            if (StringUtil.isNullOrEmpty(queryString)) {
                pageLink = "?page=";
            } else {
                String[] items = queryString.split("&");
                StringBuilder queryItems = new StringBuilder(queryString.length());
                queryItems.append("?");
                for (String item : items) {
                    if (!StringUtils.startsWithIgnoreCase(item, "page=")) {
                        if (queryItems.length() > 1) {
                            queryItems.append("&");
                        }
                        queryItems.append(item);
                    }
                }
                if (queryItems.length() > 1) {
                    queryItems.append("&");
                }
                queryItems.append("page=");
                pageLink = queryItems.toString();
            }
        }
        return pageLink;
    }
    
    /**
     * 生成页面的页码链接
     *
     * @param page
     */
    public String createPageLink(int page) {
        return getPageLink().concat(String.valueOf(page));
    }
    
    /**
     * 生成页码导航
     *
     * @param maxCount 每次最大生成的页码数量，如果小于0则表示全生成
     */
    public List<Integer> createPages(int maxCount) {
        int startPage, endPage;
        if (maxCount < 1) {
            startPage = 1;
            endPage = this.pageSize;
        } else {
            startPage = Math.max(1, this.page - (maxCount / 2));
            endPage = Math.min(this.totalPage, startPage + maxCount - 1);
            if ((endPage - startPage) < maxCount) {
                startPage = Math.max(1, endPage - maxCount + 1);
            }
        }

        List<Integer> pages = new ArrayList<Integer>(endPage - startPage + 1);
        for (int i = startPage; i <= endPage; i++) {
            pages.add(i);
        }
        return pages;
    }
	
	private void init(int total, int page, int pageSize) {
		// 设置基本参数
		this.total = total;
		this.pageSize = pageSize;
		this.totalPage = (this.total - 1) / this.pageSize + 1;

		// 根据输入可能错误的当前号码进行自动纠正
		if (page < 1) {
			this.page = 1;
		} else if (page > this.totalPage) {
			this.page = this.totalPage;
		} else {
			this.page = page;
		}

		// 以及页面边界的判定
		judgePageBoudary();
	}
	
	/**
	 * 判定页面边界
	 */
	private void judgePageBoudary() {
		isFirstPage = (this.page == 1);
		isLastPage = (this.page == this.totalPage) && this.page != 1;
		hasPreviousPage = (this.page != 1);
		hasNextPage = (this.page != this.totalPage);
	}
}
