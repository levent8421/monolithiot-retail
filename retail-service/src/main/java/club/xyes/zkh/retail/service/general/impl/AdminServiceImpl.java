package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.Admin;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.repository.dao.mapper.AdminMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.encrypt.PasswordEncryptor;
import club.xyes.zkh.retail.service.general.AdminService;
import org.springframework.stereotype.Service;

/**
 * Create by 郭文梁 2019/6/20 0020 10:41
 * AdminServiceImpl
 * 管理员账户相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@Service
public class AdminServiceImpl extends AbstractServiceImpl<Admin> implements AdminService {
    private final AdminMapper adminMapper;
    private final PasswordEncryptor passwordEncryptor;

    public AdminServiceImpl(AdminMapper mapper, PasswordEncryptor passwordEncryptor) {
        super(mapper);
        this.adminMapper = mapper;
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public Admin login(String name, String password) {
        final Admin admin = findByName(name);
        if (admin == null) {
            throw new BadRequestException("账户不存在！");
        }
        if (!passwordEncryptor.matches(admin.getPassword(), password)) {
            throw new BadRequestException("密码错误！");
        }
        return admin;
    }

    @Override
    public Admin create(Admin admin) {
        if (findByName(admin.getName()) != null) {
            throw new BadRequestException("登录名已存在");
        }
        admin.setPassword(passwordEncryptor.encode(admin.getPassword()));
        return save(admin);
    }

    @Override
    public Admin update(Admin admin) {
        admin.setPassword(passwordEncryptor.encode(admin.getPassword()));
        return updateById(admin);
    }

    /**
     * 通过登录名查找管理员长湖
     *
     * @param name 登录名
     * @return Admin
     */
    private Admin findByName(String name) {
        Admin query = new Admin();
        query.setName(name);
        return findOneByQuery(query);
    }
}
