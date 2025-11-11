package com.example.agent.util;

import jakarta.servlet.http.HttpServletRequest;

public final class IpUtil {
	private IpUtil() {}

	public static String getClientIp(HttpServletRequest request) {
		String[] headers = {
			"X-Forwarded-For",
			"X-Real-IP",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_CLIENT_IP",
			"HTTP_X_FORWARDED_FOR"
		};
		for (String header : headers) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				if (ip.contains(",")) {
					return ip.split(",")[0].trim();
				}
				return ip;
			}
		}
		return request.getRemoteAddr(); //获取不到 直接添加Nginx的IP
	}
}


