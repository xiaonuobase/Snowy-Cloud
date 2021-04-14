package vip.xiaonuo.common.timer;

/**
 * 定时器执行者
 * <p>
 * 定时器都要实现本接口，并需要把实现类加入到spring容器中
 *
 * @author yubaoshan
 * @date 2020/6/28 21:28
 */
public interface TimerTaskRunner {

    /**
     * 任务执行的具体内容
     *
     * @author yubaoshan
     * @date 2020/6/28 21:29
     */
    void action();

}
