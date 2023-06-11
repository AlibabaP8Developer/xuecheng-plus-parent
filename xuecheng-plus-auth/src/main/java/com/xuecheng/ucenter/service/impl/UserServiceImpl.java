package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcMenu;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private XcUserMapper userMapper;

    @Autowired
    private XcMenuMapper menuMapper;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 连接数据库查询用户信息
     * 传入的请求认证的参数就是AuthParamsDto
     *
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        // 将传入的json串转成AuthParamsDto对象
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = JSON.parseObject(account, AuthParamsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求认证参数不符合要求");
        }

        // 认证类型 password、wx
        String authType = authParamsDto.getAuthType();

        // 根据认证的类型从spring容器取出bean
        String beanName = authType + "_authservice";
        AuthService authService = applicationContext.getBean(beanName, AuthService.class);
        // 调用统一execute方法完成认证
        XcUserExt xcUserExt = authService.execute(authParamsDto);

        //// 根据username账号查询数据库
        //String username = authParamsDto.getUsername();
        //XcUser xcUser = this.userMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        //// 查询到用户不存在，要返回null即可，spring security框架抛出异常用户不存在
        //if (xcUser == null) {
        //    return null;
        //}
        //// 如果查询到了用户，拿到正确的密码，最终封装成一个UserDetails对象给spring security框架返回，由框架进行密码比对
        //String password = xcUser.getPassword();

        // 封装xcUserExt用户信息为UserDetails
        // 根据UserDetails对象生成令牌
        UserDetails userDetails = getUserPrincipal(xcUserExt);
        return userDetails;
    }

    /**
     * @description 查询用户信息
     * @param xcUser  用户id，主键
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     * @author Mr.M
     * @date 2022/9/29 12:19
     */
    public UserDetails getUserPrincipal(XcUserExt xcUser){
        String password = xcUser.getPassword();

        String[] authorities = {};
        // 根据用户ID查询用户权限
        List<XcMenu> xcMenus = menuMapper.selectPermissionByUserId(xcUser.getId());
        if (xcMenus.size()>0) {
            List<String> permissions = new ArrayList<>();
            xcMenus.forEach(xcMenu -> {
                String code = xcMenu.getCode();
                // 拿到了用户拥有的权限标识符
                permissions.add(code);
            });
            // 将permissions转成数组
            authorities = permissions.toArray(permissions.toArray(new String[0]));
        }

        // 将用户信息转JSON
        xcUser.setPassword(null);
        String userJson = JSON.toJSONString(xcUser);
        UserDetails userDetails = User.withUsername(userJson).password(password).authorities(authorities).build();
        return userDetails;
    }
}
