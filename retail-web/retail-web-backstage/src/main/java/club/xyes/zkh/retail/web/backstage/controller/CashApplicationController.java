package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.CashApplicationService;
import club.xyes.zkh.retail.service.listener.WithdrawListener;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/5/30 0030 14:51
 * CashApplicationController
 * 提现申请相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
@RestController
@RequestMapping("/api/cash")
public class CashApplicationController extends AbstractEntityController<CashApplication> {
    private final CashApplicationService cashApplicationService;
    private final CashApplicationService.ApplicationWithdrawSuccessListener applicationWithdrawSuccessListener;
    private final WithdrawListener withdrawListener;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected CashApplicationController(
            CashApplicationService service,
            CashApplicationService.ApplicationWithdrawSuccessListener applicationWithdrawSuccessListener,
            WithdrawListener withdrawListener) {
        super(service);
        this.cashApplicationService = service;
        this.applicationWithdrawSuccessListener = applicationWithdrawSuccessListener;
        this.withdrawListener = withdrawListener;
    }

    /**
     * 分页查询指定状态的提现记录
     *
     * @param status 状态
     * @param page   页码
     * @param rows   每页大小
     * @return GR with PageInfo
     */
    @GetMapping("/{status}/all")
    public GeneralResult<PageInfo<CashApplication>> all(@PathVariable("status") Integer status,
                                                        Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        PageInfo<CashApplication> res = cashApplicationService.findByStatus(status, page, rows);
        return GeneralResult.ok(res);
    }

    /**
     * 执行提现操作
     *
     * @param id 申请ID
     * @return GR
     */
    @PostMapping("/{id}/withdraw")
    public GeneralResult<CashApplication> withdraw(@PathVariable("id") Integer id) {
        @NotNull CashApplication application = cashApplicationService.require(id);
        CashApplication res = cashApplicationService.withdraw(application, applicationWithdrawSuccessListener);
        return GeneralResult.ok(res);
    }

    /**
     * 拒绝提现申请
     *
     * @param id 申请ID
     * @return GR
     */
    @PostMapping("/{id}/refuse")
    public GeneralResult<CashApplication> refuse(@PathVariable("id") Integer id) {
        @NotNull CashApplication application = cashApplicationService.require(id);
        CashApplication res = cashApplicationService.refuse(application, withdrawListener);
        return GeneralResult.ok(res);
    }

    /**
     * 搜索提现记录
     *
     * @param search 搜索条件（电话 姓名 微信号）
     * @return GR
     */
    @GetMapping("/search")
    public GeneralResult<PageInfo<CashApplication>> search(@RequestParam("search") String search,
                                                           Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        final PageInfo<CashApplication> res = cashApplicationService.search(search, page, rows);
        return GeneralResult.ok(res);
    }
}
