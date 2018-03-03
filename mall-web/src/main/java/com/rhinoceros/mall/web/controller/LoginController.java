package com.rhinoceros.mall.web.controller;

import com.rhinoceros.mall.core.dto.LoginUserDto;
import com.rhinoceros.mall.core.pojo.User;
import com.rhinoceros.mall.service.impl.exception.UserException;
import com.rhinoceros.mall.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.PublicKey;

@Controller
public class LoginController {

    private static final String USERNAME = "user";
    @Autowired
    private UserService userService;

    /**
     * 登录页面展示
     *
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpSession session) {
        // 用户已登录，直接返回首页
        if (session.getAttribute(USERNAME) != null) {
            return "redirect:/index";
        }
        return "login";
    }

    /**
     * 登录表单提交
     *
     * @param userDto
     * @param br
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/loginSubmit")
    public String login(@Validated @ModelAttribute("loginUser") LoginUserDto userDto, BindingResult br, HttpSession session, Model model) {
        // 用户已登录，直接返回首页
        if (session.getAttribute(USERNAME) != null) {
            return "redirect:/index";
        }
        // 检查用户输入是否规范，不规范则返回到登录页面
        if (br.hasErrors()) {
            model.addAttribute("error", br.getFieldError().getDefaultMessage());
            return "login";
        }

        //检查输入的用户是否存在，存在则跳转到到主页面，不存在则返回到登录页面
        try {
            User user = userService.login(userDto);
            //将用户信息放入session
            session.setAttribute(USERNAME, user);
            return "redirect:/index";
        } catch (UserException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    /**
     * 点击忘记密码时，跳转至密码找回界面
     * @return
     */
    @RequestMapping("/retrievePassword")
    public String retrievePassword(){
        return "retrievePassword";
    }

    /**
     * 点击确定时验证邮箱和验证码校对
     * @param mail
     * @param code
     * @param session
     * @return
     */
    @RequestMapping("/verifyMail")
    public String verifyMail(@RequestParam("mail") String mail, String code, HttpSession session){
        String validateCode = (String) session.getAttribute("validateCode");
        //查询数据库，确定邮箱对应的用户是否存在，
        if(validateCode != null && validateCode.equals(code)){

            //如果存在，发送激活链接到对应的邮箱

            //用户在一定时间内点击对应的激活链接，

            //如果链接有效，校验链接中的加密字符串，获取用户信息（不能基于session），跳转到重置密码界面
            //链接中加密用户id，可以使用jwt，这样检验的controller就可以获取到用户id

            //如果链接失效，提示链接失效
            return "resetPassword";
        }else {
            //如果用户不存在，提示用户未注册
            return "validateCode incorrect";
        }

    }
}
