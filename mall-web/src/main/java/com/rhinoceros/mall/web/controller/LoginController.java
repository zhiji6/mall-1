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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
    @ResponseBody
    @RequestMapping("/verifyMail")
    public String verifyMail(String mail,String code,HttpSession session){
        String validateCode = (String) session.getAttribute("validateCode");
        if(validateCode != null && validateCode.equals(code)){
            //TODO 验证mail
            return "OK";
        }else {
            return "validateCode incorrect";
        }

    }
}
