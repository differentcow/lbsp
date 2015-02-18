package com.lbsp.promotion.webplatform.controller;

import com.lbsp.promotion.util.handler.LogUtil;
import com.lbsp.promotion.webplatform.security.filter.CustomUsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@Controller
public class ValidateCodeController {
	private char mapTable[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	private void getCertPic(int width, int height, String validateCode,OutputStream os)
			throws IOException {
		if (width <= 0)
			width = 60;
		if (height <= 0)
			height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// 画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// 　　将认证码显示到图像中,如果要生成更多位的认证码,增加drawString语句
		g.setColor(Color.black);
		g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
		String str = validateCode.substring(0, 1);
		g.drawString(str, 8, 17);
		str = validateCode.substring(1, 2);
		g.drawString(str, 20, 15);
		str = validateCode.substring(2, 3);
		g.drawString(str, 35, 18);
		str = validateCode.substring(3, 4);
		g.drawString(str, 45, 15);
		// 随机产生10个干扰点
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			g.drawOval(x, y, 1, 1);
		}
		// 释放图形上下文
		g.dispose();
		// 输出图像到页面
		ImageIO.write(image, "JPEG", os);
	}
	private String getValidateCode(){
		// 取随机产生的认证码
		String strEnsure = "";
		// 4代表4位验证码,如果要生成更多位的认证码,则加大数值
		for (int i = 0; i < 4; ++i) {
			strEnsure += mapTable[(int) (mapTable.length * Math.random())];
		}
		return strEnsure;
	}
	@RequestMapping("/validateCode/generate")
	public void generate(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String validateCode = this.getValidateCode();
			request.getSession().setAttribute(
					CustomUsernamePasswordAuthenticationFilter.VALIDATE_CODE,
					validateCode);
			this.getCertPic(80, 30,validateCode,response.getOutputStream());
		} catch (IOException e) {
			LogUtil.logError("生成验证码出错");
            e.printStackTrace();
		}
	}
}
