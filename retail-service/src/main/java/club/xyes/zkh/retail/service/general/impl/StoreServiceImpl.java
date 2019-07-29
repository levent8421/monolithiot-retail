package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.CollectionUtils;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.repository.dao.mapper.StoreMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.encrypt.PasswordEncryptor;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.WxOAuth2AccessToken;
import club.xyes.zkh.retail.wechat.props.WechatConfig;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create by 郭文梁 2019/5/20 0020 11:13
 * StoreServiceImpl
 * 商铺相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@Service
@Slf4j
public class StoreServiceImpl extends AbstractServiceImpl<Store> implements StoreService {
    /**
     * 最多允许记录5个openId
     */
    private static final int MAX_NUM_OF_OPEN_ID = 5;
    private final StoreMapper storeMapper;
    private final PasswordEncryptor passwordEncryptor;
    private final Wechat wechat;
    private final WechatConfig wechatConfig;

    public StoreServiceImpl(StoreMapper mapper,
                            PasswordEncryptor passwordEncryptor,
                            Wechat wechat,
                            WechatConfig wechatConfig) {
        super(mapper);
        this.storeMapper = mapper;
        this.passwordEncryptor = passwordEncryptor;
        this.wechat = wechat;
        this.wechatConfig = wechatConfig;
    }

    @Override
    public Store login(String loginName, String password, String openId) {
        final Store store = findByLoginName(loginName);
        if (store != null && passwordEncryptor.matches(store.getPassword(), password)) {
            return appendOpenId(store, openId);
        }
        throw new BadRequestException("用户名或密码错误");
    }

    /**
     * 追加记录OpenId
     *
     * @param store  商铺
     * @param openId openId
     * @return 商铺
     */
    private Store appendOpenId(Store store, String openId) {
        final String openIdStr = store.getWxOpenId();
        if (TextUtils.isTrimedEmpty(openIdStr)) {
            store.setWxOpenId(openId);
            return updateById(store);
        }
        final List<String> openIdList = Arrays.stream(openIdStr.split(ApplicationConstants.OPEN_ID_DELIMITER))
                .filter(TextUtils::isTrimedNotEmpty)
                .collect(Collectors.toList());
        if (openIdList.isEmpty()) {
            store.setWxOpenId(openId);
            return updateById(store);
        }
        if (openIdList.contains(openId)) {
            return store;
        }
        if (openIdList.size() >= MAX_NUM_OF_OPEN_ID) {
            openIdList.remove(0);
        }
        openIdList.add(openId);
        final String openIds = CollectionUtils.join(openIdList.stream(), ApplicationConstants.OPEN_ID_DELIMITER);
        store.setWxOpenId(openIds);
        return updateById(store);
    }

    @Override
    public Store findByLoginName(String loginName) {
        Store query = new Store();
        query.setLoginName(loginName);
        return findOneByQuery(query);
    }

    @Override
    public Store loginByOAuthCode(String code, Map<String, Object> context) {
        String appId = wechatConfig.getAppId();
        String secret = wechatConfig.getSecret();
        WxOAuth2AccessToken accessToken = wechat.code2Token(appId, secret, code);
        context.put("token", accessToken);
        return findByWxOpenId(accessToken.getOpenId());
    }

    @Override
    public Store create(Store store) {
        Store exists = findByLoginName(store.getLoginName());
        if (exists != null) {
            throw new BadRequestException("登录名已存在");
        }
        store.setPassword(passwordEncryptor.encode(store.getPassword()));
        return save(store);
    }

    @Override
    public void cleanOpenId(Store store) {
        store.setWxOpenId(null);
        updateById(store);
    }

    @Override
    public void cleanOpenId(Store store, String openId) {
        final String openIdsStr = store.getWxOpenId();
        if (TextUtils.isTrimedEmpty(openIdsStr)) {
            return;
        }
        final List<String> openIdsList = Arrays.stream(openIdsStr.split(ApplicationConstants.OPEN_ID_DELIMITER))
                .filter(TextUtils::isTrimedNotEmpty)
                .collect(Collectors.toList());
        updateById(store);
        openIdsList.remove(openId);
        store.setWxOpenId(CollectionUtils.join(openIdsList.stream(), ApplicationConstants.OPEN_ID_DELIMITER));
        updateById(store);
    }

    private Store findByWxOpenId(String openId) {
        return storeMapper.findLikeOpenId("%" + openId + "%");
    }

    @Override
    public void setAddressIndex(Store store, AddressIndex index) {
        store.setAddressIndex(index);
        store.setAddressIndexId(index.getId());
        updateById(store);
    }

    @Override
    public void setOrderNum(Store store, Integer orderNum) {
        store.setOrderNum(orderNum);
        updateById(store);
    }

    @Override
    public PageInfo<Store> listFetchAll(Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(storeMapper::selectFetchAll);
    }
}
