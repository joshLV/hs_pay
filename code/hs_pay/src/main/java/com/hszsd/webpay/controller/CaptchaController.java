package com.hszsd.webpay.controller;

import com.google.code.kaptcha.Producer;
import com.hszsd.webpay.util.CaptchaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
public class CaptchaController {

    private final static Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    /**
     * 生成验证码信息
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping({"captchaImage"})
    public void generateCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.info("generateCaptchaImage is starting");
        Producer captchaProducer = CaptchaUtil.getInstance().getProducer();
        String code = captchaProducer.createText();
        logger.info("generateCaptchaImage code={}", code);
        request.getSession().setAttribute(KAPTCHA_SESSION_KEY, code.toUpperCase());

        ServletOutputStream out = response.getOutputStream();
        try {
            BufferedImage bi = captchaProducer.createImage(code);
            ImageIO.write(bi, "jpg", out);
            out.flush();
        }catch (Exception e){
            logger.error("generateCaptchaImage occurs an error and cause by {}", e.getMessage());
        }finally {
            out.close();
        }
    }
}
