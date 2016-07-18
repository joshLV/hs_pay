package com.hszsd.common.util.servlet;

import com.google.code.kaptcha.Producer;
import com.hszsd.common.util.captcha.CaptchaUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 * 验证码生成控制器
 * Created by gzhengDu on 2016/6/29.
 */
public class CaptchaServlet {

    /**
     * 生成验证码信息
     * @param request
     * @param response
     * @throws IOException
     */
    public void generateCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException{
        Producer captchaProducer = CaptchaUtil.getInstance().getProducer();
        String code = captchaProducer.createText();
        request.getSession().setAttribute(KAPTCHA_SESSION_KEY, code);

        BufferedImage bi = captchaProducer.createImage(code);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        }finally {
            out.close();
        }
    }
}
