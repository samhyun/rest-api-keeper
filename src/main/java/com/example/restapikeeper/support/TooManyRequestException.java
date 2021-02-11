package com.example.restapikeeper.support;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.TOO_MANY_REQUESTS,
        reason = "호출 횟수 한도를 초과하였습니다."
)
public class TooManyRequestException extends RuntimeException {
}
