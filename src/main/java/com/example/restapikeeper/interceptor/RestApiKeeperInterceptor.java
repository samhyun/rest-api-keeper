package com.example.restapikeeper.interceptor;

import com.example.restapikeeper.RestApiKeeper;
import com.example.restapikeeper.support.TooManyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RestApiKeeperInterceptor implements HandlerInterceptor {

    private final RestApiKeeper keeper;

    public RestApiKeeperInterceptor(RestApiKeeper keeper) {
        this.keeper = keeper;
    }

    /**
     * 요청에 대해서 호출 가능 여부를 판별
     * 호출이 불가능할경우 TooManyRequestException 을 던진다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info(request.getRemoteAddr());
        if (keeper.checkPassable(request.getRemoteAddr())) {
            keeper.addRequestTime(request.getRemoteAddr());
            return true;
        }
        throw new TooManyRequestException();
    }
}
