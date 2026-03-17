package com.fruitshop.controller.admin;

import com.fruitshop.common.Result;
import com.fruitshop.dto.LoginDTO;
import com.fruitshop.entity.Admin;
import com.fruitshop.service.AdminService;
import com.fruitshop.util.JwtUtil;
import com.fruitshop.util.PasswordUtil;
import com.fruitshop.vo.AdminInfoVO;
import com.fruitshop.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-登录
 */
@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        // 从数据库验证用户名密码
        Admin admin = adminService.getByUsername(loginDTO.getUsername());
        if (admin == null) {
            return Result.error(401, "用户名或密码错误");
        }
        String encode = PasswordUtil.encode("admin123");
        System.out.println( encode);
        // 验证密码（使用BCrypt加密比对）
        if (!PasswordUtil.matches(loginDTO.getPassword(), admin.getPassword())) {
            return Result.error(401, "用户名或密码错误");
        }
        
        // 检查账号状态
        if (admin.getStatus() == null || admin.getStatus() != 1) {
            return Result.error(403, "账号已被禁用");
        }
        
        // 更新最后登录时间
        adminService.updateLastLoginTime(admin.getId());
        
        // 生成token
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername());
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUsername(admin.getUsername());
        loginVO.setName(admin.getName());
        return Result.success(loginVO);
    }

    @GetMapping("/info")
    public Result<AdminInfoVO> info(@RequestAttribute Long userId) {
        // 从数据库获取管理员信息
        Admin admin = adminService.getById(userId);
        if (admin == null) {
            return Result.error(404, "管理员不存在");
        }
        
        AdminInfoVO infoVO = new AdminInfoVO();
        infoVO.setId(admin.getId());
        infoVO.setUsername(admin.getUsername());
        infoVO.setName(admin.getName());
        infoVO.setAvatar(admin.getAvatar());
        infoVO.setPhone(admin.getPhone());
        infoVO.setEmail(admin.getEmail());
        return Result.success(infoVO);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // 清除服务端的token相关信息（如果有的话）
        // 由于使用JWT无状态认证，客户端只需删除本地存储的token即可
        return Result.success();
    }
}
