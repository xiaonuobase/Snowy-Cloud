package vip.xiaonuo.core.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import io.jsonwebtoken.*;

import java.util.Date;

/**
 * JwtToken工具类
 *
 * @author xuyuxiang
 * @date 2020/3/12 17:39
 */
public class JwtTokenUtil {

    /**
     * 生成token
     *
     * @author xuyuxiang
     * @date 2020/3/12 17:52
     */
    public static String generateToken(JwtPayLoad jwtPayLoad) {

        DateTime expirationDate = DateUtil.offsetSecond(new Date(),
                Convert.toInt(ConstantContextHolder.getTokenExpireSec()));
        return Jwts.builder()
                .setClaims(BeanUtil.beanToMap(jwtPayLoad))
                .setSubject(jwtPayLoad.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, ConstantContextHolder.getJwtSecret())
                .compact();
    }

    /**
     * 根据token获取Claims
     *
     * @author xuyuxiang
     * @date 2020/3/13 10:29
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(ConstantContextHolder.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取JwtPayLoad部分
     *
     * @author xuyuxiang
     * @date 2020/3/12 17:53
     */
    public static JwtPayLoad getJwtPayLoad(String token) {
        Claims claims = getClaimsFromToken(token);
        return BeanUtil.mapToBean(claims, JwtPayLoad.class, false);
    }

    /**
     * 校验token是否正确
     *
     * @author xuyuxiang
     * @date 2020/3/13 10:36
     */
    public static Boolean checkToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException jwtException) {
            return false;
        }
    }

    /**
     * 校验token是否失效
     *
     * @author xuyuxiang
     * @date 2020/3/13 10:30
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            final Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }
}
