-- 全局限流+业务限流
-- 全局限流：每秒最多放过去多少流量，可以通过redis配置，定时读取redis的QPS数量即可
-- 这里没有做，写死了

global_limiting=10000

-- 业务限流
-- 读取redis秒杀场次，把每个商品数量读出来
-- 业务限流的流量 = 商品数量 * 1.1
-- 这里也没有从redis取出来，而是写死的
business_session_id=101
business_session_products={}
business_session_products[518]=1000
business_session_products[519]=10000
business_session_products[520]=550

-- 先全局限流

-- 获取当前时间
currentTime="" -- 用于截取每一秒的请求
timestamp=os.date("%Y-%m-%d %H:%M:%S") -- 当前时间

currentRequests = 0
currentProductRequests = {}

-- 判断该请求是否在当前这秒
if(timestamp == currentTime)
then
    if(currentRequests<global_limiting)
    then
        -- 业务限流
        -- 通过openResty提取http请求参数，获得秒杀商品的id
        local productId=518
        -- 拿到当前商品的请求流量
        local productRequests = currentProductRequests[productId]
        -- 判断是否有过当前商品的请求流量
        if(productRequests == nil or productRequests == 0)
        then
            -- 如果没有就令该业务限流的流量为1
            currentProductRequests[productId] = 1
            -- 然后全局限流+1
            currentRequests=currentRequests + 1
        else
            -- 如果有业务限流的流量，就判断是否大于 商品数量 * 1.1
            local business_limiting = currentSessionProductLimiting[productId]  -- 先获取商品数量
            if(productRequests <= business_limiting * 1.1)
            then
                -- 业务限流和全局限流+1
                currentProductRequests[productId] = productRequests + 1
                currentRequests = currentRequests + 1
            else
                -- 对秒杀商品的放行过去的请求数量已经超过了限购数量的1.1倍了
                -- 此时进行业务限流，返回响应给客户端，说明抢购失败
            end
        end
    else
        -- 当流量超过global_limiting，就通过openResty返回抢购失败给客户端
    end
-- 该请求是下一秒的时候，就重新设置currentTime
else
    currentTime = timestamp
    currentRequests = 1
end
