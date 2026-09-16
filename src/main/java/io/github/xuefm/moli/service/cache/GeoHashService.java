package io.github.xuefm.moli.service.cache;

import lombok.AllArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GeoHashService {


    private final RedisTemplate redisTemplate;

    /***
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中。
     * @param key redis的key
     * @param longitude   经度
     * @param latitude   纬度
     * @param name  名称
     * @return
     */
    public Long redisGeoAdd(String key, double longitude, double latitude, String name) {
        //params: key, Point(经度, 纬度), 地方名称
        //Long addedNum = redisTemplate.opsForGeo().add("city", new Point(116.405285, 39.904989), "北京");
        Long addedNum = redisTemplate.opsForGeo().add(key, new Point(longitude, latitude), name);
        return addedNum;
    }

    /***
     * 从key里返回所有给定位置元素的位置（经度和纬度）。
     * @param key redis的key
     * @param nameList  名称的集合
     */
    public List<Point> redisGeoGet(String key, List<String> nameList) {
        //params: key, 地方名称...
        List<Point> points = redisTemplate.opsForGeo().position(key, nameList);
        return points;
    }


    /***
     * 返回两个给定位置之间的距离。
     * @param key redis的key
     * @param name1 地方名称1
     * @param name2 地方名称2
     * @return
     */
    public Distance geoDist(String key, String name1, String name2) {
        //params: key, 地方名称1, 地方名称2, 距离单位
        Distance distance = redisTemplate.opsForGeo().distance(key, name1, name2, RedisGeoCommands.DistanceUnit.KILOMETERS);

        return distance;
    }


    /***
     * 以给定的经纬度为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素，并给出所有位置元素与中心的平均距离。
     * @param key redis的key
     * @param longitude 经度
     * @param latitude 纬度
     * @param distance 距离
     * @param count 人数
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> redisNearByXY(String key, double longitude, double latitude, Integer distance, Integer count) {
        //Point(经度, 纬度) Distance(距离量, 距离单位)
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(distance, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(count);
        //params: key, Circle, GeoRadiusCommandArgs
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(key, circle, args);
        return results;
    }

    /***
     * 以给定的城市为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素，并给出所有位置元素与中心的平均距离。
     * @param key redis的key
     * @param name 名称
     * @param distance 距离
     * @param count 人数
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoNearByPlace(String key, String name, Integer distance, Integer count) {
        //params: 距离量, 距离单位
        Distance distances = new Distance(distance, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(count);
        //params: key, 地方名称, Circle, GeoRadiusCommandArgs
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(key, name, distances, args);
        return results;
    }


    /***
     * 返回一个或多个位置元素的 Geohash 表示
     * @param key redis的key
     * @param nameList  名称的集合
     */
    public List<String> geoHash(String key, List<String> nameList) {
        //params: key, 地方名称...
        List<String> results = redisTemplate.opsForGeo().hash(key, nameList);
        return results;
    }


}
