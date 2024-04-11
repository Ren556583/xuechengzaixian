package com.xuecheng.ucenter.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsImpl implements UserDetailsService {
    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * @param name 用户输入的登录账号
     * @return UserDetails
     * @throws UsernameNotFoundException 传入的请求认证参数就是AuthParamsDto
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //传入的json转为AuthParamsDto对象
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            log.error("认证请求数据格式不对：{}", s);
            throw new RuntimeException("请求认证参数不符合要求");
        }

        // 获取认证类型，beanName就是 认证类型 + 后缀，例如 password + _authservice = password_authservice
        String authType = authParamsDto.getAuthType();
        // 根据认证类型，从Spring容器中取出对应的bean
        AuthService authService = applicationContext.getBean(authType + "_authservice", AuthService.class);
        XcUserExt user = authService.execute(authParamsDto);

        //String name = authParamsDto.getUsername();
        // 根据username去XcUser表中查询对应的用户信息
        //XcUser user = xcUserMapper.selectOne(new La下·mbdaQueryWrapper<XcUser>().eq(XcUser::getUsername, name));
        return getUserPrincipal(user);
    }

    public UserDetails getUserPrincipal(XcUserExt user) {
        String[] authorities = {"test"};
        String password = user.getPassword();
        user.setPassword(null);
        String userJsonStr = JSON.toJSONString(user);
        UserDetails userDetails = User.withUsername(userJsonStr).password(password).authorities(authorities).build();
        return userDetails;
    }
}
