package com.example.restapikeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RestApiKeeper {

    //rest api 호출한 정보를 담을 cache
    private final ConcurrentHashMap<String, List<Long>> cache;

    //호출 제한 시간
    private final long interval;

    //호출 제한 횟수
    private final int limit;

    public RestApiKeeper(long interval, int limit) {
        this.cache = new ConcurrentHashMap<>();
        this.interval = interval;
        this.limit = limit;
    }

    /**
     * 호출 가능 여부 판단 하는 함수
     * @param key - client 를 구분할 수 있는 구분자( ex. api-key, remote-address)
     * @return true - 호출 가능, false - 호출 불가
     */
    public boolean checkPassable(String key) {
        if (!this.cache.containsKey(key)) {
            return true;
        }
        return removeOldRequestCache(key) < limit;
    }

    /**
     * 현 시점에서 interval 만큼을 뺀 시점 이전에 들어온 요청 기록을 삭제하는 함수
     * @param key - client 를 구분할 수 있는 구분자( ex. api-key, remote-address)
     * @return cache value size - 삭제하고 남은 기록의 수
     */
    private long removeOldRequestCache(String key) {
        long minimum = System.currentTimeMillis() - interval;
        cache.get(key).removeIf(requestTime -> minimum > requestTime);
        return cache.get(key).size();
    }

    /**
     * cache 에 현재 들어온 요청 시간을 저장하는 함수
     * @param key - client 를 구분할 수 있는 구분자( ex. api-key, remote-address)
     */
    public void addRequestTime(String key) {
        if (!this.cache.containsKey(key)) {
            this.cache.put(key, new ArrayList<>());
        }
        this.cache.get(key).add(System.currentTimeMillis());
    }
}
