 package vip.xiaonuo.dev.api.context;

 import cn.hutool.json.JSONObject;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.stereotype.Component;
 import vip.xiaonuo.dev.api.DevSlideshowApi;
 import vip.xiaonuo.dev.feign.DevSlideshowFeign;

 import java.util.List;

/**
 * DevSlideshowApi上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DevSlideshowApiContextBean implements DevSlideshowApi {

    final private DevSlideshowFeign devSlideShowFeign;

    /**
     * 通过位置获得轮播图列表
     *
     * @param place
     * @author yubaoshan
     * @date 2024/07/13 00:31
     */
    @Override
    public List<JSONObject> getListByPlace(String place) {
        return this.devSlideShowFeign.getListByPlace(place);
    }
}
