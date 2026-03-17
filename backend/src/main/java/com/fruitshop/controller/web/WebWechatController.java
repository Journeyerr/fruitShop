package com.fruitshop.controller.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Member;
import com.fruitshop.service.MemberService;
import com.fruitshop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 小程序-微信登录
 */
@RestController
@RequestMapping("/web/wechat")
public class WebWechatController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        String nickname = params.get("nickname");
        String avatar = params.get("avatar");

        // 调用微信接口获取openid
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
                "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(result);

        String openid = jsonObject.getString("openid");
        if (openid == null) {
            return Result.error("微信登录失败");
        }

        // 登录或注册
        Member member = memberService.wechatLogin(openid, nickname, avatar);

        // 生成token
        String token = jwtUtil.generateToken(member.getId(), member.getNickname());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", member);
        return Result.success(data);
    }

    @PostMapping("/phone")
    public Result<String> getPhone(@RequestBody Map<String, String> params, @RequestAttribute Long userId) {
        String code = params.get("code");
        
        // TODO: 调用微信接口获取手机号
        // 需要使用getPhoneNumber接口
        
        return Result.success();
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
